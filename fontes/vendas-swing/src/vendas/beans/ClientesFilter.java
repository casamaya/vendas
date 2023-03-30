/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;
import java.util.List;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.Repres;
import vendas.entity.RoteiroVendedor;
import vendas.entity.SegMercado;
import vendas.entity.Vendedor;

/**
 *
 * @author Sam
 */
public class ClientesFilter extends FilterBean {

    private Cliente cliente;
    private Repres repres;
    private Vendedor vendedor;
    private Date dtInclusaoIni;
    private Date dtInclusaoEnd;
    private Date dtPedidoIni;
    private Date dtPedidoEnd;
    private RoteiroVendedor roteiro;
    private SegMercado segmento;
    private boolean semVisita;
    private int order;
    private int tipoBloqueado;
    private Boolean pedidoDesabilitado;
    private Boolean entregarBoleto;
    private Boolean decrescente;
    private Boolean relacao;
    private String uf;
    private GrupoCliente grupoCliente;
    private List<Integer> clientes;
    
    public ClientesFilter() {
        repres = new Repres();
        vendedor = new Vendedor();
        relacao = true;
        repres.setRazao("TODOS");
        cliente = new Cliente();
        roteiro = new RoteiroVendedor();
        roteiro.setDescricao("TODOS");
        segmento = new SegMercado();
        segmento.setNome("TODOS");
        order = 0;
        pedidoDesabilitado = false;
        entregarBoleto = false;
        tipoBloqueado = 0;
    }

    public int getTipoBloqueado() {
        return tipoBloqueado;
    }

    public void setTipoBloqueado(int tipoBloqueado) {
        this.tipoBloqueado = tipoBloqueado;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Boolean getPedidoDesabilitado() {
        return pedidoDesabilitado;
    }

    public void setPedidoDesabilitado(Boolean pedidoDesabilitado) {
        this.pedidoDesabilitado = pedidoDesabilitado;
    }

    public SegMercado getSegmento() {
        return segmento;
    }

    public void setSegmento(SegMercado segmento) {
        this.segmento = segmento;
    }

    public RoteiroVendedor getRoteiro() {
        return roteiro;
    }

    public void setRoteiro(RoteiroVendedor roteiro) {
        this.roteiro = roteiro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public GrupoCliente getGrupoCliente() {
        return grupoCliente;
    }

    public void setGrupoCliente(GrupoCliente grupoCliente) {
        this.grupoCliente = grupoCliente;
    }

    public Date getDtInclusaoIni() {
        return dtInclusaoIni;
    }

    public void setDtInclusaoIni(Date dtInclusaoIni) {
        this.dtInclusaoIni = dtInclusaoIni;
    }

    public Date getDtInclusaoEnd() {
        return dtInclusaoEnd;
    }

    public void setDtInclusaoEnd(Date dtInclusaoEnd) {
        this.dtInclusaoEnd = dtInclusaoEnd;
    }

    public Date getDtPedidoEnd() {
        return dtPedidoEnd;
    }

    public void setDtPedidoEnd(Date dtPedidoEnd) {
        this.dtPedidoEnd = dtPedidoEnd;
    }

    public Date getDtPedidoIni() {
        return dtPedidoIni;
    }

    public void setDtPedidoIni(Date dtPedidoIni) {
        this.dtPedidoIni = dtPedidoIni;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isSemVisita() {
        return semVisita;
    }

    public void setSemVisita(boolean semVisita) {
        this.semVisita = semVisita;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Boolean getDecrescente() {
        return decrescente;
    }

    public void setDecrescente(Boolean decrescente) {
        this.decrescente = decrescente;
    }

    public Boolean getEntregarBoleto() {
        return entregarBoleto;
    }

    public void setEntregarBoleto(Boolean entregarBoleto) {
        this.entregarBoleto = entregarBoleto;
    }

    public Boolean getRelacao() {
        return relacao;
    }

    public void setRelacao(Boolean relacao) {
        this.relacao = relacao;
    }

    public List<Integer> getClientes() {
        return clientes;
    }

    public void setClientes(List<Integer> clientes) {
        this.clientes = clientes;
    }
    
}
