/*
 * APagar.java
 * 
 * Created on 24/10/2007, 22:15:16
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBAPAGAR")
public class APagar implements Compromisso, Serializable {
    @Id
    @GeneratedValue(generator = "TBAPAGAR_IDCOMPROMISSO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBAPAGAR_IDCOMPROMISSO_SEQ", sequenceName = "TBAPAGAR_IDCOMPROMISSO_SEQ", allocationSize = 1)
    @Column(name = "IDCOMPROMISSO", nullable = false)
    private Integer idCompromisso;
    @Column(name = "DTVENCIMENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtVencimento;
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "DTPGTO")
    @Temporal(TemporalType.DATE)
    private Date dtPgto;
    @JoinColumn(name = "IDCONTA", referencedColumnName = "IDCONTA", nullable = false)
    @ManyToOne
    private Conta conta;
    @JoinColumn(name = "IDTIPOPGTO", referencedColumnName = "IDTIPOPGTO", nullable = false)
    @ManyToOne
    private TipoPgtoFinanceiro tipoPgto;
    @Column(name = "OBSERVACAO", nullable = true, length=512)
    private String observacao;
    
    public APagar() {
    }

    public APagar(Integer idCompromisso) {
        this.idCompromisso = idCompromisso;
    }

    public APagar(Integer idCompromisso, Date dtVencimento, BigDecimal valor) {
        this.idCompromisso = idCompromisso;
        this.dtVencimento = dtVencimento;
        this.valor = valor;
    }

    @Override
    public Integer getIdCompromisso() {
        return idCompromisso;
    }

    @Override
    public void setIdCompromisso(Integer idCompromisso) {
        this.idCompromisso = idCompromisso;
    }

    @Override
    public Date getDtVencimento() {
        return dtVencimento;
    }

    @Override
    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    @Override
    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public Date getDtPgto() {
        return dtPgto;
    }

    @Override
    public void setDtPgto(Date dtPgto) {
        this.dtPgto = dtPgto;
    }

    @Override
    public Conta getConta() {
        return conta;
    }

    @Override
    public void setConta(Conta idconta) {
        this.conta = idconta;
    }

    @Override
    public TipoPgtoFinanceiro getTipoPgtoFinanceiro() {
        return tipoPgto;
    }

    @Override
    public void setTipoPgto(TipoPgtoFinanceiro idtipopgto) {
        this.tipoPgto = idtipopgto;
    }

    @Override
    public String getObservacao() {
        return observacao;
    }

    @Override
    public void setObservacao(String nome) {
        this.observacao = nome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompromisso != null ? idCompromisso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof APagar)) {
            return false;
        }
        APagar other = (APagar) object;
        if ((this.idCompromisso == null && other.idCompromisso != null) || (this.idCompromisso != null && !this.idCompromisso.equals(other.idCompromisso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "contas.entity.APagar[idCompromisso=" + idCompromisso + "]";
    }

}
