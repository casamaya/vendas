/*
 * PedidoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import vendas.beans.CobrancaFilter;
import vendas.dao.PedidoDao;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.PgtoCliente;

/**
 *
 * @author Sam
 */
public class PlanilhaChequeTableModel extends ServiceTableModel {
    
    public static final int TIPO = 0;
    public static final int DTVENCIMENTO = 1;
    public static final int VALORPAGO = 2;
    public static final int AGENCIA = 3;
    public static final int CONTA = 4;
    public static final int BANCO = 5;
    public static final int CREDOR = 6;
    public static final int CPFCNPJ = 7;

    public PlanilhaChequeTableModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Tipo");
        addColumn("Dt. vencimento");
        addColumn("Valor");
        addColumn("Agência");
        addColumn("Conta");
        addColumn("Banco");
        addColumn("Credor");
        addColumn("CPF/CNPJ");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case TIPO : aClass = String.class; break;
            case DTVENCIMENTO : aClass = Date.class; break;
            case VALORPAGO : aClass = BigDecimal.class; break;
            case AGENCIA : aClass = String.class; break;
            case CONTA : aClass = Integer.class; break;
            case BANCO : aClass = Date.class; break;
            case CREDOR : aClass = BigDecimal.class; break;
            case CPFCNPJ : aClass = String.class; break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        PgtoCliente pgto = (PgtoCliente)getObject(row);
        Object obj = null;
        switch (col) {
        case TIPO : obj = pgto.getTipoPgto(); break;
        case DTVENCIMENTO : obj = pgto.getDtVencimento(); break;
        case VALORPAGO : obj = pgto.getValorPgto(); break;
        }
        return obj;
    }
    @Override
    public void deleteRow(int row) throws Exception {
        infoLog("deleteRow");
        try {
            PgtoCliente pgto = (PgtoCliente)getObject(row);
            PedidoDao dao = (PedidoDao)getDao();
            dao.deleteRow(pgto);
            infoLog("atualizando model");
            removeObject(row);
            infoLog("atualizado");
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }

    @Override
    public void select(Object obj) throws Exception {
        PedidoDao dao = (PedidoDao)getDao();
        setItemList(dao.findPgtosCliente((CobrancaFilter)obj));
    }


}
