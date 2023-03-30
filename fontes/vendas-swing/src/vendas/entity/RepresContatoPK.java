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
public class RepresContatoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDREPRES")
    private int idRepres;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private char tipo;
    @Basic(optional = false)
    @Column(name = "NUMITEM")
    private int numItem;

    public RepresContatoPK() {
    }

    public RepresContatoPK(int idrepres, char tipo, int numitem) {
        this.idRepres = idrepres;
        this.tipo = tipo;
        this.numItem = numitem;
    }

    public int getIdRepres() {
        return idRepres;
    }

    public void setIdRepres(int idrepres) {
        this.idRepres = idrepres;
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
        hash += (int) idRepres;
        hash += (int) tipo;
        hash += (int) numItem;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RepresContatoPK)) {
            return false;
        }
        RepresContatoPK other = (RepresContatoPK) object;
        if (this.idRepres != other.idRepres) {
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
        return "demo.RepresContatoPK[idrepres=" + idRepres + ", tipo=" + tipo + ", numitem=" + numItem + "]";
    }

}
