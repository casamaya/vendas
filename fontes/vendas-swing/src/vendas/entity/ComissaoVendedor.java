/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBCOMISSAOVENDEDOR")
@NamedQueries({
    @NamedQuery(name = "ComissaoVendedor.findAll", query = "SELECT c FROM ComissaoVendedor c"),
    @NamedQuery(name = "ComissaoVendedor.findByIdvendedor", query = "SELECT c FROM ComissaoVendedor c WHERE c.comissaoVendedorPK.idvendedor = :idvendedor"),
    @NamedQuery(name = "ComissaoVendedor.findByIdrepres", query = "SELECT c FROM ComissaoVendedor c WHERE c.comissaoVendedorPK.idrepres = :idrepres")
})
public class ComissaoVendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComissaoVendedorPK comissaoVendedorPK;
    @Basic(optional = false)
    @Column(name = "COMISSAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal comissao;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repres repres;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public ComissaoVendedor() {
        comissao = new BigDecimal(50l);
    }

    public ComissaoVendedor(ComissaoVendedorPK comissaoVendedorPK) {
        this.comissaoVendedorPK = comissaoVendedorPK;
    }

    public ComissaoVendedor(ComissaoVendedorPK comissaoVendedorPK, BigDecimal comissao) {
        this.comissaoVendedorPK = comissaoVendedorPK;
        this.comissao = comissao;
    }

    public ComissaoVendedor(int idvendedor, int idrepres) {
        this.comissaoVendedorPK = new ComissaoVendedorPK(idvendedor, idrepres);
    }

    public ComissaoVendedorPK getComissaoVendedorPK() {
        return comissaoVendedorPK;
    }

    public void setComissaoVendedorPK(ComissaoVendedorPK comissaoVendedorPK) {
        this.comissaoVendedorPK = comissaoVendedorPK;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comissaoVendedorPK != null ? comissaoVendedorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComissaoVendedor)) {
            return false;
        }
        ComissaoVendedor other = (ComissaoVendedor) object;
        if ((this.comissaoVendedorPK == null && other.comissaoVendedorPK != null) || (this.comissaoVendedorPK != null && !this.comissaoVendedorPK.equals(other.comissaoVendedorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return comissaoVendedorPK.toString();
    }

}
