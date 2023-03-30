/*
 * GrupoClienteModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.RoteiroVendedor;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class RoteiroVendedorModel extends BaseTableModel {
    
    public RoteiroVendedorModel(List values) {
        super(values);
    }

    public RoteiroVendedorModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Roteiro");
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
    public void setValueAt(Object aValue, int row, int column) {
        RoteiroVendedor roteiro = (RoteiroVendedor)getObject(row);
        switch (column) {
        case 0: roteiro.setDescricao((String)aValue); break;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        RoteiroVendedor roteiro = (RoteiroVendedor)getObject(row);
        Object obj = null;
        switch (column) {
        case 0: obj = roteiro.getDescricao(); break;
        }
        return obj;
    }
    
    

}
