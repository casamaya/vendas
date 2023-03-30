/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import ritual.swing.DateCellEditor;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.ListComboBoxModel;
import ritual.swing.NumericTextField;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import ritual.util.DateVerifier;
import ritual.util.NumberUtils;
import vendas.beans.MovimentoFilter;
import vendas.dao.FluxoDao;
import vendas.dao.GrupoMovimentoDao;
import vendas.entity.GrupoMovimento;
import vendas.entity.Movimento;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.MovimentoGrupoTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author sam
 */
public class MovimentoGrupoInternalFrame extends TableViewFrame {

    private FluxoDao fluxoDao;
    private JComboBox contaComboBox;
    private JButton addButton;
    private JButton edtButton;
    private JButton delButton;
    private JButton btnResumo;
    private Date dtSaldo;
    private JFormattedTextField saldoTextField = new JFormattedTextField();
    private JFormattedTextField saldoBancarioTextField = new JFormattedTextField();
    private JFormattedTextField totalGeralTextField = new JFormattedTextField();
    private JFormattedTextField creditoTextField;
    private JFormattedTextField debitoTextField;
    private JFormattedTextField creditoTotalTextField;
    private JFormattedTextField debitoTotalTextField;
    private TApplication app;
    private Integer idVendedor;

    public MovimentoGrupoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Movimento por grupo", tableModel);
    }

    public MovimentoGrupoInternalFrame() throws Exception {
        super("Movimento por grupo");
    }

    @Override
    public void view() {
        editRow();
    }

    @Override
    public void initComponents() {
        super.initComponents();
        app = TApplication.getInstance();
        idVendedor = app.getUser().getIdvendedor();
        getLogger().info("initComponents ini");
        fluxoDao = (FluxoDao) TApplication.getInstance().lookupService("fluxoDao");
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel("Grupo");
        JLabel jLabel2 = new JLabel("Saldo bancário");
        JLabel jLabel3 = new JLabel("Saldo");
        JLabel jLabel4 = new JLabel("Total geral");
        JLabel jLabel5 = new JLabel("Débito");
        JLabel jLabel6 = new JLabel("Totais:");
        JLabel jLabel7 = new JLabel("Crédito");
        JLabel jLabel8 = new JLabel("Total geral");
        JLabel jLabel9 = new JLabel("Crédito");
        JLabel jLabel10 = new JLabel("Débito");
        saldoTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldoTextField.setFocusable(false);
        saldoTextField.setColumns(12);
        saldoTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalGeralTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalGeralTextField.setFocusable(false);
        totalGeralTextField.setColumns(12);
        totalGeralTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldoBancarioTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldoBancarioTextField.setFocusable(false);
        saldoBancarioTextField.setColumns(12);
        saldoBancarioTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditoTextField = new javax.swing.JFormattedTextField();
        debitoTextField = new javax.swing.JFormattedTextField();
        creditoTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        creditoTextField.setFocusable(false);
        creditoTextField.setColumns(12);
        debitoTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        debitoTextField.setFocusable(false);
        debitoTextField.setColumns(12);
        creditoTotalTextField = new javax.swing.JFormattedTextField();
        debitoTotalTextField = new javax.swing.JFormattedTextField();
        creditoTotalTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        creditoTotalTextField.setFocusable(false);
        creditoTotalTextField.setColumns(12);
        creditoTotalTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        debitoTotalTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        debitoTotalTextField.setFocusable(false);
        debitoTotalTextField.setColumns(12);
        debitoTotalTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        contaComboBox = new JComboBox();
        contaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualiza();
            }
        });

        addButton = new JButton();
        addButton.setToolTipText("Incluir uma nova conta");
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N

        edtButton = new JButton();
        edtButton.setToolTipText("Alterar uma conta");
        edtButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Edit16.gif"))); // NOI18N

        delButton = new JButton();
        delButton.setToolTipText("Excluir uma conta");
        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N

        addButton.setEnabled(app.isGrant("INCLUIR_CONTA_MOVIMENTO"));

        edtButton.setEnabled(app.isGrant("ALTERAR_CONTA_MOVIMENTO"));

        delButton.setEnabled(app.isGrant("EXCLUIR_CONTA_MOVIMENTO"));

        btnResumo = new JButton();
        btnResumo.setText("Resumo"); // NOI18N
        btnResumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResumoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(9, 9, 9)
                .addComponent(creditoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addComponent(debitoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(370, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(contaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(creditoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(debitoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        atualizaCombo();
        MovimentoFilter filtro = getInitFilter();
        GrupoMovimento conta = (GrupoMovimento) contaComboBox.getSelectedItem();
        filtro.setGrupo(conta);
        setFilterObject(filtro);
        getLogger().info("initComponents end");
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        getTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view();
                }
            }
        });
        boolean valido = (TApplication.getInstance().getUser().isAdmin() || TApplication.getInstance().getUser().isEscritorio());
        if (valido) {
            createPopupMenu();
        }

        setSize(app.getDesktopPane().getBounds().width, app.getDesktopPane().getBounds().height);

    }

    private void btnResumoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // resumo
        ResumoMovimentoPanel panel = new ResumoMovimentoPanel();
        
        EditDialog edtDlg = new EditDialog("Resumo da movimenta\u00E7\u00E3o");
        panel.setEdtDlg(edtDlg);

        edtDlg.setEditPanel(panel, true);
        edtDlg.edit((MovimentoFilter)getFilterObject(), false);
    }

    @Override
    public void report() {
        MovimentoGrupoTableModel vtm = (MovimentoGrupoTableModel) getTable().getModel();
        List lista = vtm.getItemList();

        try {
            MovimentoFilter filtro = (MovimentoFilter) getFilterObject();
            Reports.showReport("Movimento financeiro", filtro.getTitle(), getClass().getResource(Constants.JRMOVIMENTOGRUPO), lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    @Override
    public void doRefresh() {
        try {
            GrupoMovimento conta = (GrupoMovimento) contaComboBox.getSelectedItem();
            atualizaCombo();
            contaComboBox.setSelectedItem(conta);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
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
        baixarMenuItem.setText(getBundle().getString("repetirLancamento"));
        baixarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                repetirLancamento();
            }
        });
        popupMenu.add(baixarMenuItem);
        setPopupMenu(popupMenu);
    }

    private void repetirLancamento() {
        String[] options = {getBundle().getString("repetir"), getBundle().getString("cancelar")};
        int month;
        RepetirPanel panel = new RepetirPanel();
        if (JOptionPane.showOptionDialog(this, panel, getBundle().getString("repetirLancamento"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == 0) {
            month = ((Integer) panel.getValue());
            List<Movimento> lista = new ArrayList<>();
            int rows[] = getTable().getSelectedRows();
            MovimentoGrupoTableModel model = (MovimentoGrupoTableModel) getTable().getModel();
            Movimento mov;
            for (int i : rows) {
                mov = (Movimento) model.getObject(i);
                lista.add(mov);
            }
            try {
                fluxoDao.exportarLancamento(lista, month);
                refresh();
            } catch (Exception e) {
                getLogger().error(getBundle().getString("exportError"), e);
                JOptionPane.showMessageDialog(this, getBundle().getString("exportError"), getBundle().getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private MovimentoFilter getInitFilter() {
        Date dtAtual = DateUtils.parse(DateUtils.format(new Date()));
        Date dtIni = DateUtils.getNextDate(dtAtual, -5);
        //Date dtEnd = DateUtils.getNextDate(dtAtual, 60);

        MovimentoFilter filtro = new MovimentoFilter();
        filtro.setDtInicio(dtIni);
        //filtro.setDtTermino(dtEnd);
        filtro.setVendedor(idVendedor);
        filtro.setGrupo(null);

        return filtro;
    }

    @Override
    public void cancelFilter() {

        setFilterObject(getInitFilter());
        atualiza();
    }

    private void atualiza() {
        getLogger().info("atualiza");

        DecimalFormat df = NumberUtils.getDecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        GrupoMovimento conta = (GrupoMovimento) contaComboBox.getSelectedItem();
        MovimentoFilter filtro = (MovimentoFilter) getFilterObject();
        filtro.setGrupo(conta);

        filtro.setVendedor(idVendedor);

        List<Movimento> lista = fluxoDao.findMovimento(filtro);
        setDtSaldo(lista);
        MovimentoGrupoTableModel dataModel = new MovimentoGrupoTableModel(lista);
        dataModel.setDao(fluxoDao);
        JTable jt = getTable();
        jt.setModel(dataModel);
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

        FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                String t = ((NumericTextField) e.getSource()).getText();
                if (t != null && t.length() > 0) {

                    ((NumericTextField) e.getSource()).setText(t.substring(t.length() - 1));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        };

        NumericTextField precoField = new NumericTextField(8, df);
        precoField.setInputVerifier(new DateVerifier(true));
        precoField.addFocusListener(focus);

        TableColumn col = getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DESCRICAO);
        col.setPreferredWidth(400);
        String[] itens = {"Débito", "Credito"};
        JComboBox combo = new JComboBox(itens);
        JComboBox grupoComboBox = new JComboBox();
        GrupoMovimentoDao grupoDao = new GrupoMovimentoDao();
        
        try {
            grupoComboBox.setModel(new ListComboBoxModel(grupoDao.findByExample(idVendedor)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.OBS).setPreferredWidth(400);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.CONTA).setPreferredWidth(400);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DTMOV).setCellRenderer(new DtCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DOC).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DESCRICAO).setCellRenderer(new ObsCellRenderer());

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.GRUPO).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.GRUPO).setPreferredWidth(120);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.GRUPO).setCellEditor(new DefaultCellEditor(grupoComboBox));

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.TIPO).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.OBS).setCellRenderer(new ObsCellRenderer());

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.TIPO).setPreferredWidth(80);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.TIPO).setCellEditor(new DefaultCellEditor(combo));

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.VALOR).setPreferredWidth(80);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.VALOR).setCellEditor(new DefaultCellEditor(precoField));
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.VALOR).setCellRenderer(new ValorCellRenderer(8, 2, SwingConstants.RIGHT));

        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DTMOV).setPreferredWidth(100);
        getTable().getColumnModel().getColumn(MovimentoGrupoTableModel.DTMOV).setCellEditor(new DateCellEditor());

        dataModel.fireTableDataChanged();
        
        if (conta != null) {
            atualizaSaldo(conta.getIdgrupo());
        }

        if ((getTable() != null) && (getTable().getRowCount() >= 0)) {
            getTable().scrollRectToVisible(getTable().getCellRect(0, 0, true));
        }

    }

    private void atualizaSaldo(Integer conta) {
        BigDecimal saldo;

        MovimentoFilter filter = (MovimentoFilter) getFilterObject();
        filter.getGrupo().setIdgrupo(conta);

        Movimento mov = fluxoDao.findResumoMovimento(filter);

        debitoTextField.setValue(mov.getDebito());
        creditoTextField.setValue(mov.getCredito());

        debitoTextField.invalidate();
        creditoTextField.invalidate();
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        
        MovimentoFilter filtro = getInitFilter();
        GrupoMovimento conta = (GrupoMovimento) contaComboBox.getSelectedItem();
        filtro.setGrupo(conta);
        MovimentoFilterPanel filterPanel = new MovimentoFilterPanel();
        filterPanel.habilitaGrupo(false);
        
        EditDialog edtDlg = new EditDialog("Filtrar");
        edtDlg.setEditPanel(filterPanel);
        
        while (edtDlg.edit(filtro)) {
            try {
                setFilterObject(filtro);
                doRefresh();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    private void atualizaCombo() {
        try {
            GrupoMovimentoDao dao = new GrupoMovimentoDao();
            List<GrupoMovimento> lista = dao.findByExample(idVendedor);
            if (lista != null) {
                contaComboBox.setModel(new ListComboBoxModel(lista));
                contaComboBox.invalidate();
            }
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }
    
    private void setDtSaldo(List<Movimento> lista) {
        Date crdt = DateUtils.getNextDate(DateUtils.getCurrentDate(), -1);
        dtSaldo = null;
        Date dt;
        for (Movimento m : lista) {
            dt = m.getDtMov();
            if (dt.compareTo(crdt) == 0) {
                dtSaldo = dt;
                break;
            } else if (dt.compareTo(crdt) < 0) {
                dtSaldo = dt;
            } else {
                break;
            }
        }
        if (dtSaldo == null) {
            dtSaldo = crdt;
        }
    }

    class ObsCellRenderer extends DefaultTableCellRenderer {

        public ObsCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            MovimentoGrupoTableModel model = (MovimentoGrupoTableModel) table.getModel();
            Movimento o = (Movimento) model.getObject(row);
            if ((o.getTipo().intValue() == 0) || (column == MovimentoGrupoTableModel.OBS)) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
            return c;
        }

    }

    class DtCellRenderer extends DateCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            MovimentoGrupoTableModel model = (MovimentoGrupoTableModel) table.getModel();
            Movimento o = (Movimento) model.getObject(row);
            if (o.getTipo().intValue() == 0) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
            return c;
        }

    }

    class ValorCellRenderer extends FractionCellRenderer {

        public ValorCellRenderer(int integer, int fraction, int align) {
            super(integer, fraction, align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column == MovimentoGrupoTableModel.VALOR) {
                MovimentoGrupoTableModel model = (MovimentoGrupoTableModel) table.getModel();
                Movimento o = (Movimento) model.getObject(row);
                if (o.getTipo().intValue() == 0) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
}
