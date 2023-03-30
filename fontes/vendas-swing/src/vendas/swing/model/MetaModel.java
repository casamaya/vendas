/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.entity.Meta;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class MetaModel extends ServiceTableModel {
    public static final int ANOMES = 0;
    public static final int VALOR = 1;
    
    public MetaModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Mês/Ano");
        addColumn("Valor meta");

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == VALOR;
    }
    
    

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case ANOMES: aClass = String.class; break;
        case VALOR: aClass = BigDecimal.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Meta meta = (Meta)getObject(row);
        Object obj = null;
        switch (col) {
        case ANOMES: obj = meta.getMesAno(); break;
        case VALOR: obj = meta.getValorMeta(); break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        if (col == VALOR) {
            Meta meta = (Meta)getObject(row);
             meta.setValorMeta((BigDecimal) o);
             PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
             try {
             dao.updateRow(meta);
             fireTableDataChanged();
             } catch (Exception e) {
                 getLogger().error(e.getMessage(), e);
             }
        }
    }
    

}
