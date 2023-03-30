/**
 * ClienteVisitaModel.java
 * 
 * Created on 30/06/2007, 12:12:49
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.BancoCliente;
import java.util.List;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class BancoClienteModel extends BaseTableModel {
    
    public static final int NUMERO = 0;
    public static final int NOME = 1;
    public static final int AGENCIA = 2;
    public static final int CONTA = 3;

    public BancoClienteModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Número");
        addColumn("Nome");
        addColumn("Agência");
        addColumn("Conta");
    }
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        BancoCliente visita = (BancoCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case NUMERO: obj = visita.getBanco().getBancoId(); break;
        case NOME: obj = visita.getBanco().getNome(); break;
        case AGENCIA: obj = visita.getAgencia(); break;
        case CONTA: obj = visita.getConta(); break;
        }
        return obj;
    }
}
