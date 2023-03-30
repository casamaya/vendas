/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBGRUPOPRODUTO")
@NamedQueries({
    @NamedQuery(name = "GrupoProduto.findAll", query = "SELECT g FROM GrupoProduto g order by g.nomeGrupo"),
    @NamedQuery(name = "GrupoProduto.findByName", query = "SELECT g FROM GrupoProduto g WHERE g.nomeGrupo like :name order by g.nomeGrupo")
})
public class GrupoProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBGRUPOPRODUTO_IDCODGRUPO_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBGRUPOPRODUTO_IDCODGRUPO_SEQ", sequenceName="TBGRUPOPRODUTO_IDCODGRUPO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDCODGRUPO", nullable = false)
    private Integer idCodGrupo;
    @Basic(optional = false)
    @Column(name = "NOMEGRUPO", nullable = false, length = 50)
    private String nomeGrupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupoProduto")
    private Collection<SubGrupoProduto> subGrupoProdutoList;
    //@OneToMany(mappedBy = "grupoProduto")
    //private List<Produto> produtoList;

    public GrupoProduto() {
        subGrupoProdutoList = new ArrayList();
    }

    public GrupoProduto(Integer idcodgrupo) {
        this.idCodGrupo = idcodgrupo;
    }

    public GrupoProduto(Integer idcodgrupo, String nomegrupo) {
        this.idCodGrupo = idcodgrupo;
        this.nomeGrupo = nomegrupo;
    }

    public Integer getIdCodGrupo() {
        return idCodGrupo;
    }

    public void setIdCodGrupo(Integer idcodgrupo) {
        this.idCodGrupo = idcodgrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomegrupo) {
        this.nomeGrupo = nomegrupo;
    }

    public Collection<SubGrupoProduto> getSubGrupoProdutoList() {
        return subGrupoProdutoList;
    }
    public String getOrderSubGrupoProdutoList() {
        StringBuilder sb = new StringBuilder();
        for (SubGrupoProduto sg: subGrupoProdutoList) {
            sb.append(sg.getNomeGrupo()).append("\n");
        }
        return sb.toString();
    }

    public void setSubGrupoProdutoList(Collection<SubGrupoProduto> subGrupoProdutoList) {
        this.subGrupoProdutoList = subGrupoProdutoList;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCodGrupo != null ? idCodGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoProduto)) {
            return false;
        }
        GrupoProduto other = (GrupoProduto) object;
        if ((this.idCodGrupo == null && other.idCodGrupo != null) || (this.idCodGrupo != null && !this.idCodGrupo.equals(other.idCodGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nomeGrupo;
    }

}
