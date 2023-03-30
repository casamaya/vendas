/*
 * VendasUnidadePanel.java
 *
 * Created on 30 de Novembro de 2007, 22:07
 */

package vendas.swing.app.pedido;

import java.util.Date;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.VendasUnidadeFilter;
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
public class VendasUnidadePanel extends EditPanel {
    
    /** Creates new form VendasUnidadePanel */
    public VendasUnidadePanel() {
        initComponents();
    }

    @Override
    public void init() {
        super.init();
        clienteComboBox.setModel(VendasUtil.getClienteListModel());
        represComboBox.setModel(VendasUtil.getRepresListModel(true));
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
        
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

    @Override
    public void field2Object(Object obj) {
        VendasUnidadeFilter filter = (VendasUnidadeFilter)obj;
        StringBuilder sb = new StringBuilder();
        if (dtPedido1Field.getDate() != null) {
            filter.setDtEmissaoIni(dtPedido1Field.getDate());
            filter.setDtEmissaoEnd(dtPedido2Field.getDate());
            sb.append("Pedidos de ");
            
            sb.append(DateUtils.format(dtPedido1Field.getDate()));
            sb.append(" a ");
            sb.append(DateUtils.format(dtPedido2Field.getDate()));
            sb.append(". ");
        }
        if (dtEntrega1Field.getDate() != null) {
            filter.setDtEntregaIni(dtEntrega1Field.getDate());
            filter.setDtEntregaEnd(dtEntrega2Field.getDate());
            sb.append("Prog. faturamento de  ");
            sb.append(dtEntrega1Field.getDate());
            sb.append(" a ");
            sb.append(dtEntrega2Field.getDate());
            sb.append(". ");
        }
        if (dtNota1Field.getDate() != null) {
            filter.setDtNotaIni(dtNota1Field.getDate());
            filter.setDtNotaEnd(dtNota2Field.getDate());
            sb.append("Notas de ");
            sb.append(dtNota1Field.getDate());
            sb.append(" a ");
            sb.append(dtNota2Field.getDate());
            sb.append(". ");
        }
        if (dtPgto1Field.getDate() != null) {
            try {
                Date d1 = DateUtils.parse(DateUtils.format(dtPgto1Field.getDate()));
                Date d2 = DateUtils.parse(DateUtils.format(dtPgto2Field.getDate()));
                filter.setDtPgtoNotaIni(d1);
                filter.setDtPgtoNotaEnd(d2);

                sb.append("Pagamentos de ").append(DateUtils.format(filter.getDtPgtoNotaIni()));
                sb.append(" a ");
                sb.append(DateUtils.format(filter.getDtPgtoNotaEnd()));
                sb.append(". ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (grupoButton.isSelected())
            filter.setAgrupamento(0);
        if (clienteButton.isSelected())
            filter.setAgrupamento(1);
        if (resumoButton.isSelected())
            filter.setAgrupamento(2);
        filter.setPendentes(new Boolean(pendentesCheckBox.isSelected()));
        if (pendentesCheckBox.isSelected()) {
            sb.append("Pendentes");
            sb.append(". ");
        }
        
        Repres repres = (Repres) represComboBox.getModel().getSelectedItem();
        Cliente cliente = (Cliente) clienteComboBox.getModel().getSelectedItem();
        Vendedor vendedor = (Vendedor) vendedorComboBox.getModel().getSelectedItem();
        GrupoCliente grupoCliente = (GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem();
        
        if (repres != null && repres.getIdRepres() != null) {
            filter.setFornecedor(repres);
        } else {
            filter.setFornecedor(null);
        }
        
        if (vendedor != null && vendedor.getIdVendedor() != null) {
            filter.setVendedor(vendedor);
        } else {
            filter.setVendedor(null);
        }
        
        if (cliente != null && cliente.getIdCliente() != null) {
            filter.setCliente(cliente);
        } else {
            filter.setCliente(null);
        }
        
        if (grupoCliente != null && grupoCliente.getIdGrupoCliente() != null) {
            filter.setGrupoCliente(grupoCliente);
        } else {
            filter.setGrupoCliente(null);
        }
        
        filter.setTitle(sb.toString());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtEntrega1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtEntrega2Field = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        pendentesCheckBox = new javax.swing.JCheckBox();
        grupoButton = new javax.swing.JRadioButton();
        clienteButton = new javax.swing.JRadioButton();
        resumoButton = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        dtPgto1Field = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dtPgto2Field = new com.toedter.calendar.JDateChooser();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        dtPedido2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPedido2Field.setName("dtPedido2Field"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtPedido1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPedido1Field.setName("dtPedido1Field"); // NOI18N

        jLabel5.setText(bundle.getString("dtEntrega")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        dtEntrega1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtEntrega1Field.setName("dtEntrega1Field"); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        dtEntrega2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtEntrega2Field.setName("dtEntrega2Field"); // NOI18N

        jLabel14.setText(bundle.getString("dtNota")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        dtNota1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtNota1Field.setName("dtNota1Field"); // NOI18N

        jLabel15.setText(bundle.getString("para")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        dtNota2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtNota2Field.setName("dtNota2Field"); // NOI18N

        pendentesCheckBox.setText(bundle.getString("somentePendentes")); // NOI18N
        pendentesCheckBox.setName("pendentesCheckBox"); // NOI18N

        buttonGroup1.add(grupoButton);
        grupoButton.setText(bundle.getString("detalharGrupo")); // NOI18N
        grupoButton.setName("grupoButton"); // NOI18N

        buttonGroup1.add(clienteButton);
        clienteButton.setText(bundle.getString("detalharCliente")); // NOI18N
        clienteButton.setName("clienteButton"); // NOI18N

        buttonGroup1.add(resumoButton);
        resumoButton.setSelected(true);
        resumoButton.setText(bundle.getString("resumo")); // NOI18N
        resumoButton.setName("resumoButton"); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        clienteComboBox.setName("clienteComboBox"); // NOI18N

        jLabel13.setText(bundle.getString("representada")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel7.setText(bundle.getString("pagamentos")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        dtPgto1Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPgto1Field.setName("dtPgto1Field"); // NOI18N

        jLabel6.setText(bundle.getString("para")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        dtPgto2Field.setDateFormatString(bundle.getString("dateFormat")); // NOI18N
        dtPgto2Field.setName("dtPgto2Field"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel12.setText(bundle.getString("grupoCliente")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        grupoClienteComboBox.setName("grupoClienteComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(207, 207, 207)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(dtPgto1Field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(pendentesCheckBox))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(grupoButton)
                        .addGap(1, 1, 1)
                        .addComponent(clienteButton)
                        .addGap(7, 7, 7)
                        .addComponent(resumoButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10)
                            .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addComponent(jLabel14))
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pendentesCheckBox)))
                    .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(5, 5, 5)
                        .addComponent(dtPgto1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grupoButton)
                    .addComponent(clienteButton)
                    .addComponent(resumoButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(5, 5, 5)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel13)
                .addGap(5, 5, 5)
                .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton clienteButton;
    private javax.swing.JComboBox clienteComboBox;
    private com.toedter.calendar.JDateChooser dtEntrega1Field;
    private com.toedter.calendar.JDateChooser dtEntrega2Field;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private com.toedter.calendar.JDateChooser dtPgto1Field;
    private com.toedter.calendar.JDateChooser dtPgto2Field;
    private javax.swing.JRadioButton grupoButton;
    private javax.swing.JComboBox grupoClienteComboBox;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JCheckBox pendentesCheckBox;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JRadioButton resumoButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
    
}
