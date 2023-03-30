/*
 * FichaClienteDataSource.java
 *
 * Created on 15/07/2007, 10:47:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.app.cliente;

import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vendas.entity.GrupoCliente;

/**
 *
 * @author Sam
 */
public class GrupoClienteDataSource implements JRDataSource {
    
    private Iterator itrClientes;
    private Object valorAtual;
    private boolean hasNext = true;
    
    public GrupoClienteDataSource(List lista) {
        super();
        this.itrClientes = lista.iterator();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    public boolean next() throws JRException {
        valorAtual = itrClientes.hasNext() ? itrClientes.next() : null;
        hasNext = (valorAtual != null);
        return hasNext;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        GrupoCliente cliente = (GrupoCliente) valorAtual;
        String nome = campo.getName();
        
        if ("nomeGrupo".equals(nome)) {
            valor = cliente.getNomeGrupo();
        } else if ("clientes".equals(nome)) {
            valor = new JRBeanCollectionDataSource(cliente.getClientes());
        }
        return valor;
    }
    
    
}
