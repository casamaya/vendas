/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.sql.Blob;
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
import org.hibernate.Hibernate;

/**
 *
 * @author joliveira
 */
@Entity
@Table(name = "TBRECIBOCOMISSAO")
@NamedQueries({
    @NamedQuery(name = "ReciboComissao.findAll", query = "SELECT r FROM ReciboComissao r")})
public class ReciboComissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBRECIBOCOMISAO_ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBRECIBOCOMISAO_ID_SEQ", sequenceName = "TBRECIBOCOMISAO_ID_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDRECIBO")
    private Integer idRecibo;
    @Basic(optional = false)
    @Column(name = "DTRECIBO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtRecibo;
    @Lob
    @Column(name = "RECIBO")
    private byte[] recibo;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public ReciboComissao() {
    }

    public ReciboComissao(Integer idrecibo) {
        this.idRecibo = idrecibo;
    }

    public ReciboComissao(Integer idrecibo, Date dtrecibo) {
        this.idRecibo = idrecibo;
        this.dtRecibo = dtrecibo;
    }

    public Integer getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(Integer idrecibo) {
        this.idRecibo = idrecibo;
    }

    public Date getDtRecibo() {
        return dtRecibo;
    }

    public void setDtRecibo(Date dtrecibo) {
        this.dtRecibo = dtrecibo;
    }

    public byte[] getRecibo() {
        return recibo;
    }
    
    public Blob getBlob() {
        return Hibernate.createBlob(this.recibo);
    }

    public void setRecibo(byte[] recibo) {
        this.recibo = recibo;
    }



    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecibo != null ? idRecibo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReciboComissao)) {
            return false;
        }
        ReciboComissao other = (ReciboComissao) object;
        if ((this.idRecibo == null && other.idRecibo != null) || (this.idRecibo != null && !this.idRecibo.equals(other.idRecibo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.ReciboComissao[ idrecibo=" + idRecibo + " ]";
    }
    
}
