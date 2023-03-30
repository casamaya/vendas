/*
 * ClienteContatoModel.java
 * 
 * Created on 30/06/2007, 12:12:18
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.ContatoVendedor;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ContatoVendedorModel extends BaseTableModel {
    
    public static final int NOME = 0;
    public static final int ANIVER = 1;
    public static final int MSN = 2;

    public ContatoVendedorModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Aniversário");
        addColumn("Email");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        case 1: aClass = String.class; break;
        case 2: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ContatoVendedor comprador = (ContatoVendedor)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = comprador.getContato(); break;
        case ANIVER: obj = comprador.getAniversarioExtenso(); break;
        case MSN: obj = comprador.getMsn(); break;
        }
        return obj;
    }
}
