/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jaime
 */

@Entity
@Table(name = "TBPENDENCIA")
@NamedQueries({
    @NamedQuery(name = "Pendencia.findAll", query = "SELECT p FROM Pendencia p")})
public class Pendencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBPENDENCIA_GENERATOR", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBPENDENCIA_GENERATOR", sequenceName="TBPENDENCIA_IDPENDENCIA_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDPENDENCIA")
    private Integer idPendencia;
    @Basic(optional = false)
    @Column(name = "DTINCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInclusao;
    @Column(name = "DTALTERACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "DSPENDENCIA")
    private String dsPendencia;
    @Basic(optional = false)
    @Column(name = "INRESOLVIDO")
    private String inResolvido;

    public Pendencia() {
    }

    public Pendencia(Integer idpendencia) {
        this.idPendencia = idpendencia;
    }

    public Integer getIdPendencia() {
        return idPendencia;
    }

    public void setIdPendencia(Integer idPendencia) {
        this.idPendencia = idPendencia;
    }

    public Date getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(Date dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDsPendencia() {
        return dsPendencia;
    }

    public void setDsPendencia(String dsPendencia) {
        this.dsPendencia = dsPendencia;
    }

    public String getInResolvido() {
        return inResolvido;
    }

    public void setInResolvido(String inResolvido) {
        this.inResolvido = inResolvido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPendencia != null ? idPendencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pendencia)) {
            return false;
        }
        Pendencia other = (Pendencia) object;
        if ((this.idPendencia == null && other.idPendencia != null) || (this.idPendencia != null && !this.idPendencia.equals(other.idPendencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Pendencia[ idpendencia=" + idPendencia + " ]";
    }
    
}
