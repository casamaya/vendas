/*
 * ContaTableModel.java
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
import vendas.entity.Compromisso;
import vendas.entity.Conta;
import vendas.entity.TipoPgtoFinanceiro;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class CompromissoModel extends ServiceTableModel {

    private BigDecimal totalLancamentos;

    public CompromissoModel() {
        super();
    }

    public BigDecimal getTotalLancamentos() {
        return totalLancamentos;
    }

    public void setTotalLancamentos(BigDecimal totalLancamentos) {
        this.totalLancamentos = totalLancamentos;
    }

    @Override
    public void setColumns() {
        addColumn("Vencimento");
        addColumn("Compromisso");
        addColumn("Grupo");
        addColumn("Valor");
        addColumn("Tipo pagamento");
        addColumn("Data pagamento");
        addColumn("Observa\u00E7\u00E3o");
    }

    @Override
    public void insertRecord(Object object) throws Exception {
        super.insertRecord(object);
        updateTotals();
    }

    @Override
    public void deleteRow(int row) throws Exception {
        super.deleteRow(row);
        updateTotals();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 5) {
            Compromisso compromisso = (Compromisso) getItemList().get(row);
            return (compromisso.getDtPgto() == null);
        }
        return super.isCellEditable(row, column);
    }
    
    @Override
    public void setValueAt(Object value, int row, int column) {
        Compromisso compromisso = (Compromisso) getItemList().get(row);

        switch (column) {
            case 0: compromisso.setDtVencimento((Date) value); break;
            case 1: compromisso.setConta((Conta) value); break;
            case 2: compromisso.setConta((Conta) value); break;
            case 3: compromisso.setValor((BigDecimal) value); break;
            case 4: compromisso.setTipoPgto((TipoPgtoFinanceiro) value); break;
            case 5: compromisso.setDtPgto((Date) value); break;
            case 6: compromisso.setObservacao((String) value); break;
            default: return;
        }
        try {
            getDao().updateRow(compromisso);
            fireTableCellUpdated(row, column);
        } catch (Exception e) {
            errorLog("Falha na atualiza\u00E7\u00E3o", e);
        }
    }

    @Override
    public Class getColumnClass(int column) {
        Class c;
        switch (column) {
            case 0: c = Date.class; break;
            case 1: c = String.class; break;
            case 2: c = String.class; break;
            case 3: c = BigDecimal.class; break;
            case 4: c = String.class; break;
            case 5: c = Date.class; break;
            case 6: c = String.class; break;
            default: c = String.class; break;
        }
        return c;
    }

    @Override
    public Object getValueAt(int row, int col) {
        List list = getItemList();
        Compromisso compromisso = (Compromisso) list.get(row);
        Object obj;
        switch (col) {
            case 0: obj = compromisso.getDtVencimento(); break;
            case 1: obj = compromisso.getConta().getNome(); break;
            case 2:
                obj = compromisso.getConta().getContaMaster() == null ? "" : compromisso.getConta().getContaMaster().getNome();
                break;
            case 3: obj = compromisso.getValor(); break;
            case 4: obj = compromisso.getTipoPgtoFinanceiro().getNome(); break;
            case 5: obj = compromisso.getDtPgto(); break;
            case 6: obj = compromisso.getObservacao(); break;
            default: obj = null;
        }

        return obj;
    }

    @Override
    public void select(Object obj) throws Exception {
        setItemList(getDao().findByExample(obj));
        updateTotals();
        fireTableDataChanged();
    }

    public void updateTotals() {
        totalLancamentos = new BigDecimal(0);
        List<Compromisso> lista = (List<Compromisso>)getItemList();
        for (Compromisso apagar : lista) {
            totalLancamentos = totalLancamentos.add(apagar.getValor());
        }
    }
}
