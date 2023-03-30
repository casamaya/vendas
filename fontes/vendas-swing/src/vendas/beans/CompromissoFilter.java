/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.util.Date;
import ritual.util.DateUtils;
import vendas.entity.Conta;
import vendas.entity.TipoPgto;

/**
 *
 * @author Sam
 */
public class CompromissoFilter extends FilterBean {

    private Date dtIni;
    private Date dtEnd;
    private Date dtIniPgto;
    private Date dtEndPgto;
    private Conta conta;
    private Conta grupo;
    private TipoPgto tipoPgto;
    private int situacao;

    public CompromissoFilter() {
    }

    public Conta getConta() {
        return conta;
    }

    public Conta getGrupo() {
        return grupo;
    }

    public void setGrupo(Conta grupo) {
        this.grupo = grupo;
    }

    public Date getDtIniPgto() {
        return dtIniPgto;
    }

    public void setDtIniPgto(Date dtIniPgto) {
        this.dtIniPgto = dtIniPgto;
    }

    public Date getDtEndPgto() {
        return dtEndPgto;
    }

    public void setDtEndPgto(Date dtEndPgto) {
        this.dtEndPgto = dtEndPgto;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public Date getDtIni() {
        return dtIni;
    }

    public void setDtIni(Date dtIni) {
        this.dtIni = dtIni;
    }

    public TipoPgto getTipoPgto() {
        return tipoPgto;
    }

    public void setTipoPgto(TipoPgto tipoPgto) {
        this.tipoPgto = tipoPgto;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }
}
