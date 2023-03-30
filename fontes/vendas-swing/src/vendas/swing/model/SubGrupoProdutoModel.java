/*
 * GrupoProdutoModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.SubGrupoProduto;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class SubGrupoProdutoModel extends ServiceTableModel {

    public SubGrupoProdutoModel() throws Exception {
        super();
    }
    public SubGrupoProdutoModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        SubGrupoProduto grupoProduto = (SubGrupoProduto)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = grupoProduto.getNomeGrupo(); break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        SubGrupoProduto grupoProduto = (SubGrupoProduto)getObject(row);
        if (column == 0)
            grupoProduto.setNomeGrupo((String) aValue);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (column == 0);
    }



}
