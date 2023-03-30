/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.Date;
import ritual.swing.TApplication;
import ritual.util.DateUtils;

/**
 *
 * @author sam
 */
public class AgendaFilter extends FilterBean {
    
    private String usuario;
    private Date dtInicio;
    private Date dtFim;
    
    public AgendaFilter() {
        Date dt = DateUtils.parse(DateUtils.format(new Date()));
        dtInicio = dt;
        dtFim = null;
        TApplication app = TApplication.getInstance();
        usuario = app.getUser().getUserName();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    
}
