package vendas.swing.model;

import java.util.Date;
import java.util.List;
import vendas.entity.Pendencia;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author jaime
 */
public class PendenciaModel extends ServiceTableModel {
    public static final int DTINCLUSAO = 0;
    public static final int AUTOR = 1;
    public static final int DESCRICAO = 2;
    public static final int DTALTERACAO = 3;
    public PendenciaModel() {
        
    }
    
    public PendenciaModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Dt. inclusão");
        addColumn("Autor");
        addColumn("Descricao");
        addColumn("Dt. atualização");
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case DTINCLUSAO: aClass = Date.class; break;
        case DTALTERACAO: aClass = Date.class; break;
        default: aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Pendencia meta = (Pendencia)getObject(row);
        Object obj;
        
        switch (col) {
            case DTINCLUSAO: obj = meta.getDtInclusao(); break;
            case AUTOR: obj = meta.getUsername(); break;
            case DTALTERACAO: obj = meta.getDtAlteracao(); break;
            default: obj = meta.getDsPendencia();
        }
        
        return obj;
    }  
}
