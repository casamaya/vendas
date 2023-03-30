/*
 * SituacaoClienteModel.java
 * 
 * Created on 09/07/2007, 21:41:58
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.SituacaoCliente;

/**
 *
 * @author Sam
 */
public class SituacaoClienteModel extends ServiceTableModel {

    public SituacaoClienteModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Permite pedido");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        case 1: aClass = Boolean.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        SituacaoCliente situacao = (SituacaoCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = situacao.getNome(); break;
        case 1: obj = situacao.getPermitePedido(); break;
        }
        return obj;
    }

}
