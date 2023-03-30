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
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class VisitasModel extends ServiceTableModel {
    
    public static final int DT_VISITA = 0;
    public static final int VENDEDOR = 1;
    public static final int CLIENTE = 2;


    public VisitasModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Data da visita");
        addColumn("Vendedor");
        addColumn("Cliente(s)");
    }

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;

        switch (column) {
        case DT_VISITA: aClass = Date.class; break;
        case VENDEDOR: aClass = String.class; break;
        case CLIENTE: aClass = String.class; break;
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
            case CLIENTE: obj = visita.getTipoVisita().equals("SV") ? visita.getNomeCliente() : visita.getRazaoExtenso(); break;
        }

        return obj;
    }



}
