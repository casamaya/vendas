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
import vendas.dao.ClienteDao;
import ritual.swing.TApplication;
import vendas.beans.VisitaFilter;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ResumoVisitasView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ResumoVisitasView() {
        super();
        setTitle("Resumo de visitas");
        ResumoVisitasFilterPanel editPanel = new ResumoVisitasFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        VisitaFilter pedidoFilter = new VisitaFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        VisitaFilter cliente = (VisitaFilter) getFilterObject();
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        String reportTitle;
        String reportFile;

            reportTitle = getTitle();
            reportFile = Constants.JRVISITASDIA;
        
        try {
            List lista = clienteDao.findClientesAVisitar(cliente);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                showReport(reportTitle, cliente.getTitle(), reportFile, lista);
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
