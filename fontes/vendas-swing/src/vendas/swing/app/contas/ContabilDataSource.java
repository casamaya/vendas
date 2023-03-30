/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vendas.beans.ContabilBean;

/**
 *
 * @author jaimeoliveira
 */
public class ContabilDataSource implements JRDataSource {
    
    private Iterator itrPedidos;
    private Object valorAtual;
    private boolean hasNext = true;
    
    public ContabilDataSource(List lista) {
        super();
        this.itrPedidos = lista.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        valorAtual = itrPedidos.hasNext() ? itrPedidos.next() : null;
        hasNext = (valorAtual != null);
        return hasNext;
    }
    
        @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        ContabilBean pedido = (ContabilBean) valorAtual;
        String nome = campo.getName();

        if ("model1".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getModel1());
        } else if ("model2".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getModel2());
        } if ("model3".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getModel3());
        } if ("model4".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getModel4());
        }
        return valor;
    }
}
