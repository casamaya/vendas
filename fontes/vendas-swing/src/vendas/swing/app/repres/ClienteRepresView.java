/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.repres;

import java.net.URL;
import java.util.List;
import java.util.Map;
import vendas.beans.ClientesFilter;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ClienteRepresView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ClienteRepresView() {
        super();
        setTitle(app.getResourceString("clientesRepresentadaTitle"));
        ClienteRepresPanel editPanel = new ClienteRepresPanel();
        editPanel.init();
        setEditPanel(editPanel);
        ClientesFilter pedidoFilter = new ClientesFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        ClientesFilter represFilter = (ClientesFilter) getFilterObject();
        RepresDao represDao = (RepresDao) app.lookupService("represDao");
        try {
            List lista = represDao.findClientes(represFilter);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                if (represFilter.getRelacao()) {
                    ClienteRepresDataSource ds = new ClienteRepresDataSource(lista);
                    Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("clientesRepresentadaTitle"));
                    Reports.showReport(getClass().getResource(Constants.JRCLIENTESREPRES), model, ds);
                } else {
                    URL url;
                    url = getClass().getResource(Constants.JRCLIENTEREPRESEMAIL);
                    String reportTitle = this.getTitle();
                    try {
                        Reports.showReport(reportTitle, "", url, lista);
                    } catch (Exception e) {
                        getLogger().error(e.getMessage(), e);
                        Messages.errorMessage(app.getResourceString("reportError"));
                    }

                }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}

