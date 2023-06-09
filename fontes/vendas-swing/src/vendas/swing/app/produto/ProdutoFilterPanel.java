/*
 * ProdutoFilterPanel.java
 *
 * Created on 30 de Julho de 2007, 20:06
 */
package vendas.swing.app.produto;

import vendas.beans.ProdutoFilter;
import vendas.entity.GrupoProduto;
import vendas.entity.UnidadeProduto;
import vendas.swing.core.EditPanel;
import ritual.swing.BoundedPlainDocument;
import vendas.entity.SubGrupoProduto;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class ProdutoFilterPanel extends EditPanel {

    /** Creates new form ProdutoFilterPanel */
    public ProdutoFilterPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        ProdutoFilter filter = (ProdutoFilter) obj;
        codigoField.setValue(filter.getProduto().getIdProduto());
        descricaoField.setText(filter.getProduto().getDescricao());
        grupoComboBox.setSelectedItem(filter.getProduto().getGrupoProduto());
        unidadeComboBox.setSelectedItem(filter.getProduto().getUnidade());
        inativoCheckBox.setSelected(filter.isInativo());
        codigoRadioButton.setSelected(filter.getOrder() == ProdutoFilter.ORDER_CODIGO);
        descricaoRadioButton.setSelected(filter.getOrder() == ProdutoFilter.ORDER_DESCRICAO);
        unidadeComboBox.setModel(VendasUtil.getUndProdutoListModel());
        grupoComboBox.setModel(VendasUtil.getGrupoProdutoListModel());
        bloqueadoCheckBox.setSelected(Boolean.FALSE);
    }

    @Override
    public void field2Object(Object obj) {
        ProdutoFilter filter = (ProdutoFilter) obj;
        String codigo = codigoField.getText();
        
        if ((codigo == null) || (codigo.length() == 0)) {
            filter.getProduto().setIdProduto(null);
        } else {
            filter.getProduto().setIdProduto(Integer.decode(codigoField.getText()));
        }
        
        filter.getProduto().setDescricao(descricaoField.getText());
        filter.getProduto().setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        SubGrupoProduto subGrupo = (SubGrupoProduto) subGrupoComboBox.getModel().getSelectedItem();
        filter.getProduto().setSubGrupoProduto(subGrupo);
        
        if ((subGrupo != null) && (subGrupo.getIdCodSubGrupo() != null)) {
            filter.setTitle("Sub-grupo " + subGrupo.getNomeGrupo());
        }
        
        filter.getProduto().setBloqueado(bloqueadoCheckBox.isSelected());
        filter.getProduto().setUnidade((UnidadeProduto) unidadeComboBox.getModel().getSelectedItem());
        filter.setInativo(inativoCheckBox.isSelected());
        
        if (descricaoRadioButton.isSelected()) {
            filter.setOrder(1);
        } else {
            filter.setOrder(0);
        }
    }

    public void setSelectInativos(boolean value) {
        inativoCheckBox.setVisible(value);
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
        jLabel6 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        subGrupoComboBox = new javax.swing.JComboBox();
        inativoCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        codigoRadioButton = new javax.swing.JRadioButton();
        descricaoRadioButton = new javax.swing.JRadioButton();
        bloqueadoCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("codigo")); // NOI18N

        codigoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel2.setText(bundle.getString("descricao")); // NOI18N

        descricaoField.setDocument(new BoundedPlainDocument(40));

        jLabel3.setText(bundle.getString("unidade")); // NOI18N

        jLabel6.setText(bundle.getString("grupo")); // NOI18N

        grupoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupoComboBoxActionPerformed(evt);
            }
        });

        jLabel15.setText(bundle.getString("subgrupo")); // NOI18N

        inativoCheckBox.setText(bundle.getString("produtosInativos")); // NOI18N
        inativoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        inativoCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N

        buttonGroup1.add(codigoRadioButton);
        codigoRadioButton.setText(bundle.getString("codigo")); // NOI18N
        codigoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        codigoRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(descricaoRadioButton);
        descricaoRadioButton.setSelected(true);
        descricaoRadioButton.setText(bundle.getString("descricao")); // NOI18N
        descricaoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        descricaoRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codigoRadioButton)
                    .addComponent(descricaoRadioButton))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(codigoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descricaoRadioButton))
        );

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/resources/Main"); // NOI18N
        bloqueadoCheckBox.setText(bundle1.getString("bloqueado")); // NOI18N
        bloqueadoCheckBox.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(codigoField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descricaoField, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inativoCheckBox)
                            .addComponent(bloqueadoCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unidadeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(subGrupoComboBox, 0, 212, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descricaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unidadeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inativoCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bloqueadoCheckBox))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void grupoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupoComboBoxActionPerformed
        GrupoProduto grupo = (GrupoProduto) grupoComboBox.getModel().getSelectedItem();
        subGrupoComboBox.setModel(VendasUtil.getSubGrupoProdutoListModel(grupo));
    }//GEN-LAST:event_grupoComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox bloqueadoCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField codigoField;
    private javax.swing.JRadioButton codigoRadioButton;
    private javax.swing.JFormattedTextField descricaoField;
    private javax.swing.JRadioButton descricaoRadioButton;
    private javax.swing.JComboBox grupoComboBox;
    private javax.swing.JCheckBox inativoCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox subGrupoComboBox;
    private javax.swing.JComboBox unidadeComboBox;
    // End of variables declaration//GEN-END:variables
}
