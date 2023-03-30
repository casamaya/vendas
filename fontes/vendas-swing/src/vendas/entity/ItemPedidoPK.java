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
public class ItemPedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDPEDIDO", nullable = false)
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "IDPRODUTO", nullable = false)
    private Integer idProduto;

    public ItemPedidoPK() {
    }

    public ItemPedidoPK(int idpedido, Integer idproduto) {
        this.idPedido = idpedido;
        this.idProduto = idproduto;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idpedido) {
        this.idPedido = idpedido;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idproduto) {
        this.idProduto = idproduto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.idPedido;
        hash = 23 * hash + (this.idProduto != null ? this.idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedidoPK)) {
            return false;
        }
        ItemPedidoPK other = (ItemPedidoPK) object;
        if (this.idPedido != other.idPedido) {
            return false;
        }
        if (!this.idProduto.equals(other.idProduto)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + idProduto;
    }

}
