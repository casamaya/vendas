/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.beans.EmailBean;
import vendas.entity.RepresContato;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class PosicaoAtendView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public PosicaoAtendView() {
        super();
        setTitle(app.getResourceString("posicaoAtendTitle"));
        AtendimentoFilterPanel editPanel = new AtendimentoFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setSituacao("N");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            List lista = pedidoDao.findPosicaoAtend(pedidoFilter);
            if ((lista == null) || lista.isEmpty()) {
                Messages.errorMessage(app.getResourceString("noDataReport"));
            } else {
                PedidoDataSource ds = new PedidoDataSource(lista);
                Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("posicaoAtendTitle"), pedidoFilter.getTitle());
                model.put("quebraDtEntrega", pedidoFilter.getTotalizaEntrega().booleanValue());
                model.put("dtRelatorio", new Date());
                model.put("pedidoFornecedor", pedidoFilter.getFiltrarPedidosFornecedor());
                ReportViewFrame rvf = Reports.showReport(getClass().getResource(Constants.JRATENDIMENTOS), model, ds);
                if (pedidoFilter.getRepres() != null && pedidoFilter.getRepres().getIdRepres() != null) {
                EmailBean eb = rvf.getEmail();
                List<String> to = new ArrayList();
                for (RepresContato contato : pedidoFilter.getRepres().getContatos()) {
                    if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                        to.add(contato.getEndereco());
                    }
                }
                eb.setTo(to);
            }
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
