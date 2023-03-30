/**
 * ReferenciaClienteModel.java
 * 
 * Created on 30/06/2007, 12:12:49
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.ReferenciaCliente;
import java.util.List;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ReferenciaClienteModel extends BaseTableModel {
    
    public static final int NOME = 0;
    public static final int FONE1 = 1;
    public static final int FONE2 = 2;
    public static final int EMAIL = 3;

    public ReferenciaClienteModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Fone 1");
        addColumn("Fone 2");
        addColumn("E-mail");
    }
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ReferenciaCliente visita = (ReferenciaCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = visita.getReferencia().getNome(); break;
        case FONE1: obj = visita.getFone1(); break;
        case FONE2: obj = visita.getFone2(); break;
        case EMAIL: obj = visita.getEmail(); break;
        }
        return obj;
    }
}
