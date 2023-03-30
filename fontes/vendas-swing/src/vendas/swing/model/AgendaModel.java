/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.Date;
import java.util.List;
import vendas.entity.Agenda;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class AgendaModel extends ServiceTableModel {
    public static final int DTEVENTO = 0;
    public static final int DESCRICAO = 1;
    
    public AgendaModel() throws Exception {
        super();
    }
    
    public AgendaModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Data");
        addColumn("Descri\u00E7\u00E3o");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case DTEVENTO: aClass = Date.class; break;
        case DESCRICAO: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        Agenda banco = (Agenda)getObject(row);
        Object obj = null;
        switch (col) {
        case DTEVENTO: obj = banco.getDtevento(); break;
        case DESCRICAO: obj = banco.getDescricao(); break;
        }
        return obj;
    }
    
}
