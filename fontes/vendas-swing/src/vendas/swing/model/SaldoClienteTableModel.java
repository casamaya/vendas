/*
 * PedidoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import vendas.beans.CobrancaFilter;
import vendas.beans.SaldoClienteTo;
import vendas.dao.ClienteDao;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class SaldoClienteTableModel extends ServiceTableModel {
    
    public static final int CLIENTE = 0;
    public static final int VALOR = 1;
    public static final int PERC = 2;
    
    public SaldoClienteTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Cliente");
        addColumn("Valor");
        addColumn("Perc.");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case CLIENTE : aClass = String.class; break;
            case VALOR : aClass = BigDecimal.class; break;
            case PERC : aClass = BigDecimal.class; break;
            default: aClass = String.class;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        SaldoClienteTo pgto = (SaldoClienteTo)getObject(row);
        Object obj;
        switch (col) {
        case CLIENTE : obj = pgto.getNome(); break;
        case VALOR : obj = pgto.getValor(); break;
        case PERC : obj = pgto.getPerc(); break;
        default : obj = null;
        }
        return obj;
    }

    @Override
    public void select(Object obj) throws Exception {
        ClienteDao dao = (ClienteDao)getDao();
        setItemList(dao.getSaldoCliente((CobrancaFilter)obj));
        fireTableDataChanged();
    }


}
