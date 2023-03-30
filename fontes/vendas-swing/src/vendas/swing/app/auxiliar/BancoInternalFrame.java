/**
 * BancoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.app.auxiliar;

import vendas.entity.Banco;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class BancoInternalFrame extends TableViewFrame {

    public BancoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("bancosTitle"));
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_BANCO")) {
            super.remove();
        }
    }

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_BANCO")) {
            super.insert();
        }
    }
    
    @Override
    protected void insertRow() {
        BancoEditPanel editPanel = new BancoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addBancoTitle"));
        edtDlg.setEditPanel(editPanel);
        Banco banco = new Banco();
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
        BancoEditPanel editPanel = new BancoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editBancoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_BANCO"));
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
