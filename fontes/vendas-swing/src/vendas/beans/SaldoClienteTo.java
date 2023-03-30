/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vendas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author joliveira
 */
public class SaldoClienteTo implements Serializable {
    
    private Integer idCliente;
    private String nome;
    private BigDecimal valor;
    private BigDecimal perc;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getPerc() {
        return perc;
    }

    public void setPerc(BigDecimal perc) {
        this.perc = perc;
    }
    
}
