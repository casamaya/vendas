/*
 * RepresDataSource.java
 *
 * Created on 05/08/2007, 15:50:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.produto;

import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vendas.entity.RepresProduto;

/**
 *
 * @author Sam
 */
public class RepresProdutoDataSource implements JRDataSource {

    private Iterator itrRepress;
    private Object valorAtual;
    private boolean hasNext = true;

    public RepresProdutoDataSource(List lista) {
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
        RepresProduto repres = (RepresProduto) valorAtual;
        String nome = campo.getName();

        if ("repres.razao".equals(nome)) {
            valor = repres.getRepres().getRazao();
        } else if ("produto.idProduto".equals(nome)) {
            valor = repres.getProduto().getIdProduto();
        } else if ("produto.descricao".equals(nome)) {
            valor = repres.getProduto().getDescricao();
        } else if ("produto.unidade.idUnidade".equals(nome)) {
            valor = repres.getProduto().getUnidade().getIdUnidade();
        } else if ("produto.undCumulativa.idUnidade".equals(nome)) {
            valor = repres.getProduto().getUndCumulativa().getIdUnidade();
        } else if ("produto.fatorConversao".equals(nome)) {
            valor = repres.getProduto().getFatorConversao();
        } else if ("produto.grupo.nomeGrupo".equals(nome)) {
            valor = repres.getProduto().getGrupoProduto().getNomeGrupo();
        } else if ("ativado".equals(nome)) {
            valor = repres.getAtivado();
        } else if ("preco".equals(nome)) {
            valor = repres.getPreco();
        } else if ("preco2".equals(nome)) {
            valor = repres.getPreco2();
        } else if ("frete".equals(nome)) {
            valor = repres.getFrete();
        } else if ("ipi".equals(nome)) {
            valor = repres.getIpi();
        } else if ("embalagem".equals(nome)) {
            valor = repres.getEmbalagem();
        } else if ("qtdUnd".equals(nome)) {
            valor = repres.getQtdUnd();
        } else if ("precoFinal".equals(nome)) {
            valor = repres.getPrecoFinal();
        } else if ("percComissao".equals(nome)) {
            valor = repres.getPercComissao();        }
        else if ("formasPgto".equals(nome)) {
            valor = new JRBeanCollectionDataSource(repres.getRepres().getFormasPgto());
        } else if ("repres.observacao".equals(nome)) {
            valor = repres.getRepres().getObservacao();
        } else if ("repres.dataTabela".equals(nome)) {
            valor = repres.getRepres().getDataTabela();
        } else if ("repres.obsProduto".equals(nome)) {
            valor = repres.getRepres().getObsProduto();
        }
        return valor;
    }
}
