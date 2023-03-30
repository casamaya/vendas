/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.security;

import javax.swing.JOptionPane;
import ritual.swing.TApplication;
import vendas.dao.UserDao;
import vendas.entity.User;
import vendas.util.EditDialog;

/**
 *
 * @author sam
 */
public class TrocaSenha {

    TApplication app = TApplication.getInstance();
    User user = (User) app.getUser();

    private void valida(User tmp) throws Exception {
        if (tmp.getNewPasswd().compareTo(tmp.getRepeatPasswd()) != 0)
            throw new Exception("Nova senha n\u00E3o confere");
        String senha = Crypt.encrypt(user.getUserName(), tmp.getPasswd());
        if (senha.compareTo(user.getPasswd()) != 0)
            throw new Exception("Senha inválida");
    }

    public void trocaSenha() {


        EditDialog edtDlg = new EditDialog("Troca senha");
        TrocaSenhaForm form = new TrocaSenhaForm();
        edtDlg.setEditPanel(form);
        User tmp = new User();
        while (edtDlg.edit(tmp)) {
            try {
                valida(tmp);
                UserDao ud = (UserDao) TApplication.getInstance().lookupService("userDao");
                user.setPasswd(Crypt.encrypt(user.getUserName(), tmp.getNewPasswd()));
                ud.updateRow(user);
                app.setUser(user);
                break;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
}
