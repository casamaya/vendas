/*
 * SegMercadoModel.java
 * 
 * Created on 09/07/2007, 21:38:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.Referencia;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class ReferenciaModel extends ServiceTableModel {

    public ReferenciaModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Fone");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Referencia segmento = (Referencia)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = segmento.getNome(); break;
        case 1: obj = segmento.getFone(); break;
        }
        return obj;
    }
}
