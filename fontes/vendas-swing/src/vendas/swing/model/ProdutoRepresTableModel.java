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
import java.util.List;
import vendas.entity.RepresProduto;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class ProdutoRepresTableModel extends ServiceTableModel {
    
    public static final int BLOQUEADO = 0;
    public static final int CODIGO = 1;
    public static final int DESCRICAO = 2;
    public static final int UNIDADE = 3;
    public static final int UNDCUMUL = 4;
    public static final int PESO = 5;
    public static final int FATOR = 6;
    public static final int GRUPO = 7;
    public static final int SUBGRUPO = 8;
    public static final int SITUACAO = 9;
    public static final int PERC = 10;
    public static final int IPI = 11;
    public static final int QTDUND = 12;
    public static final int PRECO = 13;
    public static final int PRECO2 = 14;
    public static final int FRETE = 15;
    public static final int PRECOFINAL = 16;
    public static final int EMBALAGEM = 17;
    
    public ProdutoRepresTableModel() throws Exception {
        super();
    }
    
    public ProdutoRepresTableModel(List values) {
        super(values);
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
        addColumn("Ativo");
        addColumn("% com.");
        addColumn("IPI");
        addColumn("Quant/Und");
        addColumn("Preço Fornec.");        
        addColumn("Preço Cliente");        
        addColumn("Frete");        
        addColumn("Preço final");        
        addColumn("Embalagem");        
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
        case PESO: aClass = String.class; break;
        case FATOR: aClass = BigDecimal.class; break;
        case GRUPO: aClass = String.class; break;
        case SUBGRUPO: aClass = String.class; break;
        case SITUACAO: aClass = Boolean.class; break;
        case PERC: aClass = BigDecimal.class; break;
        case IPI: aClass = BigDecimal.class; break;
        case QTDUND: aClass = BigDecimal.class; break;
        case PRECO: aClass = BigDecimal.class; break;
        case PRECO2: aClass = BigDecimal.class; break;
        case FRETE: aClass = BigDecimal.class; break;
        case PRECOFINAL: aClass = BigDecimal.class; break;
        case EMBALAGEM: aClass = BigDecimal.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        RepresProduto prodRepr = (RepresProduto)getObject(row);
        Object obj = null;
        switch (col) {
        case BLOQUEADO: obj = prodRepr.getProduto().isBloqueado(); break;
        case CODIGO: obj = prodRepr.getProduto().getIdProduto(); break;
        case DESCRICAO: obj = prodRepr.getProduto().getDescricao(); break;
        case UNIDADE: obj = prodRepr.getProduto().getUnidade().getIdUnidade(); break;
        case UNDCUMUL: obj = prodRepr.getProduto().getUndCumulativa().getIdUnidade(); break;
        case PESO: obj = prodRepr.getProduto().getPeso(); break;
        case FATOR: obj = prodRepr.getProduto().getFatorConversao(); break;
        case GRUPO: obj = (prodRepr.getProduto().getGrupoProduto() == null) ? null : prodRepr.getProduto().getGrupoProduto().getNomeGrupo(); break;
        case SUBGRUPO: obj = (prodRepr.getProduto().getSubGrupoProduto() == null) ? null : prodRepr.getProduto().getSubGrupoProduto().getNomeGrupo(); break;
        case SITUACAO: obj = prodRepr.getAtivado(); break;
        case PERC: obj = prodRepr.getPercComissao(); break;
        case IPI: obj = prodRepr.getIpi(); break;
        case QTDUND: obj = prodRepr.getQtdUnd(); break;
        case PRECO: obj = prodRepr.getPreco(); break;
        case PRECO2: obj = prodRepr.getPreco2(); break;
        case FRETE: obj = prodRepr.getFrete(); break;
        case PRECOFINAL: obj = prodRepr.getPrecoFinal(); break;
        case EMBALAGEM: obj = prodRepr.getEmbalagem(); break;
        }
        return obj;
    }
}
