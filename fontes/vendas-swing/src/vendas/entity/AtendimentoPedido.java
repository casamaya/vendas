/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBATENDIMENTOPEDIDO")
@NamedQueries({
    @NamedQuery(name = "AtendimentoPedido.findAll", query = "SELECT a FROM AtendimentoPedido a"),
    @NamedQuery(name = "AtendimentoPedido.findByIdPedido", query = "SELECT a FROM AtendimentoPedido a WHERE a.atendimentoPedidoPK.idPedido = :idPedido"),
    @NamedQuery(name = "AtendimentoPedido.findByNf", query = "SELECT a FROM AtendimentoPedido a WHERE a.atendimentoPedidoPK.nf = :nf"),
    @NamedQuery(name = "AtendimentoPedido.findByDtNota", query = "SELECT a FROM AtendimentoPedido a WHERE a.dtNota = :dtNota"),
    @NamedQuery(name = "AtendimentoPedido.findByDtPgtoComissao", query = "SELECT a FROM AtendimentoPedido a WHERE a.dtPgtoComissao = :dtPgtoComissao")
})
public class AtendimentoPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AtendimentoPedidoPK atendimentoPedidoPK;
    @Basic(optional = false)
    @Column(name = "DTNOTA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtNota;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "VALORDESCONTO", precision = 10, scale = 2)
    private BigDecimal valorDesconto;
    @Column(name = "VALORCOM", precision = 10, scale = 2)
    private BigDecimal valorComissao;
    @Column(name = "VALORIPI", precision = 10, scale = 2)
    private BigDecimal valorIpi;
    @Column(name = "VALORSEGURO", precision = 10, scale = 2)
    private BigDecimal valorSeguro;
    @Column(name = "VALORICMS", precision = 10, scale = 2)
    private BigDecimal valorIcms;
    @Column(name = "SUBSTRIB", precision = 10, scale = 2)
    private BigDecimal subsTrib;
    @Column(name = "VALORFRETE", precision = 10, scale = 2)
    private BigDecimal valorFrete;
    @Column(name = "PERCDESCONTO", precision = 10, scale = 2)
    private BigDecimal percDesconto;
    @Column(name = "DTDESCONTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtDesconto;
    @Column(name = "DTCOMPREV")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcomprev;
    @Column(name = "DTFRETE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFrete;
    @Column(name = "PAGO", length = 1)
    private String pago;
    @Column(name = "BOLETOFRETE", length = 1)
    private String boletoFrete;
    @Column(name = "EMITIDO", length = 1)
    private String emitido;
    @Column(name = "FORNECEDOR", length = 100)
    private String fornecedor;
    @Column(name = "OPERADOR", length = 2)
    private String operador;
    @Column(name = "RECIBO", length = 10)
    private String recibo;
    @Column(name = "MOTORISTA", length = 50)
    private String motorista;
    @Column(name = "FONEMOTORISTA", length = 20)
    private String foneMotorista;
    @Column(name = "DTCOM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtPgtoComissao;
    @Column(name = "DTRECIBO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtReciboComissao;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @JoinColumn(name = "IDPEDIDO", referencedColumnName = "IDPEDIDO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "atendimentoPedido")
    private Collection<ItemPedidoAtend> itens;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "atendimentoPedido")
    private Collection<PgtoCliente> pgtoClienteCollection;

    public AtendimentoPedido() {
        atendimentoPedidoPK = new AtendimentoPedidoPK();
        itens = new ArrayList<ItemPedidoAtend>();
        pgtoClienteCollection = new ArrayList<PgtoCliente>();
        boletoFrete = "0";
        recibo = "0";
        pago = "0";
        emitido = "0";
        valorComissao = new BigDecimal(0).setScale(2,RoundingMode.CEILING);
        dtNota = new Date();
    }

    public AtendimentoPedido(AtendimentoPedidoPK atendimentoPedidoPK) {
        this();
        this.atendimentoPedidoPK = atendimentoPedidoPK;
    }

    public AtendimentoPedido(int idpedido, String nf) {
        this(new AtendimentoPedidoPK(idpedido, nf));
    }

    public AtendimentoPedidoPK getAtendimentoPedidoPK() {
        return atendimentoPedidoPK;
    }

    public void setAtendimentoPedidoPK(AtendimentoPedidoPK atendimentoPedidoPK) {
        this.atendimentoPedidoPK = atendimentoPedidoPK;
    }

    public Date getDtNota() {
        return dtNota;
    }

    public void setDtNota(Date dtnota) {
        this.dtNota = dtnota;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getValorAtendido() {
        BigDecimal vlrAtendido = BigDecimal.ZERO;
        for (ItemPedidoAtend itemAtendimento : itens) {
            vlrAtendido = vlrAtendido.add(itemAtendimento.getTotalCliente());//.setScale(2,RoundingMode.HALF_UP));
        }
        return vlrAtendido;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorcom) {
        this.valorComissao = valorcom;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getSubsTrib() {
        return subsTrib;
    }

    public void setSubsTrib(BigDecimal subsTrib) {
        this.subsTrib = subsTrib;
    }

    public Date getDtcomprev() {
        return dtcomprev;
    }

    public void setDtcomprev(Date dtcomprev) {
        this.dtcomprev = dtcomprev;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getEmitido() {
        return emitido;
    }

    public void setEmitido(String emitido) {
        this.emitido = emitido;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    public Date getDtPgtoComissao() {
        return dtPgtoComissao;
    }

    public void setDtPgtoComissao(Date dtcom) {
        this.dtPgtoComissao = dtcom;
    }

    public Date getDtReciboComissao() {
        return dtReciboComissao;
    }

    public void setDtReciboComissao(Date dtReciboComissao) {
        this.dtReciboComissao = dtReciboComissao;
    }

    public Date getDtFrete() {
        return dtFrete;
    }

    public void setDtFrete(Date dtFrete) {
        this.dtFrete = dtFrete;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        if ((observacao == null) || (observacao.length() == 0))
            this.observacao = null;
        else
            this.observacao = observacao;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getFoneMotorista() {
        return foneMotorista;
    }

    public void setFoneMotorista(String foneMotorista) {
        this.foneMotorista = foneMotorista;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getBoletoFrete() {
        return boletoFrete;
    }

    public void setBoletoFrete(String boletoFrete) {
        this.boletoFrete = boletoFrete;
    }

    public Collection<ItemPedidoAtend> getItens() {
        return itens;
    }

    public void setItens(Collection<ItemPedidoAtend> itemPedidoAtendCollection) {
        this.itens = itemPedidoAtendCollection;
        for (ItemPedidoAtend item : itens) {
            if (item.getValor() == null) {
                item.setValor(BigDecimal.ZERO);
            }
        }
    }

    public Collection<PgtoCliente> getPgtos() {
        return pgtoClienteCollection;
    }

    public void setPgtos(Collection<PgtoCliente> pgtoClienteCollection) {
        this.pgtoClienteCollection = pgtoClienteCollection;
    }

    public BigDecimal getComissaoVendedor() {
        if (valorComissao == null)
            return getPedido().getValor().multiply(getPedido().getComissaoVendedor()).divide(new BigDecimal(100)).setScale(2,RoundingMode.CEILING);
        else
            return valorComissao.multiply(getPedido().getComissaoVendedor()).divide(new BigDecimal(100)).setScale(2,RoundingMode.CEILING);
    }

    public BigDecimal getComissaoEmpresa() {
        if (valorComissao == null)
            return getPedido().getValor().multiply(getPedido().getComissao()).subtract(getComissaoVendedor());
        else
            return valorComissao.subtract(getComissaoVendedor());
    }

    public BigDecimal getComissaoTotal() {
        if (valorComissao == null)
            return getPedido().getValor().multiply(getPedido().getComissao()).divide(new BigDecimal(100)).setScale(2,RoundingMode.CEILING);
        else
            return valorComissao;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcm) {
        this.valorIcms = valorIcm;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getValorSubTotal() {
        return getValorAtendido();
    }

    public BigDecimal getValorTotal() {
        BigDecimal result = BigDecimal.ZERO;

        if (getValorIcms() != null)
            result = result.add(getValorIcms());
        if (getValorSeguro() != null)
            result = result.add(getValorSeguro());
        if (getValorIpi() != null)
            result = result.add(getValorIpi());
        if (getValorFrete() != null)
            result = result.add(getValorFrete());
        if (getSubsTrib() != null)
            result = result.add(getSubsTrib());
        if (getValorDesconto() != null)
            result = result.subtract(getValorDesconto());
        if (getValorSubTotal() != null)
            result = result.add(getValorSubTotal());
        return result;
    }
    
    public BigDecimal getJuros() {
        BigDecimal valor = BigDecimal.ZERO;
        
        Collection<PgtoCliente> lista = getPgtos();
        
        for (PgtoCliente pgto : lista) {
            valor = valor.add(pgto.getJuros());
        }
        
        return valor;
    }

    public Date getDtDesconto() {
        return dtDesconto;
    }

    public void setDtDesconto(Date dtDesconto) {
        this.dtDesconto = dtDesconto;
    }

    public BigDecimal getPercDesconto() {
        return percDesconto;
    }

    public void setPercDesconto(BigDecimal percDesconto) {
        this.percDesconto = percDesconto;
    }
    
    public Boolean getNotaAdicional() {
        return atendimentoPedidoPK.getNf().contains("/1");
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (atendimentoPedidoPK != null ? atendimentoPedidoPK.hashCode() : 0);
        return hash;
    }
    
    public BigDecimal getUnidadesAtendidas() {
        BigDecimal result = new BigDecimal(0);
        for(ItemPedidoAtend i : itens) {
            result = result.add(i.getProduto().getFatorConversao().multiply(i.getQtd()));
        }
        return result;
    }
    
    public String getUnidadeAtendimento() {
        String result = null;
        if (itens != null && itens.size() > 0) {
            for(ItemPedidoAtend i : itens) {
                result = i.getProduto().getUndCumulativa().getIdUnidade();
                break;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtendimentoPedido)) {
            return false;
        }
        AtendimentoPedido other = (AtendimentoPedido) object;
        if ((this.atendimentoPedidoPK == null && other.atendimentoPedidoPK != null) || (this.atendimentoPedidoPK != null && !this.atendimentoPedidoPK.equals(other.atendimentoPedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return atendimentoPedidoPK.toString();
    }
}
