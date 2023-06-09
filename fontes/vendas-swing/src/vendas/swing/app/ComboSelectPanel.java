/*
 * ComboSelectPanel.java
 *
 * Created on 27 de Dezembro de 2007, 12:21
 */

package vendas.swing.app;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ritual.swing.ListComboBoxModel;

/**
 *
 * @author  p993702
 */
public class ComboSelectPanel extends JPanel {
    
    /** Creates new form ComboSelectPanel */
    public ComboSelectPanel() {
        initComponents();
    }
    
    public void setLabel(String value) {
        nameLabel.setText(value);
    }
    
    public Object getValue() {
        return valueComboBox.getModel().getSelectedItem();
    }
    
    public void setValueList(List lista) {
        valueComboBox.setModel(new ListComboBoxModel(lista));
    }
    
    public boolean select(String msg) {
        String[] options = {"Selecionar", "Cancelar"};
        int value = JOptionPane.showOptionDialog(this, this, msg, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        boolean result = (value == 0);
        return result;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        valueComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        nameLabel.setName("nameLabel"); // NOI18N

        valueComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        valueComboBox.setName("valueComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valueComboBox, 0, 380, Short.MAX_VALUE)
                    .addComponent(nameLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    private javax.swing.JComboBox valueComboBox;
    // End of variables declaration//GEN-END:variables
    
}
