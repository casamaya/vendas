/*
 * ContatoEditPanel.java
 *
 * Created on 12 de Julho de 2007, 13:49
 */

package vendas.swing.app.cliente;

import ritual.swing.BoundedPlainDocument;
import ritual.swing.ListComboBoxModel;
import ritual.swing.TApplication;
import vendas.dao.BancoDao;
import vendas.entity.Banco;
import vendas.entity.BancoCliente;
import vendas.swing.core.EditPanel;

/**
 *
 * @author  p993702
 */
public class RefBancoEditPanel extends EditPanel {
    
    /** Creates new form ContatoEditPanel */
    public RefBancoEditPanel() {
        initComponents();
        BancoDao dao = (BancoDao)TApplication.getInstance().lookupService("bancoDao");
        try {
        bancoComboBox.setModel(new ListComboBoxModel(dao.findAll()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void object2Field(Object obj) {
        BancoCliente comprador = (BancoCliente) obj;
        bancoComboBox.setSelectedItem(comprador.getBanco());
        agenciaField.setText(comprador.getAgencia());
        foneField.setText(comprador.getFone());
        contaField.setText(comprador.getConta());
    }
    
    @Override
    public void field2Object(Object obj) {
        BancoCliente comprador = (BancoCliente) obj;
        Banco banco = (Banco)bancoComboBox.getModel().getSelectedItem();
        comprador.setBanco(banco);
        comprador.setAgencia(agenciaField.getText());
        comprador.setFone(foneField.getText());
        comprador.setConta(contaField.getText());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        agenciaField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        contaField = new javax.swing.JFormattedTextField();
        bancoComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        foneField = new javax.swing.JFormattedTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel6.setText(bundle.getString("banco")); // NOI18N

        jLabel2.setText(bundle.getString("agencia")); // NOI18N

        agenciaField.setDocument(new BoundedPlainDocument(30));

        jLabel3.setText(bundle.getString("conta")); // NOI18N

        contaField.setDocument(new BoundedPlainDocument(30));

        jLabel4.setText(bundle.getString("fone")); // NOI18N

        foneField.setDocument(new BoundedPlainDocument(30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bancoComboBox, 0, 410, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(agenciaField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(contaField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(foneField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bancoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(agenciaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(foneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField agenciaField;
    private javax.swing.JComboBox bancoComboBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField contaField;
    private javax.swing.JFormattedTextField foneField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
    
}
