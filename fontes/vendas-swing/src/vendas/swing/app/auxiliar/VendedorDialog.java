/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VendedorDialog.java
 *
 * Created on Mar 8, 2010, 11:13:30 PM
 */

package vendas.swing.app.auxiliar;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.SwingConstants;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.FractionCellRenderer;
import ritual.util.NumberUtils;
import vendas.entity.Vendedor;
import vendas.swing.core.Formats;
import vendas.swing.model.ComissaoVendedorModel;
import vendas.swing.model.RoteiroVendedorModel;

/**
 *
 * @author sam
 */
public class VendedorDialog extends javax.swing.JDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form VendedorDialog */
    public VendedorDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    public void object2Field(Object obj) {
        Vendedor vendedor = (Vendedor) obj;
        roteirosTable.setModel(new RoteiroVendedorModel((List)vendedor.getRoteiros()));
        comissoesTable.setModel(new ComissaoVendedorModel((List)vendedor.getComissoesVendedor()));
        roteirosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        comissoesTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        bairroField.setText(vendedor.getBairro());
        cepField.setText(vendedor.getCep());
        cidadeField.setText(vendedor.getCidade());
        comissionadoCheckBox.setSelected(vendedor.getRecebeComissao().booleanValue());
        enderecoField.setText(vendedor.getEndereco());
        fone1Field.setText(vendedor.getFone1());
        fone2Field.setText(vendedor.getFone2());
        nomeField.setText(vendedor.getNome());
        ufField.setText(vendedor.getUf());
        valComissaoField.setValue(vendedor.getValorComissao());
    }

    public void field2Object(Object obj) {
        Vendedor vendedor = (Vendedor) obj;
        vendedor.setBairro(bairroField.getText());
        vendedor.setCep(cepField.getText());
        vendedor.setCidade(cidadeField.getText());
        vendedor.setComissionado(new Boolean(comissionadoCheckBox.isSelected()));
        vendedor.setEndereco(enderecoField.getText());
        vendedor.setFone1(fone1Field.getText());
        vendedor.setFone2(fone2Field.getText());
        vendedor.setNome(nomeField.getText());
        vendedor.setUf(ufField.getText());
        vendedor.setValorComissao((BigDecimal)valComissaoField.getValue());
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popMenu = new javax.swing.JPopupMenu();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        nomeLabel = new javax.swing.JLabel();
        enderecoLabel = new javax.swing.JLabel();
        nomeField = new javax.swing.JFormattedTextField();
        enderecoField = new javax.swing.JFormattedTextField();
        bairroLabel = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        cidadeLabel = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        cepLabel = new javax.swing.JLabel();
        cepField = new javax.swing.JFormattedTextField();
        ufLabel = new javax.swing.JLabel();
        ufField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        fone2Field = new javax.swing.JFormattedTextField();
        fone2Label = new javax.swing.JLabel();
        fone1Field = new javax.swing.JFormattedTextField();
        fone1Label = new javax.swing.JLabel();
        comissionadoCheckBox = new javax.swing.JCheckBox();
        valComissaoLabel = new javax.swing.JLabel();
        valComissaoField = Formats.newFloatFormat();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roteirosTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        comissoesTable = new javax.swing.JTable();

        popMenu.setName("popMenu"); // NOI18N

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        okButton.setText(bundle.getString("save")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(bundle.getString("close")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        nomeLabel.setText(bundle.getString("nome")); // NOI18N
        nomeLabel.setName("nomeLabel"); // NOI18N

        enderecoLabel.setText(bundle.getString("endereco")); // NOI18N
        enderecoLabel.setName("enderecoLabel"); // NOI18N

        nomeField.setColumns(40);
        nomeField.setDocument(new BoundedPlainDocument(40));
        nomeField.setFocusable(false);
        nomeField.setName("nomeField"); // NOI18N

        enderecoField.setColumns(40);
        enderecoField.setDocument(new BoundedPlainDocument(40));
        enderecoField.setFocusable(false);
        enderecoField.setName("enderecoField"); // NOI18N

        bairroLabel.setText(bundle.getString("bairro")); // NOI18N
        bairroLabel.setName("bairroLabel"); // NOI18N

        bairroField.setColumns(30);
        bairroField.setDocument(new BoundedPlainDocument(30));
        bairroField.setFocusable(false);
        bairroField.setName("bairroField"); // NOI18N

        cidadeLabel.setText(bundle.getString("cidade")); // NOI18N
        cidadeLabel.setName("cidadeLabel"); // NOI18N

        cidadeField.setColumns(30);
        cidadeField.setDocument(new BoundedPlainDocument(30));
        cidadeField.setFocusable(false);
        cidadeField.setName("cidadeField"); // NOI18N

        cepLabel.setText(bundle.getString("cep")); // NOI18N
        cepLabel.setName("cepLabel"); // NOI18N

        cepField.setColumns(8);
        cepField.setFocusable(false);
        cepField.setName("cepField"); // NOI18N

        ufLabel.setText(bundle.getString("uf")); // NOI18N
        ufLabel.setName("ufLabel"); // NOI18N

        ufField.setColumns(2);
        ufField.setFocusable(false);
        ufField.setName("ufField"); // NOI18N

        fone2Field.setColumns(12);
        fone2Field.setFocusable(false);
        fone2Field.setName("fone2Field"); // NOI18N

        fone2Label.setText(bundle.getString("fone2")); // NOI18N
        fone2Label.setName("fone2Label"); // NOI18N

        fone1Field.setColumns(12);
        fone1Field.setFocusable(false);
        fone1Field.setName("fone1Field"); // NOI18N

        fone1Label.setText(bundle.getString("fone1")); // NOI18N
        fone1Label.setName("fone1Label"); // NOI18N

        comissionadoCheckBox.setText(bundle.getString("recebeComissao")); // NOI18N
        comissionadoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        comissionadoCheckBox.setFocusable(false);
        comissionadoCheckBox.setName("comissionadoCheckBox"); // NOI18N

        valComissaoLabel.setText(bundle.getString("percComissao")); // NOI18N
        valComissaoLabel.setName("valComissaoLabel"); // NOI18N

        valComissaoField.setColumns(5);
        valComissaoField.setFormatterFactory(NumberUtils.getFormatterFactory());
        valComissaoField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valComissaoField.setFocusable(false);
        valComissaoField.setName("valComissaoField"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(enderecoLabel)
                    .addComponent(nomeLabel)
                    .addComponent(bairroLabel)
                    .addComponent(cidadeLabel)
                    .addComponent(cepLabel)
                    .addComponent(fone1Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cidadeField)
                    .addComponent(bairroField)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(comissionadoCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fone1Field))
                            .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fone2Label)
                            .addComponent(valComissaoLabel)
                            .addComponent(ufLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valComissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(enderecoField, 0, 0, Short.MAX_VALUE)
                    .addComponent(nomeField, 0, 0, Short.MAX_VALUE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeLabel)
                    .addComponent(nomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enderecoLabel)
                    .addComponent(enderecoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bairroLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cidadeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cepLabel)
                    .addComponent(ufLabel)
                    .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fone1Label)
                    .addComponent(fone2Label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comissionadoCheckBox)
                    .addComponent(valComissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valComissaoLabel))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("cadastro"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        roteirosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        roteirosTable.setName("roteirosTable"); // NOI18N
        jScrollPane1.setViewportView(roteirosTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("roteiros"), jPanel2); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        comissoesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        comissoesTable.setName("comissoesTable"); // NOI18N
        jScrollPane2.setViewportView(comissoesTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("comissoes"), jPanel3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VendedorDialog dialog = new VendedorDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JLabel bairroLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JFormattedTextField cepField;
    private javax.swing.JLabel cepLabel;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel;
    private javax.swing.JCheckBox comissionadoCheckBox;
    private javax.swing.JTable comissoesTable;
    private javax.swing.JFormattedTextField enderecoField;
    private javax.swing.JLabel enderecoLabel;
    private javax.swing.JFormattedTextField fone1Field;
    private javax.swing.JLabel fone1Label;
    private javax.swing.JFormattedTextField fone2Field;
    private javax.swing.JLabel fone2Label;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField nomeField;
    private javax.swing.JLabel nomeLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JPopupMenu popMenu;
    private javax.swing.JTable roteirosTable;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JLabel ufLabel;
    private javax.swing.JFormattedTextField valComissaoField;
    private javax.swing.JLabel valComissaoLabel;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}