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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBTRANSPORTADOR", uniqueConstraints = {@UniqueConstraint(columnNames = {"IDTRANS"})})
@NamedQueries({
    @NamedQuery(name = "Transportador.findAll", query = "SELECT t FROM Transportador t order by t.nome"),
    @NamedQuery(name = "Transportador.findByName", query = "SELECT t FROM Transportador t WHERE t.nome like :name order by t.nome")
})
public class Transportador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBTRANSPORTADOR_IDTRANS_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBTRANSPORTADOR_IDTRANS_SEQ", sequenceName="TBTRANSPORTADOR_IDTRANS_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDTRANS", nullable = false)
    private Integer idtrans;
    @Basic(optional = false)
    @Column(name = "CODTRANS", nullable = false, length = 3)
    private String codtrans;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 25)
    private String nome;
    @Column(name = "FONE1", length = 20)
    private String fone1;
    @Column(name = "FONE2", length = 20)
    private String fone2;
    @Column(name = "NUCONTACORRENTE", length = 50)
    private String nuContaCorrente;
    @JoinColumn(name = "IDPGTO", referencedColumnName = "IDPGTO")
    @ManyToOne
    private FormaPgto formaPgto;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "transportador")
    //private Collection<Pedido> pedidoCollection;

    public Transportador() {
    }

    public Transportador(Integer idtrans) {
        this.idtrans = idtrans;
    }

    public Transportador(Integer idtrans, String codtrans, String nome) {
        this.idtrans = idtrans;
        this.codtrans = codtrans;
        this.nome = nome;
    }

    public Integer getIdTrans() {
        return idtrans;
    }

    public void setIdTrans(Integer idtrans) {
        this.idtrans = idtrans;
    }

    public String getCodTrans() {
        return codtrans;
    }

    public void setCodTrans(String codtrans) {
        this.codtrans = codtrans;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }
    
    public String getFones() {
        StringBuilder sb = new StringBuilder("  ");
        if (fone1 != null) {
            sb.append(fone1).append(" ");
        }
        if (fone2 != null) {
            sb.append(fone2).append(" ");
        }
        return sb.toString();
    }

    public String getNuContaCorrente() {
        return nuContaCorrente;
    }

    public void setNuContaCorrente(String nuContaCorrente) {
        this.nuContaCorrente = nuContaCorrente;
    }

    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
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
        hash += (idtrans != null ? idtrans.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transportador)) {
            return false;
        }
        Transportador other = (Transportador) object;
        if ((this.idtrans == null && other.idtrans != null) || (this.idtrans != null && !this.idtrans.equals(other.idtrans))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
