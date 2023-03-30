/*
 * GrupoClienteModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.GrupoCliente;

/**
 *
 * @author Sam
 */
public class GrupoClienteModel extends ServiceTableModel {

    public GrupoClienteModel() throws Exception {
        super();
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
