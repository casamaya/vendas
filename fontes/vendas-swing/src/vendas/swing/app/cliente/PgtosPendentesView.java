/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.cliente;

import java.net.URL;
import java.util.List;
import java.util.Map;
import ritual.util.DateUtils;
import vendas.beans.ClientesFilter;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class PgtosPendentesView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public PgtosPendentesView() {
        super();
        setTitle(app.getResourceString("pgtosPendentes"));
        PgtosPendentesFilterPanel editPanel = new PgtosPendentesFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        ClientesFilter pedidoFilter = new ClientesFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        ClientesFilter cliente = (ClientesFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.getPgtosPendentes(cliente);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                showReport(app.getResourceString("pgtosPendentes"), cliente.getTitle(), Constants.JRPGTONAOPAGO, lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
    private void showReport(String title, String subTitle, String reportFile, List lista) throws Exception {
        URL url = getClass().getResource(reportFile);
        Map model = app.getDefaultMap(title, subTitle);
        int mes = DateUtils.getMonth(DateUtils.getDate());
        Reports.showReport(url, model, lista);
    }
}
