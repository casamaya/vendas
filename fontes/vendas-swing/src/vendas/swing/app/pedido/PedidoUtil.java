/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import vendas.dao.EmpresaDao;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.Main;
import vendas.beans.EmailBean;
import vendas.beans.PedidoBean;
import vendas.beans.PedidoFilter;
import vendas.beans.PedidoPrint;
import vendas.dao.PedidoDao;
import vendas.dao.UserDao;
import vendas.dao.VendedorDao;
import vendas.entity.ClienteContato;
import vendas.entity.ItemPedido;
import vendas.entity.Orcamento;
import vendas.entity.Params;
import vendas.entity.Pedido;
import vendas.entity.RepresContato;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.EmailUtil;
import vendas.util.Messages;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class PedidoUtil {

    public Map getReportMap(String title, String subTitle) {
        Map model = TApplication.getInstance().getDefaultMap(title, subTitle);
        //model.put("promoLogo", getClass().getResource(Constants.PROMOIMAGE));
        return model;
    }

    public void viewPedido(Pedido pedido) {
        viewPedido(pedido.getIdPedido());
    }

    public void viewPedido() {
        String pedidoText = JOptionPane.showInputDialog(null, "Pedido a localizar", "Procurar", JOptionPane.QUESTION_MESSAGE);
        if ((pedidoText == null) || (pedidoText.length() == 0)) {
            return;
        }
        Integer id;
        try {
            id = Integer.decode(pedidoText);
        } catch (Exception e) {
            Messages.errorMessage(TApplication.getInstance().getResourceString("dataMsgError"));
            return;
        }
        viewPedido(id);
    }

    public void viewPedido(Integer id) {


        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        Pedido pedido = null;
        try {
            PedidoFilter filtro = new PedidoFilter();
            filtro.setPedido(id);
            List<Pedido> lista = pedidoDao.findByExample(filtro);
            for (Pedido p : lista) {
                if (p.getRepres().getAtivo()) {
                    pedido = p;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pedido == null) {
            Messages.errorMessage(TApplication.getInstance().getResourceString("pedidoNaoEncontrado"));
            return;
        }
        
        visualizarPedido(pedido);
    }
    
    public void viewPedido(Integer id, Main mainapp) {
        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        Pedido pedido = null;
        try {
            PedidoFilter filtro = new PedidoFilter();
            filtro.setPedido(id);
            List<Pedido> lista = pedidoDao.findByExample(filtro);
            
            if (lista.isEmpty()) {
                Messages.errorMessage(TApplication.getInstance().getResourceString("pedidoNaoEncontrado"));
                return;
            }
            
            if (lista.size() == 1) {
                pedido = lista.get(0);
                visualizarPedido(pedido);
            } else {
                PedidoInternalFrame frame = (PedidoInternalFrame) TApplication.getInstance().lookupService("pedidoInternalFrame");
                mainapp.newInternalFrame(frame);
                frame.filtrarPedido(id);
                frame.doRefresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    private void visualizarPedido(Pedido pedido) {
        String pedidoTitle = "Visualizar pedido";
        if (!TApplication.getInstance().locateFrame(pedidoTitle)) {
            PedidoViewFrame viewFrame = new PedidoViewFrame(pedidoTitle);
            TApplication.getInstance().getDesktopPane().add(viewFrame);
            viewFrame.setLocation(0,0);
            viewFrame.setVisible(true);
            viewFrame.execute(pedido);
        } else {
            PedidoViewFrame viewFrame = (PedidoViewFrame) TApplication.getInstance().getFrame(pedidoTitle);
            viewFrame.execute(pedido);
        }
    }

    public void novoPedido() {
        if (!TApplication.getInstance().isGrant("INCLUIR_PEDIDO"))
            return;
        
        Pedido pedido = new Pedido();
        pedido.setDtPedido(new Date());
        pedido.setDtEntrega(new Date());
        PedidoEditPanel editPanel = new PedidoEditPanel();
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getResourceString("addPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setPedido(pedido);
        PedidoUtil util = new PedidoUtil();
        while (edtDlg.edit(pedidoBean, false)) {
            if (!util.validaPedido(pedido)) {
                    continue;
            }
            try {
                PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
                dao.insertRecord(pedido);
                dao.setNextValue(pedido.getIdPedido());
                RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
                represDao.addCliente(pedido.getRepres(), pedido.getCliente());
                viewPedido(pedido);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                Messages.errorMessage(TApplication.getInstance().getResourceString("saveErrorMessage"));
            }
        }
    }

    public void imprimirPedido(Pedido pedido) {
        ImprimirPedidoPanel editPanel = new ImprimirPedidoPanel();
        PedidoPrint referencia = new PedidoPrint();
        EditDialog edtDlg = new EditDialog("Imprimir pedido");
        edtDlg.setEditPanel(editPanel);
        if (edtDlg.edit(referencia)) {
            switch (referencia.getTipo()) {
                case 0:
                    imprimirPedido(pedido, true, referencia.getReferencia());
                    break;
                case 1:
                    imprimirPedido(pedido, false, referencia.getReferencia());
                    break;
                default:
                    imprimirPedidoCompleto(pedido);
                    break;
            }
        }
    }

    public void imprimirPedido(Pedido pedido, boolean fornecedor, boolean referencia) {
        RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        URL url;
        List<String> to = new ArrayList();
        if (fornecedor) {
            url = getClass().getResource(Constants.JRPEDIDOFORM);
            for (RepresContato contato : pedido.getRepres().getContatos()) {
                if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
        } else {
            url = getClass().getResource(Constants.JRPEDIDOCLIENTEFORM);
            for (ClienteContato contato : pedido.getClienteResponsavel().getContatos()) {
                if (ClienteContato.EMAILCOMERCIAL.equals(contato.getTipoContato()) || ClienteContato.EMAILFINANCEIRO.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
        }
        Map model = TApplication.getInstance().getDefaultMap(TApplication.getInstance().getResourceString("pedido"));
        model.put("params", params2Array());
        model.put("codigo", represDao.findCodigo(pedido.getCliente(), pedido.getRepres()));
        model.put("referencia", referencia);
        List lista = new ArrayList();
        lista.add(pedido);
        try {
            ReportViewFrame rvf = Reports.showReport(url, model, new PedidoDataSource(lista));
            rvf.setPedido(pedido, fornecedor);
            EmailBean eb = rvf.getEmail();
            eb.setTo(to);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.errorMessage(TApplication.getInstance().getResourceString("reportError"));
        }
    }

    public void imprimirOrcamento(Orcamento orcamento) {
        RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        URL url;
        url = getClass().getResource(Constants.JRORCAMENTOFORM);
        Map model = TApplication.getInstance().getDefaultMap(TApplication.getInstance().getResourceString("orcamento"));
        model.put("params", params2Array());
        model.put("codigo", represDao.findCodigo(orcamento.getCliente(), orcamento.getRepres()));
        List lista = new ArrayList();
        lista.add(orcamento);
        try {
            ReportViewFrame rvf = Reports.showReport(url, model, new OrcamentoDataSource(lista));
            EmailBean eb = rvf.getEmail();
            List<String> to = new ArrayList();
            for (ClienteContato contato : orcamento.getCliente().getContatos()) {
                if (ClienteContato.EMAILCOMERCIAL.equals(contato.getTipoContato()) || ClienteContato.EMAILFINANCEIRO.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
            for (RepresContato contato : orcamento.getRepres().getContatos()) {
                if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
            eb.setTo(to);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.errorMessage(TApplication.getInstance().getResourceString("reportError"));
        }
    }

    public Object[] params2Array() {
        EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
        Object o = new Integer(-1);
        Params value = null;
        Object[] params = null;
        try {
            value = (Params) empresaDao.findById(Params.class, o);
            Object[] params2 = {
                value.getRazao(),
                value.getEndereco(),
                value.getEmail(),
                StringUtils.formatarCep(value.getCep()),
                value.getCidade(),
                value.getUf(),
                value.getFone(),
                value.getPromocao(),
                value.getFone2()
            };
            params = params2;
        } catch (Exception e) {
            // TODO Tratar excessao
        }
        return params;
    }

    void imprimirPedidoCompleto(Pedido pedido) {
        RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        URL url;
        url = getClass().getResource(Constants.JRPEDIDOCOMPLETO);
        Map model = TApplication.getInstance().getDefaultMap(TApplication.getInstance().getResourceString("pedido"));
        model.put("params", params2Array());
        model.put("codigo", represDao.findCodigo(pedido.getCliente(), pedido.getRepres()));
        List lista = new ArrayList();
        lista.add(pedido);
        try {
            ReportViewFrame rvf = Reports.showReport(url, model, new PedidoDataSource(lista));
            EmailBean eb = rvf.getEmail();
            rvf.setPedido(pedido, true);
            List<String> to = new ArrayList();
            for (ClienteContato contato : pedido.getClienteResponsavel().getContatos()) {
                if (ClienteContato.EMAILCOMERCIAL.equals(contato.getTipoContato()) || ClienteContato.EMAILFINANCEIRO.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
            for (RepresContato contato : pedido.getRepres().getContatos()) {
                if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
            eb.setTo(to);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.errorMessage(TApplication.getInstance().getResourceString("reportError"));
        }
    }

    public boolean validaPedido(Pedido pedido) {
        PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        BigDecimal pgtosPendentes = dao.getPgtosClientePendende(pedido.getCliente().getIdCliente());
        if (pgtosPendentes == null) {
            pgtosPendentes = BigDecimal.ZERO;
        }
        BigDecimal vendasPendentes = dao.getVendasClientePendende(pedido.getCliente().getIdCliente());
        if (vendasPendentes == null) {
            vendasPendentes = BigDecimal.ZERO;
        }
        BigDecimal saldo = pgtosPendentes.add(vendasPendentes);
        BigDecimal limite = pedido.getCliente().getLimiteCredito();
        if (limite == null) {
            limite = BigDecimal.ZERO;
        }

        BigDecimal total = saldo.add(pedido.getValor());

        if (!("ANTECIPADO").equals(pedido.getFormaPgto().getNome())) {
            if (total.compareTo(limite) >= 0) {
                Messages.errorMessage("O cliente n\u00E3o possui saldo dispon�vel e n\u00E3o poder� ter pedido emitido.");
                return false;
            }
        }
        
        List<ItemPedido> itens = (List)pedido.getItens();
        if (itens == null || itens.isEmpty()) {
            Messages.errorMessage("Pedido sem itens.");
            return false;
        }
        return true;

    }

    void obsAtendimentoPedido(Pedido pedido) {
        ObsAtendimentoEditPanel editPanel = new ObsAtendimentoEditPanel();
        EditDialog edtDlg = new EditDialog( TApplication.getInstance().getResourceString("posicaoAtendimento"));
        edtDlg.setEditPanel(editPanel);
        boolean result = false;
        while (edtDlg.edit(pedido)) {
            try {
                PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
                dao.updateRow(pedido);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                Messages.errorMessage(TApplication.getInstance().getResourceString("saveErrorMessage"));
            }
        }
    }

    public void excluirAnexosPedido() {
        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        pedidoDao.excluirAnexosPedido();
    }

    public void enviarEmailClienteSemPedidoPorPrazo() {
        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        VendedorDao vendedorDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        UserDao userDao = (UserDao) TApplication.getInstance().lookupService("userDao");
        EmailBean email;
        EmailUtil emailUtil;
        List<Vendedor> vendedores = vendedorDao.findAll();
        List<Pedido> pedidos;
        List<String> erros;
        StringBuilder sbVendedor;
        StringBuilder sbFinal;

        if (vendedores == null || vendedores.isEmpty())
            return;

        email = new EmailBean();
        emailUtil = new EmailUtil();
        erros = new ArrayList();
        User user = userDao.getUserByVendedor(1);

        String from = user.getEmail();
        String server = user.getServerName();
        String userName = user.getUserName();
        Integer porta = user.getPort();

        String[] destinatarios = {"", ""};

        destinatarios[0] = from;

        email.setFrom(from);
        email.setServer(server);
        email.setUser(userName);
        email.setPort(porta);
        email.setSubject("Clientes sem pedidos h\u00E1 30 dias");
        
        Date dtAtual = DateUtils.getCurrentDate();

        for (Vendedor vendedor : vendedores) {
            if (!vendedor.isAtivo())
                continue;
            
            pedidos = pedidoDao.findClienteSemPedidoPorPrazo(vendedor.getIdVendedor(), 30, dtAtual);
            if (pedidos == null || pedidos.isEmpty()) {
                System.out.println("Sem pedidos a enviar para " + vendedor.getNome());
                continue;
            }
            sbVendedor = new StringBuilder();
            sbVendedor.append("<html>");
            sbVendedor.append("<p>").append(vendedor.getNome()).append(",").append("</p>");
            sbVendedor.append("<p>Abaixo lista de clientes que est�o a 30 dias sem pedidos:</p>");
            for (Pedido pedido : pedidos) {
                sbVendedor.append("<p>").append(pedido.getCliente().getRazao()).append("</p>");
            }
            sbVendedor.append("</html>");
            email.setText(sbVendedor.toString());
            user = userDao.getUserByVendedor(vendedor.getIdVendedor());
            if (user == null)
                continue;
            destinatarios[1] = user.getEmail();
            email.setTo(destinatarios);
            try {
                emailUtil.sendMail(email);
                System.out.println("Enviando mensagem para " + vendedor.getNome());
                for (Pedido pedido : pedidos) {
                    pedido.setEmailVendEnviado(1);
                    try {
                    pedidoDao.updatePedido(pedido);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }                
            } catch (Exception ex) {
                erros.add("Erro ao enviar email para " + vendedor.getNome());
                ex.printStackTrace();
            }
            if (!erros.isEmpty()) {
               //TODO
            }
        }
    }
}
