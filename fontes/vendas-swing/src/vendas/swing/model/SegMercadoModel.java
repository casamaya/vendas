/*
 * SegMercadoModel.java
 * 
 * Created on 09/07/2007, 21:38:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.SegMercado;

/**
 *
 * @author Sam
 */
public class SegMercadoModel extends ServiceTableModel {

    public SegMercadoModel() throws Exception {
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
        SegMercado segmento = (SegMercado)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = segmento.getNome(); break;
        }
        return obj;
    }
}
