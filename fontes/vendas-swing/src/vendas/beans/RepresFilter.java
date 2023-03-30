/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;
import vendas.entity.Repres;

/**
 *
 * @author p993702
 */
public class RepresFilter extends FilterBean {
    
    private Repres repres;
    private Date dtRecebimentoIni;
    private Date dtRecebimentoEnd;
    private Boolean desc;
    private int ordem;
    public static final int RAZAO = 0;
    public static final int VALOR = 1;

    public RepresFilter() {
        repres = new Repres();
        repres.setInativo('0');
        desc = false;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Date getDtRecebimentoIni() {
        return dtRecebimentoIni;
    }

    public void setDtRecebimentoIni(Date dtRecebimentoIni) {
        this.dtRecebimentoIni = dtRecebimentoIni;
    }

    public Date getDtRecebimentoEnd() {
        return dtRecebimentoEnd;
    }

    public void setDtRecebimentoEnd(Date dtRecebimentoEnd) {
        this.dtRecebimentoEnd = dtRecebimentoEnd;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
    
}
