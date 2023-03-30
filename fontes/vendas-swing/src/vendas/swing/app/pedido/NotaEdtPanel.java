/*
 * NotaEdtPanel.java
 *
 * Created on 30 de Novembro de 2007, 16:33
 */
package vendas.swing.app.pedido;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.NumericPlainDocument;
import ritual.swing.NumericTextField;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.entity.AtendimentoPedido;
import vendas.entity.ItemPedidoAtend;
import vendas.swing.core.EditPanel;
import vendas.swing.model.ItensNotaModel;
import ritual.util.DateVerifier;
import ritual.util.NumberUtils;
import vendas.dao.PedidoDao;
import vendas.entity.Transportador;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public class NotaEdtPanel extends EditPanel {

    DateVerifier dateVerifier = new DateVerifier();
    ItensNotaModel itensNotaModel;
    AtendimentoPedido atend;
    boolean isEditEnabled;

    /** Creates new form NotaEdtPanel */
    public NotaEdtPanel() {
        initComponents();
        itensTable.setSurrendersFocusOnKeystroke(true);
        itensTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        itensTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        isEditEnabled = true;
        DecimalFormat df = new DecimalFormat("#0.00");
        valorField.setDocument(new NumericPlainDocument(df));
        valorDescontoField.setDocument(new NumericPlainDocument(df));
        comissaoField.setDocument(new NumericPlainDocument(df));
        ipiField.setDocument(new NumericPlainDocument(df));
        subsTribField.setDocument(new NumericPlainDocument(df));
        seguroField.setDocument(new NumericPlainDocument(df));
        icmsField.setDocument(new NumericPlainDocument(df));
        freteField.setDocument(new NumericPlainDocument(df));
        

    }

    @Override
    public void object2Field(Object obj) {
        atend = (AtendimentoPedido) obj;
        dtNotaField.setDate(atend.getDtNota());
        dtFreteField.setDate(atend.getDtFrete());
        boletoFreteCheckBox.setSelected("1".equals(atend.getBoletoFrete()));
        if ((atend.getValorComissao() == null) || (atend.getValorComissao().compareTo(BigDecimal.ZERO) == 0)) {
            BigDecimal bd = new BigDecimal(0);
            BigDecimal bd100 = new BigDecimal(100);
            for (ItemPedidoAtend item : atend.getItens()) {
                BigDecimal bdItem = item.getQtd().multiply(item.getValor());
                bdItem = bdItem.divide(bd100).multiply(item.getPercComissao());
                bd = bd.add(bdItem);
            }
            atend.setValorComissao(bd);
        }
        obsTextArea.setText(atend.getObservacao());
        
        Transportador transp = atend.getPedido().getTransportador();
        
        transportadorTextField.setText(transp.getNome());
        fone1TranspTextField.setText(transp.getFone1());
        fone2TranspTextField.setText(transp.getFone2());

        itensNotaModel = new ItensNotaModel((List) atend.getItens());
        ItemPedidoAtend item;
        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        BigDecimal valor;
        
        for (Object o : itensNotaModel.getItemList()) {
            item = (ItemPedidoAtend)o;
            valor = pedidoDao.getTotalItemAtendido(item);
            if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
                valor = new BigDecimal(0);
            }
            item.setSaldo(valor);
        }
        
        if ("A".equals(atend.getPedido().getSituacao())) {
            itensNotaModel.setReadOnly(true);
        } else {
            itensNotaModel.setReadOnly(false);
        }
        itensTable.setModel(itensNotaModel);
        TableColumn col = itensTable.getColumnModel().getColumn(ItensNotaModel.DESCRICAO);
        col.setPreferredWidth(250);

        DecimalFormat df = NumberUtils.getDecimalFormat();
        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        NumericTextField vlField = new NumericTextField(8, df);
        vlField.setInputVerifier(new DateVerifier(true));
        col = itensTable.getColumnModel().getColumn(ItensNotaModel.QUANT);
        col.setPreferredWidth(80);
        DefaultCellEditor editor = new DefaultCellEditor(vlField);
        col.setCellEditor(editor);
        itensTable.getColumnModel().getColumn(ItensNotaModel.SALDO).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItensNotaModel.QUANT).setCellRenderer(new FractionCellRenderer(8, 3, SwingConstants.RIGHT));
        itensTable.getColumnModel().getColumn(ItensNotaModel.SALDO).setCellRenderer(new NotaColorCellRenderer(8, 3, SwingConstants.RIGHT));
        NumericTextField vlEmb = new NumericTextField(8, df);
        vlEmb.setInputVerifier(new DateVerifier(true));
        col = itensTable.getColumnModel().getColumn(ItensNotaModel.EMBALAGEM);
        col.setPreferredWidth(60);
        col.setCellEditor(editor);
        itensTable.getColumnModel().getColumn(ItensNotaModel.EMBALAGEM).setCellRenderer(new FractionCellRenderer(8, 3, SwingConstants.RIGHT));
        NumericTextField precoField = new NumericTextField(8, df);
        precoField.setInputVerifier(new DateVerifier(true));
        itensTable.getColumnModel().getColumn(ItensNotaModel.VALOR).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItensNotaModel.VALOR).setCellEditor(new DefaultCellEditor(precoField));
        notaField.setText(atend.getAtendimentoPedidoPK().getNf());
        emNomeField.setText(atend.getFornecedor());
        comissaoField.setValue(atend.getValorComissao());
        valorField.setValue(atend.getValor());
        valorDescontoField.setValue(atend.getValorDesconto());
        ipiField.setValue(atend.getValorIpi());
        subsTribField.setValue(atend.getSubsTrib());
        seguroField.setValue(atend.getValorSeguro());
        icmsField.setValue(atend.getValorIcms());
        freteField.setValue(atend.getValorFrete());
        motoristaTextField.setText(atend.getMotorista());
        foneTextField.setText(atend.getFoneMotorista());
        
        
    }

    public void setEditMode(boolean edit) {
        isEditEnabled = edit;
    }

    @Override
    public boolean entryValidate() {
        if (emNomeField.getText() == null || emNomeField.getText().length() == 0) {
            Messages.errorMessage("O campo 'Em nome de' deve ser preenchido.");
            return false;
        }
        return true;
    }

 

    @Override
    public void field2Object(Object obj) {
        if (!isEditEnabled) {
            return;
        }

        atend = (AtendimentoPedido) obj;

        try {
            if (dtNotaField.getDate() != null)
                atend.setDtNota(DateUtils.parse(DateUtils.format(dtNotaField.getDate())));
            if (dtFreteField.getDate() != null)
                atend.setDtFrete(DateUtils.parse(DateUtils.format(dtFreteField.getDate())));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }

        atend.setMotorista(motoristaTextField.getText());
        atend.setFoneMotorista(foneTextField.getText());
        atend.getAtendimentoPedidoPK().setNf(notaField.getText());
        atend.setValor(NumberUtils.getBigDecimal(valorField.getValue()));
        atend.setValorDesconto(NumberUtils.getBigDecimal(valorDescontoField.getValue()));
        atend.setFornecedor(emNomeField.getText());
        if (ipiField.getValue() != null) {
            atend.setValorIpi(NumberUtils.getBigDecimal(ipiField.getValue()));
        }
        if (subsTribField.getValue() != null) {
            atend.setSubsTrib(NumberUtils.getBigDecimal(subsTribField.getValue()));
        }
        if (seguroField.getValue() != null) {
            atend.setValorSeguro(NumberUtils.getBigDecimal(seguroField.getValue()));
        }
        if (freteField.getValue() != null) {
            atend.setValorFrete(NumberUtils.getBigDecimal(freteField.getValue()));
        }
        if (icmsField.getValue() != null) {
            atend.setValorIcms(NumberUtils.getBigDecimal(icmsField.getValue()));
        }
        if (comissaoField.getValue() != null) {
            atend.setValorComissao(NumberUtils.getBigDecimal(comissaoField.getValue()));
        }

        if (atendimentoCheckBox.isSelected()) {
            atend.getPedido().setSituacao("A");
            atend.getPedido().setAtendimento("A");
        } else {
            atend.getPedido().setSituacao("N");
            atend.getPedido().setAtendimento("P");
        }
        if (boletoFreteCheckBox.isSelected()) {
            atend.setBoletoFrete("1");
        } else {
            atend.setBoletoFrete("0");
        }
        
        atend.setObservacao(obsTextArea.getText());
        Collection<ItemPedidoAtend> lista = atend.getItens();
        BigDecimal bd = new BigDecimal(0);
        BigDecimal bd100 = new BigDecimal(100);
        for (ItemPedidoAtend item : lista) {
            item.getItemPedidoAtendPK().setNf(notaField.getText());
            BigDecimal bdItem = item.getQtd().multiply(item.getValor());
            bdItem = bdItem.divide(bd100).multiply(item.getPercComissao());
            bd = bd.add(bdItem);
        }
        if ((atend.getValorComissao() == null) || (atend.getValorComissao().compareTo(BigDecimal.ZERO) == 0)) {
            atend.setValorComissao(bd);
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
        notaField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        dtNotaField = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        atendimentoCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itensTable = new javax.swing.JTable();
        refreshItemButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        valorField = new javax.swing.JFormattedTextField();
        comissaoField = new javax.swing.JFormattedTextField();
        ipiField = new javax.swing.JFormattedTextField();
        seguroField = new javax.swing.JFormattedTextField();
        icmsField = new javax.swing.JFormattedTextField();
        freteField = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsTextArea = new javax.swing.JTextArea();
        dtFreteField = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        boletoFreteCheckBox = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        valorDescontoField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        emNomeField = new javax.swing.JFormattedTextField();
        subsTribField = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        motoristaTextField = new javax.swing.JTextField();
        foneTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        fone1TranspTextField = new javax.swing.JTextField();
        transportadorTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fone2TranspTextField = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("notaFiscal")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        notaField.setInputVerifier(dateVerifier);
        notaField.setName("notaField"); // NOI18N

        jLabel2.setText(bundle.getString("dtNota")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(NotaEdtPanel.class);
        dtNotaField.setDateFormatString(resourceMap.getString("dtNotaField.dateFormatString")); // NOI18N
        dtNotaField.setName("dtNotaField"); // NOI18N

        jLabel17.setText(bundle.getString("valor")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel15.setText(bundle.getString("comissao")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        atendimentoCheckBox.setText(bundle.getString("notaAtendePedido")); // NOI18N
        atendimentoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        atendimentoCheckBox.setName("atendimentoCheckBox"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Itens"));
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        itensTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itensTable.setName("itensTable"); // NOI18N
        jScrollPane1.setViewportView(itensTable);

        refreshItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Refresh16.gif"))); // NOI18N
        refreshItemButton.setName("refreshItemButton"); // NOI18N
        refreshItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshItemButtonActionPerformed(evt);
            }
        });

        deleteItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        deleteItemButton.setName("deleteItemButton"); // NOI18N
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteItemButton, 0, 0, Short.MAX_VALUE)
                    .addComponent(refreshItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(deleteItemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshItemButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setText(bundle.getString("ipi")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel16.setText(bundle.getString("seguro")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel19.setText(bundle.getString("icms")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        jLabel20.setText(bundle.getString("frete")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        valorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        valorField.setInputVerifier(dateVerifier);
        valorField.setName("valorField"); // NOI18N

        comissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        comissaoField.setInputVerifier(dateVerifier);
        comissaoField.setName("comissaoField"); // NOI18N

        ipiField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        ipiField.setInputVerifier(dateVerifier);
        ipiField.setName("ipiField"); // NOI18N
        ipiField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ipiFieldFocusLost(evt);
            }
        });

        seguroField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        seguroField.setInputVerifier(dateVerifier);
        seguroField.setName("seguroField"); // NOI18N
        seguroField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                seguroFieldFocusLost(evt);
            }
        });

        icmsField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        icmsField.setInputVerifier(dateVerifier);
        icmsField.setName("icmsField"); // NOI18N
        icmsField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                icmsFieldFocusLost(evt);
            }
        });

        freteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        freteField.setInputVerifier(dateVerifier);
        freteField.setName("freteField"); // NOI18N
        freteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                freteFieldFocusLost(evt);
            }
        });

        jLabel13.setText(bundle.getString("observacao")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        obsTextArea.setLineWrap(true);
        obsTextArea.setRows(5);
        obsTextArea.setName("obsTextArea"); // NOI18N
        jScrollPane2.setViewportView(obsTextArea);

        dtFreteField.setDateFormatString(resourceMap.getString("dtFreteField.dateFormatString")); // NOI18N
        dtFreteField.setName("dtFreteField"); // NOI18N

        jLabel3.setText(bundle.getString("dtFrete")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        boletoFreteCheckBox.setSelected(true);
        boletoFreteCheckBox.setText(bundle.getString("boletoFrete")); // NOI18N
        boletoFreteCheckBox.setName("boletoFreteCheckBox"); // NOI18N

        jLabel21.setText(bundle.getString("valorDesconto")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        valorDescontoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        valorDescontoField.setInputVerifier(dateVerifier);
        valorDescontoField.setName("valorDescontoField"); // NOI18N

        jLabel4.setText(bundle.getString("emNome")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        emNomeField.setInputVerifier(dateVerifier);
        emNomeField.setName("emNomeField"); // NOI18N

        subsTribField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        subsTribField.setInputVerifier(dateVerifier);
        subsTribField.setName("subsTribField"); // NOI18N
        subsTribField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                subsTribFieldFocusLost(evt);
            }
        });

        jLabel22.setText(bundle.getString("subsTrib")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel5.setText(bundle.getString("motorista")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        motoristaTextField.setText(resourceMap.getString("motoristaTextField.text")); // NOI18N
        motoristaTextField.setName("motoristaTextField"); // NOI18N

        foneTextField.setText(resourceMap.getString("foneTextField.text")); // NOI18N
        foneTextField.setName("foneTextField"); // NOI18N

        jLabel6.setText(bundle.getString("fone")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        fone1TranspTextField.setEditable(false);
        fone1TranspTextField.setText(resourceMap.getString("fone1TranspTextField.text")); // NOI18N
        fone1TranspTextField.setName("fone1TranspTextField"); // NOI18N

        transportadorTextField.setEditable(false);
        transportadorTextField.setText(resourceMap.getString("transportadorTextField.text")); // NOI18N
        transportadorTextField.setName("transportadorTextField"); // NOI18N

        jLabel7.setText(bundle.getString("transportador")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(bundle.getString("fone")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        fone2TranspTextField.setEditable(false);
        fone2TranspTextField.setText(resourceMap.getString("fone2TranspTextField.text")); // NOI18N
        fone2TranspTextField.setName("fone2TranspTextField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(notaField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(dtNotaField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(ipiField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(jLabel16))
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(seguroField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel19)
                                            .addComponent(icmsField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(jLabel20))
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(freteField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dtFreteField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel3)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel21)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(valorDescontoField, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(boletoFreteCheckBox))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(160, 160, 160)
                                        .addComponent(atendimentoCheckBox))
                                    .addComponent(jLabel15)))
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(emNomeField, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(subsTribField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(700, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(motoristaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(foneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(98, 98, 98)))
                        .addGap(231, 231, 231))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(transportadorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fone1TranspTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addGap(98, 98, 98)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fone2TranspTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jLabel15))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(notaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(atendimentoCheckBox)))
                    .addComponent(dtNotaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ipiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel3))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(seguroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(icmsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(freteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(valorDescontoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boletoFreteCheckBox)))
                    .addComponent(dtFreteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(motoristaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(foneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportadorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fone1TranspTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fone2TranspTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subsTribField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emNomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
    
    int[] rows = itensTable.getSelectedRows();

    List<Object> selectedObjects = new ArrayList<>();
    for (int row : rows) {
      selectedObjects.add(itensNotaModel.getObject(row));
    }
    for (Object obj : selectedObjects) {
      itensNotaModel.getItemList().remove(obj);
    }
    
    itensNotaModel.fireTableDataChanged();
}//GEN-LAST:event_deleteItemButtonActionPerformed

private void refreshItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshItemButtonActionPerformed
    // TODO add your handling code here:
    if (atend.getDtPgtoComissao() != null) {
        return;
    }
    BigDecimal bd = new BigDecimal(0);
    BigDecimal bd100 = new BigDecimal(100);
    List<ItemPedidoAtend> itens = itensNotaModel.getItemList();
    for (ItemPedidoAtend item : itens) {
        BigDecimal bdItem = item.getQtd().multiply(item.getValor());
        bdItem = bdItem.divide(bd100).multiply(item.getPercComissao());
        bd = bd.add(bdItem);
    }
    atend.setValorComissao(bd);
    comissaoField.setText(atend.getValorComissao().toString());
    comissaoField.invalidate();

}//GEN-LAST:event_refreshItemButtonActionPerformed

    private void deleteValue(java.awt.event.FocusEvent evt) {
        JFormattedTextField f = (JFormattedTextField) evt.getComponent();

        if (f.getText() == null || f.getText().length() == 0) {
            f.setValue(null);
        }

    }
private void ipiFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ipiFieldFocusLost
    deleteValue(evt);
}//GEN-LAST:event_ipiFieldFocusLost

private void seguroFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_seguroFieldFocusLost
    deleteValue(evt);
}//GEN-LAST:event_seguroFieldFocusLost

private void icmsFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_icmsFieldFocusLost
    deleteValue(evt);
}//GEN-LAST:event_icmsFieldFocusLost

private void freteFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_freteFieldFocusLost
    deleteValue(evt);
}//GEN-LAST:event_freteFieldFocusLost

