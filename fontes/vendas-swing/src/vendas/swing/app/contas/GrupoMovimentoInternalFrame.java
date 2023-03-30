/**
 * TipoPgtoFinanceiroInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import vendas.entity.GrupoMovimento;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.VendedorDao;
import vendas.entity.Vendedor;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class GrupoMovimentoInternalFrame extends TableViewFrame {
    
    Vendedor vendedor;
    private Integer idVendedor;
    private TApplication app;

    public GrupoMovimentoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Grupos de movimento", tableModel);
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
        app = TApplication.getInstance();
        idVendedor = app.getUser().getIdvendedor();
        VendedorDao fluxoDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        
        try {
            vendedor = (Vendedor)fluxoDao.findById(Vendedor.class, idVendedor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setFilterObject(idVendedor);
    }

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    public void resetFilterObject() {
        setFilterObject(vendedor.getIdVendedor());
    }

    @Override
    protected void insertRow() {
        GrupoMovimentoEditPanel editPanel = new GrupoMovimentoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addGrupoMovimentoTitle"));
        edtDlg.setEditPanel(editPanel);
        GrupoMovimento tipoPgtoFinanceiro = new GrupoMovimento();
        
        while (edtDlg.edit(tipoPgtoFinanceiro)) {
            try {
                tipoPgtoFinanceiro.setVendedor(vendedor);
                getTableModel().insertRecord(tipoPgtoFinanceiro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
   @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_GRUPO_MOVIMENTO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_GRUPO_MOVIMENTO")) {
            super.insert();
        }
    }    

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        GrupoMovimentoEditPanel editPanel = new GrupoMovimentoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editGrupoMovimentoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_GRUPO_MOVIMENTO"));
        Object grupoMovimento = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(grupoMovimento)) {
            try {
                vtm.updateRow(grupoMovimento);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
