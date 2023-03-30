/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Sam
 */
@Embeddable
public class FormaPgtoRepresPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDREPRES", nullable = false)
    private int idrepres;
    @Basic(optional = false)
    @Column(name = "IDPGTO", nullable = false, length = 3)
    private String idpgto;

    public FormaPgtoRepresPK() {
    }

    public FormaPgtoRepresPK(int idrepres, String idpgto) {
        this.idrepres = idrepres;
        this.idpgto = idpgto;
    }

    public int getIdRepres() {
        return idrepres;
    }

    public void setIdRepres(int idrepres) {
        this.idrepres = idrepres;
    }

    public String getIdPgto() {
        return idpgto;
    }

    public void setIdPgto(String idpgto) {
        this.idpgto = idpgto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idrepres;
        hash += (idpgto != null ? idpgto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaPgtoRepresPK)) {
            return false;
        }
        FormaPgtoRepresPK other = (FormaPgtoRepresPK) object;
        if (this.idrepres != other.idrepres) {
            return false;
        }
        if ((this.idpgto == null && other.idpgto != null) || (this.idpgto != null && !this.idpgto.equals(other.idpgto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.FormaPgtoRepresPK[idrepres=" + idrepres + ", idpgto=" + idpgto + "]";
    }

}
