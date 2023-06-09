/*
 * ComissaoFilterPanel.java
 *
 * Created on 30 de Novembro de 2007, 17:18
 */
package vendas.swing.app.pedido;

import java.util.Date;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.PedidoFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class NotaFilterPanel extends EditPanel {

    /** Creates new form ComissaoFilterPanel */
    public NotaFilterPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoFilter pedido = (PedidoFilter) obj;
        clienteComboBox.setSelectedItem(pedido.getCliente());
        grupoClienteComboBox.setSelectedItem(pedido.getGrupo());
        dtNota1Field.setDate(pedido.getDtNotaIni());
        dtNota2Field.setDate(pedido.getDtNotaEnd());
        dtPgto1Field.setDate(pedido.getDtPgtoNotaIni());
        dtPgto2Field.setDate(pedido.getDtPgtoNotaEnd());
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
        pedido.setGrupo((GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        pedido.setDtEmissaoIni(dtPedido1Field.getDate());
        pedido.setDtEmissaoEnd(dtPedido2Field.getDate());
        
        if (pedido.getDtEmissaoIni() != null) {
            sb.append("Emitidos de ").append(DateUtils.format(pedido.getDtEmissaoIni()));
            sb.append(" a ");
            sb.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            sb.append(". ");
        }

        if (dtNota1Field.getDate() != null) {
            pedido.setDtNotaIni(DateUtils.parse(DateUtils.format(dtNota1Field.getDate())));
            pedido.setDtNotaEnd(DateUtils.parse(DateUtils.format(dtNota2Field.getDate())));
            sb.append("Notas de ").append(DateUtils.format(pedido.getDtNotaIni()));
            sb.append(" a ");
            sb.append(DateUtils.format(pedido.getDtNotaEnd()));
            sb.append(". ");
        }
        
        if (dtPgto1Field.getDate() != null) {
            try {
                Date d1 = DateUtils.parse(DateUtils.format(dtPgto1Field.getDate()));
                Date d2 = DateUtils.parse(DateUtils.format(dtPgto2Field.getDate()));
                pedido.setDtPgtoNotaIni(d1);
                pedido.setDtPgtoNotaEnd(d2);

                sb.append("Pagamentos de ").append(DateUtils.format(pedido.getDtPgtoNotaIni()));
                sb.append(" a ");
                sb.append(DateUtils.format(pedido.getDtPgtoNotaEnd()));
                sb.append(". ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pedido.setTitle(sb.toString());

        if (clienteRadioButton.isSelected()) {
            pedido.setOrdem(0);
        }
        if (entregaRadioButton.isSelected()) {
            pedido.setOrdem(1);
        }
        if (pedidoRadioButton.isSelected()) {
            pedido.setOrdem(2);
        }
        if (notaRadioButton.isSelected()) {
            pedido.setOrdem(3);
        }
        if (dtPedidoRadioButton.isSelected()) {
            pedido.setOrdem(4);
        }
        if (nfRadioButton.isSelected()) {
            pedido.setOrdem(5);
        }
    }

    @Override
    public void init() {
        getLogger().info("init");
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        clienteComboBox.setModel(VendasUtil.getClienteListModel());
        grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
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

        buttonGroup3 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        dtPgto1Field = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dtPgto2Field = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        ordemPanel = new javax.swing.JPanel();
        clienteRadioButton = new javax.swing.JRadioButton();
        entregaRadioButton = new javax.swing.JRadioButton();
        pedidoRadioButton = new javax.swing.JRadioButton();
        notaRadioButton = new javax.swing.JRadioButton();
        dtPedidoRadioButton = new javax.swing.JRadioButton();
        nfRadioButton = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtPedido")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        dtPedido1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPedido1Field.setName("dtPedido1Field"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtPedido2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPedido2Field.setName("dtPedido2Field"); // NOI18N

        jLabel5.setText(bundle.getString("dtNota")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        dtNota1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtNota1Field.setName("dtNota1Field"); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        dtNota2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtNota2Field.setName("dtNota2Field"); // NOI18N

        jLabel7.setText(bundle.getString("pagamentos")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        dtPgto1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPgto1Field.setName("dtPgto1Field"); // NOI18N

        jLabel6.setText(bundle.getString("para")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        dtPgto2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPgto2Field.setName("dtPgto2Field"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel9.setText(bundle.getString("representada")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        clienteComboBox.setName("clienteComboBox"); // NOI18N

        ordemPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N
        ordemPanel.setName("ordemPanel"); // NOI18N

        buttonGroup3.add(clienteRadioButton);
        clienteRadioButton.setSelected(true);
        clienteRadioButton.setText(bundle.getString("cliente")); // NOI18N
        clienteRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        clienteRadioButton.setName("clienteRadioButton"); // NOI18N

        buttonGroup3.add(entregaRadioButton);
        entregaRadioButton.setText(bundle.getString("dtEntrega")); // NOI18N
        entregaRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        entregaRadioButton.setName("entregaRadioButton"); // NOI18N

        buttonGroup3.add(pedidoRadioButton);
        pedidoRadioButton.setText(bundle.getString("pedido")); // NOI18N
        pedidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pedidoRadioButton.setName("pedidoRadioButton"); // NOI18N

        buttonGroup3.add(notaRadioButton);
        notaRadioButton.setText(bundle.getString("dtNota")); // NOI18N
        notaRadioButton.setName("notaRadioButton"); // NOI18N

        buttonGroup3.add(dtPedidoRadioButton);
        dtPedidoRadioButton.setText(bundle.getString("dtPedido")); // NOI18N
        dtPedidoRadioButton.setName("dtPedidoRadioButton"); // NOI18N

        buttonGroup3.add(nfRadioButton);
        nfRadioButton.setText(bundle.getString("nf")); // NOI18N
        nfRadioButton.setName("nfRadioButton"); // NOI18N

        javax.swing.GroupLayout ordemPanelLayout = new javax.swing.GroupLayout(ordemPanel);
        ordemPanel.setLayout(ordemPanelLayout);
        ordemPanelLayout.setHorizontalGroup(
            ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordemPanelLayout.createSequentialGroup()
                .addComponent(clienteRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entregaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtPedidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nfRadioButton)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        ordemPanelLayout.setVerticalGroup(
            ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordemPanelLayout.createSequentialGroup()
                .addGroup(ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteRadioButton)
                    .addComponent(entregaRadioButton)
                    .addComponent(pedidoRadioButton)
                    .addComponent(notaRadioButton)
                    .addComponent(dtPedidoRadioButton)
                    .addComponent(nfRadioButton))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel12.setText(bundle.getString("grupoCliente")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        grupoClienteComboBox.setName("grupoClienteComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(239, 239, 239)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addGap(12, 12, 12)
                        .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(241, 241, 241)
                        .addComponent(jLabel8))
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ordemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(dtPgto1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(jLabel6)
                            .addGap(12, 12, 12)
                            .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(vendedorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel7))
                    .addComponent(jLabel8))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(dtPgto1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel10)
                .addGap(6, 6, 6)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel12)
                .addGap(6, 6, 6)
                .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(ordemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JRadioButton clienteRadioButton;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JRadioButton dtPedidoRadioButton;
    private com.toedter.calendar.JDateChooser dtPgto1Field;
    private com.toedter.calendar.JDateChooser dtPgto2Field;
    private javax.swing.JRadioButton entregaRadioButton;
    private javax.swing.JComboBox grupoClienteComboBox;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton nfRadioButton;
    private javax.swing.JRadioButton notaRadioButton;
    private javax.swing.JPanel ordemPanel;
    private javax.swing.JRadioButton pedidoRadioButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
