/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import java.util.Map;
import vendas.beans.PedidoFilter;
import vendas.beans.VendasCliente;
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
public class VendasClientesView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public VendasClientesView() {
        super();
        setTitle(app.getResourceString("vendasClienteTitle"));
        VendasClienteFilterPanel editPanel = new VendasClienteFilterPanel();
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
            List lista = pedidoDao.findVendasCliente(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                vendasCliente(lista, pedidoFilter.getTitle());
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }

    private void vendasCliente(List<VendasCliente> lista, String subTitle) throws Exception {
        double totalComissao = 0;
        for (VendasCliente venda : lista) {
            totalComissao += venda.getValorComissao().doubleValue();
        }
        Map model = app.getDefaultMap(app.getResourceString("vendasClienteTitle"), subTitle);
        model.put("valorTotal", totalComissao);
        Reports.showReport(getClass().getResource(Constants.JRVENDASCLIENTE), model, lista);
    }
}

