/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.util.Date;
import vendas.entity.ContaFluxo;
import vendas.entity.GrupoMovimento;

/**
 *
 * @author sam
 */
public class MovimentoFilter extends FilterBean {
    
    ContaFluxo conta;
    Date dtInicio;
    Date dtTermino;
    String descricao;
    String documento;
    Integer tipo;
    Integer vendedor;
    GrupoMovimento grupo;
    
    public MovimentoFilter() {
        tipo = -1;
    }

    public ContaFluxo getConta() {
        return conta;
    }

    public void setConta(ContaFluxo conta) {
        this.conta = conta;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public void setVendedor(Integer idVendedor) {
        vendedor = idVendedor;
    }
    
    public Integer getVendedor() {
        return vendedor;
    }

    public GrupoMovimento getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoMovimento grupo) {
        this.grupo = grupo;
    }
    
}
