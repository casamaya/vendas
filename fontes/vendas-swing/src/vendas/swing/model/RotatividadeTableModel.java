/**
 * ClienteContatoModel.java
 * 
 * Created on 30/06/2007, 12:12:18
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.swing.core.BaseTableModel;
import ritual.util.DateUtils;
import vendas.beans.Rotatividade;

/**
 *
 * @author Sam
 */
public class RotatividadeTableModel extends BaseTableModel {

    public RotatividadeTableModel() throws Exception {
        super();
    }

    public RotatividadeTableModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        String[] meses = DateUtils.getLastMonths(DateUtils.getMonth(DateUtils.getDate()));
        for (String mes : meses) {
            addColumn(mes);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int column) {
        return BigDecimal.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Rotatividade resumo = (Rotatividade) getObject(row);
        Object value = null;
        switch (col) {
            case 0: value = resumo.getMes12(); break;
            case 1: value = resumo.getMes11(); break;
            case 2: value = resumo.getMes10(); break;
            case 3: value = resumo.getMes9(); break;
            case 4: value = resumo.getMes8(); break;
            case 5: value = resumo.getMes7(); break;
            case 6: value = resumo.getMes6(); break;
            case 7: value = resumo.getMes5(); break;
            case 8: value = resumo.getMes4(); break;
            case 9: value = resumo.getMes3(); break;
            case 10: value = resumo.getMes2(); break;
            case 11: value = resumo.getMes1(); break;
        }
        return value;
    }
}
