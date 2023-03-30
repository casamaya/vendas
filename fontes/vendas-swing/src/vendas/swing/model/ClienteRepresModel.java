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
import java.util.List;
import vendas.entity.ClienteRepres;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class ClienteRepresModel extends ServiceTableModel {
    
    public static final int NOME = 0;
    public static final int CODIGO = 1;
    public static final int IDENTIFICADOR = 2;
    public static final int LIMITE = 3;

    public ClienteRepresModel() {
        super();
    }

    public ClienteRepresModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Código");
        addColumn("Identificador");
        addColumn("Limite");
    }
    
    @Override
    public Class getColumnClass(int col) {
        Class clazz;
        switch (col) {
            case LIMITE: clazz = BigDecimal.class; break;
            default: clazz = String.class; break;
        }
        return clazz;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ClienteRepres repres = (ClienteRepres)getObject(row);
        Object obj = null;
        switch (col) {
            case NOME: obj = repres.getRepres().getRazao(); break;
            case CODIGO: obj = repres.getCodRepres(); break;
            case IDENTIFICADOR: obj = repres.getCodIdentificador(); break;
            case LIMITE: obj = repres.getLimiteCredito(); break;
        }
        return obj;
    }

}
