/*
 * UnidadeProdutoModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.UnidadeProduto;

/**
 *
 * @author Sam
 */
public class UnidadeProdutoModel extends ServiceTableModel {

    public UnidadeProdutoModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Sigla");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        case 1: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        UnidadeProduto unidadeProduto = (UnidadeProduto)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = unidadeProduto.getNome(); break;
        case 1: obj = unidadeProduto.getIdUnidade(); break;
        }
        return obj;
    }
}
