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
public class ClienteContatoPK implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "IDCLIENTE")
    private int idCliente;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private char tipo;
    @Basic(optional = false)
    @Column(name = "NUMITEM")
    private int numItem;

    public ClienteContatoPK() {
        
    }

    public ClienteContatoPK(int idcliente, char tipo, int numitem) {
        this.idCliente = idcliente;
        this.tipo = tipo;
        this.numItem = numitem;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idcliente) {
        this.idCliente = idcliente;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numitem) {
        this.numItem = numitem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCliente;
        hash += (int) tipo;
        hash += (int) numItem;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteContatoPK)) {
            return false;
        }
        ClienteContatoPK other = (ClienteContatoPK) object;
        if (this.idCliente != other.idCliente) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.numItem != other.numItem) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "demo.ClienteContatoPK[idcliente=" + idCliente + ", tipo=" + tipo + ", numitem=" + numItem + "]";
    }

}
