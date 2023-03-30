/*
 * PgtoEditPanel.java
 *
 * Created on 30 de Novembro de 2007, 16:46
 */
package vendas.swing.app.pedido;

import java.text.DecimalFormat;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.ListComboBoxModel;
import ritual.swing.NumericPlainDocument;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import ritual.util.NumberUtils;
import vendas.entity.ContaRepres;
import vendas.entity.PgtoCliente;
import vendas.swing.core.EditPanel;

/**
 *
 * @author Sam
 */
public class PgtoEditPanel extends EditPanel {

    /**
     * Creates new form PgtoEditPanel
     */
    public PgtoEditPanel() {
        initComponents();
        DecimalFormat df = new DecimalFormat("#0.00");
        valorField.setDocument(new NumericPlainDocument(df));
        valorPgtoField.setDocument(new NumericPlainDocument(df));

        tipoField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizaDtPgto(tipoField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) { 
                atualizaDtPgto(tipoField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizaDtPgto(tipoField.getText());
            }
        });
    }

    private void atualizaDtPgto(String value) {

        if ((value == null) || (value.length() == 0)) {
            dtPgtoField.setEnabled(false);
            return;
        }

        boolean isP = "P".equals(value);
        boolean admin = TApplication.getInstance().getUser().isAdmin();

        if (isP) {
            if (admin) {
                dtPgtoField.setEnabled(true);
            } else {
                dtPgtoField.setEnabled(false);
            }
        } else {
            dtPgtoField.setEnabled(true);
        }
        dtPgtoField.invalidate();
    }

    @Override
    public void object2Field(Object obj) {
        PgtoCliente pgto = (PgtoCliente) obj;
//        complementoField.setText(pgto.getComplemento());
        dtVencimentoField.setDate(pgto.getDtVencimento());
        previsaoField.setDate(pgto.getDtPrevisaoPgto());
        tipoField.setText(pgto.getTipoPgto());
        if (pgto.getValor() != null) {
            valorField.setValue(pgto.getValor());
        }
        if ((pgto.getValorPgto() == null) && (pgto.getValor() != null)) {
            valorPgtoField.setValue(pgto.getValor());
        } else if (pgto.getValor() != null) {
            valorPgtoField.setValue(pgto.getValorPgto());
        }
        notaField.setText(pgto.getAtendimentoPedido().getAtendimentoPedidoPK().getNf());
        contaComboBox.setSelectedItem(pgto.getContaRepres());
        boolean isP;
        boolean admin;
        boolean sempgto;
        isP = "P".equals(pgto.getTipoPgto());
        admin = TApplication.getInstance().getUser().isAdmin();
        sempgto = pgto.getDtPgto() == null;

        if (isP) {
            if (admin) {
                dtPgtoField.setEnabled(true);
            } else {
                dtPgtoField.setEnabled(false);
            }
        } else {
            dtPgtoField.setEnabled(true);
        }

        if (sempgto) {
            dtPgtoField.setDate(pgto.getDtPrevisaoPgto());
        } else {
            dtPgtoField.setDate(pgto.getDtPgto());
        }

        obsTextArea.setText(pgto.getObservacao());
    }

    public void setContas(List lista) {
        contaComboBox.setModel(new ListComboBoxModel(lista));
    }

    @Override
    public void field2Object(Object obj) {
        PgtoCliente pgto = (PgtoCliente) obj;

        if (dtPgtoField.getDate() != null) {
            pgto.setDtPgto(DateUtils.parse(DateUtils.format(dtPgtoField.getDate())));
        }

        pgto.setDtVencimento(DateUtils.parse(DateUtils.format(dtVencimentoField.getDate())));

        if (previsaoField.getDate() != null) {
            pgto.setDtPrevisaoPgto(DateUtils.parse(DateUtils.format(previsaoField.getDate())));
        }

        pgto.setTipoPgto(tipoField.getText());
        pgto.setObservacao(obsTextArea.getText());

        pgto.setValor(NumberUtils.getBigDecimal(valorField.getValue()));

        if (valorPgtoField.getValue() == null) {
            pgto.setValorPgto(NumberUtils.getBigDecimal(valorField.getValue()));
        } else {
            pgto.setValorPgto(NumberUtils.getBigDecimal(valorPgtoField.getValue()));
        }

        if (pgto.getDtPgto() == null) {
            pgto.setValorPgto(null);
        }

        pgto.setContaRepres((ContaRepres) contaComboBox.getModel().getSelectedItem());

    }

    @Override
    public boolean entryValidate() {

        if (valorField.getText() == null || valorField.getText().length() == 0) {
            return false;
        }
        if (contaComboBox.getModel().getSelectedItem() == null && (tipoField.getText().equals("P") || tipoField.getText().equals("D"))) {
            return false;
        }
        if (dtVencimentoField.getDate() == null) {
            return false;
        }
        if (tipoField.getText() == null || tipoField.getText().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        notaField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        dtVencimentoField = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        valorField = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        tipoField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        previsaoField = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        dtPgtoField = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        valorPgtoField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        contaComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obsTextArea = new javax.swing.JTextArea();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("notaFiscal")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        notaField.setFocusable(false);
        notaField.setName("notaField"); // NOI18N

        jLabel2.setText(bundle.getString("dtVencimento")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        dtVencimentoField.setDateFormatString("dd/MM/yyyy"); // NOI18N
        dtVencimentoField.setName("dtVencimentoField"); // NOI18N

        jLabel4.setText(bundle.getString("valor")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        valorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        valorField.setName("valorField"); // NOI18N

        jLabel5.setText(bundle.getString("tipo")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        tipoField.setDocument(new BoundedPlainDocument(2));
        tipoField.setName("tipoField"); // NOI18N

        jLabel6.setText(bundle.getString("dtPrevisaoPgto")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        previsaoField.setDateFormatString("dd/MM/yyyy"); // NOI18N
        previsaoField.setName("previsaoField"); // NOI18N

        jLabel8.setText(bundle.getString("dtPgto")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        dtPgtoField.setDateFormatString("dd/MM/yyyy"); // NOI18N
        dtPgtoField.setName("dtPgtoField"); // NOI18N

        jLabel9.setText(bundle.getString("valorPago")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        valorPgtoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        valorPgtoField.setName("valorPgtoField"); // NOI18N

        jLabel7.setText(bundle.getString("contaCorrente")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        contaComboBox.setName("contaComboBox"); // NOI18N

        jLabel10.setText(bundle.getString("obs")); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/swing/app/pedido/Bundle"); // NOI18N
        jLabel10.setName(bundle1.getString("PgtoEditPanel.jLabel10.name")); // NOI18N

        jScrollPane1.setName(bundle1.getString("PgtoEditPanel.jScrollPane1.name")); // NOI18N

        obsTextArea.setColumns(20);
        obsTextArea.setRows(5);
        obsTextArea.setName(bundle1.getString("PgtoEditPanel.obsTextArea.name")); // NOI18N
        jScrollPane1.setViewportView(obsTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(notaField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dtVencimentoField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(contaComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(2, 2, 2)
                                                    .addComponent(jLabel4))
                                                .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(9, 9, 9)
                                                    .addComponent(jLabel5))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(tipoField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(19, 19, 19)
                                                    .addComponent(jLabel6))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(18, 18, 18)
                                                    .addComponent(previsaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(2, 2, 2)
                                                    .addComponent(jLabel8))
                                                .addComponent(dtPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(12, 12, 12)
                                                    .addComponent(jLabel9))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(valorPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jLabel7)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(jLabel10)))
                                    .addGap(86, 86, 86))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtVencimentoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(previsaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tipoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(42, 42, 42)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dtPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(valorPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox contaComboBox;
    private com.toedter.calendar.JDateChooser dtPgtoField;
    private com.toedter.calendar.JDateChooser dtVencimentoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField notaField;
    private javax.swing.JTextArea obsTextArea;
    private com.toedter.calendar.JDateChooser previsaoField;
    private javax.swing.JFormattedTextField tipoField;
    private javax.swing.JFormattedTextField valorField;
    private javax.swing.JFormattedTextField valorPgtoField;
    // End of variables declaration//GEN-END:variables

}
