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
@Table(name = "TBUNIDADEPRODUTO")
@NamedQueries({
    @NamedQuery(name = "UnidadeProduto.findAll", query = "SELECT u FROM UnidadeProduto u"),
    @NamedQuery(name = "UnidadeProduto.findByName", query = "SELECT u FROM UnidadeProduto u WHERE u.idUnidade like :name")
})
public class UnidadeProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDUNIDADE", nullable = false, length = 5)
    private String idUnidade;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 25)
    private String nome;
    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidade")
    private Collection<Produto> produtoCollection;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "undCumulativa")
    private Collection<Produto> produtoCollection1;
*/
    public UnidadeProduto() {
    }

    public UnidadeProduto(String idunidade) {
        this.idUnidade = idunidade;
    }

    public UnidadeProduto(String idunidade, String nome) {
        this.idUnidade = idunidade;
        this.nome = nome;
    }

    public String getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(String idunidade) {
        this.idUnidade = idunidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
/*
    public Collection<Produto> getProdutoCollection() {
        return produtoCollection;
    }

    public void setProdutoCollection(Collection<Produto> produtoCollection) {
        this.produtoCollection = produtoCollection;
    }

    public Collection<Produto> getProdutoCollection1() {
        return produtoCollection1;
    }

    public void setProdutoCollection1(Collection<Produto> produtoCollection1) {
        this.produtoCollection1 = produtoCollection1;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUnidade != null ? idUnidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadeProduto)) {
            return false;
        }
        UnidadeProduto other = (UnidadeProduto) object;
        if ((this.idUnidade == null && other.idUnidade != null) || (this.idUnidade != null && !this.idUnidade.equals(other.idUnidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idUnidade;
    }

}
