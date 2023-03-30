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
import java.util.Date;
import java.util.List;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class UltimoPedidoModel extends BaseTableModel {

    final public static int PEDIDO = 0;
    final public static int SITUACAO = 1;
    final public static int DATA = 2;
    final public static int REPRES = 3;
    final public static int VENDEDOR = 4;
    final public static int VALOR = 5;

    public UltimoPedidoModel() throws Exception {
        super();
    }

    public UltimoPedidoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Pedido");
        addColumn("Sit.");
        addColumn("Data");
        addColumn("Fornecedor");
        addColumn("Vendedor");
        addColumn("Valor");
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int column) {
        Class result = null;
        switch (column) {
            case 0: result = Integer.class; break;
            case 1: result = String.class; break;
            case 2: result = Date.class; break;
            case 3: result = String.class; break;
            case 4: result = String.class; break;
            case 5: result = BigDecimal.class; break;
        }
        return result;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object[] resumo = (Object[]) getObject(row);
        return resumo[col];
    }
}
