/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Sam
 */
@Embeddable
public class ClienteRepresPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDREPRES", nullable = false)
    private int idRepres;
    @Basic(optional = false)
    @Column(name = "IDCLIENTE", nullable = false)
    private int idCliente;

    public ClienteRepresPK() {
    }

    public ClienteRepresPK(int idRepres, int idCliente) {
        this.idRepres = idRepres;
        this.idCliente = idCliente;
    }

    public int getIdRepres() {
        return idRepres;
    }

    public void setIdRepres(int idRepres) {
        this.idRepres = idRepres;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRepres;
        hash += (int) idCliente;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteRepresPK)) {
            return false;
        }
        ClienteRepresPK other = (ClienteRepresPK) object;
        if (this.idRepres != other.idRepres) {
            return false;
        }
        if (this.idCliente != other.idCliente) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(idRepres).append(" ").append(idCliente);
        return sb.toString();
    }

}
