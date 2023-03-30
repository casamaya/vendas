/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import vendas.entity.User;

/**
 *
 * @author sam
 */
public class MensagemCorreio implements Serializable {
    private List<User> usuarios;
    private String mensagem;
    private Date dataEnvio;

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
    
}
