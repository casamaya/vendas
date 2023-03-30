/**
 * BancoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.TApplication;
import vendas.beans.CobrancaFilter;
import vendas.dao.PedidoDao;
import vendas.entity.Pedido;
import vendas.entity.PgtoCliente;
import vendas.swing.model.CobrancaTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author p993702
 */
public class CobrancaInternalFrame extends TableViewFrame {

    TApplication app = TApplication.getInstance();

    public CobrancaInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("ctrlPgtosTitle"));
    }

    @Override
    public void initComponents() {
        super.initComponents();

        JTable jt;
        final ServiceTableModel stm = getTableModel();
        jt = new JTable(stm) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                    int rowIndex, int vColIndex) {
                BigDecimal value = new BigDecimal(0);
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                c.setForeground(getForeground());
                PgtoCliente p = (PgtoCliente) stm.getObject(rowIndex);
                if ((vColIndex == CobrancaTableModel.CLIENTE) && "FINANCEIRO RUIM,INATIVO,FECHOU".contains(p.getAtendimentoPedido().getPedido().getCliente().getSituacaoCliente().getNome())) {
                    c.setForeground(Color.RED);
                }
                if ((vColIndex == CobrancaTableModel.PEDIDO) && p.getAtendimentoPedido().getPedido().getValorOp().compareTo(value) != 0) {
                    c.setForeground(Color.BLUE);
                }
                if (vColIndex == CobrancaTableModel.OBS) {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        };
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setDefaultRenderer(Date.class, new DateCellRenderer());
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });
        
        setTable(jt);
        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        // setSize(400, 300);
        setPreferredSize(new Dimension(800, 400));
        setFilterObject(createFilter());
        getColumn(CobrancaTableModel.OBS).setPreferredWidth(200);
        getColumn(CobrancaTableModel.VENDEDOR).setPreferredWidth(85);
        getColumn(CobrancaTableModel.CLIENTE).setPreferredWidth(150);
        getColumn(CobrancaTableModel.RESPONSAVEL).setPreferredWidth(150);
        getColumn(CobrancaTableModel.OPERADOR).setPreferredWidth(35);
        getColumn(CobrancaTableModel.DTNOTA).setPreferredWidth(80);
        //getColumn(CobrancaTableModel.DTPEDIDO).setPreferredWidth(80);
        getColumn(CobrancaTableModel.DTPGTO).setPreferredWidth(80);
        getColumn(CobrancaTableModel.DTVENCIMENTO).setPreferredWidth(80);
        getColumn(CobrancaTableModel.TIPO).setPreferredWidth(30);
        getColumn(CobrancaTableModel.FORNECEDOR).setPreferredWidth(150);
        //getColumn(CobrancaTableModel.VALORPEDIDO).setPreferredWidth(90);
        getColumn(CobrancaTableModel.DTDESCONTO).setPreferredWidth(90);
        getColumn(CobrancaTableModel.LIQUIDO).setPreferredWidth(90);
        getColumn(CobrancaTableModel.VALORPAGO).setPreferredWidth(90);
        getColumn(CobrancaTableModel.SITUACAO).setPreferredWidth(30);
        getColumn(CobrancaTableModel.ATENDIMENTO).setPreferredWidth(25);

        createPopupMenu();
    }

    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem todosMenuItem = new JMenuItem();
        todosMenuItem.setText(getBundle().getString("selecionarTodos"));
        todosMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                getTable().selectAll();
            }
        });
        popupMenu.add(todosMenuItem);
        JMenuItem inverterMenuItem = new JMenuItem();
        inverterMenuItem.setText(getBundle().getString("inverterSelecao"));
        inverterMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int[] selectedIndexs = getTable().getSelectedRows();
                getTable().selectAll();
                for (int i : selectedIndexs) {
                        getTable().removeRowSelectionInterval(i, i);
                }
            }
        });
        popupMenu.add(inverterMenuItem);


        popupMenu.addSeparator();

        JMenuItem baixarMenuItem = new JMenuItem();
        baixarMenuItem.setText(getBundle().getString("registrarPagamento"));
        baixarMenuItem.setEnabled(app.isGrant("BAIXA_AUTOMATICA"));
        baixarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                PgtoCliente pgto;
                int contador = 0;
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                
                boolean p;
                boolean admin;

                if (Messages.confirmQuestion("Confirma baixa automática?")) {
                    List<PgtoCliente> lista = new ArrayList();
                    for (int i : rows) {
                        pgto = (PgtoCliente) getTableModel().getObject(i);
                        p = "P".equals(pgto.getTipoPgto());
                        admin = app.getUser().isAdmin();
                        if (p && (!admin)) {
                            contador++;
                            continue;
                        }
                        lista.add(pgto);
                    }
                    if (!lista.isEmpty()) {                        
                        try {
                            PedidoDao dao = (PedidoDao) getTableModel().getDao();
                            dao.baixarCobranca(lista);
                            refresh();
                        } catch (Exception e) {
                            getLogger().error(getBundle().getString("exportError"), e);
                            Messages.errorMessage("Falha da atualiza\u00E7\u00E3o");
                        }
                    } else {
                        Messages.infoMessage("Não foi possível realizar a baixa automática de " + contador + " lançamento(s).");
                    }
                }

            }
        });
        popupMenu.add(baixarMenuItem);

        JMenuItem observacaoMenuItem = new JMenuItem();
        observacaoMenuItem.setText(getBundle().getString("obs"));
        observacaoMenuItem.setEnabled(app.isGrant("ALTERAR_OBSERVACAO_PGTO"));
        observacaoMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                editaObservacao();
            }
        });
        popupMenu.add(observacaoMenuItem);

        setPopupMenu(popupMenu);
    }

    private void editaObservacao() {
        ObservacaoEditPanel editPanel = new ObservacaoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("obs"));
        edtDlg.setEditPanel(editPanel);

        ServiceTableModel vtm = getTableModel();
        int n = getSelectedRow();
        Object pedido = vtm.getObject(n);
        while (edtDlg.edit(pedido)) {
            try {
                PedidoDao dao = (PedidoDao) getTableModel().getDao();
                dao.updateRow(pedido);
                vtm.fireTableDataChanged();
                getTable().setRowSelectionInterval(n, n);
                //getTable().scrollRectToVisible(getTable().getCellRect(n, 0, true));
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
        
    }

    private CobrancaFilter createFilter() {
        return new CobrancaFilter();
    }

    @Override
    public void cancelFilter() {
        setFilterObject(createFilter());
        doRefresh();
    }

    public void executeFilter(CobrancaFilter filtro) {
        try {
            getTableModel().select(filtro);
            setFilterObject(filtro);
            getTableModel().fireTableDataChanged();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        PgtoFilterPanel filterPanel = new PgtoFilterPanel();
        filterPanel.init();
        EditDialog edtDlg = new EditDialog(getBundle().getString("ctrlPgtosTitle"));
        edtDlg.setEditPanel(filterPanel);
        CobrancaFilter filtro = (CobrancaFilter)getFilterObject();

        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                getTableModel().fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    @Override
    public void viewRow() {
        PgtoCliente pgto = (PgtoCliente) getTableModel().getObject(getSelectedRow());
        Pedido pedido = pgto.getAtendimentoPedido().getPedido();
        String pedidoTitle = "Visualizar pedido";
        if (!TApplication.getInstance().locateFrame(pedidoTitle)) {
            PedidoViewFrame viewFrame = new PedidoViewFrame(pedidoTitle);
            TApplication.getInstance().getDesktopPane().add(viewFrame);
            viewFrame.setLocation(0, 0);
            viewFrame.setVisible(true);
            viewFrame.execute(pedido);
            viewFrame.setParentViewFrame(this);
        } else {
            PedidoViewFrame viewFrame = (PedidoViewFrame) TApplication.getInstance().getFrame(pedidoTitle);
            viewFrame.execute(pedido);
        }
    }

    @Override
    public void remove() {
    }

    @Override
    public void report() {
        Object[] options = {getBundle().getString("resumo"), getBundle().getString("detalhado"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        switch (n) {
            case 0:
                resumo();
                break;
            case 1:
                CobrancaFilter filter = (CobrancaFilter) getFilterObject();
                if (filter.getOrdem() == 2) {
                    reportClientes();
                } else {
                    super.report();
                }
                break;
        }
    }

    private void reportClientes() {
        CobrancaFilter filter = (CobrancaFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) getTableModel().getDao();
        try {
            List lista = getTableModel().getItemList();
            Reports.showReport(app.getResourceString("ctrlPgtosTitle"), filter.getTitle(), getClass().getResource(Constants.JRCTRLPGTOS2), lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void resumo() {
        CobrancaFilter filter = createFilter();
        PedidoDao pedidoDao = (PedidoDao) getTableModel().getDao();
        try {
            List lista = pedidoDao.findPgtosResumo(filter);
            Reports.showReport(app.getResourceString("ctrlPgtosTitle"), filter.getTitle(), getClass().getResource(Constants.JRCTRLPGTOSRESUMO), lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }
    
}
