/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.CompromissoFilter;
import vendas.beans.FechamentoBean;
import vendas.beans.MovimentoFilter;
import vendas.beans.ResumoMovimento;
import vendas.dao.CompromissoDao;
import vendas.dao.FluxoDao;
import vendas.entity.APagar;
import vendas.entity.AReceber;
import vendas.entity.Conta;
import vendas.entity.Vendedor;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;


/**
 *
 * @author Sam
 */
public class FechamentoGrupoView extends FilterFrame {

    TApplication app = TApplication.getInstance();
    Vendedor vendedor;

    public FechamentoGrupoView() {
        super();
        setTitle("Resumo por grupo");
        FechamentoFilterPanel editPanel = new FechamentoFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        CompromissoFilter filtro = new CompromissoFilter();
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
        filtro.setConta(conta);
        setFilterObject(filtro);
    }

    @Override
    public void execute() {
        CompromissoFilter filtro = (CompromissoFilter) getFilterObject();
        
        MovimentoFilter mov = new MovimentoFilter();
        mov.setVendedor(filtro.getConta().getVendedor().getIdVendedor());
        mov.setDtInicio(filtro.getDtIni());
        mov.setDtTermino(filtro.getDtEnd());
        
        FluxoDao dao = (FluxoDao)TApplication.getInstance().lookupService("fluxoDao");
        List<ResumoMovimento> lista = dao.findResumoGrupoMovimento(mov);
        
            StringBuilder filterTitle = new StringBuilder();
            filterTitle.append(DateUtils.format(filtro.getDtIni()));
            filterTitle.append(" - ");
            filterTitle.append(DateUtils.format(filtro.getDtEnd()));

        try {
            URL url = getClass().getResource(Constants.JRRESUMOGRUPOMOVIMENTO);
            Reports.showReport("Resumo", filterTitle.toString(), url, lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }


}
