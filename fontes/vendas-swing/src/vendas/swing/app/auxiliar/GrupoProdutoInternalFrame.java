/**
 * GrupoProdutoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.GrupoProduto;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.GrupoProdutoDao;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class GrupoProdutoInternalFrame extends TableViewFrame {

    public GrupoProdutoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("gruposProdutos"));
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(200);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_GRUPO_DE_PRODUTO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_GRUPO_DE_PRODUTO")) {
            super.insert();
        }
    }
    
    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        GrupoProdutoEditPanel editPanel = new GrupoProdutoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addGrupoProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        GrupoProduto grupoProduto = new GrupoProduto();
        while (edtDlg.edit(grupoProduto)) {
            try {
                getTableModel().insertRecord(grupoProduto);
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
        GrupoProdutoEditPanel editPanel = new GrupoProdutoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editGrupoProdutoTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_GRUPO_DE_PRODUTO"));
        
        Object grupoProduto = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(grupoProduto)) {
            try {
                GrupoProdutoDao grupoDao = (GrupoProdutoDao) TApplication.getInstance().lookupService("grupoProdutoDao");
                grupoDao.updateGrupoProduto((GrupoProduto)grupoProduto, editPanel.getDeletedItens());
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
