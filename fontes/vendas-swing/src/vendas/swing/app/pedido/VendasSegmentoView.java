/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import java.util.Map;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.beans.ResumoVendasUnidade;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class VendasSegmentoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public VendasSegmentoView() {
        super();
        setTitle(app.getResourceString("vendasSegmentoTitle"));
        VendedorPeriodoFilterPanel editPanel = new VendedorPeriodoFilterPanel();
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
            List<ResumoVendasUnidade> lista = pedidoDao.findVendasSegmento(pedidoFilter);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                double totalComissao = 0;
                for (ResumoVendasUnidade venda : lista) {
                    totalComissao += venda.getComissao().doubleValue();
                }
                Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("vendasSegmentoTitle"), pedidoFilter.getTitle());
                model.put("valorTotal", totalComissao);
                Reports.showReport(getClass().getResource(Constants.JRVENDASSEGMENTO), model, lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