private void subsTribFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_subsTribFieldFocusLost
    // TODO add your handling code here:
    deleteValue(evt);
}//GEN-LAST:event_subsTribFieldFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox atendimentoCheckBox;
    private javax.swing.JCheckBox boletoFreteCheckBox;
    private javax.swing.JFormattedTextField comissaoField;
    private javax.swing.JButton deleteItemButton;
    private com.toedter.calendar.JDateChooser dtFreteField;
    private com.toedter.calendar.JDateChooser dtNotaField;
    private javax.swing.JFormattedTextField emNomeField;
    private javax.swing.JTextField fone1TranspTextField;
    private javax.swing.JTextField fone2TranspTextField;
    private javax.swing.JTextField foneTextField;
    private javax.swing.JFormattedTextField freteField;
    private javax.swing.JFormattedTextField icmsField;
    private javax.swing.JFormattedTextField ipiField;
    private javax.swing.JTable itensTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField motoristaTextField;
    private javax.swing.JFormattedTextField notaField;
    private javax.swing.JTextArea obsTextArea;
    private javax.swing.JButton refreshItemButton;
    private javax.swing.JFormattedTextField seguroField;
    private javax.swing.JFormattedTextField subsTribField;
    private javax.swing.JTextField transportadorTextField;
    private javax.swing.JFormattedTextField valorDescontoField;
    private javax.swing.JFormattedTextField valorField;
    // End of variables declaration//GEN-END:variables

class NotaColorCellRenderer extends FractionCellRenderer {

        public NotaColorCellRenderer(int integer, int fraction, int align) {
            super(integer, fraction, align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
                    c.setForeground(Color.RED);

            return c;
        }

    }
}
