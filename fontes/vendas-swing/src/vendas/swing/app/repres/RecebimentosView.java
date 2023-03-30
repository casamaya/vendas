/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.repres;

import java.util.List;
import vendas.beans.RepresFilter;
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
public class RecebimentosView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public RecebimentosView() {
        super();
        setTitle(app.getResourceString("recebimentosTitle"));
        RecebimentosPanel editPanel = new RecebimentosPanel();
        editPanel.init();
        setEditPanel(editPanel);
        RepresFilter pedidoFilter = new RepresFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        RepresFilter represFilter = (RepresFilter) getFilterObject();
        RepresDao pedidoDao = (RepresDao) app.lookupService("represDao");
        try {
            List lista = pedidoDao.findRecebimentos(represFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                Reports.showReport(app.getResourceString("recebimentosTitle"), represFilter.getTitle(), getClass().getResource(Constants.JRRECEBIMENTOS), lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}

