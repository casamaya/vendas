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
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class OrcamentoFilterPanel extends EditPanel {

    /** Creates new form ComissaoFilterPanel */
    public OrcamentoFilterPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoFilter pedido = (PedidoFilter) obj;
        clienteComboBox.setSelectedItem(pedido.getCliente());
        dtPedido1Field.setDate(pedido.getDtEmissaoIni());
        dtPedido2Field.setDate(pedido.getDtEmissaoEnd());
        represComboBox.setSelectedItem(pedido.getRepres());
        vendedorComboBox.setSelectedItem(pedido.getVendedor());
    }

    @Override
    public void field2Object(Object obj) {
        StringBuilder sb = new StringBuilder();
        PedidoFilter pedido = (PedidoFilter) obj;
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        if (dtPedido1Field.getDate() != null)
        try {
        pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate())));
        pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido2Field.getDate())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pedido.getDtEmissaoIni() != null) {
            sb.append("Emitidos de ").append(DateUtils.format(pedido.getDtEmissaoIni()));
            sb.append(" a ");
            sb.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            sb.append(". ");
        }
        pedido.setTitle(sb.toString());
    }

    @Override
    public void init() {
        getLogger().info("init");
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        clienteComboBox.setModel(VendasUtil.getClienteListModel());
        represComboBox.setModel(VendasUtil.getRepresListModel(true));
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

        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtOrcamento")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(OrcamentoFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N
        dtPedido1Field.setName("dtPedido1Field"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N
        dtPedido2Field.setName("dtPedido2Field"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel9.setText(bundle.getString("representada")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        clienteComboBox.setName("clienteComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox clienteComboBox;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}