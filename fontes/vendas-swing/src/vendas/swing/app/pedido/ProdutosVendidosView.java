/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.beans.ProdutosComprados;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ProdutosVendidosView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ProdutosVendidosView() {
        super();
        setTitle(app.getResourceString("produtosMaisVendidosTitle"));
        ProdutosVendidosFilterPanel editPanel = new ProdutosVendidosFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            //List lista = pedidoDao.findComprados(pedidoFilter);
            List<ProdutosComprados> lista = pedidoDao.findProdutosMaisVendidos(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                BigDecimal bd = BigDecimal.ZERO;
                for (ProdutosComprados p : lista) {
                    bd = bd.add(p.getComissao());
                }
                    PedidoUtil util = new PedidoUtil();
                    Map model = util.getReportMap(app.getResourceString("produtosMaisVendidosTitle"), pedidoFilter.getTitle());
                    model.put("valorTotal", bd);
                    model.put("cliente", pedidoFilter.getPrecoCliente());
                    //if ((pedidoFilter.getRepres().getIdRepres() != null) || (pedidoFilter.getQuebrarRepres()))
                        Reports.showReport(getClass().getResource(Constants.JRMAISVENDIDOS), model, lista);
                    //else
                    //    Reports.showReport(getClass().getResource(Constants.JRMAISVENDIDOSGERAL), model, lista);
                   // } else {
                   //     Reports.showReport(getClass().getResource(Constants.JRCOMPRADOS), model, lista);
                   // }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
