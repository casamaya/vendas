/*
 * VendedorTableModel.java
 *
 * Created on 22/06/2007, 15:15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.Vendedor;

/**
 *
 * @author p993702
 */
public class VendedorTableModel extends ServiceTableModel {
    
    public VendedorTableModel() throws Exception {
        super();
   }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Comissionado");        
        addColumn("Ativo");
    }   
    
    @Override
    public void setValueAt(Object value, int row, int column) {
        try {
            Vendedor vendedor = (Vendedor) getObject(row);
            switch (column) {
            case 0: vendedor.setNome((String)value); break;
            case 1: vendedor.setComissionado((Boolean)value); break;
            case 2: vendedor.setAtivo((Boolean)value); break;
            }
            //service.update(Compromisso);
            fireTableCellUpdated(row, column);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        case 1: aClass = Boolean.class; break;
        case 2: aClass = Boolean.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Vendedor vendedor = (Vendedor)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = vendedor.getNome(); break;
        case 1: obj = vendedor.getRecebeComissao(); break;
        case 2: obj = vendedor.isAtivo(); break;
        }
        return obj;
    }
}
