/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import vendas.entity.Vendedor;

/**
 *
 * @author sam
 */
public class VendedorFilter extends FilterBean {
    private Vendedor vendedor;

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
}
