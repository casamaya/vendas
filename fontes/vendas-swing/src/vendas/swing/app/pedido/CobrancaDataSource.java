/*
 * FichaPedidoDataSource.java
 *
 * Created on 15/07/2007, 10:47:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.entity.AtendimentoPedido;

/**
 *
 * @author Sam
 */
public class CobrancaDataSource implements JRDataSource {

    private Iterator itrPedidos;
    private Object valorAtual;
    private boolean hasNext = true;

    PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");

    public CobrancaDataSource(List lista) {
        super();
        this.itrPedidos = lista.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        valorAtual = itrPedidos.hasNext() ? itrPedidos.next() : null;
        hasNext = (valorAtual != null);
        return hasNext;
    }


    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        AtendimentoPedido atend = (AtendimentoPedido) valorAtual;
        String nome = campo.getName();

        if ("pedido.cliente.razao".equals(nome)) {
            valor = atend.getPedido().getCliente().getRazao();
        } else if ("pedido.dtPedido".equals(nome)) {
            valor = atend.getPedido().getDtPedido();
        } else if ("dtNota".equals(nome)) {
            valor = atend.getDtNota();
        } else if ("operador".equals(nome)) {
            valor = atend.getOperador();
        } else if ("fornecedor".equals(nome)) {
            valor = atend.getFornecedor();
        } else if ("dtDesconto".equals(nome)) {
            valor = atend.getDtDesconto();
        } else if ("pgtos".equals(nome)) {
            valor = new JRBeanCollectionDataSource(atend.getPgtos());
        } else if ("pgtos2".equals(nome)) {
            valor = new JRBeanCollectionDataSource(atend.getPgtos());
        } else if ("itens".equals(nome)) {
            valor = new JRBeanCollectionDataSource(atend.getItens());
        } else if ("pedido.itens".equals(nome)) {
            valor = new JRBeanCollectionDataSource(atend.getPedido().getItens());
        } else if ("pedido.idPedido".equals(nome)) {
            valor = atend.getPedido().getIdPedido();
        } else if ("pedido.idPedidoRepres".equals(nome)) {
            valor = atend.getPedido().getIdPedidoRepres();
        } else if ("pedido.valorCliente".equals(nome)) {
            valor = atend.getPedido().getValorCliente();
        } else if ("valorDesconto".equals(nome)) {
            valor = atend.getValorDesconto();
        } else if ("valorFrete".equals(nome)) {
            valor = atend.getValorFrete();
        } else if ("subsTrib".equals(nome)) {
            valor = atend.getSubsTrib();
        } else if ("valorTotal".equals(nome)) {
            valor = atend.getValorTotal();
        } else if ("valorSubTotal".equals(nome)) {
            valor = atend.getValorSubTotal();
        } else if ("pedido.vendedor.nome".equals(nome)) {
            valor = atend.getPedido().getVendedor().getNome();
        } else if ("pedido.transportador.nome".equals(nome)) {
            valor = atend.getPedido().getTransportador().getNome();
        } else if ("pedido.transportador.fone1".equals(nome)) {
            valor = atend.getPedido().getTransportador().getFone1();
        } else if ("pedido.transportador.fone2".equals(nome)) {
            valor = atend.getPedido().getTransportador().getFone2();
        } else if ("motorista".equals(nome)) {
            String tmp = atend.getMotorista().trim();
            if (tmp != null && tmp.isEmpty()) {
                valor = null;
            } else {
                valor = tmp;
            }
        } else if ("foneMotorista".equals(nome)) {
            valor = atend.getFoneMotorista();
        } else if ("percDesconto".equals(nome)) {
            valor = atend.getPercDesconto();
        } else if ("pedido.vendedor.fone1".equals(nome)) {
            valor = atend.getPedido().getVendedor().getFone1();
        } else if ("atendimentoPedidoPK.nf".equals(nome)) {
            valor = atend.getAtendimentoPedidoPK().getNf();
        } else if ("vendedor.nome".equals(nome)) {
            valor = atend.getPedido().getVendedor().getNome();
        } else if ("pedido.repres.razao".equals(nome)) {
            valor = atend.getPedido().getRepres().getRazao();
        } else if ("pedido.obs".equals(nome)) {
            valor = atend.getPedido().getObs();
        } else if ("pedido.obs2".equals(nome)) {
            valor = atend.getPedido().getObs2();
        } else if ("observacao".equals(nome)) {
            valor = atend.getObservacao();
        }
        return valor;
    }
}
