/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.core;

import vendas.entity.User;

/**
 *
 * @author sam
 */
public class Login {

    private User user;
    private static Login login;

    private Login() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Login getInstance() {
        if (login == null)
            login = new Login();
        return login;
    }
}
