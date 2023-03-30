/*
 * ProdutoEditPanel.java
 *
 * Created on 30 de Julho de 2007, 19:41
 */
package vendas.swing.app.produto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import javax.swing.JFormattedTextField;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.ListComboBoxModel;
import ritual.swing.NumericPlainDocument;
import ritual.swing.TApplication;
import vendas.entity.GrupoProduto;
import vendas.entity.Produto;
import vendas.entity.RepresProduto;
import vendas.entity.UnidadeProduto;
import vendas.swing.core.EditPanel;
import ritual.util.NumberUtils;
import vendas.dao.ProdutoDao;
import vendas.entity.SubGrupoProduto;

/**
 *
 * @author  Sam
 */
public class ProdutoRepresEditPanel extends EditPanel {

    /** Creates new form ProdutoEditPanel */
    public ProdutoRepresEditPanel() {
        initComponents();
        DecimalFormat df = new DecimalFormat("#0.00");
        comissaoField.setDocument(new NumericPlainDocument(df));
        ipiField.setDocument(new NumericPlainDocument(df));
        precoField.setDocument(new NumericPlainDocument(df));
        preco2Field.setDocument(new NumericPlainDocument(df));
        freteField.setDocument(new NumericPlainDocument(df));
        precoFinalField.setDocument(new NumericPlainDocument(df));
        qtdUndField.setDocument(new NumericPlainDocument(df));
        df = new DecimalFormat("#0.0000");
        fatorConversaoField.setDocument(new NumericPlainDocument(df));
        pesoField.setDocument(new NumericPlainDocument(df));
    }

