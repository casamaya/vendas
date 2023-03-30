/**
 * SituacaoClienteInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.SituacaoCliente;
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
public class SituacaoClienteInternalFrame extends TableViewFrame {

    public SituacaoClienteInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("situacaoClienteTitle"));
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_SITUACAO_CLIENTE")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_SITUACAO_CLIENTE")) {
            super.insert();
        }
    }
    
    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        SituacaoClienteEditPanel editPanel = new SituacaoClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addSitClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        SituacaoCliente situacaoCliente = new SituacaoCliente();
        while (edtDlg.edit(situacaoCliente)) {
            try {
                getTableModel().insertRecord(situacaoCliente);
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
        SituacaoClienteEditPanel editPanel = new SituacaoClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editSitClienteTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_SITUACAO_CLIENTE"));
        Object situacaoCliente = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(situacaoCliente)) {
            try {
                vtm.updateRow(situacaoCliente);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
