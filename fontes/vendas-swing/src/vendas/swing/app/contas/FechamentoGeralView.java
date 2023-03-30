/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import ritual.swing.TApplication;
import vendas.beans.CompromissoFilter;
import vendas.beans.FechamentoBean;
import vendas.dao.CompromissoDao;
import vendas.entity.APagar;
import vendas.entity.AReceber;
import vendas.entity.Conta;
import vendas.entity.Vendedor;
import vendas.swing.core.FilterFrame;
import vendas.util.Messages;
import vendas.util.Reports;


/**
 *
 * @author Sam
 */
public class FechamentoGeralView extends FilterFrame {

    TApplication app = TApplication.getInstance();
    Vendedor vendedor;

    public FechamentoGeralView() {
        super();
        setTitle(app.getResourceString("fechamentoGeralTitle"));
        FechamentoFilterPanel editPanel = new FechamentoFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        CompromissoFilter pedidoFilter = new CompromissoFilter();
        //pedidoFilter.setSituacao("S");
        Integer id = TApplication.getInstance().getUser().getIdvendedor();
        if (id == null)
            id = 1;
        try {
            CompromissoDao dao = new CompromissoDao();
            vendedor = (Vendedor)dao.findById(Vendedor.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha geral");
        }
        Conta conta = new Conta();
        conta.setVendedor(vendedor);
        pedidoFilter.setConta(conta);
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        CompromissoFilter pedidoFilter = (CompromissoFilter) getFilterObject();
        CompromissoDao pedidoDao = new CompromissoDao();

        try {
            List<Object[]> result = pedidoDao.findFechamento(APagar.class, (CompromissoFilter)pedidoFilter);
            List<FechamentoBean> listaApagar = new ArrayList<>();
            BigDecimal v = BigDecimal.ZERO;
            for (Object[] o : result) {
                listaApagar.add(new FechamentoBean((String)o[0], (BigDecimal)o[1], "1"));
                v = v.add((BigDecimal)o[1]);
            }
            
            for (FechamentoBean f : listaApagar) {
                f.setPerc(new BigDecimal(f.getValor().doubleValue() * 100 / v.doubleValue()));
            }
            result = pedidoDao.findFechamento(AReceber.class, (CompromissoFilter)pedidoFilter);
            List<FechamentoBean> listaAReceber = new ArrayList<>();
            v = BigDecimal.ZERO;
            for (Object[] o : result) {
                listaAReceber.add(new FechamentoBean((String)o[0], (BigDecimal)o[1], "2"));
                v = v.add((BigDecimal)o[1]);
            }
            for (FechamentoBean f : listaAReceber) {
                f.setPerc(new BigDecimal(f.getValor().doubleValue() * 100 / v.doubleValue()));
            }
            
            List<FechamentoBean> lista = new ArrayList<>();
            lista.addAll(listaApagar);
            lista.addAll(listaAReceber);
            Reports.showReport(getTitle(), pedidoFilter.getTitle(), getClass().getResource("/vendas/report/fechamentogeral.jasper"), lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }


}
