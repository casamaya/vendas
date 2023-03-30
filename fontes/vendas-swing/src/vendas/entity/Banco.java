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
@Table(name = "TBBANCO")
@NamedQueries({
    @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b order by b.nome"),
    @NamedQuery(name = "Banco.findByName", query = "SELECT b FROM Banco b where b.nome like :name order by b.nome")
})
public class Banco implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDBANCO", nullable = false, length = 3)
    private String bancoId;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 25)
    private String nome;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "banco")
    private Collection<ContaRepres> contaRepresCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "banco")
    private Collection<ContaEmpresa> contaCollection;

    public Banco() {
    }

    public Banco(String idbanco) {
        this.bancoId = idbanco;
    }

    public Banco(String idbanco, String nome) {
        this.bancoId = idbanco;
        this.nome = nome;
    }

    public String getBancoId() {
        return bancoId;
    }

    public void setBancoId(String idbanco) {
        this.bancoId = idbanco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<ContaRepres> getContaRepresCollection() {
        return contaRepresCollection;
    }

    public void setContaRepresCollection(Collection<ContaRepres> contaRepresCollection) {
        this.contaRepresCollection = contaRepresCollection;
    }

    public Collection<ContaEmpresa> getContaCollection() {
        return contaCollection;
    }

    public void setContaCollection(Collection<ContaEmpresa> contaCollection) {
        this.contaCollection = contaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bancoId != null ? bancoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.bancoId == null && other.bancoId != null) || (this.bancoId != null && !this.bancoId.equals(other.bancoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bancoId);
        sb.append(" ").append(nome);
        return sb.toString();
    }

}
