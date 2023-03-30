/*
 * FichaClienteDataSource.java
 *
 * Created on 15/07/2007, 10:47:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.app.cliente;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.dao.PedidoDao;
import vendas.entity.Cliente;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class FichaClienteDataSource implements JRDataSource {
    
    private Iterator itrClientes;
    private Object valorAtual;
    private boolean hasNext = true;
    PedidoDao pedidoDao = (PedidoDao)TApplication.getInstance().lookupService("pedidoDao");
    
    public FichaClienteDataSource(List lista) {
        super();
        this.itrClientes = lista.iterator();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
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
    @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        Cliente cliente = (Cliente) valorAtual;
        String nome = campo.getName();
        
        if ("razao".equals(nome)) {
            valor = cliente.getRazao();
        } else if ("segmentos".equals(nome)) {
            valor = new JRBeanCollectionDataSource(cliente.getSegmentos());
        } else if ("compradores".equals(nome)) {
            valor = new JRBeanCollectionDataSource(cliente.getCompradores());
        } else if ("nomeCompradores".equals(nome)) {
            valor = cliente.getNomeCompradores();
        } else if ("bairro".equals(nome)) {
            valor = cliente.getBairro() == null ? "": cliente.getBairro();
        } else if ("cep".equals(nome)) {
            valor = cliente.getCep() == null ? "" : StringUtils.formatarCep(cliente.getCep());
        } else if ("cidade".equals(nome)) {
            valor = cliente.getCidade();
        } else if ("insc".equals(nome)) {
            valor = cliente.getCleanInscrEstadual();
        } else if ("cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(cliente.getCnpj());
        } else if ("cobranca".equals(nome)) {
            valor = cliente.getCobranca();
        } else if ("entrega".equals(nome)) {
            valor = cliente.getEntrega();
        } else if ("dtInclusao".equals(nome)) {
            valor = cliente.getDtInclusao();
        } else if ("email".equals(nome)) {
            valor = cliente.getEmail();
        } else if ("endereco".equals(nome)) {
            valor = cliente.getEndereco();
        } else if ("enderecoExtenso".equals(nome)) {
            valor = cliente.getEnderecoExtenso();
        } else if ("site".equals(nome)) {
            valor = cliente.getSite();
        } else if ("fantasia".equals(nome)) {
            valor = cliente.getFantasia();    
        } else if ("vendedoresExtenso".equals(nome)) {
            valor = cliente.getVendedoresExtenso();            
        } else if ("fone1".equals(nome)) {
            valor = cliente.getFone1();
        } else if ("fone2".equals(nome)) {
            valor = cliente.getFone2();
        } else if ("fone3".equals(nome)) {
            valor = cliente.getFone3();
        } else if ("fones".equals(nome)) {
            valor = cliente.getFones();
        } else if ("inscrEstadual".equals(nome)) {
            valor = cliente.getCleanInscrEstadual();
        } else if ("situacaoCliente".equals(nome)) {
            valor = cliente.getSituacaoCliente().getNome();
        } else if ("uf".equals(nome)) {
            valor = cliente.getUf();
        } else if ("ultVisitaCompra".equals(nome)) {
            StringBuilder sb = new StringBuilder();
            if (cliente.getUltimaVisita() != null) {
                sb.append(DateUtils.format(cliente.getUltimaVisita().getDtVisita()));
                sb.append("\n");
            } else {
                sb.append("-\n");
            }
            Date d = pedidoDao.findUltPedidoCliente(cliente.getIdCliente()).getDtPedido();
            if (d != null) {
                sb.append(DateUtils.format(d));
            } else {
                sb.append("-");
            }
            valor = sb.toString().trim();
            
        } else if ("ultCompra.dtPedido".equals(nome)) {
            valor = pedidoDao.findUltPedidoCliente(cliente.getIdCliente()).getDtPedido();
        } else if ("ultCompra.idPedido".equals(nome)) {
            valor = pedidoDao.findUltPedidoCliente(cliente.getIdCliente()).getIdPedido();
        } else if ("vendedor".equals(nome)) {
            valor = cliente.getListaVendedores();
        } else if ("vendedor.nome".equals(nome)) {
            valor = cliente.getListaVendedores();
        }
        return valor;
    }
    
    
}
