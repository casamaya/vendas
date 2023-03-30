/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBSUBGRUPOPRD")
@NamedQueries({
    @NamedQuery(name = "SubGrupoProduto.findAll", query = "SELECT s FROM SubGrupoProduto s"),
    @NamedQuery(name = "SubGrupoProduto.findByIdCodSubGrupo", query = "SELECT s FROM SubGrupoProduto s WHERE s.idCodSubGrupo = :idcodsubgrupo"),
    @NamedQuery(name = "SubGrupoProduto.findByNomeGrupo", query = "SELECT s FROM SubGrupoProduto s WHERE s.nomeGrupo = :nomegrupo")})
public class SubGrupoProduto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBSUBGRUPOPRD_IDCODSUBGRUPO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBSUBGRUPOPRD_IDCODSUBGRUPO_SEQ", sequenceName = "TBSUBGRUPOPRD_IDCODSUBGRUPO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDCODSUBGRUPO", nullable = false)
    private Integer idCodSubGrupo;
    @Basic(optional = false)
    @Column(name = "NOMEGRUPO", nullable = false, length = 50)
    private String nomeGrupo;
    @JoinColumn(name = "IDCODGRUPO", referencedColumnName = "IDCODGRUPO", nullable = false)
    @ManyToOne(optional = false)
    private GrupoProduto grupoProduto;
    @OneToMany(mappedBy = "subGrupoProduto")
    private List<Produto> produtoList;

    public SubGrupoProduto() {
    }

    public SubGrupoProduto(Integer idcodsubgrupo) {
        this.idCodSubGrupo = idcodsubgrupo;
    }

    public SubGrupoProduto(Integer idcodsubgrupo, String nomegrupo) {
        this.idCodSubGrupo = idcodsubgrupo;
        this.nomeGrupo = nomegrupo;
    }

    public Integer getIdCodSubGrupo() {
        return idCodSubGrupo;
    }

    public void setIdCodSubGrupo(Integer idcodsubgrupo) {
        this.idCodSubGrupo = idcodsubgrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomegrupo) {
        this.nomeGrupo = nomegrupo;
    }

    public GrupoProduto getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCodSubGrupo != null ? idCodSubGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubGrupoProduto)) {
            return false;
        }
        SubGrupoProduto other = (SubGrupoProduto) object;
        if ((this.idCodSubGrupo == null && other.idCodSubGrupo != null) || (this.idCodSubGrupo != null && !this.idCodSubGrupo.equals(other.idCodSubGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nomeGrupo;
    }
}
