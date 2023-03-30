/**
 * UnidadeProdutoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.UnidadeProduto;
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
public class UndProdutoInternalFrame extends TableViewFrame {

    public UndProdutoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Unidades de produto", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_UNIDADE_DE_PRODUTO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_UNIDADE_DE_PRODUTO")) {
            super.insert();
        }
    }
    
    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        UnidadeProdutoEditPanel editPanel = new UnidadeProdutoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addUndProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        UnidadeProduto unidade = new UnidadeProduto();
        while (edtDlg.edit(unidade)) {
            try {
                getTableModel().insertRecord(unidade);
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
        UnidadeProdutoEditPanel editPanel = new UnidadeProdutoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editUndProdutoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_UNIDADE_DE_PRODUTO"));
        Object unidade = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(unidade)) {
            try {
                vtm.updateRow(unidade);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
