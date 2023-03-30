/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ClientesProdutoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ClientesProdutoView() {
        super();
        setTitle(app.getResourceString("clientesProdutoTitle"));
        ProdutosClientePanel editPanel = new ProdutosClientePanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setSituacao("N");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.findClientesProduto(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                if (pedidoFilter.getOrdem() == 0) {
                    Reports.showReport(app.getResourceString("clientesProdutoTitle"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRCLIENTESPRODUTO1), lista);
                } else {
                    Reports.showReport(app.getResourceString("clientesProdutoTitle"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRCLIENTESPRODUTO2), lista);
                }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
