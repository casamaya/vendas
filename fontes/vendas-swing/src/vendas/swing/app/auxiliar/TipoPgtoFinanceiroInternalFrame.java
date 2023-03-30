/**
 * TipoPgtoFinanceiroInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.TipoPgtoFinanceiro;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.TipoPgtoFinanceiroDao;
import vendas.entity.Vendedor;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class TipoPgtoFinanceiroInternalFrame extends TableViewFrame {
    
    Vendedor vendedor;

    public TipoPgtoFinanceiroInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Tipos de pagamento", tableModel);
    }

    @Override
    public void filter() {
    }
    
    

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
        Integer id = TApplication.getInstance().getUser().getIdvendedor();
        if (id == null)
            id = 1;
        try {
            TipoPgtoFinanceiroDao dao = (TipoPgtoFinanceiroDao) getTableModel().getDao();
            vendedor = (Vendedor)dao.findById(Vendedor.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha geral");
        }
        setFilterObject(vendedor);
    }

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    public void resetFilterObject() {
        setFilterObject(vendedor);
    }
    
    

    @Override
    protected void insertRow() {
        TipoPgtoFinanceiroEditPanel editPanel = new TipoPgtoFinanceiroEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addTipoPgtoFinanceiroTitle"));
        edtDlg.setEditPanel(editPanel);
        TipoPgtoFinanceiro tipoPgtoFinanceiro = new TipoPgtoFinanceiro();
        tipoPgtoFinanceiro.setVendedor(vendedor);
        while (edtDlg.edit(tipoPgtoFinanceiro)) {
            try {
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
        if (TApplication.getInstance().isGrant("EXCLUIR_TIPO_DE_PAGAMENTO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_TIPO_DE_PAGAMENTO")) {
            super.insert();
        }
    }    

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        TipoPgtoFinanceiroEditPanel editPanel = new TipoPgtoFinanceiroEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editTipoPgtoFinanceiroTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_TIPO_DE_PAGAMENTO"));
        Object TipoPgtoFinanceiro = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(TipoPgtoFinanceiro)) {
            try {
                vtm.updateRow(TipoPgtoFinanceiro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

}
