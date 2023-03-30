/*
 * SegMercadoModel.java
 * 
 * Created on 09/07/2007, 21:38:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.Date;
import java.util.List;
import vendas.entity.PedidoEmbarque;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class PedidoEmbarqueModel extends BaseTableModel {

    public PedidoEmbarqueModel(List values)  {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Embarques anteriores");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        return Date.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        PedidoEmbarque segmento = (PedidoEmbarque)getObject(row);
        Object obj = null;
        if (col == 0) {
            obj = segmento.getDtEmbarque();
        }
        return obj;
    }
}
