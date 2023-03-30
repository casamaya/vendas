/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.util.List;
import vendas.entity.ArquivoPedido;
import vendas.entity.AtendimentoPedido;

/**
 *
 * @author sam
 */
public class CobrancaVO {
    
    private AtendimentoPedido atend;
    private Boolean incluirAnexo;
    private List<ArquivoPedido> anexos;
    private List<ArquivoPedido> anexosSelecionados;
    

    public AtendimentoPedido getAtend() {
        return atend;
    }

    public void setAtend(AtendimentoPedido atend) {
        this.atend = atend;
    }

    public Boolean getIncluirAnexo() {
        return incluirAnexo;
    }

    public void setIncluirAnexo(Boolean incluirAnexo) {
        this.incluirAnexo = incluirAnexo;
    }

    public List<ArquivoPedido> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<ArquivoPedido> anexos) {
        this.anexos = anexos;
    }

    public List<ArquivoPedido> getAnexosSelecionados() {
        return anexosSelecionados;
    }

    public void setAnexosSelecionados(List<ArquivoPedido> anexosSelecionados) {
        this.anexosSelecionados = anexosSelecionados;
    }
    
}
