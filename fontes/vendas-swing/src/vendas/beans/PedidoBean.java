/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.ArrayList;
import java.util.List;
import vendas.entity.Orcamento;
import vendas.entity.Pedido;

/**
 *
 * @author sam
 */
public class PedidoBean {

    private Pedido pedido;
    private Orcamento orcamento;
    private List<Integer> itensExcluidos;

    public PedidoBean() {
        itensExcluidos = new ArrayList();
    }

    public List<Integer> getItensExcluidos() {
        return itensExcluidos;
    }

    public void setItensExcluidos(List<Integer> itensExcluidos) {
        this.itensExcluidos = itensExcluidos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }


}
