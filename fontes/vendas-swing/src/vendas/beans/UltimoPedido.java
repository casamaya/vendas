/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;

/**
 *
 * @author Sam
 */
public class UltimoPedido {
    private Integer idPedido;
    private Date dtPedido;

    public Date getDtPedido() {
        return dtPedido;
    }

    public void setDtPedido(Date dtPedido) {
        this.dtPedido = dtPedido;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

}
