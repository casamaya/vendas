/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBFORMAPGTO")
@NamedQueries({
    @NamedQuery(name = "FormaPgto.findAll", query = "SELECT f FROM FormaPgto f"),
    @NamedQuery(name = "FormaPgto.findByName", query = "SELECT f FROM FormaPgto f WHERE f.nome like :name")
})
public class FormaPgto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPGTO", nullable = false, length = 3)
    private String idpgto;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "formaPgto")
    private Collection<FormaPgtoRepres> formaPgtoRepresCollection;
    //@OneToMany(cascade = CascadeType.REFRESH, mappedBy = "pgto")
    //private Collection<Pedido> pedidoCollection;
    //@OneToMany(mappedBy = "pgtoTransp")
    //private Collection<Pedido> pedidoCollection1;

    public FormaPgto() {
    }

    public FormaPgto(String idpgto) {
        this.idpgto = idpgto;
    }

    public FormaPgto(String idpgto, String nome) {
        this.idpgto = idpgto;
        this.nome = nome;
    }

    public String getIdPgto() {
        return idpgto;
    }

    public void setIdPgto(String idpgto) {
        this.idpgto = idpgto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<FormaPgtoRepres> getFormaPgtoRepresCollection() {
        return formaPgtoRepresCollection;
    }

    public void setFormaPgtoRepresCollection(Collection<FormaPgtoRepres> formaPgtoRepresCollection) {
        this.formaPgtoRepresCollection = formaPgtoRepresCollection;
    }
/*
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    public Collection<Pedido> getPedidoCollection1() {
        return pedidoCollection1;
    }

    public void setPedidoCollection1(Collection<Pedido> pedidoCollection1) {
        this.pedidoCollection1 = pedidoCollection1;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpgto != null ? idpgto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaPgto)) {
            return false;
        }
        FormaPgto other = (FormaPgto) object;
        if ((this.idpgto == null && other.idpgto != null) || (this.idpgto != null && !this.idpgto.equals(other.idpgto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
