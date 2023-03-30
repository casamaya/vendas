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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Jaime
 */
@Entity
@Table(name = "TBPERFIL")
@NamedQueries({
    @NamedQuery(name = "Perfil.findAll", query = "SELECT p FROM Perfil p")})
public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PERFIL")
    private String perfil;
    @Basic(optional = false)
    @Column(name = "INSTATUSPERFIL")
    private Character inStatusPerfil;
    @JoinTable(name = "TBUSUARIOPERFIL", joinColumns = {
        @JoinColumn(name = "PERFIL", referencedColumnName = "PERFIL")}, inverseJoinColumns = {
        @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")})
    @ManyToMany
    private List<User> users;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil1")
    private List<PerfilRecurso> recursos;

    public Perfil() {
    }

    public Character getInStatusPerfil() {
        return inStatusPerfil;
    }

    public void setInStatusPerfil(Character inStatusPerfil) {
        this.inStatusPerfil = inStatusPerfil;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<PerfilRecurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<PerfilRecurso> recursoList) {
        this.recursos = recursoList;
    }

    public Perfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfil != null ? perfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.perfil == null && other.perfil != null) || (this.perfil != null && !this.perfil.equals(other.perfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return perfil;
    }
    
}
