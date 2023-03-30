/**
 * VendedorInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.Vendedor;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.VendedorDao;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class VendedorInternalFrame extends TableViewFrame {
    
    private VendedorDao vendedorDao;

    public VendedorInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Vendedores", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(200);
        col = getColumn(1);
        col.setPreferredWidth(90);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_VENDEDOR")) {
            super.remove();
        }
    }    

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_VENDEDOR")) {
            super.insert();
        }
    }
    
    public VendedorDao getVendedorDao() {
        return vendedorDao;
    }

    public void setVendedorDao(VendedorDao vendedorDao) {
        this.vendedorDao = vendedorDao;
    }

    @Override
    public void viewRow() {
        Vendedor vendedor = (Vendedor)getTableModel().getObject(getSelectedRow());
        VendedorViewFrame viewFrame = new VendedorViewFrame("Visualizar " + vendedor.getNome());
        viewFrame.setVendedorDao(vendedorDao);
        TApplication.getInstance().getDesktopPane().add(viewFrame);
        viewFrame.setVisible(true);
        viewFrame.execute(vendedor);
    }

    @Override
    protected void insertRow() {
        VendedorEditPanel editPanel = new VendedorEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVendedorTitle"));
        edtDlg.setEditPanel(editPanel);
        Vendedor vendedor = new Vendedor();
        while (edtDlg.edit(vendedor)) {
            try {
                getTableModel().insertRecord(vendedor);
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
        VendedorEditPanel editPanel = new VendedorEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editVendedorTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_VENDEDOR"));
        Object vendedor = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(vendedor)) {
            try {
                vtm.updateRow(vendedor);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
