/*
 * OrcamentoEditPanel.java
 *
 * Created on 30 de Junho de 2007, 11:24
 */
package vendas.swing.app.pedido;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import ritual.swing.FractionCellRenderer;
import ritual.swing.ListComboBoxModel;
import ritual.swing.NumericPlainDocument;
import ritual.swing.NumericTextField;
import vendas.entity.Cliente;
import vendas.entity.Orcamento;
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.dao.ClienteDao;
import vendas.dao.FormaPgtoDao;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.dao.TransportadorDao;
import vendas.dao.VendedorDao;
import vendas.entity.ItemOrcamento;
import vendas.swing.core.EditPanel;
import vendas.swing.model.ItemOrcamentoModel;
import ritual.util.DateVerifier;
import ritual.util.NumberUtils;
import vendas.beans.PedidoBean;
import vendas.beans.ProdutoFilter;
import vendas.dao.FormaVendaDao;
import vendas.dao.OrcamentoDao;
import vendas.dao.ProdutoDao;
import vendas.dao.TipoPgtoDao;
import vendas.entity.FormaPgto;
import vendas.entity.ItemOrcamentoPK;
import vendas.entity.RepresProduto;
import vendas.entity.RepresProdutoPK;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public class OrcamentoEditPanel extends EditPanel {

    DateVerifier dateVerifier = new DateVerifier();
    JComboBox produtoComboBox = new JComboBox();
    boolean editMode = false;
    boolean updateItens = false;
    boolean updateVendedor = false;
    PedidoBean current;

    /** Creates new form OrcamentoEditPanel */
    public OrcamentoEditPanel() {
        super();
        initComponents();
        init();
        DecimalFormat df = new DecimalFormat("#0.00");
        comissaoVendedorField.setDocument(new NumericPlainDocument(df));
    }

    @Override
    public void enableControls(boolean isEditMode) {
        pedidoField.setFocusable(!isEditMode);
        emissaoField.setFocusable(!isEditMode);
        validadeField.setFocusable(!isEditMode);
    }

    public void updateProductList(Repres repres) {
        getLogger().info("updateProductList");
        ProdutoDao produtodao = new ProdutoDao();
        ProdutoFilter prodFilter = new ProdutoFilter();
        prodFilter.setRepres(repres);
        if (repres != null) {
            produtoComboBox.setModel(new ListComboBoxModel((List) produtodao.findProdutoRepres(prodFilter)));
            TableColumn col = itensTable.getColumnModel().getColumn(ItemOrcamentoModel.DESCRICAO);
            DefaultCellEditor editor = new DefaultCellEditor(produtoComboBox);
            col.setCellEditor(editor);
            
            if (updateItens) {
                MyItemModel model = (MyItemModel) itensTable.getModel();
                model.setItemList(new ArrayList(1));
                model.fireTableDataChanged();
            } else {
                updateItens = true;
            }
        }
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoBean bean = (PedidoBean) obj;
        Orcamento pedido = bean.getOrcamento();

        clienteComboBox.setSelectedItem(pedido.getCliente());

        emissaoField.setDate(pedido.getDtorcamento());
        entregaField.setDate(pedido.getDtEntrega());
        validadeField.setDate(pedido.getDtvalidade());
        obs2TextArea.setText(pedido.getObs2());
        obs3TextArea.setText(pedido.getObs3());
        pedidoField.setValue(pedido.getIdorcamento());
        represComboBox.setSelectedItem(pedido.getRepres());
        formaPgtoComboBox.setSelectedItem(pedido.getFormaPgto());
        vendedorComboBox.setSelectedItem(pedido.getVendedor());
        List<ItemOrcamento> itens = new ArrayList<ItemOrcamento>();
        for (ItemOrcamento itemOrcamento : pedido.getItems()) {
            ItemOrcamento novo = new ItemOrcamento();
            try {
                novo.setIpi(itemOrcamento.getIpi());
                ItemOrcamentoPK pk = new ItemOrcamentoPK();
                pk.setIdorcamento(itemOrcamento.getItemOrcamentoPK().getIdorcamento());
                pk.setIdproduto(itemOrcamento.getItemOrcamentoPK().getIdproduto());
                novo.setItemOrcamentoPK(pk);
                novo.setOrcamento(itemOrcamento.getOrcamento());
                novo.setProduto(itemOrcamento.getProduto());
                novo.setQtd(itemOrcamento.getQtd());
                novo.setSituacao(itemOrcamento.getSituacao());
                novo.setValorCliente(itemOrcamento.getValorCliente());
                novo.setEmbalagem(itemOrcamento.getEmbalagem());
                itens.add(novo);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        unidadesField.setValue(pedido.getVlrUnidades());

        totalField.setValue(pedido.getValor());
        MyItemModel itemModel = new MyItemModel(itens);
        itemModel.setReadOnly(false);
        itensTable.setModel(itemModel);
        updateProductList(pedido.getRepres());
        setColumnsEditor();
        
        clienteComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente rep = (Cliente) clienteComboBox.getSelectedItem();
                if (!rep.getVendedores().isEmpty())
                    vendedorComboBox.setSelectedItem(rep.getVendedores().get(0));
            }
        });
                
        represComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Repres rep = (Repres) represComboBox.getSelectedItem();
                obs3TextArea.setText(rep.getObservacaoCliente());
                entregaField.setDate(DateUtils.getNextDate(new Date(), rep.getDiasAtendimento()));
                updateProductList(rep);

            }
        });
        
        vendedorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
                if (vendedor == null)
                    return;
                if (represComboBox.getSelectedIndex() == -1) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                    return;
                }
