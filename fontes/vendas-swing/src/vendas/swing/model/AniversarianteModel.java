/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.beans.Aniversariante;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class AniversarianteModel extends ServiceTableModel {
    public static final int NOME = 0;
    public static final int ANIVER = 1;
    public static final int EMAIL = 2;
    public static final int TIPO = 3;
    public static final int RESPONSAVEL = 4;
    public static final int OBSERVACAO = 5;
    
    public AniversarianteModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Aniversário");
        addColumn("Email");
        addColumn("Tipo");
        addColumn("Responsavel");
        addColumn("Observa\u00E7\u00E3o");
    }

    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Aniversariante comprador = (Aniversariante)getObject(row);
        Object obj = null;
        switch (col) {
        case NOME: obj = comprador.getNome(); break;
        case ANIVER: obj = comprador.getAniversarioExtenso(); break;
        case EMAIL: obj = comprador.getMsn(); break;
        case TIPO: obj = comprador.getTipo(); break;
        case RESPONSAVEL: obj = comprador.getResponsavel(); break;
        case OBSERVACAO: if (comprador.getObservacao() == null)
                    obj = "";
                else if (comprador.getObservacao().length() >= 50)
                    obj = comprador.getObservacao().substring(0, 50) + "...";
                else
                    obj = comprador.getObservacao();
           break;
        }
        return obj;
    }
}
