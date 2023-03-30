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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import vendas.entity.ContaFluxo;
import vendas.entity.Movimento;
import vendas.exception.DAOException;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.MovimentoTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author sam
 */
public class FluxoInternalFrame extends TableViewFrame {

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

    public FluxoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Movimento financeiro", tableModel);
    }

    public FluxoInternalFrame() throws Exception {
        super("Movimento financeiro");
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
        JLabel jLabel1 = new JLabel("Conta");
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

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addConta();
            }
        });

        addButton.setEnabled(app.isGrant("INCLUIR_CONTA_MOVIMENTO"));

        edtButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                edtConta();
            }
        });

        edtButton.setEnabled(app.isGrant("ALTERAR_CONTA_MOVIMENTO"));

        delButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                delConta();
            }
        });

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
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel7)
                                                .addGap(9, 9, 9)
                                                .addComponent(creditoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel5)
                                                .addGap(12, 12, 12)
                                                .addComponent(debitoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel9)
                                                .addGap(9, 9, 9)
                                                .addComponent(creditoTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(debitoTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnResumo))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(contaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addButton)
                                                .addGap(6, 6, 6)
                                                .addComponent(edtButton)
                                                .addGap(6, 6, 6)
                                                .addComponent(delButton)
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saldoBancarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saldoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalGeralTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel1)
                                        .addComponent(contaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addButton)
                                        .addComponent(edtButton)
                                        .addComponent(delButton)
                                        .addComponent(jLabel2)
                                        .addComponent(saldoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(saldoBancarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(totalGeralTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                                .addComponent(jLabel6)
                                                                .addComponent(jLabel7)
                                                                .addComponent(creditoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(debitoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel8)
                                                                .addComponent(jLabel9)
                                                                .addComponent(creditoTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(debitoTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(2, 2, 2)
                                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnResumo)))
                                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        atualizaCombo();
        MovimentoFilter filtro = getInitFilter();
        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        filtro.setConta(conta);
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

    private void addConta() {
        ContaFluxo conta = new ContaFluxo();
        conta.setVendedor(fluxoDao.getVendedor(idVendedor));
        ContaEditPanel editPanel = new ContaEditPanel();
        EditDialog edtDlg = new EditDialog("Incluir conta");
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(conta)) {
            try {
                fluxoDao.insertRecord(conta);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

        atualizaCombo();
    }

    private void edtConta() {
        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        ContaEditPanel editPanel = new ContaEditPanel();
        EditDialog edtDlg = new EditDialog("Alterar conta");
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(conta)) {
            try {
                fluxoDao.updateRow(conta);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

        atualizaCombo();
    }

    @Override
    protected void editRow() {
        if (app.isGrant("ALTERAR_MOVIMENTO")) {
            super.editRow(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private void delConta() {
        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        if (conta != null) {
            try {
                if (!Messages.deleteQuestion()) {
                    return;
                }
                fluxoDao.deleteConta(conta);
                List<ContaFluxo> lista = fluxoDao.getContasFluxo(idVendedor);
                if (lista != null) {
                    contaComboBox.setModel(new ListComboBoxModel(lista));
                    contaComboBox.setSelectedItem(lista.get(0));
                    contaComboBox.invalidate();
                }
                atualizaCombo();
            } catch (Exception ex) {
                Logger.getLogger(FluxoInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                Messages.errorMessage("Falha ao excluir conta.");
            }
        }
    }

    @Override
    public void report() {
        MovimentoTableModel vtm = (MovimentoTableModel) getTable().getModel();
        List lista = vtm.getItemList();

        try {
            MovimentoFilter filtro = (MovimentoFilter) getFilterObject();
            Reports.showReport("Movimento financeiro", filtro.getTitle(), getClass().getResource(Constants.JRMOVIMENTO), lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    @Override
    public void doRefresh() {
        try {
            ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
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
            MovimentoTableModel model = (MovimentoTableModel) getTable().getModel();
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

        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        MovimentoFilter filtro = (MovimentoFilter) getFilterObject();
        filtro.setConta(conta);

        filtro.setVendedor(idVendedor);

        List<Movimento> lista = fluxoDao.findMovimento(filtro);
        setDtSaldo(lista);
        MovimentoTableModel dataModel = new MovimentoTableModel(lista);
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

        TableColumn col = getTable().getColumnModel().getColumn(MovimentoTableModel.DESCRICAO);
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

        getTable().getColumnModel().getColumn(MovimentoTableModel.OBS).setPreferredWidth(400);
        getTable().getColumnModel().getColumn(MovimentoTableModel.DTMOV).setCellRenderer(new DtCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoTableModel.DOC).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoTableModel.DESCRICAO).setCellRenderer(new ObsCellRenderer());

        getTable().getColumnModel().getColumn(MovimentoTableModel.GRUPO).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoTableModel.GRUPO).setPreferredWidth(120);
        getTable().getColumnModel().getColumn(MovimentoTableModel.GRUPO).setCellEditor(new DefaultCellEditor(grupoComboBox));

        getTable().getColumnModel().getColumn(MovimentoTableModel.TIPO).setCellRenderer(new ObsCellRenderer());
        getTable().getColumnModel().getColumn(MovimentoTableModel.OBS).setCellRenderer(new ObsCellRenderer());

        getTable().getColumnModel().getColumn(MovimentoTableModel.TIPO).setPreferredWidth(80);
        getTable().getColumnModel().getColumn(MovimentoTableModel.TIPO).setCellEditor(new DefaultCellEditor(combo));

        getTable().getColumnModel().getColumn(MovimentoTableModel.VALOR).setPreferredWidth(80);
        getTable().getColumnModel().getColumn(MovimentoTableModel.VALOR).setCellEditor(new DefaultCellEditor(precoField));
        getTable().getColumnModel().getColumn(MovimentoTableModel.VALOR).setCellRenderer(new ValorCellRenderer(8, 2, SwingConstants.RIGHT));
        getTable().getColumnModel().getColumn(MovimentoTableModel.SALDO).setPreferredWidth(80);
        getTable().getColumnModel().getColumn(MovimentoTableModel.SALDO).setCellRenderer(new ValorCellRenderer(8, 2, SwingConstants.RIGHT));

        getTable().getColumnModel().getColumn(MovimentoTableModel.DTMOV).setPreferredWidth(100);
        getTable().getColumnModel().getColumn(MovimentoTableModel.DTMOV).setCellEditor(new DateCellEditor());

        dataModel.fireTableDataChanged();

        if (conta != null) {
            atualizaSaldo(conta.getIdConta());
        }
        if ((getTable() != null) && (getTable().getRowCount() >= 0)) {
            getTable().scrollRectToVisible(getTable().getCellRect(0, 0, true));
        }

    }

    private void atualizaSaldo(Integer conta) {
        BigDecimal saldo;

        saldo = fluxoDao.getTotalSaldo(conta, idVendedor);
        saldoTextField.setValue(saldo);
        saldoTextField.invalidate();

        saldo = fluxoDao.getTotalSaldoBancario(idVendedor);
        saldoBancarioTextField.setValue(saldo);
        saldoBancarioTextField.invalidate();

        saldo = fluxoDao.getTotalGeral(idVendedor);
        totalGeralTextField.setValue(saldo);
        totalGeralTextField.invalidate();

        MovimentoFilter filter = (MovimentoFilter) getFilterObject();
        filter.getConta().setIdConta(conta);

        Movimento mov = fluxoDao.findResumoMovimento(filter);

        debitoTextField.setValue(mov.getDebito());
        creditoTextField.setValue(mov.getCredito());

        mov = fluxoDao.findResumoGeralMovimento(filter);

        debitoTotalTextField.setValue(mov.getDebito());
        creditoTotalTextField.setValue(mov.getCredito());

        debitoTextField.invalidate();
        creditoTextField.invalidate();
        debitoTotalTextField.invalidate();
        creditoTotalTextField.invalidate();
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        MovimentoFilter filtro = getInitFilter();
        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        filtro.setConta(conta);
        MovimentoFilterPanel filterPanel = new MovimentoFilterPanel();
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
            List<ContaFluxo> lista = fluxoDao.getContasFluxo(idVendedor);
            if (lista != null) {
                contaComboBox.setModel(new ListComboBoxModel(lista));
                contaComboBox.invalidate();
            }
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_MOVIMENTO")) {
            super.insert();
        }
    }

    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_MOVIMENTO")) {
            super.remove();
        }
    }

    @Override
    public void insertRow() {
        getLogger().info("insertRow");
        Movimento mov = new Movimento();
        ContaFluxo conta = (ContaFluxo) contaComboBox.getSelectedItem();
        mov.setContaFluxo(conta);
        mov.setVendedor(fluxoDao.getVendedor(idVendedor));
        
        try {
            fluxoDao.insertRecord(mov);
        } catch (DAOException ex) {
            Logger.getLogger(FluxoInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            Messages.errorMessage("Falha ao incluir movimento");
            return;
        }
        MovimentoTableModel mtb = (MovimentoTableModel) getTable().getModel();
        mtb.addObject(mov);
        int n = mtb.getRowCount() - 1;
        getTable().setRowSelectionInterval(n, n);
        getTable().scrollRectToVisible(getTable().getCellRect(n, 0, true));
    }

    @Override
    public void removeRow() {
        MovimentoTableModel mtb = (MovimentoTableModel) getTable().getModel();
        int n = getSelectedRow();
        try {
            mtb.deleteRow(n);
            if (n > 0) {
                n--;
                getTable().setRowSelectionInterval(n, n);
                getTable().scrollRectToVisible(getTable().getCellRect(n, 0, true));
            }
        } catch (Exception ex) {
            Logger.getLogger(FluxoInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            Messages.errorMessage("Falha ao excluir movimento");
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
            MovimentoTableModel model = (MovimentoTableModel) table.getModel();
            Movimento o = (Movimento) model.getObject(row);
            if ((o.getTipo().intValue() == 0) || (column == MovimentoTableModel.OBS)) {
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
            MovimentoTableModel model = (MovimentoTableModel) table.getModel();
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
            if (column == MovimentoTableModel.VALOR) {
                MovimentoTableModel model = (MovimentoTableModel) table.getModel();
                Movimento o = (Movimento) model.getObject(row);
                if (o.getTipo().intValue() == 0) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
            } else if (column == MovimentoTableModel.SALDO) {
                MovimentoTableModel model = (MovimentoTableModel) table.getModel();
                Movimento o = (Movimento) model.getObject(row);
                if (o.getDtMov().compareTo(dtSaldo) == 0) {
                    c.setForeground(Color.BLUE);
                } else {
                    c.setForeground(Color.BLACK);
                }
            }
            return c;
        }
    }

}
