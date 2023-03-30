/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.math.BigDecimal;
import vendas.entity.Repres;

/**
 *
 * @author p993702
 */
public class AlteraPreco {
    private int tipo;
    private Boolean alteraComissao;
    private Boolean alteraPreco;
    private Boolean alteraPreco2;
    private Boolean alteraPrecoFinal;
    private Boolean alteraFrete;
    private Boolean alteraIPI;
    private BigDecimal valor;
    private Repres repres;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Boolean getAlteraComissao() {
        return alteraComissao;
    }

    public void setAlteraComissao(Boolean alteraComissao) {
        this.alteraComissao = alteraComissao;
    }

    public Boolean getAlteraFrete() {
        return alteraFrete;
    }

    public void setAlteraFrete(Boolean alteraFrete) {
        this.alteraFrete = alteraFrete;
    }

    public Boolean getAlteraPreco() {
        return alteraPreco;
    }

    public void setAlteraPreco(Boolean alteraPreco) {
        this.alteraPreco = alteraPreco;
    }

    public Boolean getAlteraPreco2() {
        return alteraPreco2;
    }

    public void setAlteraPreco2(Boolean alteraPreco2) {
        this.alteraPreco2 = alteraPreco2;
    }

    public Boolean getAlteraPrecoFinal() {
        return alteraPrecoFinal;
    }

    public void setAlteraPrecoFinal(Boolean alteraPrecoFinal) {
        this.alteraPrecoFinal = alteraPrecoFinal;
    }
    public Boolean getAlteraIPI() {
        return alteraIPI;
    }

    public void setAlteraIPI(Boolean alteraIPI) {
        this.alteraIPI = alteraIPI;
    }


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }
    
}
