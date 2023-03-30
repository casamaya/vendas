/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.beans.ClientesFilter;
import vendas.beans.ClientesGrupo;
import vendas.dao.ClienteDao;
import vendas.swing.app.cliente.FichaClienteDataSource;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ClientesGrupoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ClientesGrupoView() {
        super();
        setTitle(app.getResourceString("clientesGrupoProdutoTitle"));
        ClienteGrupoPanel editPanel = new ClienteGrupoPanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setSituacao("N");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();

        if (pedidoFilter.getTipoRelatorio() == 1) {
            resumo(pedidoFilter);
        } else {
            relacao(pedidoFilter);
        }
    }

    private void resumo(PedidoFilter pedidoFilter) {
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.findClientesGrupo(pedidoFilter);
            if ((lista == null) || lista.size() == 0) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                Reports.showReport(app.getResourceString("clientesGrupoProdutoTitle"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRCLIENTESGRUPOPRODUTO), lista);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void relacao(PedidoFilter pedidoFilter) {
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");

        List<ClientesGrupo> lista = pedidoDao.findClientesGrupo(pedidoFilter);
        List<Integer> ids = new ArrayList<>();
        for (ClientesGrupo c : lista) {
            ids.add(c.getIdCliente());
        }
        
        ClientesFilter filter = new ClientesFilter();
        filter.setClientes(ids);
        
        List clientes = clienteDao.findByExample(filter);
        URL url;
        url = getClass().getResource(Constants.JRCLIENTES);
        String reportTitle = "Clientes";
        FichaClienteDataSource ds = new FichaClienteDataSource(clientes);
        try {
            Reports.showReport(reportTitle, pedidoFilter.getTitle(), url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }
}
