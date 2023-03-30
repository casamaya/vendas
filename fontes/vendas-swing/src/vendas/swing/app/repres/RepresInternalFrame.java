/*
 * RepresInternalFrame.java
 *
 * Created on 27/06/2007, 19:56:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.repres;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.model.RepresTableModel;
import vendas.entity.Repres;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import vendas.beans.EmailBean;
import vendas.beans.RepresFilter;
import vendas.dao.EmpresaDao;
import vendas.entity.Params;
import vendas.entity.User;
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
public class RepresInternalFrame extends TableViewFrame {

    public RepresInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Fornecedores", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        getLogger().info("initComponents ini");
        Repres repres = new Repres();
        repres.setInativo('A');
        RepresFilter filter = new RepresFilter();
        filter.setRepres(repres);
        setFilterObject(filter);
        TableColumn col = getColumn(RepresTableModel.RAZAO);
        col.setPreferredWidth(230);
        col = getColumn(RepresTableModel.CIDADE);
        col.setPreferredWidth(150);
        col = getColumn(RepresTableModel.ATENDIMENTO);
        col.setPreferredWidth(80);
        col = getColumn(RepresTableModel.FONE1);
        col.setPreferredWidth(120);
        col = getColumn(RepresTableModel.FONE2);
        col.setPreferredWidth(120);
        col = getColumn(RepresTableModel.FONE3);
        col.setPreferredWidth(120);
        createPopupMenu();
        getTable().setRowSelectionAllowed(true);
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getLogger().info("initComponents end");
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
                edtDlg.setEditPanel(editPanel);
                User user = (User) TApplication.getInstance().getUser();

                String from = user.getEmail();
                String server = user.getServerName();
                email.setServer(server);
                email.setFrom(from);
                email.setPort(user.getPort());
                email.setPassword(user.getEmailPasswd());
                email.setAssinatura(user.getAssinatura());
                List<String> erros = new ArrayList<>();
                List<String> destinos = new ArrayList<>();
                int enviados = 0;
                
                EmailUtil eu = new EmailUtil();
                
                while (edtDlg.edit(email)) {
                    email.setSubject(email.getSubject());
                    email.setText(email.getText());
                    
                    for (int i : rows) {
                        Repres fornecedor = (Repres) getTableModel().getObject(i);
                        email.setTo(fornecedor.getEmail());
                        try {
                            eu.sendMail(email);
                            destinos.add(fornecedor.getRazao());
                            enviados++;
                        } catch (Exception e) {
                            erros.add(fornecedor.getRazao());
                        }
                    }

                    break;

                }
                if (enviados > 0) {
                    email.setSubject("Email para fornecedor: ");

                    EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
                    Params value = null;
                    try {
                        value = (Params) empresaDao.findById(Params.class, new Integer(-1));
                        email.setTo(value.getEmail());
                        eu.sendMail(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        erros.add(value.getEmail() + "\n");
                    }
                    Messages.infoMessage("Mensagem enviada.");
                }
                if (erros.size() > 0) {
                    Messages.errorMessage("Erro com o email de " + erros);
                }

            }
        });
        emailMenuItem.setEnabled(TApplication.getInstance().isGrant("ENVIAR_EMAIL_FORNECEDOR"));
        popupMenu.add(emailMenuItem);
        setPopupMenu(popupMenu);
    }
    
    @Override
    public void showReport() {
        Object[] options = {getBundle().getString("relacao"), getBundle().getString("contasCorrente"), getBundle().getString("metaVenda"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        switch (n) {
            case 0: super.showReport(); break;
            case 1: showContas(); break;
            case 2: showMetas(); break;
        }
    }

    private void showContas() {
        URL url = getClass().getResource(Constants.JRCONTAREPRES);
        String reportTitle = "Contas corrente";
        RepresFilter filter = (RepresFilter)getFilterObject();
        String subTitle = "";
        RepresDao dao = (RepresDao) getTableModel().getDao();
        try {
            Reports.showReport(reportTitle, null, url, dao.getContas(filter));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    private void showMetas() {
        URL url = getClass().getResource(Constants.JRREPRESMETAS);
        String reportTitle = "Metas de vendas";
        try {
            Reports.showReport(reportTitle, null, url, getTableModel().getItemList());
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }
    
    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_FORNECEDOR"))
            super.insert();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_FORNECEDOR"))
            super.remove();
    }
    
    @Override
    protected void insertRow() {
        RepresEditPanel editPanel = new RepresEditPanel();
        Repres repres = new Repres();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRepresTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(repres)) {
            try {
                getTableModel().insertRecord(repres);
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
        RepresEditPanel editPanel = new RepresEditPanel();
        Object repres = vtm.getObject(getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRepresTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_FORNECEDOR"));
        while (edtDlg.edit(repres)) {
            try {
                vtm.updateRow(repres);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void viewRow() {
        Repres repres = (Repres) getTableModel().getObject(getSelectedRow());
        RepresViewFrame viewFrame = new RepresViewFrame("Visualizar " + repres.getRazao());
        viewFrame.setRepresDao((RepresDao) getTableModel().getDao());
        TApplication.getInstance().getDesktopPane().add(viewFrame);
        viewFrame.setLocation(0, 0);
        viewFrame.setVisible(true);
        viewFrame.execute(repres);
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        RepresFilterPanel filterPanel = new RepresFilterPanel();
        RepresFilter filtro = (RepresFilter) getFilterObject();
        EditDialog edtDlg = new EditDialog(getBundle().getString("findRepresTitle"));
        edtDlg.setEditPanel(filterPanel);
        while (edtDlg.edit(filtro.getRepres())) {
            try {
                //Repres repres = (Repres)filtro;
                getTableModel().select(filtro);
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    public void executeFilter(String toUpperCase) {
        RepresFilter represFilter = createFilter();
        represFilter.getRepres().setRazao(toUpperCase);
        represFilter.getRepres().setInativo('T');
        setFilterObject(represFilter);
        doRefresh();
        if (getTableModel().getItemList().isEmpty()) {
            Messages.errorMessage(getBundle().getString("emptyList"));
        }
    }

    @Override
    public void resetFilterObject() {
        createFilter();
    }


        private RepresFilter createFilter() {
        Repres repres = new Repres();
        repres.setInativo('A');
        RepresFilter filter = new RepresFilter();
        filter.setRepres(repres);
        return filter;
    }
}
