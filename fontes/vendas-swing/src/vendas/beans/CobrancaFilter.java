/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.Repres;
import vendas.entity.Vendedor;

/**
 *
 * @author p993702
 */
public class CobrancaFilter extends FilterBean {
    
    private Date dtEmissaoIni;
    private Date dtEmissaoEnd;
    private Date dtPgtoIni;
    private Date dtPgtoEnd;
    private Date dtPrevPgtoIni;
    private Date dtPrevPgtoEnd;
    private Date dtVencimentoIni;
    private Date dtVencimentoEnd;
    private Date dtDescontoIni;
    private Date dtDescontoEnd;
    private Vendedor vendedor;
    private Repres repres;
    private Cliente cliente;
    private Cliente responsavel;
    private String situacao;
    private String tipoPgto;
    private String operador;
    private int ordem;
    private GrupoCliente grupo;

    public CobrancaFilter() {
        situacao = "N";
        ordem = 0;
        setTitle("Não atendidos");
        grupo = new GrupoCliente();
    }

    public Date getDtEmissaoIni() {
        return dtEmissaoIni;
    }

    public void setDtEmissaoIni(Date dtEmissaoIni) {
        this.dtEmissaoIni = dtEmissaoIni;
    }

    public Date getDtEmissaoEnd() {
        return dtEmissaoEnd;
    }

    public void setDtEmissaoEnd(Date dtEmissaoEnd) {
        this.dtEmissaoEnd = dtEmissaoEnd;
    }

    public Date getDtPgtoIni() {
        return dtPgtoIni;
    }

    public void setDtPgtoIni(Date dtPgtoIni) {
        this.dtPgtoIni = dtPgtoIni;
    }

    public Date getDtPgtoEnd() {
        return dtPgtoEnd;
    }

    public void setDtPgtoEnd(Date dtPgtoEnd) {
        this.dtPgtoEnd = dtPgtoEnd;
    }

    public Date getDtPrevPgtoIni() {
        return dtPrevPgtoIni;
    }

    public void setDtPrevPgtoIni(Date dtPrevPgtoIni) {
        this.dtPrevPgtoIni = dtPrevPgtoIni;
    }

    public Date getDtPrevPgtoEnd() {
        return dtPrevPgtoEnd;
    }

    public void setDtPrevPgtoEnd(Date dtPrevPgtoEnd) {
        this.dtPrevPgtoEnd = dtPrevPgtoEnd;
    }

    public Date getDtVencimentoIni() {
        return dtVencimentoIni;
    }

    public void setDtVencimentoIni(Date dtVencimentoIni) {
        this.dtVencimentoIni = dtVencimentoIni;
    }

    public Date getDtVencimentoEnd() {
        return dtVencimentoEnd;
    }

    public void setDtVencimentoEnd(Date dtVencimentoEnd) {
        this.dtVencimentoEnd = dtVencimentoEnd;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Cliente responsavel) {
        this.responsavel = responsavel;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTipoPgto() {
        return tipoPgto;
    }

    public void setTipoPgto(String tipoPgto) {
        this.tipoPgto = tipoPgto;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public GrupoCliente getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoCliente grupo) {
        this.grupo = grupo;
    }

    public Date getDtDescontoEnd() {
        return dtDescontoEnd;
    }

    public void setDtDescontoEnd(Date dtDescontoEnd) {
        this.dtDescontoEnd = dtDescontoEnd;
    }

    public Date getDtDescontoIni() {
        return dtDescontoIni;
    }

    public void setDtDescontoIni(Date dtDescontoIni) {
        this.dtDescontoIni = dtDescontoIni;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

}
