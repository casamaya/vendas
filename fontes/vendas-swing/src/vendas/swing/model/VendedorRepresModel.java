/*
 * VendedorRepresModel.java
 * 
 * Created on 09/07/2007, 21:41:58
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.VendedorRepres;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class VendedorRepresModel extends BaseTableModel {
    
   public static final int NOME = 0;
   public static final int ANIVER = 1;

    public VendedorRepresModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Aniversário");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case NOME: aClass = String.class; break;
        case ANIVER: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        VendedorRepres vendedor = (VendedorRepres)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = vendedor.getContato(); break;
        case ANIVER: obj = vendedor.getAniversarioExtenso(); break;
        }
        return obj;
    }

}
