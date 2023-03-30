/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import vendas.beans.EmailBean;
import ritual.swing.TApplication;
import vendas.dao.EmpresaDao;
import vendas.dao.PedidoDao;
import vendas.dao.UserDao;
import vendas.entity.ArquivoOrcamento;
import vendas.entity.ArquivoPedido;
import vendas.entity.Orcamento;
import vendas.entity.Params;
import vendas.entity.Pedido;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.swing.app.pedido.PedidoViewFrame;

/**
 *
 * @author Sam
 */
public class ReportViewFrame extends ViewFrame {

    private JasperPrint jasperPrint;
    private EmailBean email;
    //private Properties props;
    private Pedido pedido;
    private Pedido cobranca;
    private Orcamento orcamento;
    private boolean fornecedor;
    private InputStream customStream;
    private byte[] bytes;
    private Boolean incluirAnexo;
    private PedidoViewFrame parentViewFrame;
    private List<ArquivoPedido> anexos;

    public ReportViewFrame() {
        super();
        email = (EmailBean) TApplication.getInstance().lookupService("emailBean");
        loadProperties();
    }

    public ReportViewFrame(String title) {
        super(title);
        email = (EmailBean) TApplication.getInstance().lookupService("emailBean");
        email.setSubject(title);
        loadProperties();
    }

    public ReportViewFrame(String title, JasperPrint jasperPrint) {
        super(title);
        email = (EmailBean) TApplication.getInstance().lookupService("emailBean");
        email.setSubject(title);
        this.jasperPrint = jasperPrint;
        loadProperties();
    }

    public PedidoViewFrame getParentViewFrame() {
        return parentViewFrame;
    }

    public void setParentViewFrame(PedidoViewFrame parentViewFrame) {
        this.parentViewFrame = parentViewFrame;
    }

    public Boolean getIncluirAnexo() {
        return incluirAnexo;
    }

    public void setIncluirAnexo(Boolean incluirAnexo) {
        this.incluirAnexo = incluirAnexo;
    }

    public InputStream getCustomStream() {
        return customStream;
    }

