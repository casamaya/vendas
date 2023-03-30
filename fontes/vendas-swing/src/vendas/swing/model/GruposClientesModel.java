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
import vendas.entity.GrupoCliente;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class GruposClientesModel extends BaseTableModel {

    public GruposClientesModel(List values) {
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
        GrupoCliente grupoCliente = (GrupoCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = grupoCliente.getNomeGrupo(); break;
        }
        return obj;
    }
}
