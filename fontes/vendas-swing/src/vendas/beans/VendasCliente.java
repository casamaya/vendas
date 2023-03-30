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
public class VendasCliente {
    
    private String razao;
    private Integer id;
    private String vendedor;
    private BigDecimal valorVenda;
    private BigDecimal valorComissao;
    private BigDecimal valorVencido;
    private BigDecimal valorPgto;
    private BigDecimal numDiasAtraso;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazao() {
        return razao;
    }

    public void setRAZAO(String razao) {
        this.razao = razao;
    }

    public BigDecimal getValorPgto() {
        return valorPgto;
    }

    public void setValorPgto(BigDecimal valorPgto) {
        this.valorPgto = valorPgto;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setVALORVENDA(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setVALORCOMISSAO(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public BigDecimal getValorVencido() {
        return valorVencido;
    }

    public void setValorVencido(BigDecimal valorVencido) {
        this.valorVencido = valorVencido;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getNumDiasAtraso() {
        return numDiasAtraso;
    }

    public void setNumDiasAtraso(BigDecimal numDiasAtraso) {
        this.numDiasAtraso = numDiasAtraso;
    }

    

}
