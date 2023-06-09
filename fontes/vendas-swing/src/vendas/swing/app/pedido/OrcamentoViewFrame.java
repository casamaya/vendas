/*
 * OrcamentoEditPanel.java
 *
 * Created on 30 de Junho de 2007, 11:24
 */
package vendas.swing.app.pedido;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.swing.JFileChooser;
import org.jdesktop.application.Action;
import ritual.swing.TApplication;
import vendas.entity.Orcamento;
import ritual.swing.ViewFrame;
import vendas.beans.EmailBean;
import vendas.beans.PedidoBean;
import vendas.dao.EmpresaDao;
import vendas.dao.OrcamentoDao;
import vendas.entity.ArquivoOrcamento;
import vendas.entity.ArquivoPedido;
import vendas.entity.Params;
import vendas.entity.User;
import vendas.swing.model.ArquivoOrcamentoModel;
import vendas.swing.model.ArquivoPedidoModel;
import vendas.swing.model.ItemOrcamentoModel;
import vendas.util.EditDialog;
import vendas.util.EmailPanel;
import vendas.util.Messages;
import vendas.util.Reports;
import vendas.util.SimpleAuth;

/**
 *
 * @author  Sam
 */
public class OrcamentoViewFrame extends ViewFrame {


    /** Creates new form OrcamentoEditPanel */
    public OrcamentoViewFrame(String title) {
        super(title, false, true, true, true);
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        Orcamento pedido = (Orcamento) obj;

        clienteField.setText(pedido.getCliente().getRazao());

        emissaoField.setValue(pedido.getDtorcamento());
        validadeField.setValue(pedido.getDtvalidade());
        embarqueField.setValue(pedido.getDtEntrega());
        obs2TextArea.setText(pedido.getObs2());
        obs3TextArea.setText(pedido.getObs3());
        pedidoField.setValue(pedido.getIdorcamento());
        represField.setText(pedido.getRepres().getRazao());
        vendedorField.setText(pedido.getVendedor().getNome());
        unidadesField2.setValue(pedido.getVlrUnidades());
        formaPgtoField.setText(pedido.getFormaPgto().getNome());

        totalField2.setValue(pedido.getValor());
        ItemOrcamentoModel itemModel = new ItemOrcamentoModel(pedido.getItems());
        itemModel.setReadOnly(false);
        itensTable2.setModel(itemModel);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.DESCRICAO).setPreferredWidth(280);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.CODIGO).setPreferredWidth(70);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.UNIDADE).setPreferredWidth(36);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.QUANT).setPreferredWidth(80);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.VALOR).setPreferredWidth(80);
        itensTable2.getColumnModel().getColumn(ItemOrcamentoModel.EMBALAGEM).setPreferredWidth(36);

        ArquivoOrcamentoModel arqModel = new ArquivoOrcamentoModel(pedido.getArquivos());
        anexosTable.setModel(arqModel);
        anexosTable.getColumnModel().getColumn(ArquivoOrcamentoModel.NOME).setPreferredWidth(150);
    }

    @Action
    public void editPedido() {
        Orcamento pedido = (Orcamento) getValueObject();
        OrcamentoEditPanel editPanel = new OrcamentoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editOrcamentoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_ORCAMENTO"));
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setOrcamento(pedido);
        while (edtDlg.edit(pedidoBean)) {
            try {
                OrcamentoDao pedidoDao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");
                pedidoDao.updateOrcamento(pedidoBean.getOrcamento());
                object2Field(pedido);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void report() {
        PedidoUtil util = new PedidoUtil();
        Orcamento pedido = (Orcamento) getValueObject();
        try {
            util.imprimirOrcamento(pedido);
        } catch (Exception e) {
            getLogger().error(e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pedidoField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        emissaoField = new javax.swing.JFormattedTextField();
        validadeField = new javax.swing.JFormattedTextField();
        clienteField = new javax.swing.JFormattedTextField();
        represField = new javax.swing.JFormattedTextField();
        vendedorField = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        itensTable2 = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        unidadesField2 = new javax.swing.JFormattedTextField();
        totalField2 = new javax.swing.JFormattedTextField();
        alterarButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        anexosTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        emailItemButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        embarqueField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        formaPgtoField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        obs2TextArea = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        obs3TextArea = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("orcamento")); // NOI18N

        pedidoField.setEditable(false);

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        jLabel3.setText(bundle.getString("cliente")); // NOI18N

        jLabel5.setText(bundle.getString("representada")); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        jLabel4.setText(bundle.getString("dtValidade")); // NOI18N

        emissaoField.setEditable(false);
        emissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        validadeField.setEditable(false);
        validadeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        clienteField.setEditable(false);

        represField.setEditable(false);
        represField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        vendedorField.setEditable(false);
        vendedorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(OrcamentoViewFrame.class);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N

        itensTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itensTable2.setEnabled(false);
        jScrollPane3.setViewportView(itensTable2);

        jLabel20.setText(bundle.getString("unidades")); // NOI18N

        jLabel21.setText(bundle.getString("total")); // NOI18N

        unidadesField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        unidadesField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unidadesField2.setFocusable(false);

        totalField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalField2.setFocusable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unidadesField2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalField2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(447, 447, 447))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 839, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(unidadesField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(OrcamentoViewFrame.class, this);
        alterarButton.setAction(actionMap.get("editPedido")); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("anexos"))); // NOI18N

        anexosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane7.setViewportView(anexosTable);

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/New16.gif"))); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        viewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Open16.gif"))); // NOI18N
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });

        deleteItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });

        emailItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/SendMail16.gif"))); // NOI18N
        emailItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailItemButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewButton)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailItemButton))
                    .addComponent(deleteItemButton))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteItemButton))
                    .addComponent(emailItemButton))
                .addContainerGap())
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );

        jLabel6.setText(bundle.getString("dtEntregaOrcamento")); // NOI18N

        embarqueField.setEditable(false);
        embarqueField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));

        jLabel7.setText(bundle.getString("formaPgto")); // NOI18N

        formaPgtoField.setEditable(false);

        jLabel14.setText(bundle.getString("observacao2")); // NOI18N

        obs2TextArea.setEditable(false);
        obs2TextArea.setLineWrap(true);
        obs2TextArea.setRows(5);
        jScrollPane4.setViewportView(obs2TextArea);

        obs3TextArea.setEditable(false);
        obs3TextArea.setLineWrap(true);
        obs3TextArea.setRows(5);
        jScrollPane5.setViewportView(obs3TextArea);

        jLabel22.setText(bundle.getString("obsCliente")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(validadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(vendedorField, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                                    .addComponent(clienteField)
                                    .addComponent(represField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(embarqueField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(formaPgtoField, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(alterarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel22))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(124, 124, 124))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(validadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(alterarButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(embarqueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(formaPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(clienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(5, 5, 5)
                        .addComponent(represField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!("pdf").equals(getExtension(file))) {
                Messages.errorMessage("Arquivo deve ser PDF.");
                return;
            }
            Orcamento pedido = (Orcamento)getObject();
            ArquivoOrcamento arq = new ArquivoOrcamento();
            arq.setOrcamento(pedido);
            arq.setDescricao(file.getName());
            byte[] bFile = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(bFile);
                fileInputStream.close();
                arq.setArquivo(bFile);
                OrcamentoDao pedidoDao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");
                pedidoDao.insertRecord(arq);
                ArquivoOrcamentoModel model = (ArquivoOrcamentoModel)anexosTable.getModel();
                model.addObject(arq);
                model.fireTableDataChanged();
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
}//GEN-LAST:event_addButtonActionPerformed
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        // TODO add your handling code here:
        int i = anexosTable.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage("Selecione um arquivo");
            return;
        }
        ArquivoOrcamentoModel model = (ArquivoOrcamentoModel)anexosTable.getModel();
        ArquivoOrcamento arq = (ArquivoOrcamento) model.getObject(i);
        try {
            InputStream is = arq.getBlob().getBinaryStream();
            Reports.showReportStream(is, arq.getDescricao());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
}//GEN-LAST:event_viewButtonActionPerformed

    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed

        int i = anexosTable.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage("Selecione um arquivo");
            return;
        }
        if (i >= 0) {
            ArquivoOrcamentoModel model = (ArquivoOrcamentoModel)anexosTable.getModel();
            ArquivoOrcamento arq = (ArquivoOrcamento) model.getObject(i);
            OrcamentoDao pedidoDao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");
            try {
                pedidoDao.deleteRow(arq);
                model.removeObject(i);
            } catch (Exception e) {
                getLogger().error(getBundle().getString("deleteErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
            }
        }
}//GEN-LAST:event_deleteItemButtonActionPerformed
    private void loadProperties(EmailBean email) {
        User user = (User) TApplication.getInstance().getUser();
        TApplication app = TApplication.getInstance();
        String from = user.getEmail();
        String server = user.getServerName();
        String userName = user.getUserName();

        email.setFrom(from);
        email.setServer(server);
        email.setUser(userName);

        Params params = getParams();
        email.setText(params.getEmailMsg());
    }

    private Params getParams() {
        EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
        Object o = new Integer(-1);
        Params value = null;
        try {
            value = (Params) empresaDao.findById(Params.class, o);
        } catch (Exception e) {
        }
        return value;

    }
    private void emailItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailItemButtonActionPerformed

        if (!TApplication.getInstance().isGrant("ENVIAR_ORCAMENTO_POR_EMAIL"))
            return;
        
        int rows[] = anexosTable.getSelectedRows();
        if (rows.length <= 0) {
            Messages.warningMessage(getBundle().getString("selectMany"));
            return;
        }
        Orcamento pedido = (Orcamento) getObject();
        String fileName = pedido.getIdorcamento() + "_" + pedido.getCliente().getFantasia() + ".pdf";
        EmailBean email = (EmailBean) TApplication.getInstance().lookupService("emailBean");
        loadProperties(email);
        TApplication app = TApplication.getInstance();
        EmailPanel editPanel = new EmailPanel();
        User user = (User) TApplication.getInstance().getUser();

        String from = user.getEmail();
        String server = user.getServerName();

        if ((from == null) || (from.length() == 0)) {
            Messages.warningMessage(app.getResourceString("emailConfig"));
            return;
        }
        if ((server == null) || (server.length() == 0)) {
            Messages.warningMessage(app.getResourceString("emailConfig"));
            return;
        }
        EditDialog edtDlg = new EditDialog(app.getResourceString("sendEmail"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(email)) {
            if (email.getTo().length == 0) {
                Messages.errorMessage("Inclua um destinat�rio.");
                continue;
            }
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", server); //server SMTP do GMAIL
            props.put("mail.smtp.auth", "true"); //ativa autenticacao
            props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", user.getPort()); //porta

            SimpleAuth auth = new SimpleAuth(from, user.getEmailPasswd().toString());
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(false); //Habilita o LOG das a��es executadas durante o envio do email

            //Objeto que cont�m a mensagem
            Message msg = new MimeMessage(session);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            try {
                messageBodyPart.setText(email.getText() + '\n' + user.getAssinatura());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                DataSource source = null;
                ArquivoPedidoModel model = (ArquivoPedidoModel) anexosTable.getModel();

                for (int row : rows) {
                    ArquivoPedido arq = (ArquivoPedido) model.getObject(row);

                    source = new ByteArrayDataSource(arq.getBlob().getBinaryStream(), "application/pdf");
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(arq.getDescricao());
                    multipart.addBodyPart(messageBodyPart);
                }
                msg.setContent(multipart);

                //Setando o destinat�rio
                for (String s : email.getTo()) {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
                }
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
                EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
                Object o = new Integer(-1);
                Params value = null;
                value = (Params) empresaDao.findById(Params.class, o);
                // msg.addRecipient(Message.RecipientType.TO, new InternetAddress(value.getEmail()));

                //Setando a origem do email
                msg.setFrom(new InternetAddress(from));
                //Setando o assunto
                msg.setSubject(email.getSubject());


                //Objeto encarregado de enviar os dados para o email
                Transport tr;
                tr = session.getTransport("smtp"); //define smtp para transporte

                tr.connect(server, user.getPort(), from, user.getEmailPasswd());
                msg.saveChanges(); // don't forget this
                tr.sendMessage(msg, msg.getAllRecipients());
                tr.close();
                Messages.infoMessage("Mensagem enviada.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                Messages.infoMessage("Erro ao enviar mensagem.");
            }

        }
}//GEN-LAST:event_emailItemButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton alterarButton;
    private javax.swing.JTable anexosTable;
    private javax.swing.JFormattedTextField clienteField;
    private javax.swing.JButton deleteItemButton;
    private javax.swing.JButton emailItemButton;
    private javax.swing.JFormattedTextField embarqueField;
    private javax.swing.JFormattedTextField emissaoField;
    private javax.swing.JTextField formaPgtoField;
    private javax.swing.JTable itensTable2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextArea obs2TextArea;
    private javax.swing.JTextArea obs3TextArea;
    private javax.swing.JFormattedTextField pedidoField;
    private javax.swing.JFormattedTextField represField;
    private javax.swing.JFormattedTextField totalField2;
    private javax.swing.JFormattedTextField unidadesField2;
    private javax.swing.JFormattedTextField validadeField;
    private javax.swing.JFormattedTextField vendedorField;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}
