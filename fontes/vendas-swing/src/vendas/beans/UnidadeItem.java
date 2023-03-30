/*
 * UnidadeItem.java
 * 
 * Created on 08/09/2007, 17:39:00
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Sam
 */
public class UnidadeItem implements Serializable {
    
    private String unidade;
    private Integer tipo;
    private BigDecimal valor;

    public UnidadeItem(String und, BigDecimal vlr) {
        unidade = und;
        valor = vlr;
        tipo = 1;
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

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

}
