/**
 * SegMercadoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.SegMercado;
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
public class SegMercadoInternalFrame extends TableViewFrame {

    public SegMercadoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Segmentos de mercado", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(220);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_SEGMENTO_MERCADO")) {
            super.remove();
        }
    }       

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_SEGMENTO_MERCADO")) {
            super.insert();
        }
    }
    
    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        SegMercadoEditPanel editPanel = new SegMercadoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addSegMercadoTitle"));
        edtDlg.setEditPanel(editPanel);
        SegMercado segMercado = new SegMercado();
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
        SegMercadoEditPanel editPanel = new SegMercadoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editSegMercadoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_SEGMENTO_MERCADO"));
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
