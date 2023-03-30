/*
 * ClienteTableModel.java
 *
 * Created on 27/06/2007, 18:31:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import ritual.jasperreports.StringUtils;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.Cliente;

/**
 *
 * @author Sam
 */
public class ClienteTableModel extends ServiceTableModel {

    private PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
    
    public static final int BLOQUEADO = 0;
    public static final int RAZAO = 1;
    public static final int REDUZIDO = 2;
    public static final int CNPJ = 3;
    public static final int INSC = 4;
    public static final int PGTOPENDENTE = 5;
    public static final int VALORPENDENTE = 6;
    public static final int LIMITE = 7;
    public static final int NUMDIASATRASO = 8;
    public static final int VENDEDOR = 9;
    public static final int FONE1 = 10;
    public static final int FONE2 =11;
    public static final int FONE3 = 12;
    public static final int CIDADE = 13;
    public static final int BAIRRO = 14;
    public static final int UF = 15;
    public static final int SITUACAO = 16;
    public static final int BOLETO = 17;
    public static final int ULTVISITA = 18;
    
    public ClienteTableModel() throws Exception {
        super();
    }
    
    @Override
    public void setColumns() {
        addColumn("Bloqueado");
        addColumn("Razão social");
        addColumn("Nome fantasia");
        addColumn("CNPJ");
        addColumn("I.E.");
        addColumn("Pgto. pendente");
        addColumn("Pedido pendente");
        addColumn("Limite");
        addColumn("Dias atraso");
        addColumn("Vendedor");
        addColumn("Fone 1");
        addColumn("Fone 2");
        addColumn("Fone 3");
        addColumn("Cidade");
        addColumn("Bairro");
        addColumn("UF");
        addColumn("Situa\u00E7\u00E3o");
        addColumn("Boleto em mãos");
        addColumn("Última visita");
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case BLOQUEADO: aClass = Boolean.class; break;
        case PGTOPENDENTE: aClass = BigDecimal.class; break;
        case VALORPENDENTE: aClass = BigDecimal.class; break;
        case LIMITE: aClass = BigDecimal.class; break;
        case NUMDIASATRASO: aClass = BigDecimal.class; break;
        case ULTVISITA: aClass = Date.class; break;
        default: aClass = String.class;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Cliente cliente = (Cliente)getObject(row);
        Object obj = null;
        switch (col) {
        case BLOQUEADO: obj = cliente.isBloqueado(); break;
        case RAZAO: obj = cliente.getRazao(); break;
        case REDUZIDO: obj = cliente.getFantasia(); break;
        case CNPJ: obj = StringUtils.formatarString(cliente.getCnpj(), StringUtils.CNPJ_MASK); break;
        case INSC: obj = cliente.getInscrEstadual(); break;
        case PGTOPENDENTE: {
            if (cliente.getPgtoPendente() == null) {
            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
            cliente.setPgtoPendente(dao.getPgtosClientePendende(cliente.getIdCliente()));
            }
            obj = cliente.getPgtoPendente(); break;
        }
        case VALORPENDENTE: {
            if (cliente.getValorPendente() == null) {
            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
            cliente.setValorPendente(dao.getVendasClientePendende(cliente.getIdCliente()));
            }
            obj = cliente.getValorPendente(); break;
        }
        case LIMITE: obj = cliente.getLimiteCredito(); break;
        case NUMDIASATRASO: obj = cliente.getNumDiasAtraso(); break;
        case VENDEDOR: obj = cliente.getListaVendedores(); break;
        case FONE1: obj = cliente.getFone1(); break;
        case FONE2: obj = cliente.getFone2(); break;
        case FONE3: obj = cliente.getFone3(); break;
        case CIDADE: obj = cliente.getCidade(); break;
        case BAIRRO: obj = cliente.getBairro(); break;
        case UF: obj = cliente.getUf();break;
        case SITUACAO: obj = cliente.getSituacaoCliente().getNome(); break;
        case BOLETO: obj = cliente.isEntregarBoleto() ? "Sim" : ""; break;
        case ULTVISITA: obj = cliente.getUltimaVisita() == null ? null : cliente.getUltimaVisita().getDtVisita(); break;
        }
        return obj;
    }
}
