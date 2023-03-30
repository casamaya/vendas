/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.util.List;
import vendas.entity.Cliente;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ClienteGrupoModel extends BaseTableModel {

    public ClienteGrupoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Razão social");
    }

    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }
    @Override
    public Object getValueAt(int row, int col) {
        Cliente cliente = (Cliente)getObject(row);
        return cliente.getRazao();
    }
}
