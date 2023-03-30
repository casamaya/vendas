/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import vendas.entity.Colaborador;
import vendas.entity.FuncaoColaborador;

/**
 *
 * @author Sam
 */
public class ColaboradorFilter extends FilterBean {

    private Colaborador colaborador;

    public ColaboradorFilter() {
        colaborador = new Colaborador();
        colaborador.setFuncaoColaborador(new FuncaoColaborador());
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }
    
}