    public void setCustomStream(InputStream customStream) {
        this.customStream = customStream;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido, boolean fornec) {
        this.pedido = pedido;
        fornecedor = fornec;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public void setCobranca(Pedido pedido) {
        cobranca = pedido;
    }

    public EmailBean getEmail() {
        return email;
    }

    public void setEmail(EmailBean email) {
        this.email = email;
    }

    private void loadProperties() {
        User user = (User) TApplication.getInstance().getUser();
        String from = user.getEmail();
        String server = user.getServerName();
        String userName = user.getUserName();

        email.setFrom(from);
        email.setServer(server);
        email.setUser(userName);

        incluirAnexo = false;
    }

    public void setTo(String[] to) {
        email.setTo(to);
    }

    public String retiraEspaco(String nome) {
        return nome.replace(' ', '_');
    }

    public boolean sendMail() throws Exception {
        TApplication app = TApplication.getInstance();
        
        String fileName = "venda.pdf";
        if (pedido != null) {
            fileName = pedido.getIdPedido() + "_" + pedido.getCliente().getRazao() + ".pdf";
            if (pedido.isPrePedido()) {
                Messages.errorMessage("Pedido n\u00E3o autorizado.");
                return false;
            }
            if (pedido.getCliente().getLimiteCredito() == null) {
                Messages.errorMessage("Cliente n\u00E3o possui limite de cr\u00E9dito definido.");
                return false;
            } else 
            if (pedido.getCliente().getLimiteCredito().compareTo(pedido.getValorCliente()) < 0) {
                Messages.errorMessage("Valor do pedido n\u00E3o pode ser maior que o limite de cr\u00E9dito do cliente.");
                return false;
            }
            email.setSubject("Pedido " + pedido.getIdPedido() + " - " + pedido.getCliente().getRazao() + " - " + pedido.getRepres().getRazao());
        } else if (cobranca != null) {
            fileName = cobranca.getIdPedido() + "_" + cobranca.getCliente().getRazao() + ".pdf";
            fileName = fileName.replace(" ", "_");
            //if (cobranca.isPreCobranca()) {
            //    Messages.errorMessage("Demonstrativo n\u00E3o est? autorizado.");
            //    return false;
           // }
            email.setSubject("Demonstrativo Pedido " + cobranca.getIdPedido() + " - " + cobranca.getCliente().getRazao() + " - " + cobranca.getRepres().getRazao());

        }
        Params params = getParams();
        if (cobranca != null) {
            email.setText(params.getEmailMsgCobranca());
        }
        
        if (pedido != null) {
            if (fornecedor) {
                if (!app.isGrant("ENVIAR_PEDIDO_PARA_FORNECEDOR"))
                    return false;
                email.setText(params.getEmailMsg());
            } else {
                if (!app.isGrant("ENVIAR_PEDIDO_PARA_CLIENTE"))
                    return false;
                email.setText(params.getEmailMsgCliente());
            }
        }
        
        if (orcamento != null) {
            if (!app.isGrant("ENVIAR_ORCAMENTO_POR_EMAIL"))
                return false;
            email.setText(params.getEmailMsgCliente());
        }
        
        EmailPanel editPanel = new EmailPanel();
        User user = app.getUser();

        String from = user.getEmail();
        String server = user.getServerName();

        boolean result = false;
        if ((from == null) || (from.length() == 0)) {
            Messages.warningMessage(app.getResourceString("emailConfig"));
            return false;
        }
        if ((server == null) || (server.length() == 0)) {
            Messages.warningMessage(app.getResourceString("emailConfig"));
            return false;
        }
        EditDialog edtDlg = new EditDialog(app.getResourceString("sendEmail"));
        edtDlg.setEditPanel(editPanel);
        UserDao userDao = (UserDao) TApplication.getInstance().lookupService("userDao");

        List<Vendedor> vendedores = new ArrayList<>();
        if ((cobranca != null) || (pedido != null)) {
            if (cobranca == null) {
                //vendedores = pedido.getCliente().getVendedores();
                vendedores.add(pedido.getVendedor());
            } else {
                //vendedores = cobranca.getCliente().getVendedores();
                vendedores.add(cobranca.getVendedor());
            }

            ArrayList itens = new ArrayList();
            for (String item : email.getTo()) {
                itens.add(item);
            }
            for (Vendedor tmp : vendedores) {
                User u = userDao.getUserByVendedor(tmp.getIdVendedor());
                if (u != null) {
                    itens.add(u.getEmail());
                }
            }
            email.setTo(itens);
        }
        while (edtDlg.edit(email)) {
            if (email.getTo().length == 0) {
                Messages.errorMessage("Inclua um destinat?rio.");
                continue;
            }
            
            Properties props = new Properties();
            
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.host", server); //server SMTP do GMAIL
            props.put("mail.smtp.auth", "true"); //ativa autenticacao
            props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", user.getPort()); //porta
            props.put("mail.smtp.auth.mechanisms", "PLAIN");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.enable", "true");
            
            SimpleAuth auth = null;
            auth = new SimpleAuth(from, user.getEmailPasswd().toString());
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(false); //Habilita o LOG das a??es executadas durante o envio do email

            //Objeto que cont?m a mensagem
            Message msg = new MimeMessage(session);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(email.getText() + '\n' + user.getAssinatura());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = null;

            if ((bytes == null) && (customStream == null)) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                JRAbstractExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
                exporter.exportReport();
                source = new ByteArrayDataSource(stream.toByteArray(), "application/pdf");
            } else if (customStream != null) {
                source = new ByteArrayDataSource(customStream, "application/pdf");
            } else if (bytes != null) {
                source = new ByteArrayDataSource(bytes, "application/pdf");
            }
            
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            if (incluirAnexo) {
                List<ArquivoPedido> lista = anexos; //cobranca.getArquivos();
                for (ArquivoPedido arq : lista) {
                    try {
                        DataSource src = new ByteArrayDataSource(arq.getBlob().getBinaryStream(), "application/pdf");
                        messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setDataHandler(new DataHandler(src));
                        messageBodyPart.setFileName(arq.getDescricao());
                        multipart.addBodyPart(messageBodyPart);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.errorMessage("Falha ao incluir anexo");
                        break;
                    }
                }
            }
            
            if (email.getIncluiAnexoPadrao() && params.getAnexoPadrao() != null) {
                try {
                        DataSource src = new ByteArrayDataSource(params.getAnexoPadrao(), "application/pdf");
                        messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setDataHandler(new DataHandler(src));
                        messageBodyPart.setFileName("promo.pdf");
                        multipart.addBodyPart(messageBodyPart);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.errorMessage("Falha ao incluir anexo");
                        break;
                    }
            }
            
            if (orcamento != null) {
                List<ArquivoOrcamento> lista = orcamento.getArquivos();
                for (ArquivoOrcamento arq : lista) {
                    try {
                        DataSource src = new ByteArrayDataSource(arq.getBlob().getBinaryStream(), "application/pdf");
                        messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setDataHandler(new DataHandler(src));
                        messageBodyPart.setFileName(arq.getDescricao());
                        multipart.addBodyPart(messageBodyPart);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.errorMessage("Falha ao incluir anexo");
                        break;
                    }
                }
            }
            
            msg.setContent(multipart);
            
            Set<String> destinatarios = new HashSet();

            //Setando o destinatario
            for (String s : email.getTo()) {
                //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
                destinatarios.add(s);
            }
            
            //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
            destinatarios.add(from);
            
            if (pedido != null || cobranca != null) {
                User u = userDao.getUserByVendedor(1);
                //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
                destinatarios.add(u.getEmail());
            }
            
            for (String s : destinatarios) {
                msg.addRecipient(Message.RecipientType.BCC ,new InternetAddress(s));
            }
            
            //Setando a origem do email
            msg.setFrom(new InternetAddress(from));
            //Setando o assunto
            msg.setSubject(email.getSubject());

            //Objeto encarregado de enviar os dados para o email
            Transport tr;
            
            try {
                tr = session.getTransport("smtp"); //define smtp para transporte

                tr.connect(server, user.getPort(), from, user.getEmailPasswd());
                msg.saveChanges(); // don't forget this
                tr.sendMessage(msg, msg.getAllRecipients());
                tr.close();
                result = true;
                
                if (pedido != null) {
                    updatePedido();
                }
                
                if (cobranca != null) {
                    updateCobranca();
                }
                
                Messages.infoMessage("Mensagem enviada.");
                break;
            } catch (Exception e) {
                System.out.println(">> Erro: Envio Mensagem");
                e.printStackTrace();
                throw e;
            }
        }
        
        return result;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    void updatePedido() {
        boolean atualiza = false;
        if (fornecedor) {
            if (!"1".equals(pedido.getEmitido())) {
                pedido.setEmitido("1");
                atualiza = true;
            }
        } else {
            if (!"1".equals(pedido.getEmitidoCliente())) {
                pedido.setEmitidoCliente("1");
                atualiza = true;
            }
        }
        if (atualiza) {
            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
            dao.setEmitido(pedido);
        }

    }

    private void updateCobranca() {
        boolean atualiza = false;
        if (!"1".equals(cobranca.getEmitidoCobranca())) {
            cobranca.setEmitidoCobranca("1");
            atualiza = true;
        }
        if (atualiza) {
            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
            dao.setEmitidoCobranca(cobranca);
            parentViewFrame.atualizaEmitidoCobranca();
        }

    }

    private Params getParams() {
        EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
        Object o = new Integer(-1);
        Params value = null;
        try {
            value = (Params) empresaDao.findById(Params.class, o);
        } catch (Exception e) {
        }
        return value;

    }

    public List<ArquivoPedido> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<ArquivoPedido> anexos) {
        this.anexos = anexos;
    }
    
    
}
