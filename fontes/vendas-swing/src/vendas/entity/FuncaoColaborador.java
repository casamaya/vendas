/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jaimeoliveira
 */
@Entity
@Table(name = "TBFUNCAOCOLABORADOR")
@NamedQueries({
    @NamedQuery(name = "FuncaoColaborador.findAll", query = "SELECT f FROM FuncaoColaborador f")})
public class FuncaoColaborador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDFUNCAO", nullable = false)
    private Integer idfuncao;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;
    @OneToMany(mappedBy = "funcaoColaborador")
    private List<Colaborador> colaboradorList;

    public FuncaoColaborador() {
    }

    public FuncaoColaborador(Integer idfuncao) {
        this.idfuncao = idfuncao;
    }

    public FuncaoColaborador(Integer idfuncao, String nome) {
        this.idfuncao = idfuncao;
        this.nome = nome;
    }

    public Integer getIdfuncao() {
        return idfuncao;
    }

    public void setIdfuncao(Integer idfuncao) {
        this.idfuncao = idfuncao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Colaborador> getColaboradorList() {
        return colaboradorList;
    }

    public void setColaboradorList(List<Colaborador> colaboradorList) {
        this.colaboradorList = colaboradorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfuncao != null ? idfuncao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncaoColaborador)) {
            return false;
        }
        FuncaoColaborador other = (FuncaoColaborador) object;
        if ((this.idfuncao == null && other.idfuncao != null) || (this.idfuncao != null && !this.idfuncao.equals(other.idfuncao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
