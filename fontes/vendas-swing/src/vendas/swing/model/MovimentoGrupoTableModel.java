/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import vendas.entity.Movimento;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author jaime
 */
public class MovimentoGrupoTableModel  extends ServiceTableModel {
    public static final int DTMOV = 0;
    public static final int CONTA = 1;
    public static final int DOC = 2;
    public static final int DESCRICAO = 3;
    public static final int GRUPO = 4;
    public static final int TIPO = 5;
    public static final int VALOR = 6;
    public static final int OBS = 7;
    
    public MovimentoGrupoTableModel() throws Exception {
        super();
    }
    
    public MovimentoGrupoTableModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Data");
        addColumn("Conta");
        addColumn("Documento");
        addColumn("Descricao");
        addColumn("Grupo");
        addColumn("Tipo");
        addColumn("Valor");
        addColumn("Saldo");
        addColumn("Observa\u00E7\u00E3o");
    } 
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass;
        switch (column) {
        case DTMOV  : aClass = Date.class; break;
        case VALOR  : aClass = BigDecimal.class; break;
        default : aClass = String.class;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        Movimento movimento = (Movimento)getObject(row);
        Object obj = null;
        switch (col) {
        case DTMOV: obj = movimento.getDtMov(); break;
        case CONTA: obj = movimento.getContaFluxo().getNome(); break;
        case DOC: obj = movimento.getDocumento(); break;
        case DESCRICAO: obj = movimento.getDescricao(); break;
        case GRUPO: obj = movimento.getGrupoMovimento() != null ? movimento.getGrupoMovimento().getNomeGrupo() : null; break;
        case TIPO:  obj = movimento.getTipo() == 0 ? "Débito" : "Crédito"; break;
        case VALOR: obj = movimento.getValor(); break;
        case OBS: obj = movimento.getObs(); break;
        }
        return obj;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
}
