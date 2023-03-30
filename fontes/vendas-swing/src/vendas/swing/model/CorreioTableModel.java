/*
 * BancoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.Correio;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class CorreioTableModel extends ServiceTableModel {

    public CorreioTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Origem");
        addColumn("Data envio");
        addColumn("Mensagem");
    }   
    
    @Override
    public Class getColumnClass(int column) {
            return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Correio banco = (Correio)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = banco.getUserOrigem().getUserName(); break;
        case 1: obj = banco.getDataEnvio(); break;
        case 2: obj = banco.getMensagem(); break;
        }
        return obj;
    }
}
