/*
 * BancoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.Colaborador;

/**
 *
 * @author Sam
 */
public class ColaboradorTableModel extends ServiceTableModel {

    public ColaboradorTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Fun\u00E7\u00E3o");
        addColumn("Aniversário");
        addColumn("Cidade");
        addColumn("UF");
        addColumn("Fone 1");
        addColumn("Fone 2");
        addColumn("Fone 3");
        addColumn("E-mail");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Colaborador colaborador = (Colaborador)getObject(row);
        Object obj = null;
        switch (col) {
        case 0: obj = colaborador.getNome(); break;
        case 1: {
            if (colaborador.getFuncaoColaborador() != null) {
                obj = colaborador.getFuncaoColaborador().getNome();
            }
        } break;
        case 2: obj = colaborador.getAniversario(); break;
        case 3: obj = colaborador.getCidade(); break;
        case 4: obj = colaborador.getUf(); break;
        case 5: obj = colaborador.getFone1(); break;
        case 6: obj = colaborador.getFone2(); break;
        case 7: obj = colaborador.getFone3(); break;
        case 8: obj = colaborador.getEmail(); break;
        }
        return obj;
    }
}
