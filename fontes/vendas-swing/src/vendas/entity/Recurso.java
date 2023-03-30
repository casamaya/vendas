/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Jaime
 */
@Entity
@Table(name = "TBRECURSO")
@NamedQueries({
    @NamedQuery(name = "Recurso.findAll", query = "SELECT r FROM Recurso r")})
public class Recurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDRECURSO")
    private Integer idRecurso;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Column(name = "DESCRICAO")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "MNEMONICO")
    private String mnemonico;
    @Basic(optional = false)
    @Column(name = "INSTATUSRECURSO")
    private Character inStatusRecurso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recurso")
    private List<PerfilRecurso> perfilList;
    @OneToMany(mappedBy = "recursoPai")
    private List<Recurso> recursos;
    @JoinColumn(name = "IDRECURSOPAI", referencedColumnName = "IDRECURSO")
    @ManyToOne
    private Recurso recursoPai;

    public Recurso() {
    }

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMnemonico() {
        return mnemonico;
    }

    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Character getInStatusRecurso() {
        return inStatusRecurso;
    }

    public void setInStatusRecurso(Character inStatusRecurso) {
        this.inStatusRecurso = inStatusRecurso;
    }

    public List<PerfilRecurso> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<PerfilRecurso> perfilList) {
        this.perfilList = perfilList;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public Recurso getRecursoPai() {
        return recursoPai;
    }

    public void setRecursoPai(Recurso recursoPai) {
        this.recursoPai = recursoPai;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecurso != null ? idRecurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recurso)) {
            return false;
        }
        Recurso other = (Recurso) object;
        if ((this.idRecurso == null && other.idRecurso != null) || (this.idRecurso != null && !this.idRecurso.equals(other.idRecurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
