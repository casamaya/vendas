/*
 * RepresTableModel.java
 *
 * Created on 27/06/2007, 18:31:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.Repres;

/**
 *
 * @author Sam
 */
public class RepresTableModel extends ServiceTableModel {
    
    public static final int RAZAO = 0;
    public static final int COMISSAO = 1;
    public static final int SALDO = 2;
    public static final int ATENDIMENTO = 3;
    public static final int CIDADE = 4;
    public static final int UF = 5;
    public static final int FONE1 = 6;
    public static final int FONE2 = 7;
    public static final int FONE3 = 8;
    public static final int ATIVO = 9;

    public RepresTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("Comissão");
        addColumn("Saldo");
        addColumn("Dias atend.");
        addColumn("Cidade");
        addColumn("UF");
        addColumn("Fone 1");
        addColumn("Fone 2");
        addColumn("Fone 3");
        addColumn("Ativo");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case RAZAO: aClass = String.class; break;
        case COMISSAO: aClass = BigDecimal.class; break;
        case SALDO: aClass = BigDecimal.class; break;
        case ATENDIMENTO: aClass = Integer.class; break;
        case CIDADE: aClass = String.class; break;
        case UF: aClass = String.class; break;
        case FONE1: aClass = String.class; break;
        case FONE2: aClass = String.class; break;
        case FONE3: aClass = String.class; break;
        case ATIVO: aClass = Boolean.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Repres repres = (Repres)getObject(row);
        Object obj = null;
        switch (col) {
        case RAZAO: obj = repres.getRazao(); break;
        case COMISSAO: obj = repres.getComissao(); break;
        case SALDO: obj = repres.getTotal(); break;
        case ATENDIMENTO: obj = repres.getDiasAtendimento(); break;
        case CIDADE: obj = repres.getCidade(); break;
        case UF: obj = repres.getUf(); break;
        case FONE1: obj = repres.getFone1(); break;
        case FONE2: obj = repres.getFone2(); break;
        case FONE3: obj = repres.getFone3(); break;
        case ATIVO: obj = repres.getAtivo(); break;
        }
        return obj;
    }
}
