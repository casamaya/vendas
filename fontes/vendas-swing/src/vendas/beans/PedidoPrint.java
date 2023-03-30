/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

/**
 *
 * @author sam
 */
public class PedidoPrint {

    private int tipo;
    private Boolean referencia;

    public PedidoPrint() {
        referencia = false;
    }

    public Boolean getReferencia() {
        return referencia;
    }

    public void setReferencia(Boolean referencia) {
        this.referencia = referencia;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    

}
