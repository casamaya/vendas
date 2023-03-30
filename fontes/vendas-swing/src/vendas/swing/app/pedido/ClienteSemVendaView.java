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
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ClienteSemVendaView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ClienteSemVendaView() {
        super();
        setTitle(app.getResourceString("clienteSemVendaTitle"));
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
            List lista = pedidoDao.findClienteSemVenda(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("clienteSemVendaTitle"), pedidoFilter.getTitle());
                Reports.showReport(getClass().getResource(Constants.JRCLIENTESEMVENDA), model, lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
