/**
 * ReferenciaInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.Referencia;
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
public class ReferenciaInternalFrame extends TableViewFrame {

    public ReferenciaInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Referências comerciais", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(300);
    }

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        ReferenciaEditPanel editPanel = new ReferenciaEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addReferenciaTitle"));
        edtDlg.setEditPanel(editPanel);
        Referencia segMercado = new Referencia();
        while (edtDlg.edit(segMercado)) {
            try {
                getTableModel().insertRecord(segMercado);
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
        ReferenciaEditPanel editPanel = new ReferenciaEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editReferenciaTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_REFERENCIA"));
        Object segMercado = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(segMercado)) {
            try {
                vtm.updateRow(segMercado);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
