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
public class MapaComissao {
    private String razao;
    private BigDecimal mes1;
    private BigDecimal mes2;
    private BigDecimal mes3;
    private BigDecimal mes4;
    private BigDecimal mes5;
    private BigDecimal mes6;
    private BigDecimal mes7;
    private BigDecimal mes8;
    private BigDecimal mes9;
    private BigDecimal mes10;
    private BigDecimal mes11;
    private BigDecimal mes12;

    public BigDecimal getMes1() {
        return mes1;
    }

    public void setMes1(BigDecimal mes1) {
        this.mes1 = mes1;
    }

    public BigDecimal getMes10() {
        return mes10;
    }

    public void setMes10(BigDecimal mes10) {
        this.mes10 = mes10;
    }

    public BigDecimal getMes11() {
        return mes11;
    }

    public void setMes11(BigDecimal mes11) {
        this.mes11 = mes11;
    }

    public BigDecimal getMes12() {
        return mes12;
    }

    public void setMes12(BigDecimal mes12) {
        this.mes12 = mes12;
    }

    public BigDecimal getMes2() {
        return mes2;
    }

    public void setMes2(BigDecimal mes2) {
        this.mes2 = mes2;
    }

    public BigDecimal getMes3() {
        return mes3;
    }

    public void setMes3(BigDecimal mes3) {
        this.mes3 = mes3;
    }

    public BigDecimal getMes4() {
        return mes4;
    }

    public void setMes4(BigDecimal mes4) {
        this.mes4 = mes4;
    }

    public BigDecimal getMes5() {
        return mes5;
    }

    public void setMes5(BigDecimal mes5) {
        this.mes5 = mes5;
    }

    public BigDecimal getMes6() {
        return mes6;
    }

    public void setMes6(BigDecimal mes6) {
        this.mes6 = mes6;
    }

    public BigDecimal getMes7() {
        return mes7;
    }

    public void setMes7(BigDecimal mes7) {
        this.mes7 = mes7;
    }

    public BigDecimal getMes8() {
        return mes8;
    }

    public void setMes8(BigDecimal mes8) {
        this.mes8 = mes8;
    }

    public BigDecimal getMes9() {
        return mes9;
    }

    public void setMes9(BigDecimal mes9) {
        this.mes9 = mes9;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }
    
    public BigDecimal getTotalMeses() {
        BigDecimal total = BigDecimal.ZERO;
        if (mes1 != null) total = total.add(mes1);
        if (mes2 != null) total = total.add(mes2);
        if (mes3 != null) total = total.add(mes3);
        if (mes4 != null) total = total.add(mes4);
        if (mes5 != null) total = total.add(mes5);
        if (mes6 != null) total = total.add(mes6);
        if (mes7 != null) total = total.add(mes7);
        if (mes8 != null) total = total.add(mes8);
        if (mes9 != null) total = total.add(mes9);
        if (mes10 != null) total = total.add(mes10);
        if (mes11 != null) total = total.add(mes11);
        if (mes12 != null) total = total.add(mes12);
        return total;
    }


}
