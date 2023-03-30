/*
 * BancoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.beans.UserSelect;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class UsersTableModel extends ServiceTableModel {

    public UsersTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("S");
        addColumn("Usuário");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        if (column == 0)
            return Boolean.class;
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        UserSelect banco = (UserSelect)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = banco.getSelecionado(); break;
        case 1: obj = banco.getUser().getUserName(); break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        UserSelect banco = (UserSelect)getObject(row);
        
        if (col == 0)
            banco.setSelecionado((Boolean)o);

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }
    
    
    
}
