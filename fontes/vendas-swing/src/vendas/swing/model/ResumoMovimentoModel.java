/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.beans.ResumoMovimento;
import vendas.entity.Agenda;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class ResumoMovimentoModel extends ServiceTableModel {
    public static final int ANOMES = 0;
    public static final int DEBITO = 1;
    public static final int CREDITO = 2;
    
    public ResumoMovimentoModel() throws Exception {
        super();
    }
    
    public ResumoMovimentoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Período");
        addColumn("Débito");
        addColumn("Crédito");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case ANOMES: aClass = String.class; break;
        default: aClass = BigDecimal.class;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ResumoMovimento banco = (ResumoMovimento)getObject(row);
        Object obj = null;
        switch (col) {
        case ANOMES: obj = banco.getAnoMes(); break;
        case DEBITO: obj = banco.getValorDebito(); break;
        case CREDITO: obj = banco.getValorCredito(); break;
        }
        return obj;
    }
    
}
