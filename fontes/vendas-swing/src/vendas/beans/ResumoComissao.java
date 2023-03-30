/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.math.BigDecimal;

/**
 *
 * @author p993702
 */
public class ResumoComissao {
    private String representada;
    private String vendedor;
    private Integer idVendedor;
    private String segmento;
    private BigDecimal valorVenda;
    private BigDecimal comissaoVenda;
    private BigDecimal comissaoVendedor;
    private BigDecimal comissaoEmpresa;
    private BigDecimal comissaoTotal;

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public BigDecimal getComissaoEmpresa() {
        return comissaoEmpresa;
    }

    public void setComissaoEmpresa(BigDecimal comissaoEmpresa) {
        this.comissaoEmpresa = comissaoEmpresa;
    }

    public BigDecimal getComissaoTotal() {
        return comissaoTotal;
    }

    public void setComissaoTotal(BigDecimal comissaoTotal) {
        this.comissaoTotal = comissaoTotal;
    }

    public BigDecimal getComissaoVenda() {
        return comissaoVenda;
    }

    public void setComissaoVenda(BigDecimal comissaoVenda) {
        this.comissaoVenda = comissaoVenda;
    }

    public BigDecimal getComissaoVendedor() {
        return comissaoVendedor;
    }

    public void setComissaoVendedor(BigDecimal comissaoVendedor) {
        this.comissaoVendedor = comissaoVendedor;
    }

    public String getRepresentada() {
        return representada;
    }

    public void setRepresentada(String representada) {
        this.representada = representada;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    
}
