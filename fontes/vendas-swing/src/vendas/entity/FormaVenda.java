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
 * @author sam
 */
@Entity
@Table(name = "TBFORMAVENDA")
@NamedQueries({
    @NamedQuery(name = "FormaVenda.findAll", query = "SELECT f FROM FormaVenda f")})
public class FormaVenda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDFORMAVENDA")
    private Integer idformavenda;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;

    public FormaVenda() {
    }

    public FormaVenda(Integer idformavenda) {
        this.idformavenda = idformavenda;
    }

    public FormaVenda(Integer idformavenda, String nome) {
        this.idformavenda = idformavenda;
        this.nome = nome;
    }

    public Integer getIdformavenda() {
        return idformavenda;
    }

    public void setIdformavenda(Integer idformavenda) {
        this.idformavenda = idformavenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idformavenda != null ? idformavenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaVenda)) {
            return false;
        }
        FormaVenda other = (FormaVenda) object;
        if ((this.idformavenda == null && other.idformavenda != null) || (this.idformavenda != null && !this.idformavenda.equals(other.idformavenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
