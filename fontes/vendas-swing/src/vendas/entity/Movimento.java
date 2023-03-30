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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import ritual.util.DateUtils;

/**
 *
 * @author sam
 */
@Entity
@Table(name = "TBMOVIMENTO")
public class Movimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBMOVIMENTO_IDMOVIMENTO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBMOVIMENTO_IDMOVIMENTO_SEQ", sequenceName = "TBMOVIMENTO_IDMOVIMENTO_SEQ", allocationSize = 1)
    @Column(name = "IDMOVIMENTO")
    private Integer idMovimento;
    @Basic(optional = false)
    @Column(name = "DTMOV")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtMov;
    @Column(name = "DOCUMENTO")
    private String documento;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private Integer tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Transient
    private BigDecimal saldo;
    @Transient
    private BigDecimal credito;
    @Transient
    private BigDecimal debito;
    @Lob
    @Column(name = "OBS")
    private String obs;
        @Lob
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "IDCONTA", referencedColumnName = "IDCONTA")
    @ManyToOne(optional = false)
    private ContaFluxo contaFluxo;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    @JoinColumn(name = "IDGRUPO", referencedColumnName = "IDGRUPO")
    @ManyToOne
    private GrupoMovimento grupoMovimento;

    public Movimento() {
        tipo = 0;
        dtMov = DateUtils.parse(DateUtils.format(DateUtils.getDate()));
        valor = BigDecimal.ZERO;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(Integer idMovimento) {
        this.idMovimento = idMovimento;
    }

    public Date getDtMov() {
        return dtMov;
    }

    public void setDtMov(Date dtMov) {
        this.dtMov = dtMov;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTipo() {
        return tipo;
    }
    
    public Boolean getIndicaCredito() {
        return tipo == 1;
    }

    public void setTipo(Integer value) {
        if (value == null)
            value = 0;
        this.tipo = value;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public ContaFluxo getContaFluxo() {
        return contaFluxo;
    }

    public void setContaFluxo(ContaFluxo contaFluxo) {
        this.contaFluxo = contaFluxo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public GrupoMovimento getGrupoMovimento() {
        return grupoMovimento;
    }

    public void setGrupoMovimento(GrupoMovimento grupoMovimento) {
        this.grupoMovimento = grupoMovimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimento != null ? idMovimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimento)) {
            return false;
        }
        Movimento other = (Movimento) object;
        if ((this.idMovimento == null && other.idMovimento != null) || (this.idMovimento != null && !this.idMovimento.equals(other.idMovimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Movimento[ idmovimento=" + idMovimento + " ]";
    }
    
}
