/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.produto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import ritual.swing.NumericPlainDocument;
import ritual.util.NumberUtils;
import vendas.entity.RepresProduto;
import vendas.swing.core.EditPanel;

/**
 *
 * @author Jaime
 */
public class AtualizarIPIPanel extends EditPanel {

    /**
     * Creates new form AtualizarIPIPanel
     */
    public AtualizarIPIPanel() {
        initComponents();
        DecimalFormat df = new DecimalFormat("#0.00");
        valorField.setDocument(new NumericPlainDocument(df));
    }
    
    @Override
    public void field2Object(Object obj) {
        RepresProduto produto = (RepresProduto)obj;
        produto.setIpi( NumberUtils.getBigDecimal(valorField.getValue()));
        System.out.println(produto.getIpi());
    }

    @Override
    public void object2Field(Object obj) {
        RepresProduto produto = (RepresProduto)obj;
        valorField.setValue(produto.getIpi());
    }

    @Override
    public boolean entryValidate() {
        BigDecimal value = NumberUtils.getBigDecimal(valorField.getValue());
        return value != null;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        valorField = new javax.swing.JFormattedTextField();

        jLabel1.setText("Informe o novo valor de IPI");

        valorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(40, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JFormattedTextField valorField;
    // End of variables declaration//GEN-END:variables
}