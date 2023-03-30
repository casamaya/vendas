/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author p993702
 */
public class ProdutosComprados {
    private String cliente;
    private String vendedor;
    private String repres;
    private String descricao;
    private String nf;
    private String situacao;
    private String atendimento;
    private Date dtEntrega;
    private Date dtPedido;
    private Date dtEntregaPedido;
    private Date dtNota;
    private BigDecimal qtd;
    private BigDecimal qtdEntrega;
    private BigDecimal fatorConversao;
    private BigDecimal valor;
    private BigDecimal comissao;
    private BigDecimal valorCliente;
    private BigDecimal unidades;
    private BigDecimal qtdSaldo;
    private Integer pedido;
    private Integer pedidoRepres;
    private String produto;

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    

    public String getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(String atendimento) {
        this.atendimento = atendimento;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public Date getDtEntregaPedido() {
        return dtEntregaPedido;
    }

    public void setDtEntregaPedido(Date dtEntregaFornec) {
        this.dtEntregaPedido = dtEntregaFornec;
    }

    public Date getDtNota() {
        return dtNota;
    }

    public void setDtNota(Date dtNota) {
        this.dtNota = dtNota;
    }

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtPedido) {
        this.dtPedido = dtPedido;
    }

    public BigDecimal getFatorConversao() {
        return fatorConversao;
    }

    public void setFatorConversao(BigDecimal fatorConversao) {
        this.fatorConversao = fatorConversao;
    }

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public Integer getPedido() {
        return pedido;
    }

    public void setPedido(Integer pedido) {
        this.pedido = pedido;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getQtd() {
        return qtd;
    }

    public void setQtd(BigDecimal qtd) {
        this.qtd = qtd;
    }

    public BigDecimal getQtdEntrega() {
        return qtdEntrega;
    }

    public void setQtdEntrega(BigDecimal qtdEntrega) {
        this.qtdEntrega = qtdEntrega;
    }

    public String getRepres() {
        return repres;
    }

    public void setRepres(String repres) {
        this.repres = repres;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorCliente() {
        return valorCliente;
    }

    public void setValorCliente(BigDecimal valorCliente) {
        this.valorCliente = valorCliente;
    }

    public BigDecimal getUnidades() {
        return unidades;
    }

    public void setUnidades(BigDecimal unidades) {
        this.unidades = unidades;
    }

    public BigDecimal getQtdSaldo() {
        return qtdSaldo;
    }

    public void setQtdSaldo(BigDecimal qtdSaldo) {
        this.qtdSaldo = qtdSaldo;
    }

    public Integer getPedidoRepres() {
        return pedidoRepres;
    }

    public void setPedidoRepres(Integer pedidoRepres) {
        this.pedidoRepres = pedidoRepres;
    }
    
}
