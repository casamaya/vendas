/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import vendas.beans.*;
import java.util.Date;
import vendas.entity.Vendedor;

/**
 *
 * @author Sam
 */
public class VisitaFilter extends FilterBean {
    
    private Vendedor vendedor;
    private Date dtInicio;
    private Date dtFim;
    private Boolean clientes;
    private Boolean resumo;

    public VisitaFilter() {
        clientes = new Boolean(false);
        resumo = new Boolean(false);
    }

    public Boolean getClientes() {
        return clientes;
    }

    public void setClientes(Boolean clientes) {
        this.clientes = clientes;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Boolean getResumo() {
        return resumo;
    }

    public void setResumo(Boolean resumo) {
        this.resumo = resumo;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    
}
