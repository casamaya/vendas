/*
 * RepresProdutoModel.java
 * 
 * Created on 30/06/2007, 12:12:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.entity.RepresProduto;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class RepresProdutoModel extends BaseTableModel {
    
    public static final int NOME = 0;
    public static final int SITUACAO = 1;
    public static final int PERC = 2;
    public static final int IPI = 3;
    public static final int QTDUND = 4;
    public static final int PRECO = 5;
    public static final int EMBALAGEM = 6;

    public RepresProdutoModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Ativo");
        addColumn("% com.");
        addColumn("IPI");
        addColumn("Quant/Und");
        addColumn("Preço");        
        addColumn("Embalagem");        
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case NOME: aClass = String.class; break;
        case SITUACAO: aClass = Boolean.class; break;
        case IPI: aClass = BigDecimal.class; break;
        case PERC: aClass = BigDecimal.class; break;
        case QTDUND: aClass = BigDecimal.class; break;
        case PRECO: aClass = BigDecimal.class; break;
        case EMBALAGEM: aClass = BigDecimal.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        RepresProduto repres = (RepresProduto)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = repres.getRepres().getRazao(); break;
        case SITUACAO: obj = repres.getAtivado(); break;
        case PERC: obj = repres.getPercComissao(); break;
        case IPI: obj = repres.getIpi(); break;
        case QTDUND: obj = repres.getQtdUnd(); break;
        case PRECO: obj = repres.getPreco(); break;
        case EMBALAGEM: obj = repres.getEmbalagem(); break;
        }
        return obj;
    }
}
