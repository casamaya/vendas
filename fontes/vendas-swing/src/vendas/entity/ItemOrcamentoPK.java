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
 * @author sam
 */
@Embeddable
public class ItemOrcamentoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDORCAMENTO")
    private int idorcamento;
    @Basic(optional = false)
    @Column(name = "IDPRODUTO")
    private int idproduto;

    public ItemOrcamentoPK() {
    }

    public ItemOrcamentoPK(int idorcamento, int idproduto) {
        this.idorcamento = idorcamento;
        this.idproduto = idproduto;
    }

    public int getIdorcamento() {
        return idorcamento;
    }

    public void setIdorcamento(int idorcamento) {
        this.idorcamento = idorcamento;
    }

    public int getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(int idproduto) {
        this.idproduto = idproduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idorcamento;
        hash += (int) idproduto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOrcamentoPK)) {
            return false;
        }
        ItemOrcamentoPK other = (ItemOrcamentoPK) object;
        if (this.idorcamento != other.idorcamento) {
            return false;
        }
        if (this.idproduto != other.idproduto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ItemOrcamentoPK[idorcamento=" + idorcamento + ", idproduto=" + idproduto + "]";
    }

}
