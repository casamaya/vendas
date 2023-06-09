/*
 * FormaPgtoEditPanel.java
 *
 * Created on 10 de Julho de 2007, 16:57
 */

package vendas.swing.app.auxiliar;

import vendas.entity.FormaPgto;
import vendas.swing.core.EditPanel;
import ritual.swing.BoundedPlainDocument;

/**
 *
 * @author  p993702
 */
public class FormaPgtoEditPanel extends EditPanel {
    
    /** Creates new form FormaPgtoEditPanel */
    public FormaPgtoEditPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        FormaPgto formaPgto = (FormaPgto) obj;
        nomeField.setText(formaPgto.getNome());
        idFormaPgtoField.setText(formaPgto.getIdPgto());
    }

    @Override
    public void field2Object(Object obj) {
        FormaPgto formaPgto = (FormaPgto) obj;
        formaPgto.setNome(nomeField.getText());
        formaPgto.setIdPgto(idFormaPgtoField.getText());
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        idFormaPgtoField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        nomeField = new javax.swing.JFormattedTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("codigo")); // NOI18N

        idFormaPgtoField.setColumns(2);
        idFormaPgtoField.setDocument(new BoundedPlainDocument(3));

        jLabel3.setText(bundle.getString("nome")); // NOI18N

        nomeField.setDocument(new BoundedPlainDocument(50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idFormaPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomeField, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(idFormaPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField idFormaPgtoField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JFormattedTextField nomeField;
    // End of variables declaration//GEN-END:variables
    
}
