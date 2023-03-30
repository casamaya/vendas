/**
 * ClienteVisitaModel.java
 * 
 * Created on 30/06/2007, 12:12:49
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.entity.VisitaCliente;
import java.util.Date;
import java.util.List;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ClienteVisitaModel extends BaseTableModel {
    
    public static final int DT_VISITA = 0;
    public static final int VENDEDOR = 1;
    public static final int PEDIDO = 2;
    public static final int OBS = 3;

    public ClienteVisitaModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Data da visita");
        addColumn("Vendedor");
        addColumn("Gerou pedido");
        addColumn("Observa\u00E7\u00E3o");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case DT_VISITA: aClass = Date.class; break;
        case VENDEDOR: aClass = String.class; break;
        case PEDIDO: aClass = Boolean.class; break;
        case OBS: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        VisitaCliente visita = (VisitaCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case DT_VISITA: obj = visita.getDtVisita(); break;
        case VENDEDOR: obj = visita.getVendedor().getNome(); break;
        case PEDIDO: obj = visita.getGerouPedido(); break;
        case OBS: obj = visita.getTipoVisita().equals("N") ? ("SEM VISITA: " + visita.getObs()) : visita.getObs(); break;
        }
        return obj;
    }
}
