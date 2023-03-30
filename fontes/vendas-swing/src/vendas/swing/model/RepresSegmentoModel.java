/*
 * ClienteSegmentoModel.java
 * 
 * Created on 14/07/2007, 11:09:46
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.SegMercado;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class RepresSegmentoModel extends BaseTableModel {
    
    public RepresSegmentoModel(List values) {
        super(values);
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