    @Override
    public void object2Field(Object obj) {
        RepresProduto repres = (RepresProduto) obj;
        Produto produto = repres.getProduto();
        codigoField.setValue(produto.getIdProduto());
        descricaoField.setText(produto.getDescricao());
        unidadeRadioButton.setSelected("U".equals(repres.getConversaoFrete()));
        pesoRadioButton.setSelected("P".equals(repres.getConversaoFrete()));
        if (produto.getFatorConversao() != null) {
            fatorConversaoField.setValue(produto.getFatorConversao());
        }
        if (produto.getPeso() != null) {
            pesoField.setValue(produto.getPeso());
        } else {
            pesoField.setValue(BigDecimal.ONE);
        }
        grupoComboBox.setSelectedItem(produto.getGrupoProduto());
        grupoComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GrupoProduto grupo = (GrupoProduto) grupoComboBox.getSelectedItem();
                if (grupo != null) {
                    setSubGrupo(grupo.getSubGrupoProdutoList());
                }

            }
        });
        if (produto.getGrupoProduto() != null) {
            setSubGrupo(produto.getGrupoProduto().getSubGrupoProdutoList());
        }
        subGrupoComboBox.setSelectedItem(produto.getSubGrupoProduto());
        undCumulativaComboBox.setSelectedItem(produto.getUndCumulativa());
        unidadeComboBox.setSelectedItem(produto.getUnidade());
        ativoCheckBox.setSelected(repres.getAtivado());
        if (repres.getIpi() != null) {
            ipiField.setValue(repres.getIpi());
        }
        if (repres.getPreco() != null) {
            precoField.setValue(repres.getPreco());
        }
        if (repres.getPreco2() != null) {
            preco2Field.setValue(repres.getPreco2());
        }
        if (repres.getFrete() != null) {
            freteField.setValue(repres.getFrete());
        }
        if (repres.getPrecoFinal() != null) {
            precoFinalField.setValue(repres.getPrecoFinal());
        }
        if (repres.getQtdUnd() != null) {
            qtdUndField.setValue(repres.getQtdUnd());
        }
        if (repres.getEmbalagem() != null) {
            embalagemField.setValue(repres.getEmbalagem());
        }
        if (repres.getPercComissao() != null) {
            comissaoField.setValue(repres.getPercComissao());
        }
        if (unidadeRadioButton.isSelected()) {
            repres.setConversaoFrete("U");
        } else {
            repres.setConversaoFrete("P");

        }
    }

    @Override
    public void field2Object(Object obj) {
        RepresProduto repres = (RepresProduto) obj;
        Produto produto = repres.getProduto();

        ProdutoDao produtoDao = (ProdutoDao) TApplication.getInstance().lookupService("produtoDao");

        String codigo = codigoField.getText();
        if (codigo == null || codigo.length() == 0) {
            produto.setIdProduto(produtoDao.getNextValue());
        } else {
            produto.setIdProduto(Integer.decode(codigoField.getText()));
        }
        produto.setDescricao(descricaoField.getText());
        produto.setFatorConversao(NumberUtils.getBigDecimal(fatorConversaoField.getValue()));
        produto.setPeso(NumberUtils.getBigDecimal(pesoField.getValue()));
        if (produto.getPeso() == null)
            produto.setPeso(BigDecimal.ONE);
        produto.setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        produto.setSubGrupoProduto((SubGrupoProduto) subGrupoComboBox.getModel().getSelectedItem());
        produto.setUnidade((UnidadeProduto) unidadeComboBox.getModel().getSelectedItem());
        produto.setUndCumulativa((UnidadeProduto) undCumulativaComboBox.getModel().getSelectedItem());
        repres.getRepresProdutoPK().setIdProduto(produto.getIdProduto());
        repres.setAtivado(ativoCheckBox.isSelected());
        repres.setIpi(NumberUtils.getBigDecimal(ipiField.getValue()));
        repres.setPreco(NumberUtils.getBigDecimal(precoField.getValue()));
        repres.setPreco2(NumberUtils.getBigDecimal(preco2Field.getValue()));
        repres.setFrete(NumberUtils.getBigDecimal(freteField.getValue()));
        calculaPrecoFinal();
        repres.setPrecoFinal(NumberUtils.getBigDecimal(precoFinalField.getValue()));
        if (comissaoField.getText() != null && comissaoField.getText().length() > 0) {
            repres.setPercComissao(NumberUtils.getBigDecimal(comissaoField.getValue()));
        } else {
            repres.setPercComissao(BigDecimal.ZERO);
        }
        repres.setEmbalagem(NumberUtils.getBigDecimal(embalagemField.getValue()));
        repres.setQtdUnd(NumberUtils.getBigDecimal(qtdUndField.getValue()));
    }

    public void setUnidades(List lista) {
        unidadeComboBox.setModel(new ListComboBoxModel(lista));
        undCumulativaComboBox.setModel(new ListComboBoxModel(lista));
    }

    public void setGrupo(List lista) {
        grupoComboBox.setModel(new ListComboBoxModel(lista));
    }

    public void setSubGrupo(Collection lista) {
        subGrupoComboBox.setModel(new ListComboBoxModel((List) lista));
    }

    void enableCode(boolean b) {
        codigoField.setEnabled(b);
    }
    
    private void calculaPrecoFinal(BigDecimal preco2, BigDecimal ipi, BigDecimal frete) {
        if (preco2 == null)
            preco2 = BigDecimal.ZERO;
        if (ipi == null)
            ipi = BigDecimal.ZERO;
        if (frete == BigDecimal.ZERO)
            frete = BigDecimal.ZERO;
        precoFinalField.setValue(preco2.add(ipi).add(frete));
        precoFinalField.invalidate();
    }
    
    private void calculaPrecoFinal() {
        BigDecimal preco2 = NumberUtils.getBigDecimal(preco2Field.getValue());
        BigDecimal ipi = NumberUtils.getBigDecimal(ipiField.getValue());
        BigDecimal frete = NumberUtils.getBigDecimal(freteField.getValue());
        BigDecimal divisor = new BigDecimal(100);
        BigDecimal total;
        if (preco2 == null)
            preco2 = BigDecimal.ZERO;
        if (ipi == null)
            ipi = BigDecimal.ZERO;
        if (frete == BigDecimal.ZERO)
            frete = BigDecimal.ZERO;
        total = preco2.divide(divisor).multiply(ipi);
        total = total.add(frete).add(preco2);
        precoFinalField.setValue(total);
        precoFinalField.invalidate();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        codigoField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        descricaoField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        unidadeComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        undCumulativaComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ativoCheckBox = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        subGrupoComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        ipiField = new javax.swing.JFormattedTextField();
        precoField = new javax.swing.JFormattedTextField();
        qtdUndField = new javax.swing.JFormattedTextField();
        embalagemField = new javax.swing.JFormattedTextField();
        comissaoField = new javax.swing.JFormattedTextField();
        fatorConversaoField = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        pesoField = new javax.swing.JFormattedTextField();
        unidadeRadioButton = new javax.swing.JRadioButton();
        pesoRadioButton = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        preco2Field = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        precoFinalField = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        freteField = new javax.swing.JFormattedTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("codigo")); // NOI18N

        codigoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        codigoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                codigoFieldFocusLost(evt);
            }
        });

        jLabel2.setText(bundle.getString("descricao")); // NOI18N

        descricaoField.setDocument(new BoundedPlainDocument(100));

        jLabel3.setText(bundle.getString("unidade")); // NOI18N

        jLabel4.setText(bundle.getString("unidadeConversao")); // NOI18N

        jLabel5.setText(bundle.getString("fatorConversao")); // NOI18N

        jLabel6.setText(bundle.getString("grupo")); // NOI18N

        jLabel7.setText(bundle.getString("ipi")); // NOI18N

        jLabel9.setText(bundle.getString("preco")); // NOI18N

        jLabel10.setText(bundle.getString("qtdUnidade")); // NOI18N

        jLabel13.setText(bundle.getString("embalagem")); // NOI18N

        ativoCheckBox.setText(bundle.getString("ativo")); // NOI18N
        ativoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        jLabel14.setText(bundle.getString("percComissao")); // NOI18N

        jLabel8.setText(bundle.getString("subgrupo")); // NOI18N

        ipiField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        ipiField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ipiFieldFocusLost(evt);
            }
        });

        precoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        precoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                precoFieldFocusLost(evt);
            }
        });

        qtdUndField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        qtdUndField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                qtdUndFieldFocusLost(evt);
            }
        });

        embalagemField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        embalagemField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                embalagemFieldFocusLost(evt);
            }
        });

        comissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        fatorConversaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));

        jLabel11.setText(bundle.getString("peso")); // NOI18N

        pesoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));

        buttonGroup1.add(unidadeRadioButton);
        unidadeRadioButton.setSelected(true);
        unidadeRadioButton.setText(bundle.getString("unidade")); // NOI18N

        buttonGroup1.add(pesoRadioButton);
        pesoRadioButton.setText(bundle.getString("peso")); // NOI18N

        jLabel12.setText(bundle.getString("calcularFrete")); // NOI18N

        jLabel15.setText(bundle.getString("preco2")); // NOI18N

        preco2Field.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        preco2Field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                preco2FieldFocusLost(evt);
            }
        });

        jLabel16.setText(bundle.getString("precoFinal")); // NOI18N

        precoFinalField.setEditable(false);
        precoFinalField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        precoFinalField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                precoFinalFieldFocusLost(evt);
            }
        });

        jLabel17.setText(bundle.getString("frete")); // NOI18N

        freteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        freteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                freteFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(codigoField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(unidadeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4))
                            .addComponent(undCumulativaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(pesoField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(fatorConversaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(descricaoField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(unidadeRadioButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pesoRadioButton)))
                            .addGap(44, 44, 44))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(47, 47, 47))
                                .addComponent(ipiField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17)
                                .addComponent(freteField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(precoField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel15)
                                .addComponent(preco2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addComponent(precoFinalField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(95, 95, 95)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(qtdUndField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(embalagemField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ativoCheckBox)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codigoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descricaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unidadeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(undCumulativaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pesoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fatorConversaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(unidadeRadioButton)
                                    .addComponent(pesoRadioButton))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ipiField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(precoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(preco2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(precoFinalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel9))
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16))
                                .addGap(36, 36, 36))))
                    .addComponent(freteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(36, 36, 36)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(embalagemField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ativoCheckBox)
                            .addComponent(qtdUndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void codigoFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoFieldFocusLost
        // TODO add your handling code here:

        String s = codigoField.getText();
        if ((s == null) || (s.length() == 0)) {
            return;
        }
        ProdutoDao prd = new ProdutoDao();
        try {
            Produto produto = (Produto) prd.findById(Produto.class, Integer.decode(s));
            if (produto != null) {
                descricaoField.setText(produto.getDescricao());
                fatorConversaoField.setValue(produto.getFatorConversao());
                grupoComboBox.setSelectedItem(produto.getGrupoProduto());
                undCumulativaComboBox.setSelectedItem(produto.getUndCumulativa());
                unidadeComboBox.setSelectedItem(produto.getUnidade());
            } /*else {
            Messages.infoMessage("Produto n\u00E3o encontrado. O sistema irá gerar um código automático");
            codigoField.setValue(null);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_codigoFieldFocusLost

    private void ipiFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ipiFieldFocusLost
        JFormattedTextField value = (JFormattedTextField)evt.getSource();
        deleteValue(evt);        
        //calculaPrecoFinal(NumberUtils.getBigDecimal(preco2Field.getValue()), new BigDecimal(value.getText()), NumberUtils.getBigDecimal(freteField.getValue()));
    }//GEN-LAST:event_ipiFieldFocusLost

    private void precoFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_precoFieldFocusLost
        deleteValue(evt);
    }//GEN-LAST:event_precoFieldFocusLost

    private void qtdUndFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_qtdUndFieldFocusLost
        deleteValue(evt);
    }//GEN-LAST:event_qtdUndFieldFocusLost

    private void embalagemFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_embalagemFieldFocusLost
        deleteValue(evt);
    }//GEN-LAST:event_embalagemFieldFocusLost

    private void preco2FieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_preco2FieldFocusLost
        deleteValue(evt);
        JFormattedTextField value = (JFormattedTextField)evt.getSource();
        //calculaPrecoFinal(new BigDecimal(value.getText()), NumberUtils.getBigDecimal(ipiField.getValue()), NumberUtils.getBigDecimal(freteField.getValue()));
    }//GEN-LAST:event_preco2FieldFocusLost

    private void precoFinalFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_precoFinalFieldFocusLost
        //deleteValue(evt);
    }//GEN-LAST:event_precoFinalFieldFocusLost

    private void freteFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_freteFieldFocusLost
        deleteValue(evt);
        JFormattedTextField value = (JFormattedTextField)evt.getSource();
        //calculaPrecoFinal(NumberUtils.getBigDecimal(preco2Field.getValue()), NumberUtils.getBigDecimal(ipiField.getValue()), new BigDecimal(value.getText()));
    }//GEN-LAST:event_freteFieldFocusLost

    private void deleteValue(java.awt.event.FocusEvent evt) {
        JFormattedTextField f = (JFormattedTextField) evt.getComponent();
        if (f.getText() == null || f.getText().length() == 0) {
            f.setValue(null);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ativoCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField codigoField;
    private javax.swing.JFormattedTextField comissaoField;
    private javax.swing.JFormattedTextField descricaoField;
    private javax.swing.JFormattedTextField embalagemField;
    private javax.swing.JFormattedTextField fatorConversaoField;
    private javax.swing.JFormattedTextField freteField;
    private javax.swing.JComboBox grupoComboBox;
    private javax.swing.JFormattedTextField ipiField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JFormattedTextField pesoField;
    private javax.swing.JRadioButton pesoRadioButton;
    private javax.swing.JFormattedTextField preco2Field;
    private javax.swing.JFormattedTextField precoField;
    private javax.swing.JFormattedTextField precoFinalField;
    private javax.swing.JFormattedTextField qtdUndField;
    private javax.swing.JComboBox subGrupoComboBox;
    private javax.swing.JComboBox undCumulativaComboBox;
    private javax.swing.JComboBox unidadeComboBox;
    private javax.swing.JRadioButton unidadeRadioButton;
    // End of variables declaration//GEN-END:variables
}
