/*
 * TipoPgtoModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.TipoPgtoFinanceiro;

/**
 *
 * @author Sam
 */
public class TipoPgtoFinanceiroModel extends ServiceTableModel {

    public TipoPgtoFinanceiroModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case 0: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        TipoPgtoFinanceiro formaPgto = (TipoPgtoFinanceiro)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = formaPgto.getNome(); break;
        }
        return obj;
    }
}
