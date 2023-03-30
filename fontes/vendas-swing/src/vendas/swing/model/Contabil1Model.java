/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import vendas.entity.Contabil;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author jaimeoliveira
 */
public class Contabil1Model extends ServiceTableModel {

    public static final int DESCRICAO = 0;
    public static final int COMPROMISSO = 1;
    public static final int DATA = 2;
    public static final int VALOR = 3;
    
    public Contabil1Model(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Nome");
        addColumn("NF");
        addColumn("Data");
        addColumn("Valor");
    }

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case DESCRICAO: aClass = String.class; break;
            case COMPROMISSO: aClass = String.class; break;
            case DATA: aClass = Date.class; break;
            case VALOR: aClass = BigDecimal.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Contabil comprador = (Contabil)getObject(row);
        Object obj = null;
        switch (col) {
            case DESCRICAO: obj = comprador.getDescricao(); break;
            case COMPROMISSO: obj = comprador.getCompromisso(); break;
            case DATA: obj = comprador.getDtVencimento(); break;
            case VALOR: obj = comprador.getValor(); break;
        }
        return obj;
    }
}
