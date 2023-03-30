/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author p993702
 */
public interface Compromisso {
    
    public Integer getIdCompromisso();

    public void setIdCompromisso(Integer idCompromisso);

    public Date getDtVencimento();

    public void setDtVencimento(Date dtVencimento);

    public BigDecimal getValor();

    public void setValor(BigDecimal valor);

    public Date getDtPgto();

    public void setDtPgto(Date dtPgto);

    public Conta getConta();

    public void setConta(Conta idconta);

    public TipoPgtoFinanceiro getTipoPgtoFinanceiro();

    public void setTipoPgto(TipoPgtoFinanceiro idtipopgto);

    public String getObservacao();

    public void setObservacao(String nome);

}
