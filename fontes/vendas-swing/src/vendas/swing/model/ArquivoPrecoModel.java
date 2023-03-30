/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.ArquivoPreco;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class ArquivoPrecoModel extends ServiceTableModel {
    public static final int NOME = 0;
    
    public ArquivoPrecoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Arquivo");
    }

    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ArquivoPreco comprador = (ArquivoPreco)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = comprador.getDescricao(); break;
        }
        return obj;
    }

}
