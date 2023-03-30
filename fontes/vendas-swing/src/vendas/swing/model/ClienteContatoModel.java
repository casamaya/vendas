/**
 * ClienteContatoModel.java
 * 
 * Created on 30/06/2007, 12:12:49
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.ClienteContato;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ClienteContatoModel extends BaseTableModel {
    
    public static final int TIPO = 0;
    public static final int ENDERECO = 1;

    public ClienteContatoModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Tipo");
        addColumn("Endereço");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case TIPO: aClass = String.class; break;
        case ENDERECO: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ClienteContato visita = (ClienteContato)getObject(row);
        Object obj = null;
        switch (col) {
        case TIPO: obj = visita.getTipoContato(); break;
        case ENDERECO: obj = visita.getEndereco(); break;
        }
        return obj;
    }
}
