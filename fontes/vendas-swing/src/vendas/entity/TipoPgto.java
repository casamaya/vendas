/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBTIPOPGTO")
@NamedQueries({
    @NamedQuery(name = "TipoPgto.findAll", query = "SELECT f FROM TipoPgto f"),
    @NamedQuery(name = "TipoPgto.findByName", query = "SELECT f FROM TipoPgto f WHERE f.nome like :name")
})
public class TipoPgto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDTIPOPGTO", nullable = false, length = 3)
    private String idTipoPgto;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 25)
    private String nome;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPgto")
    //private Collection<Pedido> pedidoCollection;

    public TipoPgto() {
    }

    public TipoPgto(String idpgto) {
        this.idTipoPgto = idpgto;
    }

    public TipoPgto(String idpgto, String nome) {
        this.idTipoPgto = idpgto;
        this.nome = nome;
    }

    public String getIdTipoPgto() {
        return idTipoPgto;
    }

    public void setIdTipoPgto(String idpgto) {
        this.idTipoPgto = idpgto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        if (!(object instanceof TipoPgto)) {
            return false;
        }
        TipoPgto other = (TipoPgto) object;
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
