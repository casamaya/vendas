/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author sam
 */
public class FechamentoBean implements Serializable {
    String nome;
    BigDecimal valor;
    BigDecimal perc;
    String tipo;
    
    public FechamentoBean(String nome, BigDecimal valor, String tipo) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPerc() {
        return perc;
    }

    public void setPerc(BigDecimal perc) {
        this.perc = perc;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
}
