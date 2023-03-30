/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import vendas.beans.CobrancaFilter;
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
public class CtrlPagamentosView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public CtrlPagamentosView() {
        super();
        setTitle(app.getResourceString("ctrlPgtosTitle"));
        PgtoFilterPanel editPanel = new PgtoFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        CobrancaFilter pedidoFilter = new CobrancaFilter();
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {

        //if (Messages.confirmQuestion("Imprimir resumo")) {
        //    resumo();
        //} else {
            ctrlPgto();
        //}
    }

    private void resumo() {
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        CobrancaFilter filter = (CobrancaFilter) getFilterObject();
        try {
            List lista = pedidoDao.findPgtosResumo(filter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                Reports.showReport(app.getResourceString("ctrlPgtosTitle"), filter.getTitle() + " - Resumo", getClass().getResource(Constants.JRCTRLPGTOSRESUMO), lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void ctrlPgto() {
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        CobrancaFilter filter = (CobrancaFilter) getFilterObject();
        try {
            List lista = pedidoDao.findPgtosCliente(filter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                Reports.showReport(app.getResourceString("ctrlPgtosTitle"), filter.getTitle(), getClass().getResource(Constants.JRCTRLPGTOS2), lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

}
