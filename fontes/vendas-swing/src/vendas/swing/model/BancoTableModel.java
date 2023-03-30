/*
 * BancoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.Banco;

/**
 *
 * @author Sam
 */
public class BancoTableModel extends ServiceTableModel {

    public BancoTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Código");
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
        Banco banco = (Banco)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = banco.getNome(); break;
        case 1: obj = banco.getBancoId(); break;
        }
        return obj;
    }
}
