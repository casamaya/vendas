/*
 * VendedorTableModel.java
 *
 * Created on 22/06/2007, 15:15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.User;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author p993702
 */
public class UserTableModel extends ServiceTableModel {
    
    public UserTableModel() throws Exception {
        super();
   }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Perfil");        
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
        User vendedor = (User)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = vendedor.getUserName(); break;
        case 1: obj = vendedor.getPerfisExtenso(); break;
        }
        return obj;
    }
}
