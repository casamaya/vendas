/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Jaime
 */
@Embeddable
public class PerfilRecursoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "PERFIL")
    private String perfil;
    @Basic(optional = false)
    @Column(name = "IDRECURSO")
    private int idRecurso;

    public PerfilRecursoPK() {
    }

    public PerfilRecursoPK(String perfil, int idrecurso) {
        this.perfil = perfil;
        this.idRecurso = idrecurso;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfil != null ? perfil.hashCode() : 0);
        hash += (int) idRecurso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilRecursoPK)) {
            return false;
        }
        PerfilRecursoPK other = (PerfilRecursoPK) object;
        if ((this.perfil == null && other.perfil != null) || (this.perfil != null && !this.perfil.equals(other.perfil))) {
            return false;
        }
        if (this.idRecurso != other.idRecurso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.PerfilRecursoPK[ perfil=" + perfil + ", idrecurso=" + idRecurso + " ]";
    }
    
}
