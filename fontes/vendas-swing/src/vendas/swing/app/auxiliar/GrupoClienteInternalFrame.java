/**
 * GrupoClienteInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.net.URL;
import vendas.entity.GrupoCliente;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.GrupoClienteDao;
import vendas.swing.app.cliente.GrupoClienteDataSource;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author p993702
 */
public class GrupoClienteInternalFrame extends TableViewFrame {

    public GrupoClienteInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Grupos de clientes", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(150);
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_GRUPO_DE_CLIENTE")) {
            super.remove();
        }
    } 

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_GRUPO_DE_CLIENTE")) {
            super.insert();
        }
    }
    
    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        GrupoClienteEditPanel editPanel = new GrupoClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addGrupoClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        GrupoCliente grupoCliente = new GrupoCliente();
        while (edtDlg.edit(grupoCliente)) {
            try {
                getTableModel().insertRecord(grupoCliente);
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
        GrupoClienteEditPanel editPanel = new GrupoClienteEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editGrupoClienteTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_GRUPO_DE_CLIENTE"));

        Object grupoCliente = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(grupoCliente)) {
            try {
                vtm.updateRow(grupoCliente);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void report() {
        Object[] options = {getBundle().getString("grupoClientes"), getBundle().getString("clientesGrupo"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        switch (n) {
            case 0:
                super.report();
                break;
            case 1:
                showClientesGrupo();
                break;
        }
    }

    private void showClientesGrupo() {

        GrupoClienteDao grupoClienteDao = new GrupoClienteDao();
        URL url = getClass().getResource(Constants.JRCLIENTESGRUPO);
        GrupoClienteDataSource ds = new GrupoClienteDataSource(grupoClienteDao.findAll());
        try {
            Reports.showReport(this.getTitle(), null, url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }

    }
}
