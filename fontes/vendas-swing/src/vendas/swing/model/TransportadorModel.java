/*
 * TransportadorModel.java
 * 
 * Created on 09/07/2007, 21:38:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import vendas.swing.core.ServiceTableModel;
import vendas.entity.Transportador;

/**
 *
 * @author Sam
 */
public class TransportadorModel extends ServiceTableModel {
    
    public static final int CODIGO = 0;
    public static final int NOME = 1;
    public static final int FONE1 = 2;
    public static final int FONE2 = 3;
    public static final int FORMAPGTO = 4;
    public static final int NUCONTACORRENTE = 5;

    public TransportadorModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Código");
        addColumn("Nome");
        addColumn("Fone 1");
        addColumn("Fone 2");
        addColumn("Forma pagamento");
        addColumn("Conta/PIX");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Transportador trans = (Transportador)getObject(row);
        Object obj = null;
        switch (col) {
            case CODIGO: obj = trans.getCodTrans(); break;
            case NOME: obj = trans.getNome(); break;
            case FONE1: obj = trans.getFone1(); break;
            case FONE2: obj = trans.getFone2(); break;
            case FORMAPGTO: obj = trans.getFormaPgto() == null ? "" : trans.getFormaPgto().getNome(); break;
            case NUCONTACORRENTE: obj = trans.getNuContaCorrente();
        }
        return obj;
    }
}
