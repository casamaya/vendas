/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBTIPOPGTOFINANCEIRO")
@NamedQueries({
    @NamedQuery(name = "TipoPgtoFinanceiro.findAll", query = "SELECT f FROM TipoPgtoFinanceiro f"),
    @NamedQuery(name = "TipoPgtoFinanceiro.findByName", query = "SELECT f FROM TipoPgtoFinanceiro f WHERE f.nome like :name")
})
public class TipoPgtoFinanceiro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="TBTPPGTOFINANC_IDTIPOPGTO_seq", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBTPPGTOFINANC_IDTIPOPGTO_seq", sequenceName="TBTPPGTOFINANC_IDTIPOPGTO_seq", allocationSize=1)
    @Column(name = "IDTIPOPGTO", nullable = false, length = 3)
    private Integer idTipoPgto;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 25)
    private String nome;
        @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPgto")
    //private Collection<Pedido> pedidoCollection;

    public TipoPgtoFinanceiro() {
    }

    public TipoPgtoFinanceiro(Integer idpgto) {
        this.idTipoPgto = idpgto;
    }

    public TipoPgtoFinanceiro(Integer idpgto, String nome) {
        this.idTipoPgto = idpgto;
        this.nome = nome;
    }

    public Integer getIdTipoPgto() {
        return idTipoPgto;
    }

    public void setIdTipoPgto(Integer idpgto) {
        this.idTipoPgto = idpgto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
    
/*
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPgto != null ? idTipoPgto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPgtoFinanceiro)) {
            return false;
        }
        TipoPgtoFinanceiro other = (TipoPgtoFinanceiro) object;
        if ((this.idTipoPgto == null && other.idTipoPgto != null) || (this.idTipoPgto != null && !this.idTipoPgto.equals(other.idTipoPgto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
