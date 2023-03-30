/*
 * GrupoClienteModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.entity.ComissaoVendedor;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ComissaoVendedorModel extends BaseTableModel {
    
    public ComissaoVendedorModel(List values) {
        super(values);
    }

    public ComissaoVendedorModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Comissão");
        addColumn("Fornecedor");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = BigDecimal.class; break;
        case 1: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        ComissaoVendedor comissao = (ComissaoVendedor)getObject(row);
        switch (column) {
        case 0: comissao.setComissao((BigDecimal)aValue); break;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result;
        switch (column) {
        case 0: result = true; break;
        default: result = false;
        }
        return result;
    }

    @Override
    public Object getValueAt(int row, int column) {
        ComissaoVendedor comissao = (ComissaoVendedor)getObject(row);
        Object obj = null;
        switch (column) {
        case 0: obj = comissao.getComissao(); break;
        case 1: obj = comissao.getRepres().getRazao(); break;
        }
        return obj;
    }
    
    

}
