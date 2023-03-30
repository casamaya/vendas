/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.beans.ResumoMovimento;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author sam
 */
public class ResumoGrupoMovimentoModel extends ServiceTableModel {
    public static final int NOME = 0;
    public static final int CREDITO = 1;
    public static final int DEBITO = 2;

    public ResumoGrupoMovimentoModel() throws Exception {
        super();
    }
    
    public ResumoGrupoMovimentoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Grupo");
        addColumn("Crébito");
        addColumn("Débito");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass;
        
        switch (column) {
            case NOME: aClass = String.class; break;
            default: aClass = BigDecimal.class;
        }
        
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ResumoMovimento banco = (ResumoMovimento)getObject(row);
        Object obj;
        
        switch (col) {
            case CREDITO: obj = banco.getValorCredito(); break;
            case DEBITO: obj = banco.getValorDebito(); break;
            default: obj = banco.getGrupoMovimento();
        }
        return obj;
    }
}
