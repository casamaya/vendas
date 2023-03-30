/*
 * ComissaoFilterPanel.java
 *
 * Created on 30 de Novembro de 2007, 17:18
 */
package vendas.swing.app.pedido;

import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.PedidoFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.GrupoProduto;
import vendas.entity.Repres;
import vendas.entity.SubGrupoProduto;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class AtendimentoFilterPanel extends EditPanel {

    /** Creates new form ComissaoFilterPanel */
    public AtendimentoFilterPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoFilter pedido = (PedidoFilter) obj;
        clienteComboBox.setSelectedItem(pedido.getCliente());
        dtNota1Field.setDate(pedido.getDtNotaIni());
        dtNota2Field.setDate(pedido.getDtNotaEnd());
        dtPedido1Field.setDate(pedido.getDtEmissaoIni());
        dtPedido2Field.setDate(pedido.getDtEmissaoEnd());
        represComboBox.setSelectedItem(pedido.getRepres());
        vendedorComboBox.setSelectedItem(pedido.getVendedor());
        pedidosRepresCheckBox.setSelected(pedido.getFiltrarPedidosFornecedor());
        String situacao = pedido.getSituacao();
        if ("T".equals(situacao)) {
            todosRadioButton.setSelected(true);
        } else if ("P".equals(situacao)) {
            pendenteRadioButton.setSelected(true);
        } else {
            atendidoRadioButton.setSelected(true);
        }
    }

    @Override
    public void field2Object(Object obj) {
        PedidoFilter pedido = (PedidoFilter) obj;
        StringBuilder sb = new StringBuilder();
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        
        if (pedido.getRepres() != null && pedido.getRepres().getIdRepres() != null) {
            pedido.setFiltrarPedidosFornecedor(pedidosRepresCheckBox.isSelected());
        } else 
            pedido.setFiltrarPedidosFornecedor(false);
        
        Vendedor vendedor = (Vendedor) vendedorComboBox.getModel().getSelectedItem();
        if (vendedor.getIdVendedor() != null) {
            sb.append("Vendedor ").append(vendedor.getNome()).append(". ");
        }
        pedido.setVendedor(vendedor);
        pedido.setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        SubGrupoProduto subGrupo = (SubGrupoProduto) subGrupoComboBox.getModel().getSelectedItem();
        pedido.setSubGrupoProduto(subGrupo);

        if (dtPedido1Field.getDate() != null) {
        pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate())));
        pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido2Field.getDate())));
        }
        if (dtNota1Field.getDate() != null) {
        pedido.setDtNotaIni(DateUtils.parse(DateUtils.format(dtNota1Field.getDate())));
        pedido.setDtNotaEnd(DateUtils.parse(DateUtils.format(dtNota2Field.getDate())));
        }
        if (naoAtendidoRadioButton.isSelected()) {
            sb.append("Não atendidos. ");
            pedido.setSituacao("N");
        } else if (pendenteRadioButton.isSelected()) {
            sb.append("Atrasados. ");
            pedido.setSituacao("P");
        } else if (atendidoRadioButton.isSelected()) {
            sb.append("Atendidos. ");
            pedido.setSituacao("A");
        } else {
            pedido.setSituacao("T");
        }
        if ((subGrupo != null) && (subGrupo.getIdCodSubGrupo() != null)) {
            sb.append("Sub-grupo ").append(subGrupo.getNomeGrupo()).append(". ");
        }
        if (pedidoRadioButton.isSelected()) {
            pedido.setOrdem(0);
        } else if (emissaoRadioButton.isSelected()) {
            pedido.setOrdem(1);
        } else {
            pedido.setOrdem(2);
        }
        pedido.setTotalizaEntrega(totalizaCheckBox.isSelected());
        pedido.setTitle(sb.toString());
    }

    @Override
    public void init() {
        getLogger().info("init");
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        clienteComboBox.setModel(VendasUtil.getClienteListModel());
        represComboBox.setModel(VendasUtil.getRepresListModel(true));
        grupoComboBox.setModel(VendasUtil.getGrupoProdutoListModel());
        TApplication app = TApplication.getInstance();
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedorComboBox.setSelectedItem(dao.findById(Vendedor.class, app.getUser().getIdvendedor()));
                vendedorComboBox.setEnabled(false);
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        subGrupoComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        todosRadioButton = new javax.swing.JRadioButton();
        atendidoRadioButton = new javax.swing.JRadioButton();
        pendenteRadioButton = new javax.swing.JRadioButton();
        naoAtendidoRadioButton = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        pedidoRadioButton = new javax.swing.JRadioButton();
        emissaoRadioButton = new javax.swing.JRadioButton();
        entregaRadioButton = new javax.swing.JRadioButton();
        totalizaCheckBox = new javax.swing.JCheckBox();
        pedidosRepresCheckBox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtPedido")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(AtendimentoFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N
        dtPedido1Field.setName("dtPedido1Field"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N
        dtPedido2Field.setName("dtPedido2Field"); // NOI18N

        jLabel5.setText(bundle.getString("dtEntrega")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        dtNota1Field.setDateFormatString(resourceMap.getString("dtNota1Field.dateFormatString")); // NOI18N
        dtNota1Field.setName("dtNota1Field"); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        dtNota2Field.setDateFormatString(resourceMap.getString("dtNota2Field.dateFormatString")); // NOI18N
        dtNota2Field.setName("dtNota2Field"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel11.setText(bundle.getString("grupoProduto")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        grupoComboBox.setName("grupoComboBox"); // NOI18N
        grupoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupoComboBoxActionPerformed(evt);
            }
        });

        jLabel12.setText(bundle.getString("subgrupo")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        subGrupoComboBox.setName("subGrupoComboBox"); // NOI18N

        jLabel9.setText(bundle.getString("representada")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        clienteComboBox.setName("clienteComboBox"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("situacao"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        buttonGroup1.add(todosRadioButton);
        todosRadioButton.setText(bundle.getString("todos")); // NOI18N
        todosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        todosRadioButton.setName("todosRadioButton"); // NOI18N

        buttonGroup1.add(atendidoRadioButton);
        atendidoRadioButton.setText(bundle.getString("atendido")); // NOI18N
        atendidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        atendidoRadioButton.setName("atendidoRadioButton"); // NOI18N

        buttonGroup1.add(pendenteRadioButton);
        pendenteRadioButton.setLabel(bundle.getString("atrasados")); // NOI18N
        pendenteRadioButton.setName("pendenteRadioButton"); // NOI18N

        buttonGroup1.add(naoAtendidoRadioButton);
        naoAtendidoRadioButton.setSelected(true);
        naoAtendidoRadioButton.setLabel(bundle.getString("naoAtendido")); // NOI18N
        naoAtendidoRadioButton.setName("naoAtendidoRadioButton"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(todosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(atendidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(naoAtendidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pendenteRadioButton)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(todosRadioButton)
                    .addComponent(atendidoRadioButton)
                    .addComponent(naoAtendidoRadioButton)
                    .addComponent(pendenteRadioButton))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        buttonGroup2.add(pedidoRadioButton);
        pedidoRadioButton.setText(bundle.getString("pedido")); // NOI18N
        pedidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pedidoRadioButton.setName("pedidoRadioButton"); // NOI18N
        pedidoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidoRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(emissaoRadioButton);
        emissaoRadioButton.setText(bundle.getString("dtEmissao")); // NOI18N
        emissaoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        emissaoRadioButton.setName("emissaoRadioButton"); // NOI18N
        emissaoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emissaoRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(entregaRadioButton);
        entregaRadioButton.setSelected(true);
        entregaRadioButton.setText(bundle.getString("dtEntrega")); // NOI18N
        entregaRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        entregaRadioButton.setName("entregaRadioButton"); // NOI18N
        entregaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entregaRadioButtonActionPerformed(evt);
            }
        });

        totalizaCheckBox.setText(bundle.getString("totalizaVencimentos")); // NOI18N
        totalizaCheckBox.setName("totalizaCheckBox"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(pedidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emissaoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalizaCheckBox)
                    .addComponent(entregaRadioButton)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pedidoRadioButton)
                    .addComponent(emissaoRadioButton)
                    .addComponent(entregaRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalizaCheckBox))
        );

        pedidosRepresCheckBox.setText(bundle.getString("filtrarPedidosFornecedor")); // NOI18N
        pedidosRepresCheckBox.setName("pedidosRepresCheckBox"); // NOI18N
        pedidosRepresCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidosRepresCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pedidosRepresCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(10, 10, 10)
                                .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addGap(10, 10, 10)
                                        .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(grupoComboBox, 0, 311, Short.MAX_VALUE)
                                    .addComponent(jLabel11)
                                    .addComponent(subGrupoComboBox, 0, 311, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(141, 141, 141)))))
                        .addGap(211, 211, 211))
                    .addComponent(jLabel10)
                    .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pedidosRepresCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void entregaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entregaRadioButtonActionPerformed
        totalizaCheckBox.setEnabled(entregaRadioButton.isSelected());
    }//GEN-LAST:event_entregaRadioButtonActionPerformed

    private void emissaoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emissaoRadioButtonActionPerformed
        totalizaCheckBox.setEnabled(false);
        totalizaCheckBox.setSelected(false);
    }//GEN-LAST:event_emissaoRadioButtonActionPerformed

    private void pedidoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidoRadioButtonActionPerformed
        totalizaCheckBox.setEnabled(false);
        totalizaCheckBox.setSelected(false);
    }//GEN-LAST:event_pedidoRadioButtonActionPerformed

    private void grupoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupoComboBoxActionPerformed
        // TODO add your handling code here:
        GrupoProduto grupo = (GrupoProduto) grupoComboBox.getModel().getSelectedItem();
        subGrupoComboBox.setModel(VendasUtil.getSubGrupoProdutoListModel(grupo));
    }//GEN-LAST:event_grupoComboBoxActionPerformed

    private void pedidosRepresCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidosRepresCheckBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_pedidosRepresCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton atendidoRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox clienteComboBox;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JRadioButton emissaoRadioButton;
    private javax.swing.JRadioButton entregaRadioButton;
    private javax.swing.JComboBox grupoComboBox;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton naoAtendidoRadioButton;
    private javax.swing.JRadioButton pedidoRadioButton;
    private javax.swing.JCheckBox pedidosRepresCheckBox;
    private javax.swing.JRadioButton pendenteRadioButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox subGrupoComboBox;
    private javax.swing.JRadioButton todosRadioButton;
    private javax.swing.JCheckBox totalizaCheckBox;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
