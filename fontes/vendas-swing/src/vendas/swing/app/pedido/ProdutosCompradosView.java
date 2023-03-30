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
public class ProdutosCompradosView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ProdutosCompradosView() {
        super();
        setTitle(app.getResourceString("produtosCompradosTitle"));
        CompradosFilterPanel editPanel = new CompradosFilterPanel();
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
            //List lista = pedidoDao.findComprados(pedidoFilter);
            List lista = pedidoDao.findCompradosNota(pedidoFilter);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                    PedidoUtil util = new PedidoUtil();
                    Map model = util.getReportMap(app.getResourceString("produtosCompradosTitle"), pedidoFilter.getTitle());
                    model.put("cliente", pedidoFilter.getPrecoCliente());
                    //if (pedidoFilter.getDtNotaIni() != null) {
                        Reports.showReport(getClass().getResource(Constants.JRCOMPRADOSNOTA), model, lista);
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
