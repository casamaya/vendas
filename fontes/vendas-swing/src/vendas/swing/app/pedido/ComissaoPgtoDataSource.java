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
import vendas.dao.PedidoDao;
import vendas.entity.AtendimentoPedido;
import vendas.entity.FormaPgto;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class ComissaoPgtoDataSource implements JRDataSource {

    private Iterator itrPedidos;
    private Object valorAtual;
    private boolean hasNext = true;
    private String situacao = "T";

    PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");

    public ComissaoPgtoDataSource(List lista) {
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
        AtendimentoPedido atendimento = (AtendimentoPedido) valorAtual;
        String nome = campo.getName();


        if ("nf".equals(nome)) {
            valor = atendimento.getAtendimentoPedidoPK().getNf();
        } else if("dtNota".equals(nome)) {
            valor = atendimento.getDtNota();
        } else if("dtPgtoComissao".equals(nome)) {
            valor = atendimento.getDtPgtoComissao();
        } else if("valorNota".equals(nome)) {
            valor = atendimento.getValor();
        } else if("valorComissao".equals(nome)) {
            valor = atendimento.getValorComissao();
        } else if("comissaoVendedor".equals(nome)) {
            valor = atendimento.getComissaoVendedor();
        } else if("comissaoEmpresa".equals(nome)) {
            valor = atendimento.getComissaoEmpresa();
        } else if("valorComissao".equals(nome)) {
            valor = atendimento.getValorComissao();
        } else if ("cliente".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getRazao();
        } else if ("cliente.razao".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getRazao();
       // } else if ("clienteResponsavel".equals(nome)) {
       //     Cliente cliente = pedido.getClienteResponsavel();
       //     if (cliente != null)
       //         valor = cliente.getRazao();
        } else if ("cliente.endereco".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEndereco();
        } else if ("atendimento".equals(nome)) {
            valor = atendimento.getPedido().getAtendimento();
        } else if ("cliente.fone1".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getFone1();
        } else if ("cliente.fone2".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getFone2();
        } else if ("cliente.cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(atendimento.getPedido().getCliente().getCnpj());
        } else if ("cliente.cidade".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getCidade();
        } else if ("cliente.cep".equals(nome)) {
            valor = StringUtils.formatarCep(atendimento.getPedido().getCliente().getCep());
        } else if ("cliente.uf".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getUf();
        } else if ("cliente.bairro".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getBairro();
        } else if ("entrega.endereco".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoEntrega().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("entrega.bairro".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoEntrega().getBairro();
        } else if ("cliente.email".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEmail();
        } else if ("entrega.cidade".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoEntrega().getCidade();
        } else if ("entrega.cep".equals(nome)) {
            valor = StringUtils.formatarCep(atendimento.getPedido().getCliente().getEnderecoEntrega().getCep());
        } else if ("entrega.uf".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoEntrega().getUf();
        } else if ("cobranca.endereco".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoCobranca().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("cobranca.bairro".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoCobranca().getBairro();
        } else if ("cobranca.cidade".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoCobranca().getCidade();
        } else if ("cobranca.cep".equals(nome)) {
            valor = StringUtils.formatarCep(atendimento.getPedido().getCliente().getEnderecoCobranca().getCep());
        } else if ("cobranca.uf".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getEnderecoCobranca().getUf();
        } else if ("cliente.nomeCompradores".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getNomeCompradores();
        } else if ("cliente.inscrEstadual".equals(nome)) {
            valor = atendimento.getPedido().getCliente().getInscrEstadual();
        } else if ("notaAdicional".equals(nome)) {
            valor = atendimento.getNotaAdicional();
        } else if ("resumoUnidades".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.getUnidadesAtendidas(atendimento.getPedido().getIdPedido(), atendimento.getAtendimentoPedidoPK().getNf()));
        } else if ("unidades".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.findUnidadesByPedido(atendimento.getPedido().getIdPedido()));
        } else if ("unidadesAtendidas".equals(nome)) {
            valor = atendimento.getUnidadesAtendidas();
        } else if ("unidadeAtendimento".equals(nome)) {
            valor = atendimento.getUnidadeAtendimento();
        } else if ("unidadesExtenso".equals(nome)) {
                        StringBuilder unidades = new StringBuilder();
            Collection<UnidadeItem> unidadesItens = pedidoDao.findUnidadesByPedido(atendimento.getPedido().getIdPedido());
            for (UnidadeItem und : unidadesItens) {
                unidades.append(und.getUnidade());
                unidades.append(" ");
                unidades.append(NumberUtils.format(und.getValor()));
                unidades.append(" ");
            }
            valor = unidades.toString();
        } else if ("vlrUnidades".equals(nome)) {
            valor = atendimento.getPedido().getVlrUnidades();
        } else if ("itens".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.findItensPedido(atendimento.getPedido().getIdPedido()));
        } else if ("idPedido".equals(nome)) {
            valor = atendimento.getPedido().getIdPedido();
        } else if ("comissao".equals(nome)) {
            valor = atendimento.getPedido().getComissao();
        } else if ("comissaoVendedor".equals(nome)) {
            valor = atendimento.getComissaoVendedor();
        } else if ("dtPedido".equals(nome)) {
            valor = atendimento.getPedido().getDtPedido();
        } else if ("valor".equals(nome)) {
            valor = atendimento.getValor();
        } else if ("dtEntrega".equals(nome)) {
            valor = atendimento.getPedido().getDtEntrega();
        } else if ("obs".equals(nome)) {
            valor = atendimento.getPedido().getObs();
        } else if ("obs2".equals(nome)) {
            valor = atendimento.getPedido().getObs2();
        } else if ("vendedor".equals(nome)) {
            valor = atendimento.getPedido().getVendedor().getNome();
        } else if ("vendedor.nome".equals(nome)) {
            valor = atendimento.getPedido().getVendedor().getNome();
        } else if ("vendedor.fone1".equals(nome)) {
            valor = atendimento.getPedido().getVendedor().getFone1();
        } else if ("situacao".equals(nome)) {
            valor = atendimento.getPedido().getSituacao();
        } else if ("vlrAtendido".equals(nome)) {
            valor = atendimento.getPedido().getVlrAtendido();
        } else if ("vlrComissaoTotal".equals(nome)) {
            valor = atendimento.getPedido().getVlrComissaoTotal();
        } else if ("vlrComVendedorAtendida".equals(nome)) {
            valor = atendimento.getPedido().getVlrComVendedorAtendida();
        } else if ("vlrComEmpresaAtendida".equals(nome)) {
            valor = atendimento.getPedido().getVlrComEmpresaAtendida();
        } else if ("vlrComissaoVendedor".equals(nome)) {
            valor = atendimento.getPedido().getVlrComissaoVendedor();
        } else if ("vlrComissaoEmpresa".equals(nome)) {
            valor = atendimento.getPedido().getVlrComissaoEmpresa();
        } else if ("valorPendente".equals(nome)) {
            valor = atendimento.getPedido().getValorPendente();
        } else if ("valorSaldo".equals(nome)) {
            valor = atendimento.getPedido().getVlrSaldoComissao();
        } else if ("vlrComissaoAtendida".equals(nome)) {
            valor = atendimento.getPedido().getVlrComissaoAtendida();
        } else if ("empilhadeira".equals(nome)) {
            valor = atendimento.getPedido().getCliente().isEmpilhadeira();
        } else if ("vlrComissaoReal".equals(nome)) {
            if (("A".equals(atendimento.getPedido().getSituacao())) || ("P".equals(atendimento.getPedido().getAtendimento()))) {
                valor = atendimento.getPedido().getVlrComissaoAtendida();
            } else {
                valor = atendimento.getPedido().getVlrComissaoTotal();
            }
        } else if ("vlrVendedorReal".equals(nome)) {
            if (("A".equals(atendimento.getPedido().getSituacao())) || ("P".equals(atendimento.getPedido().getAtendimento()))) {
//                valor = pedido.getVlrComissaoAtendida().divide(new BigDecimal(100)).multiply(pedido.getComissaoVendedor());
                valor = atendimento.getPedido().getVlrComVendedorAtendida();
            } else {
                valor = atendimento.getPedido().getVlrComissaoVendedor();
            }
        } else if ("vlrEmpresaReal".equals(nome)) {
            if (("A".equals(atendimento.getPedido().getSituacao())) || ("P".equals(atendimento.getPedido().getAtendimento()))) {
                //valor = pedido.getVlrComissaoAtendida().subtract(pedido.getVlrComissaoAtendida().divide(new BigDecimal(100)).multiply(pedido.getComissaoVendedor()));
                valor = atendimento.getPedido().getVlrComEmpresaAtendida();
            } else {
                valor = atendimento.getPedido().getVlrComissaoEmpresa();
            }
        } else if ("repres".equals(nome)) {
            valor = atendimento.getPedido().getRepres().getRazao();
        } else if ("repres.razao".equals(nome)) {
            valor = atendimento.getPedido().getRepres().getRazao();
        } else if ("repres.cidade".equals(nome)) {
            valor = atendimento.getPedido().getRepres().getCidade();
        } else if ("repres.uf".equals(nome)) {
            valor = atendimento.getPedido().getRepres().getUf();
        } else if ("formaPgto.nome".equals(nome)) {
            valor = atendimento.getPedido().getFormaPgto().getNome();
        } else if ("tipoPgto.nome".equals(nome)) {
            valor = atendimento.getPedido().getTipoPgto().getNome();
        } else if ("transportador.nome".equals(nome)) {
            valor = atendimento.getPedido().getTransportador().getNome();
        } else if ("transportador.fone1".equals(nome)) {
            valor = atendimento.getPedido().getTransportador().getFone1();
        } else if ("transportador.codTrans".equals(nome)) {
            valor = atendimento.getPedido().getTransportador().getCodTrans();
        } else if ("formaPgtoTransp.nome".equals(nome)) {
            FormaPgto pgtoTransp = atendimento.getPedido().getFormaPgtoTransp();
            if (pgtoTransp != null) {
                valor = pgtoTransp.getNome();
            }
        }
        return valor;
    }
}
