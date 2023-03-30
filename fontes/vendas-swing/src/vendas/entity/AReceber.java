/*
 * AReceber.java
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
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBARECEBER")
@NamedQueries({})
public class AReceber implements Compromisso, Serializable {
    @Id
    @Column(name = "IDCOMPROMISSO", nullable = false)
    @GeneratedValue(generator = "TBARECEBER_IDCOMPROMISSO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBARECEBER_IDCOMPROMISSO_SEQ", sequenceName = "TBARECEBER_IDCOMPROMISSO_SEQ", allocationSize = 1)
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

    public AReceber() {
    }

    public AReceber(Integer idCompromisso) {
        this.idCompromisso = idCompromisso;
    }

    public AReceber(Integer idCompromisso, Date dtVencimento, BigDecimal valor) {
        this.idCompromisso = idCompromisso;
        this.dtVencimento = dtVencimento;
        this.valor = valor;
    }

    public Integer getIdCompromisso() {
        return idCompromisso;
    }

    public void setIdCompromisso(Integer idCompromisso) {
        this.idCompromisso = idCompromisso;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDtPgto() {
        return dtPgto;
    }

    public void setDtPgto(Date dtPgto) {
        this.dtPgto = dtPgto;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta idConta) {
        this.conta = idConta;
    }

    public TipoPgtoFinanceiro getTipoPgtoFinanceiro() {
        return tipoPgto;
    }

    public void setTipoPgto(TipoPgtoFinanceiro idtipopgto) {
        this.tipoPgto = idtipopgto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        if (!(object instanceof AReceber)) {
            return false;
        }
        AReceber other = (AReceber) object;
        if ((this.idCompromisso == null && other.idCompromisso != null) || (this.idCompromisso != null && !this.idCompromisso.equals(other.idCompromisso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "contas.entity.AReceber[idCompromisso=" + idCompromisso + "]";
    }

}
