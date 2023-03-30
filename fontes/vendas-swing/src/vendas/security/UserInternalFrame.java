/**
 * UserInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.security;

import vendas.swing.app.auxiliar.*;
import vendas.entity.User;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.TApplication;
import vendas.dao.UserDao;
import vendas.dao.UserDao;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class UserInternalFrame extends TableViewFrame {
    
    private UserDao userDao;

    public UserInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Usuários", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        //TableColumn col = getColumn(0);
        ///col.setPreferredWidth(200);
        //col = getColumn(1);
        //col.setPreferredWidth(90);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void viewRow() {
        editRow();
    }

    @Override
    protected void insertRow() {
        UserForm editPanel = new UserForm();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addUserTitle"));
        edtDlg.setEditPanel(editPanel);
        User user = new User();
        while (edtDlg.edit(user)) {
            try {
                getTableModel().insertRecord(user);
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
        UserForm editPanel = new UserForm();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editUserTitle"));
        edtDlg.setEditPanel(editPanel);
        Object user = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(user)) {
            try {
                vtm.updateRow(user);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}
