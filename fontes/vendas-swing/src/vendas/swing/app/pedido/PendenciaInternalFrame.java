/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.entity.Pendencia;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.PendenciaModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author jaime
 */
public class PendenciaInternalFrame extends TableViewFrame {
    public PendenciaInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle("Pendências");
    }

    @Override
    public void viewRow() {
        editRow();
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(PendenciaModel.DESCRICAO);
        col.setPreferredWidth(600);
        col = getColumn(PendenciaModel.DTALTERACAO);
        col.setPreferredWidth(95);
    }
   
    
    @Override
    protected void insertRow() {
        PendenciaEditPanel editPanel = new PendenciaEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addPendenciaTitle"));
        edtDlg.setEditPanel(editPanel);
        Pendencia formaPgto = new Pendencia();
        formaPgto.setUsername(TApplication.getInstance().getUser().getUserName());
        
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
        PendenciaEditPanel editPanel = new PendenciaEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editPendenciaTitle"));

        edtDlg.setEditPanel(editPanel);

        Pendencia formaPgto = (Pendencia)vtm.getObject(getSelectedRow());
        formaPgto.setUsername(TApplication.getInstance().getUser().getUserName());
        
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
    
    @Override
    public void remove() {
        if (TApplication.getInstance().getUser().isAdmin()) {
            super.remove();
        }
    }
}
