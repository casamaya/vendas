/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vendas.dao.FluxoDao;
import vendas.dao.GrupoMovimentoDao;
import vendas.entity.GrupoMovimento;
import vendas.entity.Movimento;
import vendas.exception.DAOException;
import vendas.swing.core.ServiceTableModel;
import vendas.util.Messages;

/**
 *
 * @author sam
 */
public class MovimentoTableModel extends ServiceTableModel {
    public static final int DTMOV = 0;
    public static final int DOC = 1;
    public static final int DESCRICAO = 2;
    public static final int GRUPO = 3;
    public static final int TIPO = 4;
    public static final int VALOR = 5;
    public static final int SALDO = 6;
    public static final int OBS = 7;
    
    public MovimentoTableModel() throws Exception {
        super();
    }
    
    public MovimentoTableModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Data");
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
//        case GRUPO: aClass = Integer.class; break;
        case VALOR  : aClass = BigDecimal.class; break;
        case SALDO  : aClass = BigDecimal.class; break;
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
        case DOC: obj = movimento.getDocumento(); break;
        case DESCRICAO: obj = movimento.getDescricao(); break;
        case GRUPO: obj = movimento.getGrupoMovimento() != null ? movimento.getGrupoMovimento().getNomeGrupo() : null; break;
        case TIPO:  obj = movimento.getTipo() == 0 ? "Débito" : "Crédito"; break;
        case VALOR: obj = movimento.getValor(); break;
        case SALDO: {
                obj = movimento.getSaldo();
                if (obj == null)
                    atualizaSaldo(movimento);
                obj = movimento.getSaldo();
            } break;
        case OBS: obj = movimento.getObs(); break;
        }
        return obj;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != SALDO;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Movimento movimento = (Movimento)getObject(row);
        switch (column) {
        case DTMOV: try {
                            movimento.setDtMov((Date)aValue);
                        } catch (Exception e) {
                            getLogger().error(e);
                            Messages.errorMessage("Data inválida");
                        } break;
        case DOC: movimento.setDocumento((String)aValue); break;
        case DESCRICAO: movimento.setDescricao((String)aValue); break;
        case GRUPO: {
            GrupoMovimentoDao dao = new GrupoMovimentoDao();
            try {
                //GrupoMovimento grupoMovimento = (GrupoMovimento)dao.findById(GrupoMovimento.class, (GrupoMovimento)aValue);
                movimento.setGrupoMovimento((GrupoMovimento)aValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }; break;
        case TIPO: { String value = (String)aValue;
                        if ("Crédito".equals(value) || "Credito".equals(value))
                            movimento.setTipo(1);
                        else
                            movimento.setTipo(0);
                   } break;
        case VALOR: movimento.setValor(new BigDecimal((String)aValue)); atualizaSaldo(movimento);break;
        case OBS: movimento.setObs((String)aValue);
        }
        
        try {
            getDao().updateRow(movimento);
        } catch (DAOException ex) {
            Logger.getLogger(MovimentoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //fireTableCellUpdated(row, column);
        fireTableDataChanged();
    }
    
    private void atualizaSaldo(Movimento obj) {
        FluxoDao dao = (FluxoDao)getDao();
        BigDecimal value = dao.getSaldoMovimento(obj);
        obj.setSaldo(value);           
    }
}
