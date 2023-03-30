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
import vendas.entity.FormaPgto;
import vendas.entity.Pedido;
import vendas.util.StringUtils;

/**
 *
 * @author Sam
 */
public class PedidoDataSource implements JRDataSource {

    private Iterator itrPedidos;
    private Object valorAtual;
    private boolean hasNext = true;
    private String situacao = "T";

    PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");

    public PedidoDataSource(List lista) {
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
        Pedido pedido = (Pedido) valorAtual;
        String nome = campo.getName();

        if ("cliente".equals(nome)) {
            valor = pedido.getCliente().getRazao();
        } else if ("cliente.razao".equals(nome)) {
            valor = pedido.getCliente().getRazao();
       // } else if ("clienteResponsavel".equals(nome)) {
       //     Cliente cliente = pedido.getClienteResponsavel();
       //     if (cliente != null)
       //         valor = cliente.getRazao();
        } else if ("cliente.referenciasCliente".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getCliente().getReferenciasCliente());
        } else if ("cliente.referenciasBanco".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getCliente().getReferenciasBanco());
        } else if ("cliente.endereco".equals(nome)) {
            valor = pedido.getCliente().getEndereco();
        } else if ("cliente.site".equals(nome)) {
            valor = pedido.getCliente().getSite();
        } else if ("cliente.fantasia".equals(nome)) {
            valor = pedido.getCliente().getFantasia();
        } else if ("cliente.enderecoExtenso".equals(nome)) {
            valor = pedido.getCliente().getEnderecoExtenso();            
        } else if ("cliente.fone1".equals(nome)) {
            valor = pedido.getCliente().getFone1();
        } else if ("cliente.fone2".equals(nome)) {
            valor = pedido.getCliente().getFone2();
        } else if ("cliente.cnpj".equals(nome)) {
            valor = StringUtils.formatarCnpj(pedido.getCliente().getCnpj());
        } else if ("atraso".equals(nome)) {
            valor = pedido.getAtraso();
        } else if ("cliente.cidade".equals(nome)) {
            valor = pedido.getCliente().getCidade();
        } else if ("cliente.cep".equals(nome)) {
            valor = StringUtils.formatarCep(pedido.getCliente().getCep());
        } else if ("cliente.uf".equals(nome)) {
            valor = pedido.getCliente().getUf();
        } else if ("cliente.bairro".equals(nome)) {
            valor = pedido.getCliente().getBairro();
        } else if ("entrega.endereco".equals(nome)) {
            valor = pedido.getCliente().getEnderecoEntrega().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("entrega.bairro".equals(nome)) {
            valor = pedido.getCliente().getEnderecoEntrega().getBairro();
        } else if ("cliente.email".equals(nome)) {
            valor = pedido.getCliente().getEmail();
        } else if ("cliente.emailFNE".equals(nome)) {
            valor = pedido.getCliente().getEmailFNE();
        } else if ("entrega.cidade".equals(nome)) {
            valor = pedido.getCliente().getEnderecoEntrega().getCidade();
        } else if ("entrega.cep".equals(nome)) {
            if (pedido.getCliente().getEnderecoEntrega() != null)
            try {
            valor = StringUtils.formatarCep(pedido.getCliente().getEnderecoEntrega().getCep());
                } catch (Exception e) {
                    valor = pedido.getCliente().getEnderecoEntrega().getCep();
                }
            else
                valor = "";
        } else if ("entrega.uf".equals(nome)) {
            valor = pedido.getCliente().getEnderecoEntrega().getUf();
        } else if ("cobranca.endereco".equals(nome)) {
            valor = pedido.getCliente().getEnderecoCobranca().getEndereco();
            if (valor == null)
                valor = "O MESMO";
        } else if ("cobranca.bairro".equals(nome)) {
            valor = pedido.getCliente().getEnderecoCobranca().getBairro();
        } else if ("cobranca.cidade".equals(nome)) {
            valor = pedido.getCliente().getEnderecoCobranca().getCidade();
        } else if ("cobranca.cep".equals(nome)) {
            if (pedido.getCliente().getEnderecoCobranca() != null)
                if ((pedido.getCliente().getEnderecoCobranca().getCep() != null) && (pedido.getCliente().getEnderecoCobranca().getCep().length() > 0))
                valor = StringUtils.formatarCep(pedido.getCliente().getEnderecoCobranca().getCep());
        } else if ("cobranca.uf".equals(nome)) {
            valor = pedido.getCliente().getEnderecoCobranca().getUf();
        } else if ("cliente.nomeCompradores".equals(nome)) {
            valor = pedido.getCliente().getNomeCompradores();
        } else if ("obsAtendimento".equals(nome)) {
            valor = pedido.getObsAtendimento();
        } else if ("cliente.inscrEstadual".equals(nome)) {
            valor = pedido.getCliente().getInscrEstadual();
        } else if ("pagamentos".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedido.getPagamentos());
        } else if ("atendimentos".equals(nome)) {
            //if ("S".equals(situacao))
           //     valor = new JRBeanCollectionDataSource(pedido.getAtendimentosSaldo());
            //else
                valor = new JRBeanCollectionDataSource(pedido.getAtendimentos());
        } else if ("unidades".equals(nome)) {
            if ( ("A".equals(pedido.getAtendimento()) && "A".equals(pedido.getSituacao())) || ("N".equals(pedido.getAtendimento()) && "N".equals(pedido.getSituacao())) ){
                valor = new JRBeanCollectionDataSource(pedidoDao.findUnidadesByPedido(pedido.getIdPedido()));
            } else {
                List<UnidadeItem> unds = pedidoDao.findUnidadesByPedido(pedido.getIdPedido());
                List<UnidadeItem> saldo = pedidoDao.findUndSaldoByPedido(pedido.getIdPedido());
                if (!saldo.isEmpty()) {
                    for (UnidadeItem u: saldo) {
                        if (u.getValor().doubleValue() > 0) {
                            u.setTipo(2);
                            unds.add(u);
                        }
                    }
                }
                //unds.addAll(saldo);
                valor = new JRBeanCollectionDataSource(unds);
            }
        } else if ("unidadesExtenso".equals(nome)) {
            StringBuilder unidades = new StringBuilder();
            Collection<UnidadeItem> unidadesItens = pedidoDao.findUnidadesByPedido(pedido.getIdPedido());
            for (UnidadeItem und : unidadesItens) {
                unidades.append(und.getUnidade());
                unidades.append(" ");
                unidades.append(NumberUtils.format(und.getValor()));
                unidades.append(" ");
            }
            valor = unidades.toString();
        } else if ("vlrUnidades".equals(nome)) {
            valor = pedido.getVlrUnidades();
        } else if ("financeiroRuim".equals(nome)) {
            valor = "FINANCEIRO RUIM".equals(pedido.getCliente().getSituacaoCliente().getNome());
        } else if ("itens".equals(nome)) {
            valor = new JRBeanCollectionDataSource(pedidoDao.findItensPedido(pedido.getIdPedido()));
        } else if ("idPedido".equals(nome)) {
            valor = pedido.getIdPedido();
        } else if ("idPedidoRepres".equals(nome)) {
            valor = pedido.getIdPedidoRepres();
        } else if ("comissao".equals(nome)) {
            valor = pedido.getComissao();
        } else if ("comissaoVendedor".equals(nome)) {
            valor = pedido.getComissaoVendedor();
        } else if ("dtPedido".equals(nome)) {
            valor = pedido.getDtPedido();
        } else if ("valor".equals(nome)) {
            valor = pedido.getValor();
        } else if ("valorCliente".equals(nome)) {
            valor = pedido.getValorCliente();
        } else if ("dtEntrega".equals(nome)) {
            valor = pedido.getDtEntrega();
        } else if ("obs".equals(nome)) {
            valor = pedido.getObs();
        } else if ("obs2".equals(nome)) {
            valor = pedido.getObs2();
        } else if ("obs3".equals(nome)) {
            valor = pedido.getObs3();
        } else if ("atendimento".equals(nome)) {
            valor = pedido.getAtendimento();
        } else if ("vendedor".equals(nome)) {
            valor = pedido.getVendedor().getNome();
        } else if ("vendedor.nome".equals(nome)) {
            valor = pedido.getVendedor().getNome();
        } else if ("vendedor.fone1".equals(nome)) {
            valor = pedido.getVendedor().getFone1();
        } else if ("situacao".equals(nome)) {
            valor = pedido.getSituacao();
        } else if ("vlrAtendido".equals(nome)) {
            valor = pedido.getVlrAtendido();
        } else if ("vlrComissaoTotal".equals(nome)) {
            valor = pedido.getVlrComissaoTotal();
        } else if ("vlrComVendedorAtendida".equals(nome)) {
            valor = pedido.getVlrComVendedorAtendida();
        } else if ("vlrComEmpresaAtendida".equals(nome)) {
            valor = pedido.getVlrComEmpresaAtendida();
        } else if ("vlrComissaoVendedor".equals(nome)) {
            valor = pedido.getVlrComissaoVendedor();
        } else if ("vlrComissaoEmpresa".equals(nome)) {
            valor = pedido.getVlrComissaoEmpresa();
        } else if ("valorPendente".equals(nome)) {
            valor = pedido.getValorPendente();
        } else if ("valorSaldo".equals(nome)) {
            valor = pedido.getVlrSaldoComissao();
        } else if ("vlrComissaoAtendida".equals(nome)) {
            valor = pedido.getVlrComissaoAtendida();
        } else if ("empilhadeira".equals(nome)) {
            valor = pedido.getCliente().isEmpilhadeira();
        } else if ("vlrComissaoReal".equals(nome)) {
            if (("A".equals(pedido.getSituacao())) || ("P".equals(pedido.getAtendimento()))) {
                valor = pedido.getVlrComissaoAtendida();
            } else {
                valor = pedido.getVlrComissaoTotal();
            }
        } else if ("vlrVendedorReal".equals(nome)) {
            if (("A".equals(pedido.getSituacao())) || ("P".equals(pedido.getAtendimento()))) {
//                valor = pedido.getVlrComissaoAtendida().divide(new BigDecimal(100)).multiply(pedido.getComissaoVendedor());
                valor = pedido.getVlrComVendedorAtendida();
            } else {
                valor = pedido.getVlrComissaoVendedor();
            }
        } else if ("vlrEmpresaReal".equals(nome)) {
            if (("A".equals(pedido.getSituacao())) || ("P".equals(pedido.getAtendimento()))) {
                //valor = pedido.getVlrComissaoAtendida().subtract(pedido.getVlrComissaoAtendida().divide(new BigDecimal(100)).multiply(pedido.getComissaoVendedor()));
                valor = pedido.getVlrComEmpresaAtendida();
            } else {
                valor = pedido.getVlrComissaoEmpresa();
            }
        } else if ("repres".equals(nome)) {
            valor = pedido.getRepres().getRazao();
        } else if ("repres.razao".equals(nome)) {
            valor = pedido.getRepres().getRazao();
        } else if ("repres.cidade".equals(nome)) {
            valor = pedido.getRepres().getCidade();
        } else if ("repres.uf".equals(nome)) {
            valor = pedido.getRepres().getUf();
        } else if ("formaPgto.nome".equals(nome)) {
            valor = pedido.getFormaPgto().getNome();
        } else if ("formaPgtoCliente.nome".equals(nome)) {
            valor = pedido.getFormaPgtoCliente() == null ? "" : pedido.getFormaPgtoCliente().getNome();
        } else if ("tipoPgto.nome".equals(nome)) {
            valor = pedido.getTipoPgto().getNome();
        } else if ("transportador.nome".equals(nome)) {
            valor = pedido.getTransportador().getNome();
        } else if ("transportador.fone1".equals(nome)) {
            valor = pedido.getTransportador().getFone1();
        } else if ("transportador.fone2".equals(nome)) {
            valor = pedido.getTransportador().getFone2();
        } else if ("transportador.codTrans".equals(nome)) {
            valor = pedido.getTransportador().getCodTrans();
        } else if ("formaPgtoTransp.nome".equals(nome)) {
            FormaPgto pgtoTransp = pedido.getFormaPgtoTransp();
            if (pgtoTransp != null) {
                valor = pgtoTransp.getNome();
            }
        }
        return valor;
    }
}
