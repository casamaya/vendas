/*
 * EmailPanel.java
 *
 * Created on 3 de Fevereiro de 2008, 18:36
 */

package vendas.util;

import java.util.ArrayList;
import ritual.swing.ListComboBoxModel;
import vendas.beans.EmailBean;
import vendas.swing.core.EditPanel;

/**
 *
 * @author  Sam
 */
public class EmailPanel extends EditPanel {
    
    ListComboBoxModel lista;
    
    /** Creates new form EmailPanel */
    public EmailPanel() {
        initComponents();
    }

    public EmailPanel(String s) {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        EmailBean email = (EmailBean)obj;
        subjectField.setText(email.getSubject());
        textPane.setText(email.getText());
        ArrayList itens = new ArrayList();
        itens.add("-- Selecione --");
        for (String item : email.getTo()) {
            itens.add(item);
        }
        lista = new ListComboBoxModel(email.getTo());
        destinoComboBox.setModel(new ListComboBoxModel(itens));
        toList.setModel(lista);
    }

    @Override
    public void field2Object(Object obj) {
        EmailBean email = (EmailBean)obj;
        email.setSubject(subjectField.getText());
        email.setText(textPane.getText());
        String[] emails = new String[lista.getSize()];
        for (int i = 0; i < lista.getSize(); i++)
            emails[i] = (String)lista.getElementAt(i);
        email.setTo(emails);
        
        email.setIncluiAnexoPadrao(anexoCheckBox.isSelected());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        subjectField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        toField = new javax.swing.JFormattedTextField();
        addButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        toList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        delButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        destinoComboBox = new javax.swing.JComboBox();
        anexoCheckBox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("assunto")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        subjectField.setName("subjectField"); // NOI18N

        jLabel1.setText(bundle.getString("destino")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        toField.setName("toField"); // NOI18N

        addButton.setText(bundle.getString("incluir")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        toList.setName("toList"); // NOI18N
        jScrollPane2.setViewportView(toList);

        jLabel3.setText(bundle.getString("mensagem")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        delButton.setText(bundle.getString("Excluir")); // NOI18N
        delButton.setName("delButton"); // NOI18N
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        textPane.setName("textPane"); // NOI18N
        jScrollPane1.setViewportView(textPane);

        destinoComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        destinoComboBox.setName("destinoComboBox"); // NOI18N

        anexoCheckBox.setText(bundle.getString("incluirAnexoPadrao")); // NOI18N
        anexoCheckBox.setName("anexoCheckBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(toField, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addComponent(subjectField, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                            .addComponent(destinoComboBox, 0, 369, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(delButton)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(anexoCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(destinoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anexoCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    if (toField.getText().length() > 0) {
        lista.addElement(toField.getText());
        toField.setText("");
    }
    if (destinoComboBox.getSelectedIndex() > 0) {
        lista.addElement(destinoComboBox.getModel().getSelectedItem());
    }

}//GEN-LAST:event_addButtonActionPerformed

private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
    int i = toList.getSelectedIndex();
    if (i >= 0)
        lista.removeElementAt(i);
}//GEN-LAST:event_delButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JCheckBox anexoCheckBox;
    private javax.swing.JButton delButton;
    private javax.swing.JComboBox destinoComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField subjectField;
    private javax.swing.JTextPane textPane;
    private javax.swing.JFormattedTextField toField;
    private javax.swing.JList toList;
    // End of variables declaration//GEN-END:variables
    
}
