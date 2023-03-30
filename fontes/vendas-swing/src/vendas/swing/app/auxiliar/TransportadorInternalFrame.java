/**
 * TransportadorInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import vendas.entity.Transportador;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.swing.model.TransportadorModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class TransportadorInternalFrame extends TableViewFrame {

    public TransportadorInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Transportadores", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(TransportadorModel.NOME);
        col.setPreferredWidth(200);
        col = getColumn(TransportadorModel.FONE1);
        col.setPreferredWidth(100);
        col = getColumn(TransportadorModel.FONE2);
        col.setPreferredWidth(100);
        col = getColumn(TransportadorModel.FORMAPGTO);
        col.setPreferredWidth(170);
        col = getColumn(TransportadorModel.NUCONTACORRENTE);
        col.setPreferredWidth(170);
        setSize(600, 450);
    }

    @Override
    public void viewRow() {
        editRow();
    }
    
        @Override
    public void remove() {
    if (TApplication.getInstance().isGrant("EXCLUIR_TRANSPORTADOR")) {
            super.remove();
        }
    }

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_TRANSPORTADOR")) {
            super.insert();
        }
    }
    
    @Override
    protected void insertRow() {
        TransportadorEditPanel editPanel = new TransportadorEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addTransportadorTitle"));
        edtDlg.setEditPanel(editPanel);
        Transportador transportador = new Transportador();
        while (edtDlg.edit(transportador)) {
            try {
                getTableModel().insertRecord(transportador);
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
        TransportadorEditPanel editPanel = new TransportadorEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editTransportadorTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_TRANSPORTADOR"));
        
        Object transportador = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(transportador)) {
            try {
                getTableModel().updateRow(transportador);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
