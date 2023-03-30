/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.net.URL;
import java.util.List;
import java.util.Map;
import ritual.util.DateUtils;
import ritual.swing.TApplication;
import vendas.beans.MapaComissao;
import vendas.beans.MapaFilterBean;
import vendas.dao.PedidoDao;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class MapaComissaoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public MapaComissaoView() {
        super();
        setTitle(app.getResourceString("mapaComissaoTitle"));
        MapaFilterPanel editPanel = new MapaFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);

        MapaFilterBean mapa = new MapaFilterBean();
        setFilterObject(mapa);
    }

    @Override
    public void execute() {
        MapaFilterBean cliente = (MapaFilterBean) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List<MapaComissao> lista = pedidoDao.findMapaComissao(cliente);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                showReport(app.getResourceString("mapaComissaoTitle"), cliente.getTitle(), Constants.JRMAPACOMISSAO, lista);
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
