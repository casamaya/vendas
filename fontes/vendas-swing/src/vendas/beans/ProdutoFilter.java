/*
 * ProdutoFilter.java
 * 
 * Created on 30/07/2007, 18:22:20
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.beans;

import vendas.entity.GrupoProduto;
import vendas.entity.Produto;
import vendas.entity.Repres;
import vendas.entity.SubGrupoProduto;
import vendas.entity.UnidadeProduto;

/**
 *
 * @author Sam
 */
public class ProdutoFilter extends FilterBean {
    
    public static final int ORDER_CODIGO = 0;
    public static final int ORDER_DESCRICAO = 1;

    private Produto produto;
    private Repres repres;
    private int order;
    private boolean inativo;

    public ProdutoFilter() {
        produto = new Produto();
        repres = new Repres();
        produto.setUndCumulativa(new UnidadeProduto());
        UnidadeProduto unidade = new UnidadeProduto();
        unidade.setNome("TODOS");
        produto.setUnidade(unidade);
        GrupoProduto grupoProduto = new GrupoProduto();
        grupoProduto.setNomeGrupo("TODOS");
        produto.setGrupoProduto(grupoProduto);
        SubGrupoProduto subGrupoProduto = new SubGrupoProduto();
        subGrupoProduto.setNomeGrupo("TODOS");
        produto.setGrupoProduto(grupoProduto);
        order = ORDER_DESCRICAO;
        inativo = false;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isInativo() {
        return inativo;
    }

    public void setInativo(boolean inativo) {
        this.inativo = inativo;
    }

}
