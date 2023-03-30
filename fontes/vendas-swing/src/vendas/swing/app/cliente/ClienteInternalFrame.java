/*
 * ClienteInternalFrame.java
 *
 * Created on 27/06/2007, 19:56:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.model.ClienteTableModel;
import vendas.entity.Cliente;
import vendas.entity.SituacaoCliente;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import vendas.beans.ClientesFilter;
import vendas.entity.Vendedor;
import vendas.dao.ClienteDao;
import vendas.dao.SituacaoClienteDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.EmailBean;
import vendas.dao.EmpresaDao;
import vendas.dao.UserDao;
import vendas.dao.VendedorDao;
import vendas.entity.Params;
import vendas.entity.User;
import vendas.entity.VisitaCliente;
import vendas.exception.DAOException;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.EmailClientePanel;
import vendas.util.EmailUtil;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ClienteInternalFrame extends TableViewFrame {

    private VendedorDao vendedorDao;
    private SituacaoClienteDao situacaoDao;

    public ClienteInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Clientes", tableModel);
    }

    public void setVendedorDao(VendedorDao srvc) {
        vendedorDao = srvc;
    }

    public void setSituacaoDao(SituacaoClienteDao srvc) {
        situacaoDao = srvc;
    }

    @Override
    public void initComponents() {
        getLogger().info("initComponents ini");
        final ServiceTableModel stm = getTableModel();
        JTable jt;
        jt = new JTable(stm) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                    int rowIndex, int vColIndex) {
                BigDecimal value = new BigDecimal(0);
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                c.setForeground(getForeground());
                Cliente p = (Cliente) stm.getObject(rowIndex);
                
                if ((vColIndex == ClienteTableModel.RAZAO) && "FINANCEIRO RUIM,INATIVO,FECHOU".contains(p.getSituacaoCliente().getNome())) {
                    c.setForeground(Color.RED);
                }
                if (vColIndex == ClienteTableModel.PGTOPENDENTE) {
                    c.setForeground(Color.RED);
                }
                if (vColIndex == ClienteTableModel.VALORPENDENTE) {
                    c.setForeground(Color.RED);
                }
                if (vColIndex == ClienteTableModel.NUMDIASATRASO) {
                    c.setForeground(Color.RED);
                }
                //else {
                //    c.setForeground(getForeground());
                //}
                return c;
            }
        };
        jt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setDefaultRenderer(Date.class, new DateCellRenderer());
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        setTable(jt);
        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        //setSize(400, 300);
        setPreferredSize(new Dimension(800, 400));
        setFilterObject(createFilter());
        TableColumn col = getColumn(ClienteTableModel.RAZAO);
        col.setPreferredWidth(270);
        col = getColumn(ClienteTableModel.REDUZIDO);
        col.setPreferredWidth(150);
        col = getColumn(ClienteTableModel.VENDEDOR);
        col.setPreferredWidth(140);
        col = getColumn(ClienteTableModel.PGTOPENDENTE);
        col.setPreferredWidth(100);
        col = getColumn(ClienteTableModel.VALORPENDENTE);
        col.setPreferredWidth(100);
        col = getColumn(ClienteTableModel.FONE1);
        col.setPreferredWidth(120);
        col = getColumn(ClienteTableModel.FONE2);
        col.setPreferredWidth(120);
        col = getColumn(ClienteTableModel.FONE3);
        col.setPreferredWidth(120);
        col = getColumn(ClienteTableModel.CIDADE);
        col.setPreferredWidth(100);
        col = getColumn(ClienteTableModel.CNPJ);
        col.setPreferredWidth(150);
        col = getColumn(ClienteTableModel.INSC);
        col.setPreferredWidth(150);
        col = getColumn(ClienteTableModel.SITUACAO);
        col.setPreferredWidth(130);
        col = getColumn(ClienteTableModel.ULTVISITA);
        col.setPreferredWidth(120);
        createPopupMenu();
        getLogger().info("initComponents end");
    }

    private ClientesFilter createFilter() {
        ClientesFilter clienteFilter = new ClientesFilter();
        SituacaoCliente situacao = new SituacaoCliente();
        TApplication app = TApplication.getInstance();
        
        situacao.setNome("TODOS");
        situacao.setPedido("1");
        Vendedor vendedor = new Vendedor();
        
        if (app.getUser().isVendedor()) {
            try {
                vendedor = (Vendedor)vendedorDao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (DAOException ex) {
                Logger.getLogger(ClienteInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            vendedor = new Vendedor();
            vendedor.setNome("TODOS");
        }
        
        clienteFilter.getCliente().setVendedor(vendedor);
        clienteFilter.getCliente().setSituacaoCliente(situacao);
        return clienteFilter;
    }

    public void executeFilter(String nome) {
        ClientesFilter clienteFilter = createFilter();
        clienteFilter.getCliente().setRazao(nome);
        clienteFilter.getCliente().getSituacaoCliente().setPedido("T");
        setFilterObject(clienteFilter);
        doRefresh();
        if (getTableModel().getItemList().isEmpty()) {
            Messages.errorMessage(getBundle().getString("emptyList"));
        }
    }

    @Override
    public void cancelFilter() {
        setFilterObject(createFilter());
        doRefresh();
    }

    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_CLIENTE")) {
            super.remove();
        }
    }    

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_CLIENTE")) {
            super.insert();
        }
    }
    
    @Override
    protected void insertRow() {
        ClienteEditPanel editPanel = new ClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        try {
            editPanel.setSituacoes(situacaoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }

        Cliente cliente = new Cliente();
        while (edtDlg.edit(cliente)) {
            try {
                if (TApplication.getInstance().getUser().isAdmin()) {
                    cliente.setBloqueado("N");
                } else {
                    cliente.setBloqueado("S");
                }
                getTableModel().insertRecord(cliente);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        ClienteEditPanel editPanel = new ClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editClienteTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_CLIENTE"));
        try {
            editPanel.setSituacoes(situacaoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        Object cliente = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(cliente)) {
            try {
                vtm.updateRow(cliente);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void showReport() {
        Object[] options = {getBundle().getString("relacao"), getBundle().getString("clientesEmail"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        switch (n) {
            case 0:
                showRelacao();
                break;
            case 1:
                showRelacaoEmail();
                break;
        }

    }

    public void showRelacao() {
        URL url;
        url = getClass().getResource(Constants.JRCLIENTES);
        String reportTitle = this.getTitle();
        FichaClienteDataSource ds = new FichaClienteDataSource(getTableModel().getItemList());
        try {
            Reports.showReport(reportTitle, "", url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    public void showRelacaoEmail() {
        URL url;
        url = getClass().getResource(Constants.JRCLIENTESEMAIL);
        String reportTitle = this.getTitle();
        try {
            Reports.showReport(reportTitle, "", url, getTableModel().getItemList());
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    @Override
    public void viewRow() {
        Cliente cliente = (Cliente) getTableModel().getObject(getSelectedRow());
        ClienteViewFrame viewFrame = new ClienteViewFrame("Visualizar " + cliente.getRazao());
        viewFrame.setVendedorDao(vendedorDao);
        viewFrame.setSituacaoDao(situacaoDao);
        viewFrame.setClienteDao((ClienteDao) getTableModel().getDao());
        TApplication.getInstance().getDesktopPane().add(viewFrame);
        viewFrame.setLocation(0, 0);
        viewFrame.setVisible(true);
        viewFrame.execute(cliente);
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        ClienteFilterPanel filterPanel = new ClienteFilterPanel();
        filterPanel.init();
        EditDialog edtDlg = new EditDialog(getBundle().getString("findClienteTitle"));
        edtDlg.setEditPanel(filterPanel);
        Object filtro = getFilterObject();
        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }
    
    public void registrarNaoVisita() {
        NaoVisitaPanel editPanel = new NaoVisitaPanel();
        editPanel.setNaoVisita(Boolean.TRUE);
        ClienteDao clienteDao = (ClienteDao)getTableModel().getDao();
        Vendedor vendedor = null;
        
        try {
            editPanel.setVendedores(vendedorDao.findAllAtivos());
            editPanel.setClientes(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        
        editPanel.init();
        editPanel.habilitarCliente(false);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVisitaTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("INCLUIR_VISITA"));
        Cliente cliente = (Cliente) getTableModel().getObject(getSelectedRow());

        VisitaCliente visita = new VisitaCliente();
        visita.setCliente(cliente);
        visita.setDtVisita(new Date());
        visita.setTipoVisita("N");
        
        TApplication app = TApplication.getInstance();
        
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        
        visita.setVendedor(vendedor);
        
        while (edtDlg.edit(visita)) {
            try {
                clienteDao.insertRecord(visita);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

    }
    
    public void registrarVisita() {
        NaoVisitaPanel editPanel = new NaoVisitaPanel();
        ClienteDao clienteDao = (ClienteDao)getTableModel().getDao();
        Vendedor vendedor = null;
        
        try {
            editPanel.setVendedores(vendedorDao.findAllAtivos());
            editPanel.setClientes(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        
        editPanel.init();
        editPanel.habilitarCliente(false);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVisitaTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("INCLUIR_VISITA"));
        Cliente cliente = (Cliente) getTableModel().getObject(getSelectedRow());

        VisitaCliente visita = new VisitaCliente();
        visita.setCliente(cliente);
        visita.setDtVisita(DateUtils.getCurrentDate());
        visita.setDtVisitaRealizada(visita.getDtVisita());
        visita.setTipoVisita("V");
        
        TApplication app = TApplication.getInstance();
        
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        
        visita.setVendedor(vendedor);
        
        while (edtDlg.edit(visita)) {
            try {
                clienteDao.insertRecord(visita);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

    }
    
    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem todosMenuItem = new JMenuItem();
        todosMenuItem.setText(getBundle().getString("selecionarTodos"));
        todosMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                getTable().selectAll();
            }
        });
        popupMenu.add(todosMenuItem);

        JMenuItem naoVisitaMenuItem = new JMenuItem();
        naoVisitaMenuItem.setText("Registrar n\u00E3o visita");
        naoVisitaMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                registrarNaoVisita();
            }

        });
        popupMenu.add(naoVisitaMenuItem);
        //
        JMenuItem visitaMenuItem = new JMenuItem();
        visitaMenuItem.setText("Registrar visita");
        visitaMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                registrarVisita();
            }

        });
        popupMenu.add(visitaMenuItem);
        //
        JMenuItem emailMenuItem = new JMenuItem();
        emailMenuItem.setText("Enviar email");
        emailMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                EmailBean email = new EmailBean();
                TApplication app = TApplication.getInstance();
                EditDialog edtDlg = new EditDialog(app.getResourceString("sendEmail"));
                EmailClientePanel editPanel = new EmailClientePanel();
                edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ENVIAR_EMAIL_CLIENTE"));
                User user = (User) TApplication.getInstance().getUser();

                String from = user.getEmail();
                String server = user.getServerName();
                email.setServer(server);
                email.setFrom(from);
                email.setPort(user.getPort());
                email.setPassword(user.getEmailPasswd());
                email.setAssinatura(user.getAssinatura());
                List<String> erros = new ArrayList<String>();
                int enviados = 0;

                Set<Vendedor> vendedores = new HashSet<Vendedor>();

                while (edtDlg.edit(email)) {
                    email.setSubject(email.getSubject());
                    email.setText(email.getText());
                    EmailUtil eu = new EmailUtil();
                    for (int i : rows) {
                        Cliente cli = (Cliente) getTableModel().getObject(i);
                        email.setTo(cli.getEmailMalaDireta2());
                        vendedores.addAll(cli.getVendedores());
                        try {
                            eu.sendMail(email);
                            enviados++;
                        } catch (Exception e) {
                            erros.add(cli.getRazao());
                        }
                    }

                    UserDao ud = (UserDao) TApplication.getInstance().lookupService("userDao");
                    email.setSubject("Email para cliente: " + email.getSubject());
                    for (Vendedor v : vendedores) {
                        User u = ud.getUserByVendedor(v.getIdVendedor());
                        email.setTo(u.getEmail());
                        try {
                            eu.sendMail(email);
                        } catch (Exception e) {
                            e.printStackTrace();
                            erros.add(v.getNome());
                        }
                    }
                    EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
                    Object o = new Integer(-1);
                    Params value = null;
                    try {
                        value = (Params) empresaDao.findById(Params.class, o);
                        email.setTo(value.getEmail());
                        eu.sendMail(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        erros.add(value.getEmail());
                    }
                    break;

                }
                if (enviados > 0) {
                    Messages.infoMessage("Mensagem enviada.");
                }
                if (erros.size() > 0) {
                    Messages.errorMessage("Erro com o email de " + erros);
                }

            }
        });
        popupMenu.add(emailMenuItem);
        setPopupMenu(popupMenu);
    }
}
