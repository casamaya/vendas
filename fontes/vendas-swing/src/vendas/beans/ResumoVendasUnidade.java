/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.math.BigDecimal;

/**
 *
 * @author Sam
 */
public class ResumoVendasUnidade {
    private String repres;
    private String unidade;
    private String cliente;
    private String produto;
    private String nomeGrupo;
    private String vendedor;
    private String segmento;
    private BigDecimal valor;
    private BigDecimal comissao;
    private BigDecimal comissaoAtend;

    public String getRepresentada() {
        return repres;
    }

    public void setRepresentada(String repres) {
        this.repres = repres;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getComissaoAtend() {
        return comissaoAtend;
    }

    public void setComissaoAtend(BigDecimal comissaoAtend) {
        this.comissaoAtend = comissaoAtend;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

}
