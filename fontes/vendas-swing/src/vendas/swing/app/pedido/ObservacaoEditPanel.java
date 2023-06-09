/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ObservacaoEditPanel.java
 *
 * Created on Feb 1, 2012, 2:42:30 PM
 */

package vendas.swing.app.pedido;

import vendas.entity.PgtoCliente;
import vendas.swing.core.EditPanel;

/**
 *
 * @author sam
 */
public class ObservacaoEditPanel extends EditPanel {

    /** Creates new form ObservacaoEditPanel */
    public ObservacaoEditPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        PgtoCliente pgto = (PgtoCliente) obj;
        obsTextArea.setText(pgto.getObservacao());
    }

    @Override
    public void field2Object(Object obj) {
        PgtoCliente pgto = (PgtoCliente) obj;
        pgto.setObservacao(obsTextArea.getText());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        obsTextArea = new javax.swing.JTextArea();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        obsTextArea.setColumns(20);
        obsTextArea.setRows(5);
        obsTextArea.setName("obsTextArea"); // NOI18N
        jScrollPane1.setViewportView(obsTextArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea obsTextArea;
    // End of variables declaration//GEN-END:variables

}
