/*
 * FichaRepresDataSource.java
 *
 * Created on 15/07/2007, 10:47:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.app.repres;

import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vendas.entity.Repres;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class FichaRepresDataSource implements JRDataSource {
    
    private Iterator itrRepress;
    private Object valorAtual;
    private boolean hasNext = true;
    
    public FichaRepresDataSource(List lista) {
        super();
        this.itrRepress = lista.iterator();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        valorAtual = itrRepress.hasNext() ? itrRepress.next() : null;
        hasNext = (valorAtual != null);
        return hasNext;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        Repres repres = (Repres) valorAtual;
        String nome = campo.getName();
        
        if ("razao".equals(nome)) {
            valor = repres.getRazao();
        } else if ("vendedores".equals(nome)) {
            valor = new JRBeanCollectionDataSource(repres.getVendedores());
        } else if ("ativo".equals(nome)) {
            valor = repres.getAtivo();
        } else if ("bairro".equals(nome)) {
            valor = repres.getBairro();
        } else if ("cep".equals(nome)) {
            valor = StringUtils.formatarCep(repres.getCep());
        } else if ("cidade".equals(nome)) {
            valor = repres.getCidade();
        } else if ("cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(repres.getCnpj());
        } else if ("comissao".equals(nome)) {
            valor = repres.getComissao();
        } else if ("dtInclusao".equals(nome)) {
            valor = repres.getDtInclusao();
        } else if ("email".equals(nome)) {
            valor = repres.getEmail();
        } else if ("endereco".equals(nome)) {
            valor = repres.getEndereco();
        } else if ("fone1".equals(nome)) {
            valor = repres.getFone1();
        } else if ("fone2".equals(nome)) {
            valor = repres.getFone2();
        } else if ("fone3".equals(nome)) {
            valor = repres.getFone3();
        } else if ("inscrEstadual".equals(nome)) {
            valor = repres.getInscrEstadual();
        } else if ("uf".equals(nome)) {
            valor = repres.getUf();
        }
        return valor;
    }
    
    
}
