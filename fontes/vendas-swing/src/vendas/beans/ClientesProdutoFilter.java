/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;
import vendas.entity.Cliente;
import vendas.entity.Produto;
import vendas.entity.Repres;

/**
 *
 * @author p993702
 */
public class ClientesProdutoFilter extends FilterBean {
    private Date dtPedidoIni;
    private Date dtPedidoEnd;
    private Repres repres;
    private Cliente cliente;
    private Produto produto;
    private int ordem;

    public Date getDtPedidoIni() {
        return dtPedidoIni;
    }

    public void setDtPedidoIni(Date dtPedidoIni) {
        this.dtPedidoIni = dtPedidoIni;
    }

    public Date getDtPedidoEnd() {
        return dtPedidoEnd;
    }

    public void setDtPedidoEnd(Date dtPedidoEnd) {
        this.dtPedidoEnd = dtPedidoEnd;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
    
}
