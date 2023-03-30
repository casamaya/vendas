/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Jaime
 */
@Entity
@Table(name = "TBPERFILRECURSO")
@NamedQueries({
    @NamedQuery(name = "PerfilRecurso.findAll", query = "SELECT p FROM PerfilRecurso p")})
public class PerfilRecurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PerfilRecursoPK perfilRecursoPK;
    @Column(name = "CHECKED")
    private Character checked;
    @JoinColumn(name = "PERFIL", referencedColumnName = "PERFIL", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Perfil perfil1;
    @JoinColumn(name = "IDRECURSO", referencedColumnName = "IDRECURSO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Recurso recurso;

    public PerfilRecurso() {
    }

    public PerfilRecurso(PerfilRecursoPK perfilRecursoPK) {
        this.perfilRecursoPK = perfilRecursoPK;
    }

    public PerfilRecurso(String perfil, int idrecurso) {
        this.perfilRecursoPK = new PerfilRecursoPK(perfil, idrecurso);
    }

    public PerfilRecursoPK getPerfilRecursoPK() {
        return perfilRecursoPK;
    }

    public void setPerfilRecursoPK(PerfilRecursoPK perfilRecursoPK) {
        this.perfilRecursoPK = perfilRecursoPK;
    }

    public Character getChecked() {
        return checked;
    }

    public void setChecked(Character checked) {
        this.checked = checked;
    }

    public Perfil getPerfil1() {
        return perfil1;
    }

    public void setPerfil1(Perfil perfil1) {
        this.perfil1 = perfil1;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfilRecursoPK != null ? perfilRecursoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilRecurso)) {
            return false;
        }
        PerfilRecurso other = (PerfilRecurso) object;
        if ((this.perfilRecursoPK == null && other.perfilRecursoPK != null) || (this.perfilRecursoPK != null && !this.perfilRecursoPK.equals(other.perfilRecursoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.PerfilRecurso[ perfilRecursoPK=" + perfilRecursoPK + " ]";
    }
    
}
