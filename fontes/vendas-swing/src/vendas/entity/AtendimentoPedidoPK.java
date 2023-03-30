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
public class AtendimentoPedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDPEDIDO", nullable = false)
    private int idPedido;
    @Basic(optional = false)
    @Column(name = "NF", nullable = false, length = 10)
    private String nf;

    public AtendimentoPedidoPK() {
    }

    public AtendimentoPedidoPK(int idpedido, String nf) {
        this.idPedido = idpedido;
        this.nf = nf;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPedido;
        hash += (nf != null ? nf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtendimentoPedidoPK)) {
            return false;
        }
        AtendimentoPedidoPK other = (AtendimentoPedidoPK) object;
        if (this.idPedido != other.idPedido) {
            return false;
        }
        if ((this.nf == null && other.nf != null) || (this.nf != null && !this.nf.equals(other.nf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(idPedido).append(" - ").append(nf);
        return sb.toString();
    }

}
