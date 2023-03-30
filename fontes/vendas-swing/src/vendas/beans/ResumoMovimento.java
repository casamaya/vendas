/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.math.BigDecimal;

/**
 *
 * @author jaimeoliveira
 */
public class ResumoMovimento {
    private String anoMes;
    private String conta;
    private String grupoMovimento;
    private BigDecimal valorDebito;
    private BigDecimal valorCredito;

    public String getAnoMes() {
        return anoMes;
    }

    public void setAnoMes(String anoMes) {
        this.anoMes = anoMes;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public BigDecimal getValorDebito() {
        return valorDebito;
    }

    public void setValorDebito(BigDecimal valorDebito) {
        this.valorDebito = valorDebito;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getGrupoMovimento() {
        return grupoMovimento;
    }

    public void setGrupoMovimento(String grupoMovimento) {
        this.grupoMovimento = grupoMovimento;
    }
    
    
}
