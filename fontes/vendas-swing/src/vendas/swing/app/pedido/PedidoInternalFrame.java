/*
 * PedidoInternalFrame.java
 *
 * Created on 27/06/2007, 19:56:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.model.PedidoTableModel;
import vendas.entity.Pedido;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import vendas.beans.PedidoFilter;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.EmailBean;
import vendas.beans.MovimentoFilter;
import vendas.beans.PedidoBean;
import vendas.beans.VendasUnidadeFilter;
import vendas.dao.PedidoDao;
import vendas.entity.RepresContato;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class PedidoInternalFrame extends TableViewFrame {

    private PedidoFilterPanel filterPanel;
    private final RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
    private final TApplication app;

    public PedidoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Controle de pedidos", tableModel);
        app = TApplication.getInstance();
    }

    @Override
    public void remove() {
        if (app.isGrant("EXCLUIR_PEDIDO")) {
            super.remove();
        }
    }   
    
    private JMenuItem createAtendimentoMenuItem() {
        JMenuItem atendimentoMenuItem = new JMenuItem();
        atendimentoMenuItem.setText(getBundle().getString("posicaoAtendimento")); // NOI18N
        atendimentoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int i = getSelectedRow();
                if (i < 0) {
                    Messages.warningMessage(getBundle().getString("selectOne"));
                    return;
                }
                
                PedidoUtil util = new PedidoUtil();
                ServiceTableModel vtm = getTableModel();
                Pedido pedido = (Pedido) vtm.getObject(getSelectedRow());
                
                try {
                    util.obsAtendimentoPedido(pedido);
                } catch (Exception e) {
                    getLogger().error(e);
                }
            }
        });
        return atendimentoMenuItem;
    }

    private JMenuItem createDtEmbarqueMenuItem() {
        JMenuItem dtEmbarqueMenuItem = new JMenuItem();
        dtEmbarqueMenuItem.setText("Alterar data de embarque"); // NOI18N
        dtEmbarqueMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                EditDialog edtDlg = new EditDialog("Alterar data de embarque");
                DtEmbarqueFilterPanel edtPanel = new DtEmbarqueFilterPanel();
                
                edtDlg.setEditPanel(edtPanel);
                MovimentoFilter dtEmbarque = new MovimentoFilter();
                
                while (edtDlg.edit(dtEmbarque)) {
                    if (dtEmbarque.getDtInicio().compareTo(DateUtils.getCurrentDate()) < 0) {
                        Messages.errorMessage("Data de embarque deve ser maior ou igual à data corrente.");
                        continue;
                    }
                    PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
                    for (int i : rows) {
                        Pedido pedido = (Pedido)getTableModel().getObject(i);
                        if (pedido.getSituacao().equals("N")) {
                            pedido.setDtEmbarqueAnterior(pedido.getDtEntrega());
                            pedido.setDtEntrega(dtEmbarque.getDtInicio());
                            dao.updateEmbarquePedido(pedido);
                        }
                    }
                    refresh();
                    break; 
                }                
            }
        });
        return dtEmbarqueMenuItem;
    }
    
    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        if (app.isGrant("ALTERAR_PEDIDO")) {
            popupMenu.add(createAtendimentoMenuItem());
        }
        
        if (app.getUser().isAdmin()) {
            popupMenu.add(createDtEmbarqueMenuItem());
        }

        JMenuItem printMenuItem = new JMenuItem();
        printMenuItem.setText(getBundle().getString("imprimirPedido")); // NOI18N
        printMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int i = getSelectedRow();
                if (i < 0) {
                    Messages.warningMessage(getBundle().getString("selectOne"));
                    return;
                }
                PedidoUtil util = new PedidoUtil();
                ServiceTableModel vtm = getTableModel();
                Pedido pedido = (Pedido) vtm.getObject(getSelectedRow());
                try {
                    util.imprimirPedido(pedido);
                } catch (Exception e) {
                    getLogger().error(e);
                }
            }
        });
        popupMenu.add(printMenuItem);

        setPopupMenu(popupMenu);
    }
    
    private JMenuItem createMarcarMenuItem() {
        JMenuItem marcarMenuItem = new JMenuItem();
        marcarMenuItem.setText(getBundle().getString("marcarDesmarcarPedido")); // NOI18N
        marcarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int i = getSelectedRow();
                if (i < 0) {
                    Messages.warningMessage(getBundle().getString("selectOne"));
                    return;
                }
                ServiceTableModel vtm = getTableModel();
                Pedido pedido = (Pedido) vtm.getObject(getSelectedRow());
                if (pedido.isAcompanhar()) {
                    pedido.setAcompanhar(new Boolean(false));
                } else {
                    pedido.setAcompanhar(new Boolean(true));
                }
                try {
                    vtm.updateRow(pedido);
                } catch (Exception e) {
                    getLogger().error(getBundle().getString("saveErrorMessage"), e);
                    Messages.errorMessage(getBundle().getString("saveErrorMessage"));
                }
            }
        });
        return marcarMenuItem;
    }

    public PedidoFilterPanel getFilterPanel() {
        return filterPanel;
    }

    public void setFilterPanel(PedidoFilterPanel filterPanel) {
        this.filterPanel = filterPanel;
    }
    
    @Override
    public void resetFilterObject() {
        PedidoFilter filter = new PedidoFilter();
        filter.setSituacao("N");
        setFilterObject(filter);
    }

    @Override
    public void initComponents() {
        final ServiceTableModel stm = getTableModel();

        JTable jt;
        jt = new JTable(stm) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                BigDecimal value = new BigDecimal(0);
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                c.setForeground(getForeground());
                Pedido p = (Pedido) stm.getObject(rowIndex);
                if (p == null)
                    return c;
                if ((vColIndex == PedidoTableModel.CLIENTE) && "FINANCEIRO RUIM,INATIVO,FECHOU".contains(p.getCliente().getSituacaoCliente().getNome())) {
                    c.setForeground(Color.RED);
                }
                if ((vColIndex == PedidoTableModel.PEDIDO) && p.getValorOp() != null && p.getValorOp().compareTo(value) != 0) {
                    c.setForeground(Color.BLUE);
                }

                return c;
            }
        };
        jt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });
        //jt.setPreferredScrollableViewportSize(new Dimension(400, 300));
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        //jt.setDefaultRenderer(Float.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setDefaultRenderer(Date.class, new DateCellRenderer());
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        setTable(jt);
        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        //setSize(400, 300);
        
        setPreferredSize(new Dimension(800, 400));
        getLogger().info("initComponents ini");
        resetFilterObject();
        TableColumn col = getColumn(PedidoTableModel.REPRES);
        col.setPreferredWidth(150);
        col = getColumn(PedidoTableModel.PREPEDIDO);
        col.setPreferredWidth(50);
        col = getColumn(PedidoTableModel.EMITIDO);
        col.setPreferredWidth(20);
        col = getColumn(PedidoTableModel.PEDIDOREPRES);
        col.setPreferredWidth(100);
        col = getColumn(PedidoTableModel.EMITIDOCLIENTE );
        col.setPreferredWidth(20);
        col = getColumn(PedidoTableModel.EMITIDOCOBRANCA );
        col.setPreferredWidth(20);
        col = getColumn(PedidoTableModel.EMISSAO);
        col.setPreferredWidth(80);
        col = getColumn(PedidoTableModel.ENTREGA);
        col.setPreferredWidth(80);
        col = getColumn(PedidoTableModel.CLIENTE);
        col.setPreferredWidth(150);
        col = getColumn(PedidoTableModel.RESPONSAVEL);
        col.setPreferredWidth(150);
        col = getColumn(PedidoTableModel.SITUACAO);
        col.setPreferredWidth(30);
        col = getColumn(PedidoTableModel.ATENDIMENTO);
        col.setPreferredWidth(25);
        getLogger().info("initComponents end");
        setSize(700, 500);
        createPopupMenu();
    }

    @Override
    public void insert() {
        if (app.isGrant("INCLUIR_PEDIDO"))
            super.insert();
    }
    
    @Override
    public void edit() {
        if (app.isGrant("ALTERAR_PEDIDO"))
            super.insert();
    }
    
    @Override
    protected void insertRow() {
        Pedido pedido = new Pedido();
        PedidoEditPanel editPanel = new PedidoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setPedido(pedido);
        while (edtDlg.edit(pedidoBean, false)) {
            try {
                PedidoUtil util = new PedidoUtil();
                if (!util.validaPedido(pedido)) {
                    continue;
                }
                getTableModel().insertRecord(pedidoBean.getPedido());
                PedidoDao dao = (PedidoDao) app.lookupService("pedidoDao");
                dao.setNextValue(pedido.getIdPedido());
                represDao.addCliente(pedido.getRepres(), pedido.getCliente());
                viewPedido(pedidoBean.getPedido());
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        Pedido pedido = (Pedido) vtm.getObject(getSelectedRow());
        if ("A".equals(pedido.getSituacao())) {
            Messages.warningMessage(getBundle().getString("naoEditarPedido"));
            return;
        }
        PedidoEditPanel editPanel = new PedidoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setPedido(pedido);
        PedidoUtil util = new PedidoUtil();
        while (edtDlg.edit(pedidoBean)) {
            if (!util.validaPedido(pedido)) {
                    continue;
            }
            try {
                PedidoDao pedidoDao = (PedidoDao) vtm.getDao();
                pedidoDao.updatePedido(pedidoBean.getPedido());
                //vtm.updateRow(pedidoBean.getPedido());
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void viewPedido(Pedido pedido) {
        String pedidoTitle = "Visualizar pedido";
        if (!app.locateFrame(pedidoTitle)) {
            PedidoViewFrame viewFrame = new PedidoViewFrame(pedidoTitle);
            app.getDesktopPane().add(viewFrame);
            viewFrame.setLocation(0, 0);
            viewFrame.setVisible(true);
            viewFrame.execute(pedido);
        } else {
            PedidoViewFrame viewFrame = (PedidoViewFrame) app.getFrame(pedidoTitle);
            viewFrame.execute(pedido);
        }
    }

    @Override
    public void viewRow() {
        Pedido pedido = (Pedido) getTableModel().getObject(getSelectedRow());
        viewPedido(pedido);
    }
    
    public void filtrarPedido(Integer idpedido) {
        PedidoFilter filtro = new PedidoFilter();
        filtro.setPedido(idpedido);
        
        executeFilter(filtro);
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        PedidoFilter filtro = new PedidoFilter();
        EditDialog edtDlg = new EditDialog(getBundle().getString("filterPedidoTitle"));
        PedidoFilterPanel pedidoFilter = new PedidoFilterPanel();
        pedidoFilter.init();
        edtDlg.setEditPanel(pedidoFilter);
        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    @Override
    public void showReport() {
        Object[] options = {getBundle().getString("relacao"), getBundle().getString("detalhado"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        switch (n) {
            case 0:
                showPedidos();
                break;
            case 1:
                showPedidosDetalhados();
                break;
        }
    }

    private void showPedidos() {
        URL url;
        PedidoFilter filtro = (PedidoFilter) getFilterObject();
        if (filtro.getOrdem() == 1) // ordem por cliente
        {
            url = getClass().getResource(Constants.JRCTRLPEDIDOS2);
        } else {
            url = getClass().getResource(Constants.JRCTRLPEDIDOS1);
        }
        String reportTitle = this.getTitle();
        String subTitle = "";
        if (getFilterObject() != null) {
            subTitle += filtro.getTitle();
        }
        PedidoUtil util = new PedidoUtil();
        Map model = util.getReportMap(reportTitle, subTitle);
        model.put("cliente", filtro.getPrecoCliente());
        model.put("pedidoFornecedor", filtro.getFiltrarPedidosFornecedor());
        
        VendasUnidadeFilter filter = new VendasUnidadeFilter();
        filter.setCliente(filtro.getCliente());
        filter.setClienteResponsavel(filtro.getResponsavel());
        filter.setDtEmissaoIni(filtro.getDtEmissaoIni());
        filter.setDtEmissaoEnd(filtro.getDtEmissaoEnd());
        filter.setDtEntregaIni(filtro.getDtEntregaIni());
        filter.setDtEntregaEnd(filtro.getDtEntregaEnd());
        filter.setDtNotaIni(filtro.getDtNotaIni());
        filter.setDtNotaEnd(filtro.getDtNotaEnd());
        filter.setFornecedor(filtro.getRepres());
        filter.setVendedor(filtro.getVendedor());
        filter.setPendentes(Boolean.FALSE);
        filter.setGrupoCliente(filtro.getGrupo());
        filter.setSituacao(filtro.getSituacao());
        filter.setAtendimento(filtro.getAtendimento());
        
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        List unidades = pedidoDao.vendasUnidadeResumo(filter);
        model.put("resumo", new JRBeanCollectionDataSource(unidades));
        
        try {
            ReportViewFrame rvf = Reports.showReport(url, model, new PedidoDataSource(getTableModel().getItemList()));
            if (filtro.getRepres() != null && filtro.getRepres().getIdRepres() != null) {
                EmailBean eb = rvf.getEmail();
                List<String> to = new ArrayList();
                for (RepresContato contato : filtro.getRepres().getContatos()) {
                    if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                        to.add(contato.getEndereco());
                    }
                }
                eb.setTo(to);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    private void showPedidosDetalhados() {
        URL url;
        PedidoFilter filtro = (PedidoFilter) getFilterObject();
        url = getClass().getResource(Constants.JRPEDIDOSDETALHE);
        String reportTitle = this.getTitle() + " - Detalhado";
        String subTitle = "";
        if (getFilterObject() != null) {
            subTitle += filtro.getTitle();
        }
        PedidoUtil util = new PedidoUtil();
        Map model = util.getReportMap(reportTitle, subTitle);
        model.put("pedidoFornecedor", filtro.getFiltrarPedidosFornecedor());
        PedidoDataSource ds = new PedidoDataSource(getTableModel().getItemList());
        try {
            ReportViewFrame rvf = Reports.showReport(url, model, ds);
            if (filtro.getRepres() != null && filtro.getRepres().getIdRepres() != null) {
                EmailBean eb = rvf.getEmail();
                List<String> to = new ArrayList();
                for (RepresContato contato : filtro.getRepres().getContatos()) {
                    if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
                        to.add(contato.getEndereco());
                    }
                }
                eb.setTo(to);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    public void executeFilter(PedidoFilter filtro) {
        setFilterObject(filtro);
        doRefresh();
    }
    
    public void searchNota(String value) {
        PedidoFilter filtro = new PedidoFilter();
        filtro.setNota(value);
        filtro.setTitle("Nota");
        filtro.setSituacao("T");
        setFilterObject(filtro);
        doRefresh();
        if (getTableModel().getItemList().isEmpty()) {
            Messages.errorMessage(getBundle().getString("emptyList"));
        }
    }
}
