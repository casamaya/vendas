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
import vendas.entity.ContaRepres;
import vendas.entity.PgtoCliente;
import vendas.swing.core.BaseTableModel;
import vendas.util.Constants;
import vendas.util.Messages;

/**
 *
 * @author Sam
 */
public class PgtoPedidoModel extends BaseTableModel {

    private PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
    public static final int NOTA = 0;
    public static final int VENCIMENTO = 1;
    public static final int VALOR = 2;
    public static final int IMPRIMIR = 3;
    public static final int TIPO = 4;
    public static final int OBS = 5;
    public static final int PREVPGTO = 6;
    public static final int DATAPGTO = 7;
    public static final int VALORPGTO = 8;
    public static final int CONTA = 9;
    public static final int DIAS = 10;
    public static final int JUROS = 11;
    public static final int LIQUIDO = 12;

    public PgtoPedidoModel() throws Exception {
        super();
    }

    public PgtoPedidoModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Nota");
        addColumn("Vencimento");
        addColumn("Valor");
        addColumn("Imprimir");
        addColumn("Tipo pgto");
        addColumn("Observa\u00E7\u00E3o");
        addColumn("Dt. prev. pgto");
        addColumn("Data pgto");
        addColumn("Valor pago");
        addColumn("Conta");
        addColumn("Dias");
        addColumn("Juros");
        addColumn("Liquido");
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result = false;
        PgtoCliente nota = (PgtoCliente) getObject(row);
//        if (nota.getDtPgto() != null)
//            return false;
        switch (column) {
            //case VENCIMENTO: result = true; break;
            //case VALOR: result = true; break;
            //case TIPO: result = true; break;
            case IMPRIMIR:
                result = true;
                break;
//        case COMPL: result = true; break;
            case PREVPGTO:
                result = true;
                break;
//        case VALORCOBRANCA: result = true; break;

            //case VALORPGTO: result = true; break;
        }
        return result;
    }

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case NOTA: aClass = String.class; break;
            case VENCIMENTO:
                aClass = Date.class;
                break;
            case VALOR:
                aClass = BigDecimal.class;
                break;
            case IMPRIMIR:
                aClass = Boolean.class;
                break;
            case TIPO:
                aClass = String.class;
                break;
            case OBS:
                aClass = String.class;
                break;
            case PREVPGTO:
                aClass = Date.class;
                break;
            case DATAPGTO:
                aClass = Date.class;
                break;
            case VALORPGTO:
                aClass = BigDecimal.class;
                break;
            case CONTA:
                aClass = String.class;
                break;
            case DIAS:
                aClass = Integer.class;
                break;
            case JUROS:
                aClass = BigDecimal.class;
                break;
            case LIQUIDO:
                aClass = BigDecimal.class;
                break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        PgtoCliente nota = (PgtoCliente) getObject(row);
        Object obj = null;
        switch (col) {
            case NOTA:
                obj = nota.getAtendimentoPedido().getAtendimentoPedidoPK().getNf();
                break;
            case VENCIMENTO:
                obj = nota.getDtVencimento();
                break;
            case VALOR:
                obj = nota.getValor();
                break;
            case IMPRIMIR:
                obj = nota.getImprimirCobranca();
                break;
            case TIPO:
                obj = nota.getTipoPgto();
                break;
            case OBS:
                if (nota.getObservacao() == null)
                    obj = "";
                else if (nota.getObservacao().length() >= 30)
                    obj = nota.getObservacao().substring(0, 30) + "...";
                else
                    obj = nota.getObservacao();
                break;
            case PREVPGTO:
                obj = nota.getDtPrevisaoPgto();
                break;
            case DATAPGTO:
                obj = nota.getDtPgto();
                break;
            case VALORPGTO:
                obj = nota.getValorPgto();
                break;
//        case VALORCOBRANCA: obj = nota.getValorCobranca(); break;
            case CONTA:
                ContaRepres conta = nota.getContaRepres();
                
                if (conta != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(conta.getTipoConta()).append(" ");
                    if (conta.getBanco() != null) {
                        sb.append(conta.getBanco()).append(" ").append(conta.getAgencia()).append(" ");
                    }
                    obj = sb.append(conta.getContaCorrente()).toString();
                }
                break;
            case DIAS:
                obj = nota.getDias();
                break;
            case JUROS:
                obj = nota.getJuros();
                break;
            case LIQUIDO:
                obj = nota.getLiquido();
                break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        PgtoCliente nota = (PgtoCliente) getObject(row);
        if (column == IMPRIMIR) {
            if (!TApplication.getInstance().getUser().isVendedor()) {
                if (!Messages.confirmQuestion("Confirma altera\u00E7\u00E3o")) {
                    return;
                }
            }
        }
        switch (column) {
            case VENCIMENTO:
                nota.setDtVencimento((Date) aValue);
                break;
            case VALOR:
                nota.setValor((BigDecimal) aValue);
                break;
            case IMPRIMIR:
                nota.setCobranca((Boolean) aValue);
                break;
            case TIPO:
                nota.setTipoPgto((String) aValue);
                break;
            case OBS:
                nota.setComplemento((String) aValue);
                break;
            case PREVPGTO:
                nota.setDtPrevisaoPgto((Date) aValue);
                break;
            case DATAPGTO:
                if (aValue == null)
                    nota.setDtPgto(null);
                else
                    nota.setDtPgto((Date) aValue);
                break;
            case VALORPGTO:
                nota.setValorPgto((BigDecimal) aValue);
                break;
//        case VALORCOBRANCA: nota.setValorCobranca(new BigDecimal(aValue.toString())); break;
        }
        try {
            pedidoDao.updateRow(nota);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
        //fireTableDataChanged();
    }
}
