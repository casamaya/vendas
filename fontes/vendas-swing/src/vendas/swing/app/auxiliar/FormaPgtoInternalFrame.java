/**
 * FormaPgtoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.FormaPgto;
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
public class FormaPgtoInternalFrame extends TableViewFrame {

    public FormaPgtoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("formasPgto"));
    }

    @Override
    public void viewRow() {
        editRow();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_FORMA_DE_PAGAMENTO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_FORMA_DE_PAGAMENTO")) {
            super.insert();
        }
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(1);
        col.setPreferredWidth(300);
    }

    @Override
    protected void insertRow() {
        FormaPgtoEditPanel editPanel = new FormaPgtoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addFormaPgtoTitle"));
        edtDlg.setEditPanel(editPanel);
        FormaPgto formaPgto = new FormaPgto();
        while (edtDlg.edit(formaPgto)) {
            try {
                getTableModel().insertRecord(formaPgto);
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
        FormaPgtoEditPanel editPanel = new FormaPgtoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editFormaPgtoTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_FORMA_DE_PAGAMENTO"));

        Object formaPgto = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(formaPgto)) {
            try {
                vtm.updateRow(formaPgto);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
