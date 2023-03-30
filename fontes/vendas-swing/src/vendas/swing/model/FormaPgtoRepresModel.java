/*
 * ClienteVisitaModel.java
 *
 * Created on 30/06/2007, 12:12:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.FormaPgtoRepres;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class FormaPgtoRepresModel extends BaseTableModel {
    
    public static final int FORMAPGTO = 0;
    public static final int FRETE = 1;
    public static final int PGTOFRETE = 2;
    public static final int PERCNOTA = 3;
    public static final int IPI = 4;
    public static final int ICMS = 5;
    public static final int ENTREGA = 6;
    public static final int PEDIDOMINIMO = 7;
    public static final int PROCEDENCIA = 8;
    public static final int DISPONIBILIDADE = 9;
    public static final int OBS = 10;
    
    public FormaPgtoRepresModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Forma de pagamento");
        addColumn("Frete");
        addColumn("Pagamento frete");
        addColumn("Perc. nota");
        addColumn("IPI");
        addColumn("ICMS");
        addColumn("Entrega");
        addColumn("Pedido mínimo");
        addColumn("Procedência");
        addColumn("Disponibilidade");
        addColumn("Obs");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case FORMAPGTO: aClass = String.class; break;
        case FRETE: aClass = String.class; break;
        case PGTOFRETE: aClass = String.class; break;
        case PERCNOTA: aClass = String.class; break;
        case IPI: aClass = String.class; break;
        case ICMS: aClass = String.class; break;
        case ENTREGA: aClass = String.class; break;
        case PEDIDOMINIMO: aClass = String.class; break;
        case PROCEDENCIA: aClass = String.class; break;
        case DISPONIBILIDADE: aClass = String.class; break;
        case OBS: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        FormaPgtoRepres formaPgto = (FormaPgtoRepres)getObject(row);
        Object obj = null;
        switch (col) {
                                                                                                                                                                                                                                                                case FORMAPGTO: obj = formaPgto.getFormaPgto().getNome(); break;
        case FRETE: obj = formaPgto.getFrete(); break;
        case PGTOFRETE: obj = formaPgto.getPgtoFrete(); break;
        case PERCNOTA: obj = formaPgto.getPercentualNota(); break;
        case IPI: obj = formaPgto.getIpi(); break;
        case ICMS: obj = formaPgto.getIcms(); break;
        case ENTREGA: obj = formaPgto.getEntrega(); break;
        case PEDIDOMINIMO: obj = formaPgto.getPedidoMinimo(); break;
        case PROCEDENCIA: obj = formaPgto.getProcedencia(); break;
        case DISPONIBILIDADE: obj = formaPgto.getDisponibilidade(); break;
        case OBS: obj = formaPgto.getObs(); break;
        }
        return obj;
    }
}
