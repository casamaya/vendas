/*
 * EntradaRepresPanel.java
 *
 * Created on 22 de Julho de 2007, 21:24
 */

package vendas.swing.app.repres;

import java.text.DecimalFormat;
import ritual.swing.NumericPlainDocument;
import ritual.util.NumberUtils;
import vendas.entity.EntradaRepres;
import vendas.swing.core.EditPanel;

/**
 *
 * @author  Sam
 */
public class EntradaRepresPanel extends EditPanel {
    
    /** Creates new form EntradaRepresPanel */
    public EntradaRepresPanel() {
        initComponents();
                DecimalFormat df = new DecimalFormat("#0.00");
        valorField.setDocument(new NumericPlainDocument(df));
    }
    
    @Override
    public void object2Field(Object obj) {
        EntradaRepres entrada = (EntradaRepres) obj;        
        dataField.setDate(entrada.getDtEntrada());
        pedidosField.setText(entrada.getPedido());
        if (entrada.getValor() != null)
        valorField.setValue(entrada.getValor());
    }
    
    @Override
    public void field2Object(Object obj) {
        EntradaRepres entrada = (EntradaRepres) obj;        
        entrada.setDtEntrada(dataField.getDate());
        entrada.setPedido(pedidosField.getText());
        entrada.setValor(NumberUtils.getBigDecimal(valorField.getValue()));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        dataField = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        valorField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        pedidosField = new javax.swing.JFormattedTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("data")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(EntradaRepresPanel.class);
        dataField.setDateFormatString(resourceMap.getString("dataField.dateFormatString")); // NOI18N

        jLabel2.setText(bundle.getString("valor")); // NOI18N

        valorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel3.setText(bundle.getString("pedidosRelacionados")); // NOI18N

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
                            .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addComponent(jLabel3)
                    .addComponent(pedidosField, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidosField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dataField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JFormattedTextField pedidosField;
    private javax.swing.JFormattedTextField valorField;
    // End of variables declaration//GEN-END:variables
    
}
