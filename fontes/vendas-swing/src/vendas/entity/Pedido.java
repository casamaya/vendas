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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBPEDIDO")
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByIdPedido", query = "SELECT p FROM Pedido p WHERE p.idPedido = :idPedido"),
    @NamedQuery(name = "Pedido.findByDtPedido", query = "SELECT p FROM Pedido p WHERE p.dtPedido = :dtPedido"),
    @NamedQuery(name = "Pedido.findUltPedidoCliente", query = "select p.idPedido as idPedido, p.dtPedido as dtPedido from Pedido as p where p.dtPedido in (select max(p2.dtPedido) as dtPedido from Pedido as p2 where p2.cliente = p.cliente) and p.cliente.idCliente = :cliente"),
    @NamedQuery(name = "Pedido.findUltPedidoClienteRepres", query = "select p.idPedido as idPedido, p.dtPedido as dtPedido from Pedido as p where p.dtPedido in (select max(p2.dtPedido) as dtPedido from Pedido as p2 where p2.cliente = p.cliente and p2.repres = p.repres) and p.cliente.idCliente = :cliente and p.repres.idRepres = :repres")
})
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
//    @GeneratedValue(generator = "TBPEDIDO_IDPEDIDO_SEQ", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "TBPEDIDO_IDPEDIDO_SEQ", sequenceName = "TBPEDIDO_IDPEDIDO_SEQ", allocationSize = 1)
    @Column(name = "IDPEDIDO", nullable = false)
    private Integer idPedido;
    @Column(name = "IDPEDIDOREPRES")
    private Integer idPedidoRepres;
    @Basic(optional = false)
    @Column(name = "DTPEDIDO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtPedido;
    @Basic(optional = false)
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "VALORCLIENTE", precision = 10, scale = 2)
    private BigDecimal valorCliente;
    @Basic(optional = false)
    @Column(name = "VALORCOMISSAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorComissao;
    @Column(name = "VALOROP", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorOp;
    @Column(name = "VALORPEND", precision = 10, scale = 2)
    private BigDecimal valorPendente;
    @Basic(optional = false)
    @Column(name = "COMISSVEN", nullable = false, precision = 10, scale = 2)
    private BigDecimal comissaoVendedor;
    @Basic(optional = false)
    @Column(name = "COMISSAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal comissao;
    @Column(name = "EMITIDO", length = 1)
    private String emitido;
    @Column(name = "EMITIDOCLIENTE", length = 1)
    private String emitidoCliente;
    @Column(name = "EMITIDOCOBRANCA", length = 1)
    private String emitidoCobranca;
    @Column(name = "PRE", length = 1)
    private String prePedido;
    @Column(name = "PRECOBRANCA", length = 1)
    private String preCobranca;
    @Column(name = "ENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEntrega;
    @Transient
    private Date dtEmbarqueAnterior;
    @Column(name = "TPENTREGA", length = 20)
    private String tpentrega;
    @Column(name = "FRETE", precision = 10, scale = 2)
    private BigDecimal frete;
    @Column(name = "DTCANCEL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcancel;
    @Basic(optional = false)
    @Column(name = "ATEND", nullable = false, length = 1)
    private String atendimento;
    @Basic(optional = false)
    @Column(name = "SITUACAO", nullable = false, length = 1)
    private String situacao;
    @Column(name = "SUBSTRIBUTARIA", length = 1)
    private String substituicaoTributaria;
    @Column(name = "EMAILVENDENVIADO")
    private Short emailVendEnviado;
    @Basic(optional = false)
    @Column(name = "EMBARQUEIMEDIATO", nullable = false, length = 1)
    private String embarqueImediato;
    @Basic(optional = false)
    @Column(name = "ACOMPANHAR", nullable = false, length = 1)
    private String acompanhar;
    @Column(name = "TIPOPEDIDO")
    private Short tipoPedido;
    @Column(name = "ISROMANEIO")
    private Short isromaneio;
    @Column(name = "IPI", precision = 10, scale = 2)
    private BigDecimal ipi;
    @Column(name = "SEGURO", precision = 10, scale = 2)
    private BigDecimal seguro;
    @Lob
    @Column(name = "OBS", length = 0)
    private String obs;
    @Lob
    @Column(name = "OBS2", length = 0)
    private String obs2;
    @Lob
    @Column(name = "OBSATENDIMENTO", length = 0)
    private String obsAtendimento;
    @Lob
    @Column(name = "OBS3", length = 0)
    private String obs3;
    @Lob
    @Column(name = "complemento", length = 0)
    private String complemento;
    @Transient
    BigDecimal vlrComissaoVendedor;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "pedido")
    private Collection<AtendimentoPedido> atendimentos;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "pedido")
    private List<ItemPedido> itens;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "pedido")
    private List<ArquivoPedido> arquivos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<PedidoEmbarque> pedidoEmbarqueList;

    @JoinColumn(name = "IDCLIENTERES", referencedColumnName = "IDCLIENTE")
    @ManyToOne
    private Cliente clienteResponsavel;
    
    @JoinColumn(name = "IDPGTO", referencedColumnName = "IDPGTO", nullable = false)
    @ManyToOne(optional = false)
    private FormaPgto formaPgto;
    @JoinColumn(name = "IDFORMAVENDA", referencedColumnName = "IDFORMAVENDA", nullable = false)
    @ManyToOne(optional = false)
    private FormaVenda formaVenda;
    @JoinColumn(name = "IDPGTOCLIENTE", referencedColumnName = "IDPGTO")
    @ManyToOne
    private FormaPgto formaPgtoCliente;
    @JoinColumn(name = "IDTIPOPGTO", referencedColumnName = "IDTIPOPGTO", nullable = false)
    @ManyToOne(optional = false)
    private TipoPgto tipoPgto;
    @JoinColumn(name = "IDPGTOTRANSP", referencedColumnName = "IDPGTO")
    @ManyToOne
    private FormaPgto formaPgtoTransp;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)
    @ManyToOne(optional = false)
    private Repres repres;
    @JoinColumn(name = "IDTRANS", referencedColumnName = "IDTRANS", nullable = false)
    @ManyToOne(optional = false)
    private Transportador transportador;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR", nullable = false)
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public Pedido() {
        atendimento = "N";
        situacao = "N";
        emitido = "0";
        tipoPedido = 0;
        itens = new ArrayList<ItemPedido>();
        acompanhar = "0";
        comissao = BigDecimal.ZERO;
        prePedido = "1";
        //preCobranca = "1";
        preCobranca = "0";
        emitido = "0";
        emitidoCliente = "0";
        emitidoCobranca = "0";
        embarqueImediato = "0";
    }

    public Pedido(Integer idpedido) {
        this.idPedido = idpedido;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idpedido) {
        this.idPedido = idpedido;
    }

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtpedido) {
        this.dtPedido = dtpedido;
    }

    public Boolean isEmbarqueImediato() {
        return "1".equals(embarqueImediato);
    }
    public String getEmbarqueImediato() {
        return embarqueImediato;
    }

    public void setEmbarqueImediato(String embarqueImediato) {
        this.embarqueImediato = embarqueImediato;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getAtraso() {
        return dtEntrega.compareTo(new Date()) < 0;
    }

    public BigDecimal getValorCliente() {
        return valorCliente;
    }

    public void setValorCliente(BigDecimal valorCliente) {
        this.valorCliente = valorCliente;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public BigDecimal getValorOp() {
        if (valorOp == null) {
            return new BigDecimal(0);
        }
        return valorOp;
    }

    public void setValorOp(BigDecimal valor) {
        this.valorOp = valor;
    }

    public BigDecimal getValorPendente() {
        BigDecimal total;
        if ("A".equals(getSituacao())) {
            total = new BigDecimal(0);
        } else {
            total = getValor().subtract(getVlrAtendido());
        }
        return total;
    }

    /**
     * Define o valorpend deste Pedido para o valor especificado.
     * @param valorpend o novo valorpend
     */
    public void setValorPendente(BigDecimal valorpend) {
        this.valorPendente = valorpend;
    }

    public BigDecimal getComissaoVendedor() {
        return comissaoVendedor;
    }

    public void setComissaoVendedor(BigDecimal comissven) {
        this.comissaoVendedor = comissven;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getEmitido() {
        return emitido;
    }

    public void setEmitido(String emitido) {
        this.emitido = emitido;
    }

    public String getEmitidoCliente() {
        return emitidoCliente;
    }

    public void setEmitidoCliente(String emitidoCliente) {
        this.emitidoCliente = emitidoCliente;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date entrega) {
        this.dtEntrega = entrega;
    }

    public Date getDtEmbarqueAnterior() {
        return dtEmbarqueAnterior;
    }

    public void setDtEmbarqueAnterior(Date dtEmbarqueAnterior) {
        this.dtEmbarqueAnterior = dtEmbarqueAnterior;
    }

    public String getTpentrega() {
        return tpentrega;
    }

    public void setTpentrega(String tpentrega) {
        this.tpentrega = tpentrega;
    }

    public Boolean getEntregaImediata() {
        return "I".equals(tpentrega);
    }

    public void setEntregaImediata(Boolean value) {
        tpentrega = value ? "I" : "N";
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
    }

    public Date getDtcancel() {
        return dtcancel;
    }

    public void setDtcancel(Date dtcancel) {
        this.dtcancel = dtcancel;
    }

    public String getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(String atend) {
        this.atendimento = atend;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAcompanhar() {
        return acompanhar;
    }

    public void setAcompanhar(String acompanhar) {
        this.acompanhar = acompanhar;
    }

    public Short getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(Short tipopedido) {
        this.tipoPedido = tipopedido;
    }

    public Short getIsromaneio() {
        return isromaneio;
    }

    public void setIsromaneio(Short isromaneio) {
        this.isromaneio = isromaneio;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getSeguro() {
        return seguro;
    }

    public void setSeguro(BigDecimal seguro) {
        this.seguro = seguro;
    }

    public String getSubstituicaoTributaria() {
        return substituicaoTributaria;
    }
    
    public Boolean isSubstituicaoTributaria() {
        return (substituicaoTributaria != null) && "1".equals(substituicaoTributaria);
    }

    public void setSubstituicaoTributaria(String substituicaoTributaria) {
        this.substituicaoTributaria = substituicaoTributaria;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getObs2() {
        return obs2;
    }

    public void setObs2(String obs2) {
        this.obs2 = obs2;
    }

    public String getObs3() {
        return obs3;
    }

    public void setObs3(String obs3) {
        this.obs3 = obs3;
    }

    public String getObsAtendimento() {
        return obsAtendimento;
    }

    public void setObsAtendimento(String obsAtendimento) {
        this.obsAtendimento = obsAtendimento;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Collection<AtendimentoPedido> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(Collection<AtendimentoPedido> atendimentoPedidoCollection) {
        this.atendimentos = atendimentoPedidoCollection;
    }

    public Collection<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itemPedidoCollection) {
        this.itens = itemPedidoCollection;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente idcliente) {
        this.cliente = idcliente;
    }
    /*
    public Cliente getClienteResponsavel() {
    return clienteResponsavel;
    }
    
    public void setClienteResponsavel(Cliente idclienteres) {
    this.clienteResponsavel = idclienteres;
    }
     */

    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(FormaPgto idpgto) {
        this.formaPgto = idpgto;
    }

    public FormaPgto getFormaPgtoCliente() {
        return formaPgtoCliente;
    }

    public void setFormaPgtoCliente(FormaPgto formaPgtoCliente) {
        this.formaPgtoCliente = formaPgtoCliente;
    }

    public FormaPgto getFormaPgtoTransp() {
        return formaPgtoTransp;
    }

    public void setFormaPgtoTransp(FormaPgto idpgtotransp) {
        this.formaPgtoTransp = idpgtotransp;
    }

    public TipoPgto getTipoPgto() {
        return tipoPgto;
    }

    public void setTipoPgto(TipoPgto tipoPgto) {
        this.tipoPgto = tipoPgto;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres idrepres) {
        this.repres = idrepres;
    }

    public Cliente getClienteResponsavel() {
        return clienteResponsavel;
    }

    public void setClienteResponsavel(Cliente clienteResponsavel) {
        this.clienteResponsavel = clienteResponsavel;
    }

    public Transportador getTransportador() {
        return transportador;
    }

    public void setTransportador(Transportador idtrans) {
        this.transportador = idtrans;
    }

    public FormaVenda getFormaVenda() {
        return formaVenda;
    }

    public void setFormaVenda(FormaVenda formaVenda) {
        this.formaVenda = formaVenda;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor idvendedor) {
        this.vendedor = idvendedor;
    }
    
    public boolean isAtendimentos() {
        return ((atendimentos != null) && (atendimentos.size() > 0));
    }

    public boolean isPagamentos() {
        boolean result = false;
        if (isAtendimentos())
            for (AtendimentoPedido atend : atendimentos) {
                if (atend.getPgtos() != null && atend.getPgtos().size() > 0) {
                    result = true;
                    break;
                }            
            }
        return result;

    }

    public BigDecimal getVlrComissaoTotal() {
        BigDecimal div = new BigDecimal(100);
        //return getValorOp().add(valor.divide(div).multiply(comissao)); //.setScale(2,RoundingMode.HALF_UP);
        return getValorOp().add(valorComissao); //.setScale(2,RoundingMode.HALF_UP);
    }

    public void setVlrComissaoVendedor(BigDecimal vlr) {
        vlrComissaoVendedor = vlr;
    }
    
    public BigDecimal getVlrComissaoVendedor() {
        BigDecimal div = new BigDecimal(100);
        return getVlrComissaoTotal().divide(div).multiply(comissaoVendedor);
    }

    public BigDecimal getVlrComissaoEmpresa() {
        return getVlrComissaoTotal().subtract(getVlrComissaoVendedor());
    }

    public BigDecimal getVlrSaldoComissao() {
        BigDecimal total;
        if (("A".equals(getSituacao())) || ("P".equals(getAtendimento()))) {
        //if ("A".equals(getSituacao())) {
            total = new BigDecimal(0);

            Collection<AtendimentoPedido> lista = getAtendimentos();
            for (AtendimentoPedido itemAtendimento : lista) {
                if (itemAtendimento.getDtPgtoComissao() == null) {
                    total = total.add(itemAtendimento.getValorComissao());//.setScale(2,RoundingMode.HALF_UP));
                }
            }
            return total;
        } else {
            total = getVlrComissaoTotal().subtract(getVlrComissaoAtendida());
            if (total.compareTo(new BigDecimal(0)) < 0) {
                total = new BigDecimal(0);
            }
        }
        return total;
    }

    public BigDecimal getVlrAtendido() {
        Collection<AtendimentoPedido> lista = getAtendimentos();
        BigDecimal vlrAtendido = new BigDecimal(0);//.setScale(2,RoundingMode.HALF_UP);

        for (AtendimentoPedido itemAtendimento : lista) {
            if (itemAtendimento.getAtendimentoPedidoPK().getNf().contains("/1")) {
                vlrAtendido = vlrAtendido.add(itemAtendimento.getValor());//.setScale(2,RoundingMode.HALF_UP));
            }
        }
        return vlrAtendido;
    }

    public BigDecimal getVlrComissaoAtendida() {
        Collection<AtendimentoPedido> lista = getAtendimentos();
        BigDecimal vlrAtendido = new BigDecimal(0);//.setScale(2,RoundingMode.HALF_UP);
        for (AtendimentoPedido itemAtendimento : lista) {
            vlrAtendido = vlrAtendido.add(itemAtendimento.getValorComissao());//.setScale(2,RoundingMode.HALF_UP));
        }
        return vlrAtendido;
    }

    public BigDecimal getVlrComVendedorAtendida() {
        return getVlrComissaoAtendida().divide(new BigDecimal(100)).multiply(getComissaoVendedor());//.setScale(2,RoundingMode.HALF_UP);
    }

    public BigDecimal getVlrComEmpresaAtendida() {
        return getVlrComissaoAtendida().subtract(getVlrComVendedorAtendida());
    }

    public BigDecimal getVlrUnidades() {
        BigDecimal vlrUnidades = new BigDecimal(0);
        for (ItemPedido item : itens) {
            vlrUnidades = vlrUnidades.add(item.getQtd().multiply(item.getProduto().getFatorConversao()));
        }
        return vlrUnidades;
    }

    public Boolean getReposicao() {
        return tipoPedido == 1;
    }

    public void setReposicao(Boolean value) {
        if (value) {
            tipoPedido = 1;
        } else {
            tipoPedido = 0;
        }
    }

    public String getPrePedido() {
        return prePedido;
    }

    public void setPrePedido(String prePedido) {
        this.prePedido = prePedido;
    }

    public void setPrePedido(Boolean value) {
        if (value) {
            this.prePedido = "1";
        } else {
            this.prePedido = "0";
        }
    }

    public Boolean isPrePedido() {
        return "1".equals(prePedido);
    }
    public String getPreCobranca() {
        return preCobranca;
    }

    public void setPreCobranca(String preCobranca) {
        this.preCobranca = preCobranca;
    }

    public void setPreCobranca(Boolean value) {
        if (value) {
            this.preCobranca = "1";
        } else {
            this.preCobranca = "0";
        }
    }

    public Boolean isPreCobranca() {
        return ("1".equals(preCobranca) && isPagamentos());
    }

    public Boolean isAcompanhar() {
        return this.acompanhar.equals("A");
    }

    public Short getEmailVendEnviado() {
        return emailVendEnviado;
    }
    
    public int getEmailVendedorEnviado() {
        return emailVendEnviado.intValue();
    }

    public void setEmailVendEnviado(Short emailVendEnviado) {
        this.emailVendEnviado = emailVendEnviado;
    }

    public void setEmailVendEnviado(int value) {
        this.emailVendEnviado = Integer.valueOf(value).shortValue();
    }

    public Integer getIdPedidoRepres() {
        return idPedidoRepres;
    }

    public void setIdPedidoRepres(Integer idPedidoRepres) {
        this.idPedidoRepres = idPedidoRepres;
    }

    public Object isEmitido() {
        return "1".equals(emitido);
    }

    public Object isEmitidoCliente() {
        return "1".equals(emitidoCliente);
    }

    public Object isEmitidoCobranca() {
        return "1".equals(emitidoCobranca);
    }

    public String getEmitidoCobranca() {
        return emitidoCobranca;
    }

    public void setEmitidoCobranca(String emitidoCobranca) {
        this.emitidoCobranca = emitidoCobranca;
    }

    /**
     * Define o acompanhar deste Pedido para o valor especificado.
     * @param acompanhar o novo acompanhar
     */
    public void setAcompanhar(Boolean acompanhar) {
        if (acompanhar) {
            this.acompanhar = "A";
        } else {
            this.acompanhar = "0";
        }
    }

    public List<PgtoCliente> getPagamentos() {
        List<PgtoCliente> pgtos = new ArrayList<PgtoCliente>();
        for (AtendimentoPedido nota : getAtendimentos()) {
            pgtos.addAll(nota.getPgtos());
        }
        return pgtos;
    }

    public List<ArquivoPedido> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoPedido> arquivos) {
        this.arquivos = arquivos;
    }

    public List<PedidoEmbarque> getPedidoEmbarqueList() {
        return pedidoEmbarqueList;
    }

    public void setPedidoEmbarqueList(List<PedidoEmbarque> pedidoEmbarqueList) {
        this.pedidoEmbarqueList = pedidoEmbarqueList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedido != null ? idPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idPedido == null && other.idPedido != null) || (this.idPedido != null && !this.idPedido.equals(other.idPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idPedido.toString();
    }
}
