/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import vendas.entity.User;

/**
 *
 * @author sam
 */
public class UserSelect {
    
    Boolean selecionado;
    User user;

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
