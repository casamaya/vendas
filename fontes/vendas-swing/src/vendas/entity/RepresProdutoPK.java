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
public class RepresProdutoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDREPRES", nullable = false)
    private int idRepres;
    @Basic(optional = false)
    @Column(name = "IDPRODUTO", nullable = false)
    private Integer idproduto;

    public RepresProdutoPK() {
    }

    public RepresProdutoPK(int idrepres, Integer idproduto) {
        this.idRepres = idrepres;
        this.idproduto = idproduto;
    }

    public int getIdRepres() {
        return idRepres;
    }

    public void setIdRepres(int idrepres) {
        this.idRepres = idrepres;
    }

    public Integer getIdProduto() {
        return idproduto;
    }

    public void setIdProduto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.idRepres;
        hash = 23 * hash + (this.idproduto != null ? this.idproduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RepresProdutoPK)) {
            return false;
        }
        RepresProdutoPK other = (RepresProdutoPK) object;
        if (this.idRepres != other.idRepres) {
            return false;
        }
        if (!this.idproduto.equals(other.idproduto)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.RepresProdutoPK[idrepres=" + idRepres + ", idproduto=" + idproduto + "]";
    }

}
