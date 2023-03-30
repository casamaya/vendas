/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.cliente;

import java.net.URL;
import vendas.swing.app.pedido.*;
import java.util.List;
import java.util.Map;
import ritual.util.DateUtils;
import vendas.beans.ClientesFilter;
import vendas.dao.ClienteDao;
import ritual.swing.TApplication;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class RotatividadeView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public RotatividadeView() {
        super();
        setTitle(app.getResourceString("rotatividadeVendasTitle"));
        ClienteFilterPanel editPanel = new ClienteFilterPanel();
        editPanel.init();
        editPanel.setOrderPanel(false);
        setEditPanel(editPanel);
        ClientesFilter pedidoFilter = new ClientesFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        ClientesFilter cliente = (ClientesFilter) getFilterObject();
        ClienteDao pedidoDao = (ClienteDao) app.lookupService("clienteDao");
        try {
            List lista = pedidoDao.findRotatividade(cliente);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                showReport(app.getResourceString("rotatividadeVendasTitle"), cliente.getTitle(), Constants.JRROTATIVIDADE, lista);
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
        model.put("titles", DateUtils.getLastMonths(mes));
        Reports.showReport(url, model, lista);
    }
}
