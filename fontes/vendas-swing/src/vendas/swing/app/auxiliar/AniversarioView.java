/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.app.auxiliar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import ritual.swing.TApplication;
import vendas.beans.Aniversariante;
import vendas.beans.AniversarioFilter;
import vendas.beans.EmailBean;
import vendas.dao.EmpresaDao;
import vendas.entity.Params;
import vendas.entity.Repres;
import vendas.entity.User;
import vendas.exception.DAOException;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.AniversarianteModel;
import vendas.util.EditDialog;
import vendas.util.EmailClientePanel;
import vendas.util.EmailUtil;
import vendas.util.Messages;

/**
 *
 * @author sam
 */
public class AniversarioView extends TableViewFrame {

    AniversarioController controller = new AniversarioController();

    public AniversarioView() throws Exception {
        super("Aniversariantes");
    }

    @Override
    public void init() {
        AniversarioFilter filter = new AniversarioFilter();
        setFilterObject(filter);

        AniversarianteModel model = new AniversarianteModel(controller.getAniversariantes(filter));
        setTableModel(model);
        super.init();
        getColumn(AniversarianteModel.NOME).setPreferredWidth(200);
        getColumn(AniversarianteModel.EMAIL).setPreferredWidth(400);
        getColumn(AniversarianteModel.RESPONSAVEL).setPreferredWidth(300);
        getColumn(AniversarianteModel.OBSERVACAO).setPreferredWidth(300);
        getTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view();
                }
            }
        });
        getTable().setRowSelectionAllowed(true);
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        createPopupMenu();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_ANIVERSARIO")) {
            super.remove();
        }
    }   

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_ANIVERSARIO")) {
            super.insert();
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
        
        JMenuItem emailMenuItem = new JMenuItem();
        emailMenuItem.setText("Enviar email");
        emailMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TApplication app = TApplication.getInstance();

                if (!app.isGrant("ENVIAR_EMAIL_ANIVERSARIO"))
                    return;
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                EmailBean email = new EmailBean();
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
                List<String> erros = new ArrayList<String>();
                List<String> destinos = new ArrayList<String>();
                int enviados = 0;
                
                EmailUtil eu = new EmailUtil();
                
                EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
                Object o = new Integer(-1);
                Params value;
                try {
                    value = (Params) empresaDao.findById(Params.class, o);
                } catch (DAOException ex) {
                    Logger.getLogger(AniversarioView.class.getName()).log(Level.SEVERE, null, ex);
                    Messages.errorMessage("Falha ao recuperar parâmetros");
                    return;
                }
                
                email.setText(value.getMsgAniversario());
                
                while (edtDlg.edit(email)) {
                    email.setSubject(email.getSubject());
                    email.setText(email.getText());
                    
                    for (int i : rows) {
                        Aniversariante aniv = (Aniversariante) getTableModel().getObject(i);
                        email.setTo(aniv.getEmail());
                        try {
                            //eu.sendMail(email);
                            destinos.add(aniv.getNome());
                            enviados++;
                        } catch (Exception e) {
                            erros.add(aniv.getNome());
                        }
                    }

                    break;

                }
                if (enviados > 0) {
                    StringBuilder sb =  new StringBuilder();
                    sb.append("Email para fornecedor: ").append(email.getSubject()).append("\n\n");
                    for(String s : destinos) {
                        sb.append(s).append("\n");
                    }
                    email.setSubject(sb.toString());
                    email.setTo(value.getEmail());

                    try {
                        eu.sendMail(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        erros.add(value.getEmail());
                    }
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

    @Override
    public void removeRow() {
        ServiceTableModel vtm = getTableModel();
        int i = getSelectedRow();
        Aniversariante aniversario = (Aniversariante) vtm.getObject(i);
        if (Aniversariante.VENDEDOR == aniversario.getClasse()) {
            Messages.errorMessage("Vendedor n\u00E3o pode ser excluído.");
        }
        try {
            controller.remove(aniversario);
        vtm.removeObject(i);
        getTableModel().fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    @Override
    public void doRefresh() {
        getTableModel().setItemList(controller.getAniversariantes((AniversarioFilter)getFilterObject()));
        getTableModel().fireTableDataChanged();
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        AniversarioFilter filtro = new AniversarioFilter();
        EditDialog edtDlg = new EditDialog(getBundle().getString("filterAniversarioTitle"));
        AniversarioFilterPanel pedidoFilter = new AniversarioFilterPanel();
        pedidoFilter.init();
        edtDlg.setEditPanel(pedidoFilter);
        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().setItemList(controller.getAniversariantes(filtro));
                getTableModel().fireTableDataChanged();
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    public void executeFilter(AniversarioFilter filter) {
        setFilterObject(filter);
        doRefresh();
    }

    @Override
    public void view() {
        ServiceTableModel vtm = getTableModel();
        int i = getSelectedRow();
        Aniversariante aniversario = (Aniversariante) vtm.getObject(i);
        if (aniversario != null) {
            if (Aniversariante.VENDEDOR == aniversario.getClasse()) {
                Messages.errorMessage("Vendedor n\u00E3o pode ser alterado.");
                return;
            }
            edit();
        }
    }
    
    
    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        int i = getSelectedRow();
        Aniversariante aniversario = (Aniversariante) vtm.getObject(i);
        aniversario = controller.edit(aniversario);
        vtm.getItemList().set(i, aniversario);
        getTableModel().fireTableDataChanged();
        getTable().setRowSelectionInterval(i, i);
    }
    
    
}
