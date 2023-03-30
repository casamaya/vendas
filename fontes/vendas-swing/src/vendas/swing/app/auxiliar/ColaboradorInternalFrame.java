/**
 * BancoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.app.auxiliar;

import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.beans.ColaboradorFilter;
import vendas.entity.Colaborador;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class ColaboradorInternalFrame extends TableViewFrame {

    public ColaboradorInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle("Especificadores");
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(250);
        col = getColumn(1);
        col.setPreferredWidth(120);
        col = getColumn(2);
        col.setPreferredWidth(120);
        col = getColumn(5);
        col.setPreferredWidth(120);
        col = getColumn(6);
        col.setPreferredWidth(120);
        col = getColumn(7);
        col.setPreferredWidth(120);
        col = getColumn(8);
        col.setPreferredWidth(250);
    }
    
        @Override
    public void filter() {
        getLogger().info("filter");
        ColaboradorFilter filtro = new ColaboradorFilter();
        ColaboradorFilterPanel filterPanel = new ColaboradorFilterPanel();
        EditDialog edtDlg = new EditDialog("Filtrar colaboradores");
        edtDlg.setEditPanel(filterPanel);
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
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_COLABORADOR")) {
            super.remove();
        }
    }

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_COLABORADOR")) {
            super.insert();
        }
    }
    
    @Override
    protected void insertRow() {
        ColaboradorEditPanel editPanel = new ColaboradorEditPanel();
        EditDialog edtDlg = new EditDialog("Incluir colaborador");
        edtDlg.setEditPanel(editPanel);
        Colaborador banco = new Colaborador();
        while (edtDlg.edit(banco)) {
            try {
                getTableModel().insertRecord(banco);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        ColaboradorEditPanel editPanel = new ColaboradorEditPanel();
        EditDialog edtDlg = new EditDialog("Alterar colaborador");
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_COLABORADOR"));
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
}
