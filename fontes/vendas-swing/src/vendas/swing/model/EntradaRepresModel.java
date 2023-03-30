/*
 * ClienteRepresModel.java
 * 
 * Created on 30/06/2007, 12:12:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import vendas.entity.EntradaRepres;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class EntradaRepresModel extends BaseTableModel {
    
    public static final int DATA = 0;
    public static final int VALOR = 1;
    public static final int PEDIDOS = 2;
    public static final int OBS = 3;

    public EntradaRepresModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Data");
        addColumn("Valor");
        addColumn("Pedidos");
        addColumn("Observa\u00E7\u00E3o");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case DATA: aClass = Date.class; break;
        case VALOR: aClass = BigDecimal.class; break;
        case PEDIDOS: aClass = String.class; break;
        case OBS: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        EntradaRepres entrada = (EntradaRepres)getObject(row);
        Object obj = null;
        switch (col) {
        case DATA: obj = entrada.getDtEntrada(); break;
        case VALOR: obj = entrada.getValor(); break;
        case PEDIDOS: obj = entrada.getPedido(); break;
        case OBS: obj = entrada.getObservacao(); break;
        }
        return obj;
    }
}
