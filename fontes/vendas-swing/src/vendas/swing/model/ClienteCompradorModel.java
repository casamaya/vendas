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
import vendas.entity.Comprador;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ClienteCompradorModel extends BaseTableModel {
    
    public static final int NOME = 0;
    public static final int ANIVER = 1;
    public static final int MSN = 2;

    public ClienteCompradorModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Aniversário");
        addColumn("E-mail");
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
        Comprador contato = (Comprador)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = contato.getContato(); break;
        case ANIVER: obj = contato.getAniversarioExtenso(); break;
        case MSN: obj = contato.getMsn(); break;
        }
        return obj;
    }
}
