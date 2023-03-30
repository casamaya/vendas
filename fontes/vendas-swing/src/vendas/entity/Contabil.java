/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jaimeoliveira
 */
@Entity
@Table(name = "TBCONTABIL")
public class Contabil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBCONTABIL_IDCONTABIL_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBCONTABIL_IDCONTABIL_SEQ", sequenceName = "TBCONTABIL_IDCONTABIL_SEQ", allocationSize = 1)
    @Column(name = "IDCONTABIL")
    private Integer idContabil;
    @Basic(optional = false)
    @Column(name = "IDTIPO")
    private int idTipo;
    @Basic(optional = false)
    @Column(name = "NUMANO")
    private int numAno;
    @Basic(optional = false)
    @Column(name = "NUMMES")
    private int numMes;
    @Basic(optional = false)
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "COMPROMISSO")
    private String compromisso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "DTVENCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVencimento;

    public Contabil() {
    }

    public Contabil(Integer idcontabil) {
        this.idContabil = idcontabil;
    }

    public Contabil(Integer idcontabil, int idtipo, int numano, int nummes, String descricao) {
        this.idContabil = idcontabil;
        this.idTipo = idtipo;
        this.numAno = numano;
        this.numMes = nummes;
        this.descricao = descricao;
    }

    public Integer getIdContabil() {
        return idContabil;
    }

    public void setIdContabil(Integer idContabil) {
        this.idContabil = idContabil;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getNumAno() {
        return numAno;
    }

    public void setNumAno(int numAno) {
        this.numAno = numAno;
    }

    public int getNummes() {
        return numMes;
    }

    public void setNumMes(int nummes) {
        this.numMes = nummes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCompromisso() {
        return compromisso;
    }

    public void setCompromisso(String compromisso) {
        this.compromisso = compromisso;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtvencimento) {
        this.dtVencimento = dtvencimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContabil != null ? idContabil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contabil)) {
            return false;
        }
        Contabil other = (Contabil) object;
        if ((this.idContabil == null && other.idContabil != null) || (this.idContabil != null && !this.idContabil.equals(other.idContabil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Contabil[ idcontabil=" + idContabil + " ]";
    }
    
}
