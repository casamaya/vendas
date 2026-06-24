/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.util.Date;

/**
 *
 * @author jaime
 */
public class DataDiaAgendaModel {
    private String tipo;
    private Date dtEvento;
    private Integer qtDia;
    
    public DataDiaAgendaModel() {
        tipo = "D";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDtEvento() {
        return dtEvento;
    }

    public void setDtEvento(Date dtEvento) {
        this.dtEvento = dtEvento;
    }

    public Integer getQtDia() {
        return qtDia;
    }

    public void setQtDia(Integer qtDia) {
        this.qtDia = qtDia;
    }
    
    
}
