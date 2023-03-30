/*
 * FichaClienteDataSource.java
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
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.entity.Cliente;
import vendas.entity.ClienteRepres;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class ClienteRepresDataSource implements JRDataSource {
    
    private Iterator itrClientes;
    private Object valorAtual;
    private boolean hasNext = true;
    PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
    
    public ClienteRepresDataSource(List lista) {
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
        ClienteRepres clienteRepres = (ClienteRepres) valorAtual;
        Cliente cliente = clienteRepres.getCliente();
        String nome = campo.getName();
        
        if ("cliente.razao".equals(nome)) {
            valor = cliente.getRazao();
        } else if ("segmentos".equals(nome)) {
            valor = new JRBeanCollectionDataSource(cliente.getSegmentos());
        } else if ("cliente.segmentosExtenso".equals(nome)) {
            valor = cliente.getSegmentosExtenso();
        } else if ("compradores".equals(nome)) {
            valor = new JRBeanCollectionDataSource(cliente.getCompradores());
        } else if ("cliente.bairro".equals(nome)) {
            valor = cliente.getBairro();
        } else if ("cliente.cep".equals(nome)) {
            if (cliente.getCep() == null)
                valor = "";
            else
                valor = StringUtils.formatarCep(cliente.getCep());
        } else if ("cliente.cidade".equals(nome)) {
            valor = cliente.getCidade();
        } else if ("repres.razao".equals(nome)) {
            valor = clienteRepres.getRepres().getRazao();
        } else if ("limiteCredito".equals(nome)) {
            valor = clienteRepres.getLimiteCredito();
        } else if ("cliente.dtInclusao".equals(nome)) {
            valor = cliente.getDtInclusao();
        } else if ("cliente.email".equals(nome)) {
            valor = cliente.getEmailMalaDireta();
        } else if ("cliente.emailMalaDireta".equals(nome)) {
            valor = cliente.getEmail();            
        } else if ("cliente.endereco".equals(nome)) {
            valor = cliente.getEndereco();
        } else if ("cliente.enderecoExtenso".equals(nome)) {
            valor = cliente.getEnderecoExtenso();
        } else if ("cliente.site".equals(nome)) {
            valor = cliente.getSite();
        } else if ("cliente.fantasia".equals(nome)) {
            valor = cliente.getFantasia(); 
        } else if ("cliente.insc".equals(nome)) {
            valor = cliente.getCleanInscrEstadual(); 
        } else if ("cliente.inscrEstadual".equals(nome)) {
            valor = cliente.getCleanInscrEstadual(); 
        } else if ("cliente.cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(cliente.getCnpj());            
        } else if ("cliente.vendedoresExtenso".equals(nome)) {
            valor = cliente.getVendedoresExtenso();
        } else if ("cliente.fones".equals(nome)) {
            valor = cliente.getFones();
        } else if ("cliente.fonesExtenso".equals(nome)) {
            valor = cliente.getFonesExtenso();
        } else if ("cliente.nomeCompradores".equals(nome)) {
            valor = cliente.getNomeCompradores();
        } else if ("ultCompra.dtPedido".equals(nome)) {
            valor = pedidoDao.findUltPedidoClienteRepres(cliente.getIdCliente(), clienteRepres.getRepres().getIdRepres()).getDtPedido();
        } else if ("ultCompra.idPedido".equals(nome)) {
            valor = pedidoDao.findUltPedidoClienteRepres(cliente.getIdCliente(), clienteRepres.getRepres().getIdRepres()).getIdPedido();
        } else if ("codIdentificador".equals(nome)) {
            valor = clienteRepres.getCodIdentificador();
        } else if ("codRepres".equals(nome)) {
            valor = clienteRepres.getCodRepres();
        } else if ("cliente.uf".equals(nome)) {
            valor = cliente.getUf();
        } else if ("cliente.vendedor.nome".equals(nome)) {
            valor = cliente.getListaVendedores();
        }
        return valor;
    }
    
    
}