//                if (updateVendedor) {
                VendedorDao dao = new VendedorDao();
                BigDecimal valComissao = dao.findComissao(vendedor, (Repres) represComboBox.getSelectedItem());
                if (valComissao == null) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                } else {
                    comissaoVendedorField.setValue(valComissao);
                }
//                } else {
//                    updateVendedor = true;
//                }
            }
        });

    }

    private void setColumnsEditor() {
        TableColumn col;
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

        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.DESCRICAO).setPreferredWidth(280);

        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.CODIGO).setPreferredWidth(70);

        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.UNIDADE).setPreferredWidth(36);

        DecimalFormat df = NumberUtils.getDecimalFormat();
        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        
        NumericTextField vlField = new NumericTextField(8, df);
        vlField.setInputVerifier(new DateVerifier(true));
        vlField.addFocusListener(focus);

        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.QUANT).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.QUANT).setCellEditor(new DefaultCellEditor(vlField));
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.QUANT).setCellRenderer(new FractionCellRenderer(8, 3, SwingConstants.RIGHT));

        NumericTextField precoField = new NumericTextField(8, df);
        precoField.setInputVerifier(new DateVerifier(true));
        precoField.addFocusListener(focus);
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.VALOR).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.VALOR).setCellEditor(new DefaultCellEditor(precoField));
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.VALOR).setCellRenderer(new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.TOTAL).setCellRenderer(new FractionCellRenderer(8, 2, SwingConstants.RIGHT));

        DecimalFormat dfe = NumberUtils.getDecimalFormat();
        dfe.setMinimumFractionDigits(4);
        dfe.setMaximumFractionDigits(4);
        NumericTextField embalagemField = new NumericTextField(8, dfe);
        embalagemField.setInputVerifier(new DateVerifier(true));
        embalagemField.addFocusListener(focus);
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.EMBALAGEM).setPreferredWidth(36);
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.EMBALAGEM).setCellEditor(new DefaultCellEditor(embalagemField));


        df = NumberUtils.getIntegerFormat();
        NumericTextField codigo = new NumericTextField(8, df);
        codigo.addFocusListener(focus);
        codigo.setInputVerifier(new DateVerifier(true));
        itensTable.getColumnModel().getColumn(ItemOrcamentoModel.CODIGO).setCellEditor(new DefaultCellEditor(codigo));

    }

    @Override
    public void field2Object(Object obj) {
        PedidoBean bean = (PedidoBean) obj;
        Orcamento pedido = bean.getOrcamento();

        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());

        try {
            pedido.setDtorcamento(DateUtils.parse(DateUtils.format(emissaoField.getDate())));
            pedido.setDtEntrega(DateUtils.parse(DateUtils.format(entregaField.getDate())));
            pedido.setDtvalidade(DateUtils.parse(DateUtils.format(validadeField.getDate())));
        } catch (Exception e) {
        }

        if (pedidoField.getText() != null && pedidoField.getText().length() > 0) {
            pedido.setIdorcamento(Integer.decode(pedidoField.getText()));
        }

        pedido.setObs2(obs2TextArea.getText());
        pedido.setObs3(obs3TextArea.getText());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        pedido.setFormaPgto((FormaPgto) formaPgtoComboBox.getModel().getSelectedItem());
        List<ItemOrcamento> itens = ((MyItemModel) itensTable.getModel()).getItemList();
        BigDecimal value = new BigDecimal(0);
        
        if (pedido.getIdorcamento() == null) {
            OrcamentoDao dao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");
            Integer id = dao.getNextValue();
            pedido.setIdorcamento(id);
        }
        
        for (ItemOrcamento item : itens) {
            item.setOrcamento(pedido);
            if (pedido.getIdorcamento() != null) {
                item.getItemOrcamentoPK().setIdorcamento(pedido.getIdorcamento());
            }
            value = value.add(item.getTotal());
        }
        pedido.setItems(null);
        pedido.setItems(itens);
        pedido.setValor(value);
    }

    @Override
    public boolean entryValidate() {
        List<ItemOrcamento> itens = ((MyItemModel) itensTable.getModel()).getItemList();
        return ((itens.size() > 0) && (itens.size() <= 15));
    }

    @Override
    public void init() {
        getLogger().info("init");
        ClienteDao clienteDao;
        FormaVendaDao formaVendaDao;
        FormaPgtoDao formaPgtoDao;
        TipoPgtoDao tipoPgtoDao;
        RepresDao represDao;
        TransportadorDao transportadorDao;
        VendedorDao vendedorDao;
        clienteDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        formaVendaDao = (FormaVendaDao) TApplication.getInstance().lookupService("formaVendaDao");
        formaPgtoDao = (FormaPgtoDao) TApplication.getInstance().lookupService("formaPgtoDao");
        tipoPgtoDao = (TipoPgtoDao) TApplication.getInstance().lookupService("tipoPgtoDao");
        represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        vendedorDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        transportadorDao = (TransportadorDao) TApplication.getInstance().lookupService("transportadorDao");

        itensTable.setRowSelectionAllowed(false);
        itensTable.setSurrendersFocusOnKeystroke(true);

        ///////////
        try {
            clienteComboBox.setModel(new ListComboBoxModel(clienteDao.findAllAtivos()));
            represComboBox.setModel(new ListComboBoxModel(represDao.findAll()));
            vendedorComboBox.setModel(new ListComboBoxModel(vendedorDao.findAllAtivos()));
            formaPgtoComboBox.setModel(new ListComboBoxModel(formaPgtoDao.findAll()));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pedidoField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        emissaoField = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        comissaoVendedorField = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itensTable = new javax.swing.JTable();
        newItemButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        unidadesField = new javax.swing.JFormattedTextField();
        totalField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        validadeField = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        formaPgtoComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        entregaField = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obs2TextArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        obs3TextArea = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("orcamento")); // NOI18N

        pedidoField.setEditable(false);
        pedidoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        pedidoField.setInputVerifier(dateVerifier);

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(OrcamentoEditPanel.class);
        emissaoField.setDateFormatString(resourceMap.getString("emissaoField.dateFormatString")); // NOI18N

        jLabel3.setText(bundle.getString("cliente")); // NOI18N

        clienteComboBox.setInputVerifier(dateVerifier);

        jLabel5.setText(bundle.getString("representada")); // NOI18N

        represComboBox.setInputVerifier(dateVerifier);

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        vendedorComboBox.setInputVerifier(dateVerifier);

        comissaoVendedorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Itens"));

        itensTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(itensTable);

        newItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N
        newItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemButtonActionPerformed(evt);
            }
        });

        deleteItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });

        jLabel16.setText(bundle.getString("unidades")); // NOI18N

        jLabel17.setText(bundle.getString("total")); // NOI18N

        unidadesField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        unidadesField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unidadesField.setInputVerifier(dateVerifier);

        totalField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalField.setInputVerifier(dateVerifier);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteItemButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(newItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(unidadesField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(newItemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteItemButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(unidadesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel4.setText(bundle.getString("dtValidade")); // NOI18N

        validadeField.setDateFormatString(resourceMap.getString("validadeField.dateFormatString")); // NOI18N

        jLabel6.setText(bundle.getString("formaPgto")); // NOI18N

        formaPgtoComboBox.setInputVerifier(dateVerifier);

        jLabel10.setText(bundle.getString("embarque")); // NOI18N

        jLabel14.setText(bundle.getString("observacao2")); // NOI18N

        obs2TextArea.setLineWrap(true);
        obs2TextArea.setRows(5);
        jScrollPane3.setViewportView(obs2TextArea);

        obs3TextArea.setLineWrap(true);
        obs3TextArea.setRows(5);
        jScrollPane4.setViewportView(obs3TextArea);

        jLabel21.setText(bundle.getString("obsCliente")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(validadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(formaPgtoComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 549, Short.MAX_VALUE)
                                        .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel21))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(24, 24, 24))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(2, 2, 2)
                        .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(2, 2, 2)
                        .addComponent(validadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formaPgtoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel5)
                        .addGap(3, 3, 3)
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void newItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemButtonActionPerformed
        Repres rep = (Repres) represComboBox.getSelectedItem();
        if (rep == null) {
            return;
        }

        MyItemModel itp = (MyItemModel) itensTable.getModel();
        if (itp.getItemList().size() == 20) {
            Messages.errorMessage("Número de itens está completo");
            return;
        }

        ItemOrcamento itemOrcamento = new ItemOrcamento();
        //itemOrcamento.setOrcamento(current);

        //itemOrcamento.setPercComissao((BigDecimal)comissaoField.getValue());
        //itemOrcamento.getItemOrcamentoPK().setIdOrcamento(Integer.decode(pedidoField.getText()));
        editMode = true;
        itp.addObject(itemOrcamento);
        //itp.fireTableDataChanged();
        int n = itp.getRowCount() - 1;
        itensTable.setRowSelectionInterval(n, n);
        itensTable.scrollRectToVisible(itensTable.getCellRect(n, 0, true));
    }//GEN-LAST:event_newItemButtonActionPerformed

    private class MyItemModel extends ItemOrcamentoModel {

        public MyItemModel() throws Exception {
            super();
        }

        public MyItemModel(List values) {
            super(values);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            RepresProduto rp = null;
            ProdutoDao produtodao = new ProdutoDao();
            ItemOrcamento itemPedido = (ItemOrcamento) getObject(row);
            
            if (column == CODIGO)
                super.setValueAt(Integer.decode((String) aValue), row, column);
            else
                super.setValueAt(aValue, row, column);
            
            switch (column) {
                case CODIGO:
                    try {
                        RepresProdutoPK pk = new RepresProdutoPK();
                        Repres r = (Repres) represComboBox.getModel().getSelectedItem();
                        pk.setIdProduto(Integer.decode((String) aValue));
                        pk.setIdRepres(r.getIdRepres());
                        rp = (RepresProduto) produtodao.findById(RepresProduto.class, pk);
                    } catch (Exception e) {
                        getLogger().error(e);
                    }
                    if (rp == null) {
                        Messages.errorMessage("Produto nï¿½o localizado");
                    } else {
                        itemPedido.setProduto(rp.getProduto());
                        itemPedido.getItemOrcamentoPK().setIdproduto(rp.getProduto().getIdProduto());
                        itemPedido.setValor(rp.getPreco());
                        BigDecimal valor = rp.getPrecoFinal();
                        if (valor == null || valor.intValue() == 0) {
                            valor = rp.getPreco();
                        }
                        itemPedido.setValorCliente(valor);
                        itemPedido.setPercComissao(rp.getPercComissao());
                        itemPedido.setIpi(rp.getIpi());
                        
                        if (rp.getEmbalagem() == null)
                            itemPedido.setQtd(BigDecimal.ZERO);
                        else
                            itemPedido.setQtd(rp.getEmbalagem());

                    }
                    break;
                case EMBALAGEM:
                    try {
                        RepresProdutoPK pk = new RepresProdutoPK();
                        Repres r = (Repres) represComboBox.getModel().getSelectedItem();
                        pk.setIdProduto(itemPedido.getItemOrcamentoPK().getIdproduto());
                        pk.setIdRepres(r.getIdRepres());
                        rp = (RepresProduto) produtodao.findById(RepresProduto.class, pk);
                    } catch (Exception e) {
                        getLogger().error(e);
                    }
                    if (rp == null) {
                        Messages.errorMessage("Produto nï¿½o localizado");
                    } else {
                        if (rp.getEmbalagem() != null) {
                            itemPedido.setQtd(rp.getEmbalagem().multiply(new BigDecimal((String)aValue)));
                        }
                    }
                    break;
                case QUANT:
                    break;
                case VALOR:
                    break;
                default:
                    return;
            }
            List<ItemOrcamento> o = getItemList();
            BigDecimal vlrUnidades = new BigDecimal(0);
            BigDecimal vlrCliente = new BigDecimal(0);
            BigDecimal vlr = new BigDecimal(0);
            for (ItemOrcamento item : o) {
                vlrUnidades = vlrUnidades.add(item.getQtd().multiply(item.getProduto().getFatorConversao()));
                vlr = vlr.add(item.getQtd().multiply(item.getValor()));
            }
            unidadesField.setValue(vlrUnidades);
            totalField.setValue(vlr);
        }
    }

    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
        int n = itensTable.getSelectedRow();
        if (n >= 0) {
            ItemOrcamentoModel itp = (ItemOrcamentoModel) itensTable.getModel();
            //ItemOrcamento item = (ItemOrcamento) itp.getObject(n);
            //itp.getItemList().remove(item);
            //itp.fireTableDataChanged();
            ItemOrcamento item = (ItemOrcamento) itp.getObject(n);
            itp.removeObject(n);

            //current.getItens().remove(item);
            int i = itp.getRowCount();
            if (i > 0) {
                if (n >= i) {
                    n--;
                }
                itensTable.setRowSelectionInterval(n, n);
                itensTable.scrollRectToVisible(itensTable.getCellRect(n, 0, true));
            }
        }
    }//GEN-LAST:event_deleteItemButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JFormattedTextField comissaoVendedorField;
    private javax.swing.JButton deleteItemButton;
    private com.toedter.calendar.JDateChooser emissaoField;
    private com.toedter.calendar.JDateChooser entregaField;
    private javax.swing.JComboBox formaPgtoComboBox;
    private javax.swing.JTable itensTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton newItemButton;
    private javax.swing.JTextArea obs2TextArea;
    private javax.swing.JTextArea obs3TextArea;
    private javax.swing.JFormattedTextField pedidoField;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JFormattedTextField totalField;
    private javax.swing.JFormattedTextField unidadesField;
    private com.toedter.calendar.JDateChooser validadeField;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
