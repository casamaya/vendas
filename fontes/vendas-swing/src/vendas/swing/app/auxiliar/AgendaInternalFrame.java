/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.AgendaFilter;
import vendas.dao.AgendaDao;
import vendas.entity.Agenda;
import vendas.entity.User;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.AgendaModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author sam
 */
public class AgendaInternalFrame extends TableViewFrame {

    TApplication app = TApplication.getInstance();

    public AgendaInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Agenda", tableModel);
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

        JMenuItem deleteMenuItem = new JMenuItem();
        deleteMenuItem.setEnabled(TApplication.getInstance().isGrant("EXCLUIR_AGENDA"));
        deleteMenuItem.setText(getBundle().getString("excluir"));

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                if (!Messages.deleteQuestion()) {
                    return;
                }
                AgendaDao dao = new AgendaDao();
                try {
                for (int i : rows) {
                    dao.deleteRow(getTableModel().getObject(i));
                }
                } catch (Exception e) {
                    getLogger().error(e.getMessage(), e);
                    Messages.errorMessage("Falha ao excluir.");
                }
                refresh();
            }
        });

        popupMenu.add(deleteMenuItem);

        JMenuItem editMenuItem = new JMenuItem();
        editMenuItem.setEnabled(TApplication.getInstance().isGrant("ALTERAR_AGENDA"));
        editMenuItem.setText("Copiar");

        editMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                Agenda value = editMultipleRow();
                Agenda agenda;
                Agenda nova;
                if (value != null) {
                    try {
                    for (int i : rows) {
                        agenda = (Agenda)getTableModel().getObject(i);
                        nova = new Agenda();
                        nova.setUsuario(app.getUser());
                        if (value.getDtevento() != null)
                            nova.setDtevento(value.getDtevento());
                        else
                            nova.setDtevento(agenda.getDtevento());
                        
                        if (value.getDescricao().trim() != null && value.getDescricao().trim().length() > 0)
                            nova.setDescricao(value.getDescricao());
                        else
                            nova.setDescricao(agenda.getDescricao());
                        
                        getTableModel().insertRecord(nova);
                    }
                    } catch (Exception e) {
                        getLogger().error(e.getMessage(), e);
                        Messages.errorMessage("Falha ao atualizar.");
                    }
                    refresh();
                }
            }
        });

        popupMenu.add(editMenuItem);

        setPopupMenu(popupMenu);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        getColumn(AgendaModel.DTEVENTO).setPreferredWidth(85);
        getColumn(AgendaModel.DTEVENTO).setMaxWidth(85);
        getColumn(AgendaModel.DTEVENTO).setMinWidth(85);
        getColumn(AgendaModel.DESCRICAO).setPreferredWidth(700);
        getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
        createPopupMenu();
    }

    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_AGENDA")) {
            super.remove();
        }
    }

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_AGENDA")) {
            super.insert();
        }
    }

    public void executeFilter(AgendaFilter filter) {
        setFilterObject(filter);
        doRefresh();
    }

    @Override
    public void resetFilterObject() {
        AgendaFilter filter = new AgendaFilter();
        Date dt = DateUtils.parse(DateUtils.format(new Date()));
        filter.setDtInicio(dt);
        filter.setDtFim(null);
        filter.setUsuario(app.getUser().getUserName());
        setFilterObject(filter);
    }

    @Override
    protected void editRow() {
        int rows[] = getTable().getSelectedRows();
        if (rows.length > 1) {
            Messages.errorMessage("Selecione apenas uma linha");
            return;
        }
        
        ServiceTableModel vtm = getTableModel();
        AgendaEditPanel editPanel = new AgendaEditPanel();
        EditDialog edtDlg = new EditDialog("Alterar evento");

        edtDlg.setEditPanel(editPanel, !app.isGrant("ALTERAR_AGENDA"));

        Object banco = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(banco)) {
            try {
                vtm.updateRow(banco);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private Agenda editMultipleRow() {
        ServiceTableModel vtm = getTableModel();
        AgendaEditPanel editPanel = new AgendaEditPanel();
        EditDialog edtDlg = new EditDialog("Alterar evento");

        edtDlg.setEditPanel(editPanel, !app.isGrant("ALTERAR_AGENDA"));

        Agenda agenda = new Agenda();
        while (edtDlg.edit(agenda)) {
            if (agenda.getDtevento() != null || agenda.getDescricao() != null) {
                return agenda;
            }
        }
        return null;
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        AgendaFilter filtro = new AgendaFilter();
        EditDialog edtDlg = new EditDialog("Filtrar");
        IntervaloFilterPanel pedidoFilter = new IntervaloFilterPanel();
        pedidoFilter.init();
        edtDlg.setEditPanel(pedidoFilter);
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

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        Agenda agenda = new Agenda();
        try {
            User o = (User) getTableModel().getDao().findById(User.class, app.getUser().getUserName());
            agenda.setUsuario(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AgendaEditPanel panel = new AgendaEditPanel();
        EditDialog edtDlg = new EditDialog("Incluir evento");
        edtDlg.setEditPanel(panel);
        while (edtDlg.edit(agenda)) {
            try {
                getTableModel().insertRecord(agenda);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
