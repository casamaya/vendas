/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.List;
import ritual.swing.TApplication;
import vendas.beans.VendedorFilter;
import vendas.dao.PedidoDao;
import vendas.entity.AtendimentoPedido;
import vendas.swing.core.FilterFrame;
import vendas.util.Messages;

/**
 *
 * @author Sam
 */
public class ReciboComissaoView extends FilterFrame {

    TApplication app = TApplication.getInstance();
    VendedorSelectPanel editPanel;

    public ReciboComissaoView() {
        super();
        setTitle(app.getResourceString("reciboComissaoTitle"));
        editPanel = new VendedorSelectPanel();
        editPanel.init();
        setEditPanel(editPanel);
        setFilterObject(new VendedorFilter());
    }

    @Override
    public void execute() {
        VendedorFilter vendedor = (VendedorFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        PedidosReports pr = new PedidosReports();
        try {
            List<AtendimentoPedido> lista = pedidoDao.findReciboComissao(vendedor.getVendedor());
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                pr.emitirRecibo(lista, vendedor.getVendedor(), editPanel);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
    
    public void refresh() {
        editPanel = new VendedorSelectPanel();
        editPanel.init();
        setEditPanel(editPanel);
        setFilterObject(new VendedorFilter());
        editPanel.invalidate();
    }

}
