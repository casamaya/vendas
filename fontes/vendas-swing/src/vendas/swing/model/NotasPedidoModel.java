/**
 * ClienteContatoModel.java
 * 
 * Created on 30/06/2007, 12:12:18
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.entity.AtendimentoPedido;
import vendas.swing.core.BaseTableModel;
import vendas.util.Constants;

/**
 *
 * @author Sam
 */
public class NotasPedidoModel extends BaseTableModel {
    
    public static final int NOTA = 0;
    public static final int DTNOTA = 1;
    public static final int VALORNOTA = 2;
    public static final int VALORCOMISSAO = 3;
    public static final int DATAPGTO = 4;
    public static final int PERCDESCONTO = 5;
    public static final int DTDESCONTO = 6;
    public static final int DESCONTO = 7;
    public static final int OPERADOR = 8;
    
    private PedidoDao pedidoDao = (PedidoDao)TApplication.getInstance().lookupService("pedidoDao");

    public NotasPedidoModel() throws Exception {
        super();
    }
    
    public NotasPedidoModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Nota");
        addColumn("Data");
        addColumn("Valor");
        addColumn("Comissão");
        addColumn("Data pgto");
        addColumn("% desconto");
        addColumn("Data desconto");
        addColumn("Juros");
        addColumn("Operador");
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result = false;
        boolean isAcesso = TApplication.getInstance().getUser().isAdmin() || TApplication.getInstance().getUser().isEscritorio();
        switch (column) {
        //case NOTA: result = true; break;
        //case DTNOTA: result = true; break;
        //case VALORNOTA: result = true; break;
        //case VALORCOMISSAO: result = true; break;
        case PERCDESCONTO: result = isAcesso; break;
        case DTDESCONTO: result = isAcesso; break;
        //case DESCONTO: result = isAcesso; break;
        case OPERADOR: result = isAcesso; break;
        }
        return result;
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case NOTA: aClass = String.class; break;
        case DTNOTA: aClass = Date.class; break;
        case VALORNOTA: aClass = BigDecimal.class; break;
        case VALORCOMISSAO: aClass = BigDecimal.class; break;
        case DATAPGTO: aClass = Date.class; break;
        case PERCDESCONTO: aClass = BigDecimal.class; break;
        case DTDESCONTO: aClass = Date.class; break;
        case DESCONTO: aClass = BigDecimal.class; break;
        case OPERADOR: aClass = String.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        AtendimentoPedido nota = (AtendimentoPedido)getObject(row);
        Object obj = null;
        switch (col) {
        case NOTA: obj = nota.getAtendimentoPedidoPK().getNf(); break;
        case DTNOTA: obj = nota.getDtNota(); break;
        case VALORNOTA: obj = nota.getValor(); break;
        case VALORCOMISSAO: obj = nota.getValorComissao(); break;
        case DATAPGTO: obj = nota.getDtPgtoComissao();break;
        case PERCDESCONTO: obj = nota.getPercDesconto();break;
        case DTDESCONTO: obj = nota.getDtDesconto();break;
        case DESCONTO: obj = nota.getJuros();break;
        case OPERADOR: obj = nota.getOperador();break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        AtendimentoPedido nota = (AtendimentoPedido)getObject(row);
        switch (column) {
        case NOTA: nota.getAtendimentoPedidoPK().setNf((String)aValue); break;
        case DTNOTA: nota.setDtNota((Date)aValue); break;
        case VALORCOMISSAO: nota.setValorComissao((BigDecimal)aValue); break;
        case VALORNOTA: nota.setValor((BigDecimal)aValue);break;
        case PERCDESCONTO: nota.setPercDesconto((BigDecimal)aValue);break;
        case DTDESCONTO: nota.setDtDesconto((Date)aValue);break;
        //case DESCONTO: nota.setValorDesconto((BigDecimal)aValue);break;
        case OPERADOR: if (aValue == null)
                           nota.setOperador(null);
                       else {
                           String op = ((String)aValue).toUpperCase();
                           nota.setOperador(op.trim());
            }
            break;
        }
        try {
            pedidoDao.updateRow(nota);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
        //fireTableDataChanged();
    }
    
}
