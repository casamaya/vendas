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
public class ClienteEnderecoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDCLIENTE", nullable = false)
    private int idcliente;
    @Basic(optional = false)
    @Column(name = "TIPO", nullable = false)
    private char tipo;

    public ClienteEnderecoPK() {
    }

    public ClienteEnderecoPK(int idcliente, char tipo) {
        this.idcliente = idcliente;
        this.tipo = tipo;
    }

    public int getIdCliente() {
        return idcliente;
    }

    public void setIdCliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idcliente;
        hash += (int) tipo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteEnderecoPK)) {
            return false;
        }
        ClienteEnderecoPK other = (ClienteEnderecoPK) object;
        if (this.idcliente != other.idcliente) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + tipo;
    }

}
