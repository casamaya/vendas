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
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.Pedido;
import vendas.util.Messages;

/**
 *
 * @author Sam
 */
public class PedidoTableModel extends ServiceTableModel {

    public static final int PREPEDIDO = 0;
    public static final int EMITIDO = 1;
    public static final int EMITIDOCLIENTE = 2;
    public static final int EMITIDOCOBRANCA = 3;
    public static final int PEDIDO = 4;
    public static final int PEDIDOREPRES = 5;
    public static final int REPRES = 6;
    public static final int CLIENTE = 7;
    public static final int RESPONSAVEL = 8;
    public static final int VENDEDOR = 9;
    public static final int EMISSAO = 10;
    public static final int VALOR = 11;
    public static final int VALOROP = 12;
    public static final int ENTREGA = 13;
    public static final int COMVEN = 14;
    public static final int COMEMPR = 15;
    public static final int COMISSAO = 16;
    public static final int SITUACAO = 17;
    public static final int ATENDIMENTO = 18;

    public PedidoTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Pré");
        addColumn("F");
        addColumn("C");
        addColumn("D");
        addColumn("Pedido");
        addColumn("Pedido Fornec.");
        addColumn("Fornecedor");
        addColumn("Cliente");
        addColumn("Responsável");
        addColumn("Vendedor");
        addColumn("Data");
        addColumn("Valor");
        addColumn("Valor OP");
        addColumn("Embarque");
        addColumn("Com. vendedor");
        addColumn("Com. empresa");
        addColumn("Com. total");
        addColumn("Sit.");
        addColumn("At.");
    }

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case PREPEDIDO:
                aClass = Boolean.class;
                break;
//            case PRECOBRANCA:
//                aClass = Boolean.class;
//                break;
            case EMITIDO:
                aClass = Boolean.class;
                break;
            case EMITIDOCLIENTE:
                aClass = Boolean.class;
                break;
            case EMITIDOCOBRANCA:
                aClass = Boolean.class;
                break;
            case PEDIDO:
                aClass = Integer.class;
                break;
            case PEDIDOREPRES:
                aClass = Integer.class;
                break;
            case REPRES:
                aClass = String.class;
                break;
            case CLIENTE:
                aClass = String.class;
                break;
            case RESPONSAVEL:
                aClass = String.class;
                break;
            case VENDEDOR:
                aClass = String.class;
                break;
            case EMISSAO:
                aClass = Date.class;
                break;
            case VALOR:
                aClass = BigDecimal.class;
                break;
            case VALOROP:
                aClass = BigDecimal.class;
                break;
            case ENTREGA:
                aClass = Date.class;
                break;
            case COMVEN:
                aClass = BigDecimal.class;
                break;
            case COMEMPR:
                aClass = BigDecimal.class;
                break;
            case COMISSAO:
                aClass = BigDecimal.class;
                break;
            case SITUACAO:
                aClass = String.class;
                break;
            case ATENDIMENTO:
                aClass = String.class;
                break;
        }
        return aClass;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result = false;
        
        //if (!(TApplication.getInstance().getUser().isAdmin() || TApplication.getInstance().getUser().isEscritorio())) {
        //    return false;
        //}

        switch (column) {
            case EMITIDO:
                result = true;
                break;
            case EMITIDOCLIENTE:
                result = true;
                break;
            case EMITIDOCOBRANCA:
                result = true;
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        switch (i1) {
            case EMITIDO:
            case EMITIDOCLIENTE:
            case EMITIDOCOBRANCA:
                if (!Messages.confirmQuestion("Confirma altera\u00E7\u00E3o da situa\u00E7\u00E3o de emitido?")) {
                    return;
                }
        }
        Pedido pedido = (Pedido) getObject(i);
        Boolean isEmitido = (Boolean) o;
        String emitido;
        if (isEmitido) {
            emitido = "1";
        } else {
            emitido = "0";
        }

        boolean atualiza = false;
        switch (i1) {
            case EMITIDO:
                pedido.setEmitido(emitido);
                atualiza = true;
                break;
            case EMITIDOCLIENTE:
                pedido.setEmitidoCliente(emitido);
                atualiza = true;
                break;
            case EMITIDOCOBRANCA:
                pedido.setEmitidoCobranca(emitido);
                atualiza = true;
                break;
        }
        if (atualiza) {
            PedidoDao dao = (PedidoDao) getDao();
            dao.setEmitido(pedido);
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        Pedido pedido = (Pedido) getObject(row);
        if (pedido == null) {
            return null;
        }
        Object obj = null;
        switch (col) {
            case PREPEDIDO:
                obj = pedido.isPrePedido();
                break;
            case EMITIDO:
                obj = pedido.isEmitido();
                break;
            case EMITIDOCLIENTE:
                obj = pedido.isEmitidoCliente();
                break;
            case EMITIDOCOBRANCA:
                obj = pedido.isEmitidoCobranca();
                break;
            case PEDIDO:
                obj = pedido.getIdPedido();
                break;
            case PEDIDOREPRES:
                obj = pedido.getIdPedidoRepres();
                break;
            case REPRES:
                obj = pedido.getRepres().getRazao();
                break;
            case CLIENTE:
                obj = pedido.getCliente().getRazao();
                break;
            case RESPONSAVEL:
                obj = pedido.getClienteResponsavel() != null ? pedido.getClienteResponsavel().getRazao() : null;
                break;
            case VENDEDOR:
                obj = pedido.getVendedor().getNome();
                break;
            case EMISSAO:
                obj = pedido.getDtPedido();
                break;
            case VALOR:
                obj = pedido.getValor();
                break;
            case VALOROP:
                obj = pedido.getValorOp();
                break;
            case ENTREGA:
                obj = pedido.getDtEntrega();
                break;
            case COMVEN:
                obj = pedido.getVlrComissaoVendedor();
                break;
            case COMEMPR:
                obj = pedido.getVlrComissaoEmpresa();
                break;
            case COMISSAO:
                obj = pedido.getVlrComissaoTotal();
                break;
            case SITUACAO:
                obj = pedido.getSituacao();
                break;
            case ATENDIMENTO:
                obj = pedido.getAtendimento();
                break;
        }
        return obj;
    }

    @Override
    public void deleteRow(int row) throws Exception {
        infoLog("deleteRow");
        try {
            Pedido pedido = (Pedido) getObject(row);
            PedidoDao dao = (PedidoDao) getDao();
            dao.deletePedido(pedido.getIdPedido());
            infoLog("atualizando model");
            removeObject(row);
            infoLog("atualizado");
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }
}
