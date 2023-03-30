/*
 * FichaPedidoDataSource.java
 *
 * Created on 15/07/2007, 10:47:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ritual.swing.TApplication;
import ritual.util.NumberUtils;
import vendas.beans.UnidadeItem;
import vendas.dao.OrcamentoDao;
import vendas.entity.Orcamento;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class OrcamentoDataSource implements JRDataSource {

    private Iterator itrPedidos;
    private Object valorAtual;
    private boolean hasNext = true;
    private String situacao = "T";

    OrcamentoDao pedidoDao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");

    public OrcamentoDataSource(List lista) {
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        Orcamento orcamento = (Orcamento) valorAtual;
        String nome = campo.getName();

        if ("cliente".equals(nome)) {
            valor = orcamento.getCliente().getRazao();
        } else if ("cliente.razao".equals(nome)) {
            valor = orcamento.getCliente().getRazao();
       // } else if ("clienteResponsavel".equals(nome)) {
       //     Cliente cliente = pedido.getClienteResponsavel();
       //     if (cliente != null)
       //         valor = cliente.getRazao();
        } else if ("cliente.endereco".equals(nome)) {
            valor = orcamento.getCliente().getEndereco();
        } else if ("cliente.fone1".equals(nome)) {
            valor = orcamento.getCliente().getFone1();
        } else if ("cliente.fone2".equals(nome)) {
            valor = orcamento.getCliente().getFone2();
        } else if ("cliente.cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(orcamento.getCliente().getCnpj());
        } else if ("cliente.cidade".equals(nome)) {
            valor = orcamento.getCliente().getCidade();
        } else if ("cliente.cep".equals(nome)) {
            valor = StringUtils.formatarCep(orcamento.getCliente().getCep());
        } else if ("cliente.uf".equals(nome)) {
            valor = orcamento.getCliente().getUf();
        } else if ("cliente.bairro".equals(nome)) {
            valor = orcamento.getCliente().getBairro();
        } else if ("entrega.endereco".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoEntrega().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("entrega.bairro".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoEntrega().getBairro();
        } else if ("cliente.email".equals(nome)) {
            valor = orcamento.getCliente().getEmail();
        } else if ("cliente.emailFNE".equals(nome)) {
            valor = orcamento.getCliente().getEmailFNE();
        } else if ("entrega.cidade".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoEntrega().getCidade();
        } else if ("entrega.cep".equals(nome)) {
            if (orcamento.getCliente().getEnderecoEntrega() != null)
            try {
            valor = StringUtils.formatarCep(orcamento.getCliente().getEnderecoEntrega().getCep());
                } catch (Exception e) {
                    valor = orcamento.getCliente().getEnderecoEntrega().getCep();
                }
            else
                valor = "";
        } else if ("entrega.uf".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoEntrega().getUf();
        } else if ("cobranca.endereco".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoCobranca().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("cobranca.bairro".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoCobranca().getBairro();
        } else if ("cobranca.cidade".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoCobranca().getCidade();
        } else if ("cobranca.cep".equals(nome)) {
            if (orcamento.getCliente().getEnderecoCobranca() != null)
                if ((orcamento.getCliente().getEnderecoCobranca().getCep() != null) && (orcamento.getCliente().getEnderecoCobranca().getCep().length() > 0))
                valor = StringUtils.formatarCep(orcamento.getCliente().getEnderecoCobranca().getCep());
        } else if ("cobranca.uf".equals(nome)) {
            valor = orcamento.getCliente().getEnderecoCobranca().getUf();
        } else if ("cliente.nomeCompradores".equals(nome)) {
            valor = orcamento.getCliente().getNomeCompradores();
        } else if ("cliente.inscrEstadual".equals(nome)) {
            valor = orcamento.getCliente().getInscrEstadual();
        } else if ("unidades".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.findUnidadesByOrcamento(orcamento.getIdorcamento()));
        } else if ("unidadesExtenso".equals(nome)) {
                        StringBuilder unidades = new StringBuilder();
            Collection<UnidadeItem> unidadesItens = pedidoDao.findUnidadesByOrcamento(orcamento.getIdorcamento());
            for (UnidadeItem und : unidadesItens) {
                unidades.append(und.getUnidade());
                unidades.append(" ");
                unidades.append(NumberUtils.format(und.getValor()));
                unidades.append(" ");
            }
            valor = unidades.toString();
        } else if ("vlrUnidades".equals(nome)) {
            valor = orcamento.getVlrUnidades();
        } else if ("financeiroRuim".equals(nome)) {
            valor = "FINANCEIRO RUIM".equals(orcamento.getCliente().getSituacaoCliente().getNome());
        } else if ("itens".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.findItensOrcamento(orcamento.getIdorcamento()));
        } else if ("formaPgto.nome".equals(nome)) {
            valor = orcamento.getFormaPgto().getNome();
        } else if ("idPedido".equals(nome)) {
            valor = orcamento.getIdorcamento();
        } else if ("dtPedido".equals(nome)) {
            valor = orcamento.getDtorcamento();
        } else if ("dtEntrega".equals(nome)) {
            valor = orcamento.getDtEntrega();
        } else if ("dtValidade".equals(nome)) {
            valor = orcamento.getDtvalidade();
        } else if ("valor".equals(nome)) {
            valor = orcamento.getValor();
        } else if ("obs".equals(nome)) {
            valor = orcamento.getObsOrcamento();
        } else if ("vendedor".equals(nome)) {
            valor = orcamento.getVendedor().getNome();
        } else if ("vendedor.nome".equals(nome)) {
            valor = orcamento.getVendedor().getNome();
        } else if ("vendedor.fone1".equals(nome)) {
            valor = orcamento.getVendedor().getFone1();
        } else if ("situacao".equals(nome)) {
            valor = orcamento.getSituacao();
        } else if ("empilhadeira".equals(nome)) {
            valor = orcamento.getCliente().isEmpilhadeira();
        } else if ("repres".equals(nome)) {
            valor = orcamento.getRepres().getRazao();
        } else if ("repres.razao".equals(nome)) {
            valor = orcamento.getRepres().getRazao();
        } else if ("repres.cidade".equals(nome)) {
            valor = orcamento.getRepres().getCidade();
        } else if ("repres.uf".equals(nome)) {
            valor = orcamento.getRepres().getUf();
        }
        return valor;
    }
}
