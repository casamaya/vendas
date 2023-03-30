/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.beans.VendasUnidadeFilter;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class NotasPedidoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public NotasPedidoView() {
        super();
        setTitle(app.getResourceString("notasPedidoTitle"));
        NotaFilterPanel editPanel = new NotaFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setSituacao("T");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.findComissaoPgto(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                ComissaoPgtoDataSource ds = new ComissaoPgtoDataSource(lista);
                Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("notasPedidoTitle"), pedidoFilter.getTitle());
                model.put("quebraDtEntrega", false);
                VendasUnidadeFilter filter = new VendasUnidadeFilter();
                filter.setCliente(pedidoFilter.getCliente());
                filter.setDtEmissaoIni(pedidoFilter.getDtEmissaoIni());
                filter.setDtEmissaoEnd(pedidoFilter.getDtEmissaoEnd());
                filter.setDtEntregaIni(pedidoFilter.getDtEntregaIni());
                filter.setDtEntregaEnd(pedidoFilter.getDtEntregaEnd());
                filter.setDtNotaIni(pedidoFilter.getDtNotaIni());
                filter.setDtNotaEnd(pedidoFilter.getDtNotaEnd());
                filter.setFornecedor(pedidoFilter.getRepres());
                filter.setVendedor(pedidoFilter.getVendedor());
                filter.setPendentes(Boolean.FALSE);
                filter.setGrupoCliente(pedidoFilter.getGrupo());
                filter.setAtendimento("T");
                List unidades = pedidoDao.vendasUndAtendimentoResumo(filter);
                model.put("resumo", new JRBeanCollectionDataSource(unidades));
                Reports.showReport(getClass().getResource(Constants.JRNOTASPEDIDO), model, ds);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
