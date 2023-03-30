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
 * @author Sam
 */
public class VendasUnidadeFilter extends FilterBean {
    
    private Date dtEmissaoIni;
    private Date dtEmissaoEnd;
    private Date dtNotaIni;
    private Date dtNotaEnd;
        private Date dtPgtoNotaIni;
    private Date dtPgtoNotaEnd;
    private Date dtEntregaIni;
    private Date dtEntregaEnd;
    private Boolean pendentes;
    private int agrupamento;
    private Repres fornecedor;
    private Cliente cliente;
    private Cliente clienteResponsavel;
    private Vendedor vendedor;
    private GrupoCliente grupoCliente;
    private String situacao;
    private String atendimento;
    
    public VendasUnidadeFilter() {
        super();
        pendentes = new Boolean(true);
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

    public Date getDtNotaIni() {
        return dtNotaIni;
    }

    public void setDtNotaIni(Date dtNotaIni) {
        this.dtNotaIni = dtNotaIni;
    }

    public Date getDtNotaEnd() {
        return dtNotaEnd;
    }

    public void setDtNotaEnd(Date dtNotaEnd) {
        this.dtNotaEnd = dtNotaEnd;
    }

    public Date getDtEntregaIni() {
        return dtEntregaIni;
    }

    public void setDtEntregaIni(Date dtEntregaIni) {
        this.dtEntregaIni = dtEntregaIni;
    }

    public Date getDtEntregaEnd() {
        return dtEntregaEnd;
    }

    public void setDtEntregaEnd(Date dtEntregaEnd) {
        this.dtEntregaEnd = dtEntregaEnd;
    }

    public boolean isPendentes() {
        return pendentes.booleanValue();
    }

    public void setPendentes(Boolean pendentes) {
        this.pendentes = pendentes;
    }

    public int getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(int agrupamento) {
        this.agrupamento = agrupamento;
    }

    public Repres getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Repres fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Date getDtPgtoNotaEnd() {
        return dtPgtoNotaEnd;
    }

    public void setDtPgtoNotaEnd(Date dtPgtoNotaEnd) {
        this.dtPgtoNotaEnd = dtPgtoNotaEnd;
    }

    public Date getDtPgtoNotaIni() {
        return dtPgtoNotaIni;
    }

    public void setDtPgtoNotaIni(Date dtPgtoNotaIni) {
        this.dtPgtoNotaIni = dtPgtoNotaIni;
    }

    public GrupoCliente getGrupoCliente() {
        return grupoCliente;
    }

    public void setGrupoCliente(GrupoCliente grupoCliente) {
        this.grupoCliente = grupoCliente;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(String atendimento) {
        this.atendimento = atendimento;
    }

    public Cliente getClienteResponsavel() {
        return clienteResponsavel;
    }

    public void setClienteResponsavel(Cliente clienteResponsavel) {
        this.clienteResponsavel = clienteResponsavel;
    }
    
}
