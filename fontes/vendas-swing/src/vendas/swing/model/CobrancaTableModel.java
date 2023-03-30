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
import vendas.beans.CobrancaFilter;
import vendas.dao.PedidoDao;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.PgtoCliente;

/**
 *
 * @author Sam
 */
public class CobrancaTableModel extends ServiceTableModel {
    
    public static final int VENDEDOR = 0;
    public static final int CLIENTE = 1;
    public static final int RESPONSAVEL = 2;
    public static final int FORNECEDOR = 3;
    public static final int PEDIDO = 4;
    //public static final int DTPEDIDO = 5;
    //public static final int VALORPEDIDO = 6;
    public static final int ATENDIMENTO = 6;
    public static final int SITUACAO = 5;
    public static final int NOTA = 7;
    public static final int DTNOTA = 8;
    public static final int VALORNOTA = 9;
    public static final int VALORDUPLICATA = 10;
    public static final int DTVENCIMENTO = 11;
    public static final int TIPO = 12;
    public static final int OPERADOR = 13;
    public static final int OBS = 14;
    public static final int VALORPAGO = 15;
    public static final int DTPGTO = 16;
    public static final int DTDESCONTO = 17;
    public static final int DIAS = 18;
    public static final int JUROS = 19;
    public static final int LIQUIDO = 20;
    
    public CobrancaTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Vendedor");
        addColumn("Cliente");
        addColumn("Responsavel");
        addColumn("Fornecedor");
        addColumn("Pedido");
        //addColumn("Dt. pedido");
        //addColumn("Valor pedido");
        addColumn("Sit.");
        addColumn("At.");
        addColumn("Nota");
        addColumn("Dt. nota");
        addColumn("Valor nota");
        addColumn("Valor dup.");
        addColumn("Vencimento");
        addColumn("Tp");
        addColumn("Op.");
        addColumn("Observa\u00E7\u00E3o");
        addColumn("Valor pagto");
        addColumn("Dt. pagto");
        addColumn("Dt. desconto");
        addColumn("Dias");
        addColumn("Juros");
        addColumn("Líquido");
    }   
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case PEDIDO : aClass = Integer.class; break;
            //case DTPEDIDO : aClass = Date.class; break;
            //case VALORPEDIDO : aClass = BigDecimal.class; break;
            case DTNOTA : aClass = Date.class; break;
            case VALORNOTA : aClass = BigDecimal.class; break;
            case VALORDUPLICATA : aClass = BigDecimal.class; break;
            case DTVENCIMENTO : aClass = Date.class; break;
            case VALORPAGO : aClass = BigDecimal.class; break;
            case DTPGTO : aClass = Date.class; break;
            case DTDESCONTO : aClass = Date.class; break;
            case DIAS : aClass = Integer.class; break;
            case JUROS : aClass = BigDecimal.class; break;
            case LIQUIDO : aClass = BigDecimal.class; break;
            default: aClass = String.class;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        PgtoCliente pgto = (PgtoCliente)getObject(row);
        Object obj;
        switch (col) {
        case VENDEDOR : obj = pgto.getAtendimentoPedido().getPedido().getVendedor().getNome(); break;
        case CLIENTE : obj = pgto.getAtendimentoPedido().getPedido().getCliente().getRazao(); break;
        case RESPONSAVEL:
                obj = pgto.getAtendimentoPedido().getPedido().getClienteResponsavel() != null ? pgto.getAtendimentoPedido().getPedido().getClienteResponsavel().getRazao() : null;
                break;
        case FORNECEDOR : obj = pgto.getAtendimentoPedido().getPedido().getRepres().getRazao(); break;
        case PEDIDO : obj = pgto.getAtendimentoPedido().getPedido().getIdPedido(); break;
        //case DTPEDIDO : obj = pgto.getAtendimentoPedido().getPedido().getDtPedido(); break;
        //case VALORPEDIDO : obj = pgto.getAtendimentoPedido().getPedido().getValor(); break;
        case SITUACAO: obj = pgto.getAtendimentoPedido().getPedido().getSituacao(); break;
        case ATENDIMENTO: obj = pgto.getAtendimentoPedido().getPedido().getAtendimento(); break;
        case NOTA : obj = pgto.getAtendimentoPedido().getAtendimentoPedidoPK().getNf(); break;
        case DTNOTA : obj = pgto.getAtendimentoPedido().getDtNota(); break;
        case VALORNOTA : obj = pgto.getAtendimentoPedido().getValor(); break;
        case VALORDUPLICATA : obj = pgto.getValor(); break;
        case DTVENCIMENTO : obj = pgto.getDtVencimento(); break;
        case TIPO : obj = pgto.getTipoPgto(); break;
        case OPERADOR : obj = pgto.getAtendimentoPedido().getOperador(); break;
        case OBS: if (pgto.getObservacao() == null)
                    obj = "";
                else if (pgto.getObservacao().length() >= 30)
                    obj = pgto.getObservacao().substring(0, 30).toLowerCase() + "...";
                else
                    obj = pgto.getObservacao().toLowerCase();
             break;
        case VALORPAGO : obj = pgto.getValorPgto(); break;
        case DTPGTO : obj = pgto.getDtPgto(); break;
        case DTDESCONTO : obj = pgto.getAtendimentoPedido().getDtDesconto(); break;
        case DIAS : obj = pgto.getDias(); break;
        case JUROS : obj = pgto.getJuros(); break;
        case LIQUIDO : obj = pgto.getLiquido(); break;
        default : obj = null;
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
        fireTableDataChanged();
    }


}
