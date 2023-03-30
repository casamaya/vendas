/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBENTRADAREPRES")
@NamedQueries({
    @NamedQuery(name = "EntradaRepres.findAll", query = "SELECT e FROM EntradaRepres e"),
    @NamedQuery(name = "EntradaRepres.findByIdEntrada", query = "SELECT e FROM EntradaRepres e WHERE e.idEntrada = :idEntrada"),
    @NamedQuery(name = "EntradaRepres.findByDtEntrada", query = "SELECT e FROM EntradaRepres e WHERE e.dtEntrada = :dtEntrada")
})
public class EntradaRepres implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBENTRADAREPRES_IDENTRADA_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBENTRADAREPRES_IDENTRADA_SEQ", sequenceName="TBENTRADAREPRES_IDENTRADA_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDENTRADA", nullable = false)
    private Integer idEntrada;
    @Basic(optional = false)
    @Column(name = "DTENTRADA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEntrada;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "HISTORICO", length = 30)
    private String historico;
    @Column(name = "TIPO", length = 1)
    private String tipo;
    @Column(name = "PEDIDO", length = 80)
    private String pedido;
    @Column(name = "OBSERVACAO", length = 256)
    private String observacao;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public EntradaRepres() {
    }

    public EntradaRepres(Integer identrada) {
        this.idEntrada = identrada;
    }

    public EntradaRepres(Integer identrada, Date dtentrada, BigDecimal valor) {
        this.idEntrada = identrada;
        this.dtEntrada = dtentrada;
        this.valor = valor;
    }

    public Integer getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Integer identrada) {
        this.idEntrada = identrada;
    }

    public Date getDtEntrada() {
        return dtEntrada;
    }

    public void setDtEntrada(Date dtentrada) {
        this.dtEntrada = dtentrada;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres idrepres) {
        this.repres = idrepres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntrada != null ? idEntrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntradaRepres)) {
            return false;
        }
        EntradaRepres other = (EntradaRepres) object;
        if ((this.idEntrada == null && other.idEntrada != null) || (this.idEntrada != null && !this.idEntrada.equals(other.idEntrada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.EntradaRepres[identrada=" + idEntrada + "]";
    }

}
