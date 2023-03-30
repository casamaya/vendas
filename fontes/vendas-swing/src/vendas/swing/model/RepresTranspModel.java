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
import vendas.entity.Transportador;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class RepresTranspModel extends BaseTableModel {
    
    public RepresTranspModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Codigo");
        addColumn("Nome");
        addColumn("Forma pagamento");
    }

    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Transportador segmento = (Transportador)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = segmento.getCodTrans(); break;
        case 1: obj = segmento.getNome(); break;
        case 2: obj = segmento.getFormaPgto() == null ? null : segmento.getFormaPgto().getNome(); break;
        }
        return obj;
    }
}
