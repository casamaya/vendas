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
public class ComissaoVendedorPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDVENDEDOR", nullable = false)
    private int idvendedor;
    @Basic(optional = false)
    @Column(name = "IDREPRES", nullable = false)
    private int idrepres;

    public ComissaoVendedorPK() {
    }

    public ComissaoVendedorPK(int idvendedor, int idrepres) {
        this.idvendedor = idvendedor;
        this.idrepres = idrepres;
    }

    public int getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(int idvendedor) {
        this.idvendedor = idvendedor;
    }

    public int getIdrepres() {
        return idrepres;
    }

    public void setIdrepres(int idrepres) {
        this.idrepres = idrepres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idvendedor;
        hash += (int) idrepres;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComissaoVendedorPK)) {
            return false;
        }
        ComissaoVendedorPK other = (ComissaoVendedorPK) object;
        if (this.idvendedor != other.idvendedor) {
            return false;
        }
        if (this.idrepres != other.idrepres) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ComissaoVendedorPK[idvendedor=" + idvendedor + ", idrepres=" + idrepres + "]";
    }

}
