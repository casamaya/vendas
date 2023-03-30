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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Jaime
 */
@Entity
@Table(name = "TBPEDIDOEMBARQUE")
@NamedQueries({
    @NamedQuery(name = "PedidoEmbarque.findAll", query = "SELECT p FROM PedidoEmbarque p")})
public class PedidoEmbarque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBPEDIDOEMBRQ_IDPEDIDOEMBRQ_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBPEDIDOEMBRQ_IDPEDIDOEMBRQ_SEQ", sequenceName = "TBPEDIDOEMBRQ_IDPEDIDOEMBRQ_SEQ", allocationSize = 1)
    @Column(name = "IDPEDIDOEMBARQUE", nullable = false)
    private Integer idPedidoEmbarque;
    @Basic(optional = false)
    @Column(name = "DTEMBARQUE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEmbarque;
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;

    public PedidoEmbarque() {
    }

    public PedidoEmbarque(Integer idpedidoembarque) {
        this.idPedidoEmbarque = idpedidoembarque;
    }

    public PedidoEmbarque(Integer idpedidoembarque, Date dtembarque) {
        this.idPedidoEmbarque = idpedidoembarque;
        this.dtEmbarque = dtembarque;
    }

    public Integer getIdPedidoEmbarque() {
        return idPedidoEmbarque;
    }

    public void setIdPedidoEmbarque(Integer idPedidoEmbarque) {
        this.idPedidoEmbarque = idPedidoEmbarque;
    }

    public Date getDtEmbarque() {
        return dtEmbarque;
    }

    public void setDtEmbarque(Date dtEmbarque) {
        this.dtEmbarque = dtEmbarque;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedidoEmbarque != null ? idPedidoEmbarque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoEmbarque)) {
            return false;
        }
        PedidoEmbarque other = (PedidoEmbarque) object;
        if ((this.idPedidoEmbarque == null && other.idPedidoEmbarque != null) || (this.idPedidoEmbarque != null && !this.idPedidoEmbarque.equals(other.idPedidoEmbarque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.PedidoEmbarque[ idpedidoembarque=" + idPedidoEmbarque + " ]";
    }
    
}
