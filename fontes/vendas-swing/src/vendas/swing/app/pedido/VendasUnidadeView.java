/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.net.URL;
import java.util.List;
import vendas.beans.VendasUnidadeFilter;
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
public class VendasUnidadeView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public VendasUnidadeView() {
        super();
        setTitle(app.getResourceString("vendasUnidadeTitle"));
        VendasUnidadePanel editPanel = new VendasUnidadePanel();
        editPanel.init();
        setEditPanel(editPanel);
        VendasUnidadeFilter pedidoFilter = new VendasUnidadeFilter();
        pedidoFilter.setAtendimento("T");
        pedidoFilter.setSituacao("T");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        VendasUnidadeFilter pedidoFilter = (VendasUnidadeFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.vendasUndAtendimentoResumo(pedidoFilter); //findVendasUnidade
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                URL report = null;
                switch (pedidoFilter.getAgrupamento()) {
                    case 0:
                        getLogger().info("vendasundgrupo");
                        report = getClass().getResource(Constants.JRVENDASUNDGRUPO);
                        Reports.showReport(app.getResourceString("vendasUnidadeTitle") + " - Grupos", pedidoFilter.getTitle(), report, lista);
                        break;
                    case 1:
                        getLogger().info("vendasundcliente");
                        report = getClass().getResource(Constants.JRVENDASUNDCLIENTE);
                        Reports.showReport(app.getResourceString("vendasUnidadeTitle") + " - Clientes", pedidoFilter.getTitle(), report, new PedidoDataSource(pedidoDao.findVendasUnidade(pedidoFilter)));
                        break;
                    case 2:
                        getLogger().info("vendasunidade");
                        report = getClass().getResource(Constants.JRVENDASUNIDADE);
                        Reports.showReport(app.getResourceString("vendasUnidadeTitle") + " - Resumo", pedidoFilter.getTitle(), report, lista);
                        break;
                    }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
