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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBAGENDA")
public class Agenda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBAGENDA_IDAGENDA_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBAGENDA_IDAGENDA_SEQ", sequenceName = "TBAGENDA_IDAGENDA_SEQ", allocationSize = 1)
    @Column(name = "IDAGENDA")
    private Integer idagenda;
    @Basic(optional = false)
    @Column(name = "DTEVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtevento;
    @Basic(optional = false)
    @Lob
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME", nullable = false)
    @ManyToOne(optional = false)
    private User usuario;

    public Agenda() {
    }

    public Integer getIdagenda() {
        return idagenda;
    }

    public void setIdagenda(Integer idagenda) {
        this.idagenda = idagenda;
    }

    public Date getDtevento() {
        return dtevento;
    }

    public void setDtevento(Date dtevento) {
        this.dtevento = dtevento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idagenda != null ? idagenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.idagenda == null && other.idagenda != null) || (this.idagenda != null && !this.idagenda.equals(other.idagenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Agenda[ idagenda=" + idagenda + " ]";
    }
    
}
