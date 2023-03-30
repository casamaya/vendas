/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.dao.ClienteDao;
import vendas.util.Constants;
import vendas.util.Reports;

/**
 *
 * @author sam
 */
public class VisitasUtil {

    TApplication app = TApplication.getInstance();

    public void showVisitasDia(Date dtVisita) {
        VisitaFilter filter = new VisitaFilter();
        filter.setDtInicio(DateUtils.getNextDate(dtVisita, -5));
        filter.setDtFim(dtVisita);
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        List lista = clienteDao.findClientesAVisitar(filter);
        if (lista == null || lista.isEmpty())
            return;
        Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("visitasTitle"), filter.getTitle());
        try {
            Reports.showReport(getClass().getResource(Constants.JRVISITASDIA), model, lista);
        } catch (Exception e) {
        }
    }
}
