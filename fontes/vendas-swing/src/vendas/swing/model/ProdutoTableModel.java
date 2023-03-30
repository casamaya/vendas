/*
 * ProdutoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.Produto;

/**
 *
 * @author Sam
 */
public class ProdutoTableModel extends ServiceTableModel {
    public static final int BLOQUEADO = 0;
    public static final int CODIGO = 1;
    public static final int DESCRICAO = 2;
    public static final int UNIDADE = 3;
    public static final int UNDCUMUL = 4;
    public static final int PESO = 5;
    public static final int FATOR = 6;
    public static final int GRUPO = 7;
    public static final int SUBGRUPO = 8;
    
    public ProdutoTableModel() throws Exception {
        super();
    }
    
    @Override
    public void setColumns() {
        addColumn("Bloqueado");
        addColumn("Código");
        addColumn("Descri\u00E7\u00E3o");
        addColumn("Unidade");
        addColumn("Und. cumulativa");
        addColumn("Peso");
        addColumn("Fator conversão");
        addColumn("Grupo");
        addColumn("Sub-grupo");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case BLOQUEADO: aClass = Boolean.class; break;
        case CODIGO: aClass = Integer.class; break;
        case DESCRICAO: aClass = String.class; break;
        case UNIDADE: aClass = String.class; break;
        case UNDCUMUL: aClass = String.class; break;
        case PESO: aClass = BigDecimal.class; break;
        case FATOR: aClass = BigDecimal.class; break;
        case GRUPO: aClass = String.class; break;
        case SUBGRUPO: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Produto produto = (Produto)getObject(row);
        Object obj = null;
        switch (col) {
        case BLOQUEADO: obj = produto.isBloqueado(); break;
        case CODIGO: obj = produto.getIdProduto(); break;
        case DESCRICAO: obj = produto.getDescricao(); break;
        case UNIDADE: obj = produto.getUnidade().getIdUnidade(); break;
        case UNDCUMUL: obj = produto.getUndCumulativa().getIdUnidade(); break;
        case PESO: obj = produto.getPeso(); break;
        case FATOR: obj = produto.getFatorConversao(); break;
        case GRUPO: obj = (produto.getGrupoProduto() == null) ? null : produto.getGrupoProduto().getNomeGrupo(); break;
        case SUBGRUPO: obj = (produto.getSubGrupoProduto() == null) ? null : produto.getSubGrupoProduto().getNomeGrupo(); break;
        }
        return obj;
    }
}
