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
public class ItemPedidoAtendPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDPEDIDO", nullable = false)
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "NF", nullable = false, length = 10)
    private String nf;
    @Basic(optional = false)
    @Column(name = "IDPRODUTO", nullable = false)
    private Integer idProduto;

    public ItemPedidoAtendPK() {
    }

    public ItemPedidoAtendPK(int idpedido, String nf, Integer idproduto) {
        this.idPedido = idpedido;
        this.nf = nf;
        this.idProduto = idproduto;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idpedido) {
        this.idPedido = idpedido;
    }

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
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
        hash = 29 * hash + this.idPedido;
        hash = 29 * hash + (this.nf != null ? this.nf.hashCode() : 0);
        hash = 29 * hash + (this.idProduto != null ? this.idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPedidoAtendPK)) {
            return false;
        }
        ItemPedidoAtendPK other = (ItemPedidoAtendPK) object;
        if (this.idPedido != other.idPedido) {
            return false;
        }
        if ((this.nf == null && other.nf != null) || (this.nf != null && !this.nf.equals(other.nf))) {
            return false;
        }
        if (!this.idProduto.equals(other.idProduto)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ItemPedidoAtendPK[idpedido=" + idPedido + ", nf=" + nf + ", idproduto=" + idProduto + "]";
    }

}
