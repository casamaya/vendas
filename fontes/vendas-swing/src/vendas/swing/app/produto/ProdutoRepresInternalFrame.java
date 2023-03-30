/*
 * ProdutoInternalFrame.java
 *
 * Created on 27/06/2007, 19:56:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.produto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import ritual.swing.FractionCellRenderer;
import ritual.swing.ListComboBoxModel;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.beans.ProdutoFilter;
import vendas.entity.Repres;
import vendas.entity.RepresProduto;
import vendas.dao.GrupoProdutoDao;
import vendas.dao.ProdutoDao;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.AlteraPreco;
import vendas.dao.UnidadeProdutoDao;
import vendas.entity.Produto;
import vendas.exception.DAOException;
import vendas.swing.model.ProdutoRepresTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ProdutoRepresInternalFrame extends TableViewFrame {

    private GrupoProdutoDao grupoDao;
    private UnidadeProdutoDao unidadeDao;
    private RepresDao represDao;
    private ProdutoDao produtoDao;
    private JComboBox represComboBox;
    private JButton tabelaButton;
    private JButton ipiButton;

    public ProdutoRepresInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Produtos por fornecedor", tableModel);
    }

    public ProdutoRepresInternalFrame() throws Exception {
        super("Produtos por fornecedor");
    }

    @Override
    public void view() {
        editRow();
    }

    private void tabela() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                        Repres repres = (Repres) represComboBox.getSelectedItem();

                final TabelaPrecoDialog dialog = new TabelaPrecoDialog(new javax.swing.JFrame(), true, repres);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    @Override
    public void initComponents() {
        super.initComponents();
        getLogger().info("initComponents ini");
        grupoDao = (GrupoProdutoDao) TApplication.getInstance().lookupService("grupoProdutoDao");
        unidadeDao = (UnidadeProdutoDao) TApplication.getInstance().lookupService("undProdutoDao");
        represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        produtoDao = (ProdutoDao) TApplication.getInstance().lookupService("produtoDao");
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel("Fornecedor");
        represComboBox = new JComboBox();
        tabelaButton = new JButton("Tabela");
        tabelaButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tabela();
            }
        });
        
        ipiButton = new javax.swing.JButton("Alterar IPI");
        ipiButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                alteraIPI();
            }


        });
        represComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                atualiza();
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(represComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 533, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabelaButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ipiButton)
                .addContainerGap(154, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(represComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(tabelaButton)
                    .add(ipiButton)
                )
                .addContainerGap(26, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        try {
            represComboBox.setModel(new ListComboBoxModel(represDao.findAll()));
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        ProdutoFilter filtro = new ProdutoFilter();
        Repres repres = (Repres) represComboBox.getSelectedItem();
        filtro.setRepres(repres);
        setFilterObject(filtro);
        getLogger().info("initComponents end");
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view();
                }
            }
        });

        if (TApplication.getInstance().getUser().isAdmin() || TApplication.getInstance().getUser().isEscritorio())
            createPopupMenu();
    }

    private void alteraIPI() {
        AtualizarIPIPanel editPanel = new AtualizarIPIPanel();
        EditDialog edtDlg = new EditDialog("Atualizar IPI");
        edtDlg.setEditPanel(editPanel);
        RepresProduto produto = new RepresProduto();
        Repres repres = (Repres) represComboBox.getSelectedItem();
        while (edtDlg.edit(produto)) {
            try {
                represDao.alteraIPI(repres, produto.getIpi());
                ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
                atualiza();
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
                
    @Override
    public void doRefresh() {
        try {
            Repres repres = (Repres) represComboBox.getSelectedItem();
            represComboBox.setModel(new ListComboBoxModel(represDao.findAll()));
            represComboBox.setSelectedItem(repres);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    private void atualiza() {
        getLogger().info("atualiza");
        Repres repres = (Repres) represComboBox.getSelectedItem();
        ProdutoFilter filtro = (ProdutoFilter) getFilterObject();
        filtro.setRepres(repres);
        List lista = produtoDao.findProdutoRepres(filtro);
        ProdutoRepresTableModel dataModel = new ProdutoRepresTableModel(lista);
        dataModel.setDao(produtoDao);
        JTable jt = getTable();
        jt.setModel(dataModel);
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
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

        TableColumn col = getTable().getColumnModel().getColumn(ProdutoRepresTableModel.DESCRICAO);
        col.setPreferredWidth(300);

        col = getColumn(ProdutoRepresTableModel.GRUPO);
        col.setPreferredWidth(160);


        jt.getColumnModel().getColumn(ProdutoRepresTableModel.PERC).setCellRenderer(new FractionCellRenderer(8, 2, SwingConstants.RIGHT));

        jt.getColumnModel().getColumn(ProdutoRepresTableModel.PRECO).setCellRenderer(new FractionCellRenderer(8, 2, SwingConstants.RIGHT));

        jt.getColumnModel().getColumn(ProdutoRepresTableModel.FATOR).setCellRenderer(new FractionCellRenderer(8, 4, SwingConstants.RIGHT));
        jt.getColumnModel().getColumn(ProdutoRepresTableModel.EMBALAGEM).setCellRenderer(new FractionCellRenderer(8, 4, SwingConstants.RIGHT));

        dataModel.fireTableDataChanged();
    }

    @Override
    protected void removeRow() {
        getLogger().info("removeRow");
        if (!Messages.deleteQuestion()) {
            return;
        }
        try {
            ProdutoRepresTableModel dataModel = (ProdutoRepresTableModel) getTable().getModel();

            dataModel.deleteRow(getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }

    }

    @Override
    public void filter() {
        getLogger().info("filter");
        ProdutoFilter filtro = new ProdutoFilter();
        ProdutoFilterPanel filterPanel = new ProdutoFilterPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("filterProdutoTitle"));
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

    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_PRODUTO_POR_FORNECEDOR"))
            super.insert();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_PRODUTO_POR_FORNECEDOR"))
            super.remove();
    }
    
    @Override
    public void insertRow() {
        getLogger().info("insertRow");
        
        RepresProduto represProduto = new RepresProduto();
        Repres repres = (Repres) represComboBox.getSelectedItem();
        represProduto.setRepres(repres);
        represProduto.getRepresProdutoPK().setIdRepres(repres.getIdRepres());
        insertProduto(represProduto);
    }
    
    private void insertProduto(RepresProduto represProduto) {
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
        ProdutoRepresEditPanel editPanel = new ProdutoRepresEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        try {
            while (edtDlg.edit(represProduto)) {
                try {
                    if (represProduto.getProduto().getIdProduto() == null) {
                        represProduto.getProduto().setIdProduto(produtoDao.getNextValue());
                    }
                    Produto produto = (Produto) produtoDao.findById(Produto.class, represProduto.getProduto().getIdProduto());
                    if (produto == null) {
                        if (TApplication.getInstance().getUser().isAdmin()) {
                            represProduto.getProduto().setBloqueado("N");
                        } else {
                            represProduto.getProduto().setBloqueado("S");
                        }
                        represProduto.getProduto().setBloqueado("S");
                        produtoDao.insertRecord(represProduto.getProduto());
                    } else {
                        produtoDao.updateRow(produto);
                    }
                    represProduto.getRepresProdutoPK().setIdProduto(represProduto.getProduto().getIdProduto());
                    RepresProduto rp = (RepresProduto) produtoDao.findById(RepresProduto.class, represProduto.getRepresProdutoPK());
                    if (rp == null) {
                        produtoDao.insertRecord(represProduto);
                    } else {
                        produtoDao.updateRow(rp);
                    }
                    ProdutoRepresTableModel dataModel = (ProdutoRepresTableModel) getTable().getModel();
                    dataModel.addObject(represProduto);
                    vtm.fireTableDataChanged();
                    //editProduto(represProduto);
                    break;
                } catch (Exception e) {
                    getLogger().error(getBundle().getString("saveErrorMessage"), e);
                    Messages.errorMessage(getBundle().getString("saveErrorMessage"));
                }
            }
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }

    private void editProduto(RepresProduto produto) {
        ProdutoRepresEditPanel editPanel = new ProdutoRepresEditPanel();
        editPanel.enableCode(false);
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }

        EditDialog edtDlg = new EditDialog(getBundle().getString("editProdutoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_PRODUTO_POR_FORNECEDOR"));
        
        while (edtDlg.edit(produto)) {
            try {
                produtoDao.updateProdutoRepres(produto);
                //produtoDao.updateRow(produto.getProduto());
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void editRow() {
        getLogger().info("editRow");
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
        RepresProduto produto = (RepresProduto) vtm.getObject(getTable().getSelectedRow());
        editProduto(produto);
        vtm.fireTableDataChanged();
    }

    @Override
    public void resetFilterObject() {
        ProdutoFilter filtro = new ProdutoFilter();
        Repres repres = (Repres) represComboBox.getSelectedItem();
        filtro.setRepres(repres);
        setFilterObject(filtro);
        doRefresh();
    }

    @Override
    public void report() {
        URL url = getClass().getResource(Constants.JRREPRESPRODUTO);
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
        List lista = vtm.getItemList();
        RepresProdutoDataSource ds = new RepresProdutoDataSource(lista);
        try {
            Reports.showReport(getTitle(), null, url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
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
        baixarMenuItem.setText(getBundle().getString("alterarPrecoTitle"));
        baixarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                alterarPreco(rows);

            }
        });
        baixarMenuItem.setEnabled(TApplication.getInstance().isGrant("ALTERAR_PRECO_PERCENTUAIS"));
        popupMenu.add(baixarMenuItem);
        
        JMenuItem ativarMenuItem = new JMenuItem();
        ativarMenuItem.setText("Ativar produto");
        ativarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                ativarProduto(rows, true);

            }
        });
        ativarMenuItem.setEnabled(TApplication.getInstance().isGrant("ATIVAR_PRODUTO"));
        popupMenu.add(ativarMenuItem);
        
        JMenuItem desativarMenuItem = new JMenuItem();
        desativarMenuItem.setText("Inativar produto");
        desativarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                ativarProduto(rows, false);

            }
        });
        desativarMenuItem.setEnabled(TApplication.getInstance().isGrant("INATIVAR_PRODUTO"));
        popupMenu.add(desativarMenuItem);
        
        JMenuItem igualarMenuItem = new JMenuItem();
        igualarMenuItem.setText("Igualar preço 1 e preço 2");
        igualarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                igualarPrecos(rows);
            }
        });
        igualarMenuItem.setEnabled(TApplication.getInstance().isGrant("IGUALAR_PRECO_1_PRECO_2"));
        popupMenu.add(igualarMenuItem);
        
        if (TApplication.getInstance().getUser().isAdmin()) {
        
        JMenuItem repetirMenuItem = new JMenuItem();
        repetirMenuItem.setText("Repetir produto");
        repetirMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }
                repetirProduto(rows);
            }
        });

        popupMenu.add(repetirMenuItem);
        
        }
        
        setPopupMenu(popupMenu);
    }
    
    private void repetirProduto(int rows[]) {
        getLogger().info("repetirProduto");
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) getTable().getModel();
        RepresProduto produto = (RepresProduto) vtm.getObject(getTable().getSelectedRow());
        RepresProduto novo = new RepresProduto();
        novo.setAtivado(produto.getAtivado());
        novo.setConversaoFrete(produto.getConversaoFrete());
        novo.setEmbalagem(produto.getEmbalagem());
        novo.setFrete(produto.getFrete());
        novo.setIpi(produto.getIpi());
        novo.setPercComissao(produto.getPercComissao());
        novo.setPreco(produto.getPreco());
        novo.setPreco2(produto.getPreco2());
        novo.setPrecoFinal(produto.getPrecoFinal());
        novo.setPrecov(produto.getPrecov());
        novo.setQtdUnd(produto.getQtdUnd());
        novo.setRepres(produto.getRepres());
        novo.getRepresProdutoPK().setIdRepres(produto.getRepresProdutoPK().getIdRepres());
        novo.setTabela(produto.getTabela());
        Produto item = new Produto();
        item.setDescricao(produto.getProduto().getDescricao());
        item.setBlob(produto.getProduto().getBlob());
        item.setFatorConversao(produto.getProduto().getFatorConversao());
        item.setFoto(produto.getProduto().getFoto());
        item.setGrupoProduto(produto.getProduto().getGrupoProduto());
        item.setPeso(produto.getProduto().getPeso());
        item.setSubGrupoProduto(produto.getProduto().getSubGrupoProduto());
        item.setUndCumulativa(produto.getProduto().getUndCumulativa());
        item.setUnidade(produto.getProduto().getUnidade());
        novo.setProduto(item);
        insertProduto(novo);
    }
    
    private void igualarPrecos(int rows[]) {
        getLogger().info("igualarPrecos");
        getLogger().info("alterarPreco");
        ProdutoRepresTableModel model = (ProdutoRepresTableModel)getTable().getModel();
        for (int row : rows) {
            RepresProduto p = (RepresProduto) model.getObject(row);
            p.setPreco2(p.getPreco());
            try {
                produtoDao.updateProdutoRepres(p);
            } catch (DAOException ex) {
                Logger.getLogger(ProdutoRepresInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
                
        }
        model.fireTableDataChanged();       
    }

    private void alterarPreco(int rows[]) {
        getLogger().info("alterarPreco");
        AlterarPrecoEditPanel editPanel = new AlterarPrecoEditPanel();
        AlteraPreco value = new AlteraPreco();
        Repres repres = (Repres) represComboBox.getSelectedItem();
        value.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("alterarPrecoTitle"));
        List<Integer> lista = new ArrayList<Integer>();
        ProdutoRepresTableModel model = (ProdutoRepresTableModel)getTable().getModel();

        for (int row : rows) {
            RepresProduto p = (RepresProduto) model.getObject(row);
            lista.add(p.getProduto().getIdProduto());
        }
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(value)) {
            try {
                boolean result = false;
                result = produtoDao.alterarPreco(value, lista);
                if (result) {
                    repres.setDataTabela(DateUtils.getDate());
                    represDao.atualizaTabela(repres.getDataTabela(), repres.getIdRepres());
                    refresh();
                }
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
    private void ativarProduto(int rows[], Boolean ativar) {
        getLogger().info("ativaProduto");
        ProdutoRepresTableModel model = (ProdutoRepresTableModel)getTable().getModel();
        for (int row : rows) {
            RepresProduto p = (RepresProduto) model.getObject(row);
            p.setAtivado(ativar);
            try {
                produtoDao.updateProdutoRepres(p);
            } catch (DAOException ex) {
                Logger.getLogger(ProdutoRepresInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
                
        }
        model.fireTableDataChanged();
    }
}
