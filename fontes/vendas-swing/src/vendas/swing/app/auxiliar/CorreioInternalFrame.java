/**
 * CorreioInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.util.Date;
import vendas.entity.Correio;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.MensagemCorreio;
import vendas.entity.User;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class CorreioInternalFrame extends TableViewFrame {

    public CorreioInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle("Mensagens");
        Correio correio = new Correio();
        correio.setUserOrigem(TApplication.getInstance().getUser());
        correio.setUserDestino(TApplication.getInstance().getUser());
        correio.setDataLeitura(DateUtils.getNextDate(new Date(), -1));
        setFilterObject(correio);
    }

    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_MENSAGEM")) {
            super.remove();
        }
    }   

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_MENSAGEM")) {
            super.insert();
        }
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(0);
        col.setPreferredWidth(80);
        col = getColumn(1);
        col.setPreferredWidth(180);
        col = getColumn(2);
        col.setPreferredWidth(450);
    }

    @Override
    protected void insertRow() {
        CorreioEditPanel editPanel = new CorreioEditPanel();
        EditDialog edtDlg = new EditDialog("Incluir mensagem");
        edtDlg.setEditPanel(editPanel);
        MensagemCorreio banco = new MensagemCorreio();
        Correio correio;
        while (edtDlg.edit(banco)) {
            try {
                for (User u : banco.getUsuarios()) {
                    correio = new Correio();
                    correio.setMensagem(banco.getMensagem());
                    correio.setUserDestino(u);
                    correio.setUserOrigem(TApplication.getInstance().getUser());
                    correio.setDataEnvio(new Date());
                    correio.setMensagemlida('0');
                    getTableModel().getDao().insertRecord(correio);
                    Messages.infoMessage("Mensagem enviada.");
                }
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void viewRow() {
        CorreioViewFrame editPanel = new CorreioViewFrame();
        EditDialog edtDlg = new EditDialog("Visualizar mensagem");
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_MENSAGEM"));

        Correio banco = (Correio) getTableModel().getObject(getSelectedRow());
        while (edtDlg.edit(banco)) {
            try {
                banco.setDataLeitura(new Date());
                getTableModel().getDao().updateRow(banco);
                break;
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
    }
}
