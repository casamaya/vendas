package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.GrupoMovimento;

/**
 *
 * @author Sam
 */
public class GrupoMovimentoModel extends ServiceTableModel {

    public GrupoMovimentoModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        GrupoMovimento formaPgto = (GrupoMovimento)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = formaPgto.getNomeGrupo(); break;
        }
        return obj;
    }
}
