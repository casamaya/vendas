/*
 * VisitasFilterPanel.java
 *
 * Created on 30 de Novembro de 2007, 20:26
 */
package vendas.swing.app.cliente;

import java.util.Date;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.ClientesFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class VisitasFilterPanel extends EditPanel {

    /** Creates new form VisitasFilterPanel */
    public VisitasFilterPanel() {
        initComponents();
        dtVisitaIniField.setDate(DateUtils.getFirstDate(new Date()));
        dtVisitaEndField.setDate(new Date());
    }

    @Override
    public void init() {
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        TApplication app = TApplication.getInstance();
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao)app.lookupService("vendedorDao");
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
        ClientesFilter filter = (ClientesFilter) obj;
        Vendedor vendedor = (Vendedor) vendedorComboBox.getModel().getSelectedItem();
        if (vendedor.getIdVendedor() != null) {
            filter.setVendedor(vendedor);
        }
        StringBuilder sb = new StringBuilder();
        if (dtVisitaIniField.getDate() != null) {
            filter.setDtInclusaoIni(dtVisitaIniField.getDate());
            filter.setDtInclusaoEnd(dtVisitaEndField.getDate());
            sb.append("Per�odo: ");
            sb.append(DateUtils.format(dtVisitaIniField.getDate()));
            sb.append(" - ");
            sb.append(DateUtils.format(dtVisitaEndField.getDate()));
        }
        filter.setSemVisita(semVisitaCheckBox.isSelected());
        filter.setTitle(sb.toString());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        dtVisitaIniField = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtVisitaEndField = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        semVisitaCheckBox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("periodoVisitas")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/swing/app/cliente/Bundle"); // NOI18N
        dtVisitaIniField.setDateFormatString(bundle1.getString("VisitasFilterPanel.dtVisitaIniField.dateFormatString")); // NOI18N
        dtVisitaIniField.setName("dtVisitaIniField"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtVisitaEndField.setDateFormatString(bundle1.getString("VisitasFilterPanel.dtVisitaEndField.dateFormatString")); // NOI18N
        dtVisitaEndField.setName("dtVisitaEndField"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        semVisitaCheckBox.setText(bundle.getString("semVisitas")); // NOI18N
        semVisitaCheckBox.setName("semVisitaCheckBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(semVisitaCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtVisitaIniField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtVisitaEndField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(dtVisitaIniField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtVisitaEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(semVisitaCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dtVisitaEndField;
    private com.toedter.calendar.JDateChooser dtVisitaIniField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JCheckBox semVisitaCheckBox;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
