/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.table.TableColumn;
import vendas.dao.BancoDao;
import vendas.entity.Params;
import vendas.swing.core.Formats;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.ListComboBoxModel;
import ritual.swing.TApplication;
import ritual.swing.ViewFrame;
import vendas.dao.RepresDao;
import vendas.entity.Banco;
import vendas.entity.ContaRepres;
import vendas.entity.Repres;
import vendas.exception.DAOException;
import vendas.swing.model.ContaEmpresaModel;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;


/**
 *
 * @author Jaime
 */
public class ConfigEditPanel extends ViewFrame {
    
    RepresDao contaDao;
    BancoDao bancoDao;
    JComboBox bancoComboBox;
    ContaEmpresaModel dataModel;
    Params params;
    byte[] blogo;
    byte[] banexo;

    public ConfigEditPanel(String value) {
        super(value, false, true, true, true);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contaDao = (RepresDao)TApplication.getInstance().lookupService("represDao");
        bancoDao = (BancoDao)TApplication.getInstance().lookupService("bancoDao");
        bancoComboBox = new JComboBox();
        try {
            bancoComboBox.setModel(new ListComboBoxModel((List) bancoDao.findAll()));
        } catch (Exception e) {
            getLogger().error(e);
        }
        contasTable.setSurrendersFocusOnKeystroke(true);
        
        boolean habilitar = TApplication.getInstance().getUser().isEscritorio() || TApplication.getInstance().getUser().isAdmin();
        gravarButton.setEnabled(habilitar);
        addButton.setEnabled(habilitar);
        delButton.setEnabled(habilitar);
        logoButton.setEnabled(habilitar);
        anexoButton.setEnabled(habilitar);
    }
    
    @Override
    public void report() {

        URL url;
        url = getClass().getResource(Constants.JRFICHAEMPRESA);
        String reportTitle = "Ficha do Representante";
        List lista = new ArrayList();
        lista.add(params);
        try {
            Reports.showReport(reportTitle, "", url, lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    @Override
    public void object2Field(Object obj) {
        params = (Params) obj;
        enderecoField.setText(params.getEndereco());
        bairroField.setText(params.getBairro());
        cepField.setText(params.getCep());
        cidadeField.setText(params.getCidade());
        ufField.setText(params.getUf());
        enderecoMailField.setText(params.getEnderecoMail());
        bairroMailField.setText(params.getBairroMail());
        cepMailField.setText(params.getCepMail());
        cidadeMailField.setText(params.getCidadeMail());
        ufMailField.setText(params.getUfMail());
        cnpjField.setText(params.getCnpj());
        cxPostalField.setText(params.getCxPostal());
        emailField.setText(params.getEmail());
        fone1Field.setText(params.getFone());
        fone2Field.setText(params.getFone2());
        inscrEstField.setText(params.getInscEstadual());
        razaoField.setText(params.getRazao());
        try {
            dataModel = new ContaEmpresaModel(contaDao.getContasById(-1));
        } catch (Exception e) {
            getLogger().error(e);
        }
        contasTable.setModel(dataModel);
        TableColumn col = contasTable.getColumnModel().getColumn(ContaEmpresaModel.BANCO);
        DefaultCellEditor editor = new DefaultCellEditor(bancoComboBox);
        col.setCellEditor(editor);
        col = contasTable.getColumnModel().getColumn(ContaEmpresaModel.AGENCIA);
        editor = new DefaultCellEditor(new JFormattedTextField());
        col.setCellEditor(editor);
        col = contasTable.getColumnModel().getColumn(ContaEmpresaModel.CONTA);
        editor = new DefaultCellEditor(new JFormattedTextField());
        col.setCellEditor(editor);

        obsPromocaoTextArea.setText(params.getPromocao());
        emailMsgTextArea.setText(params.getEmailMsg());
        emailMsgCobrancaTextArea.setText(params.getEmailMsgCobranca());
        emailMsgClienteTextArea.setText(params.getEmailMsgCliente());
        cobrancaMsgTextArea.setText(params.getMsgCobranca());
        emailMsgAniverTextArea.setText(params.getMsgAniversario());
    }

    @Override
    public void field2Object(Object obj) {
        params = (Params) obj;
        
        params.setEndereco(enderecoField.getText());
        params.setBairro(bairroField.getText());
        params.setCep(cepField.getText());
        params.setCidade(cidadeField.getText());
        params.setUf(ufField.getText());
        params.setEnderecoMail(enderecoMailField.getText());
        params.setBairroMail(bairroMailField.getText());
        params.setCepMail(cepMailField.getText());
        params.setCidadeMail(cidadeMailField.getText());
        params.setUfMail(ufMailField.getText());
        params.setCnpj(cnpjField.getText());
        params.setCxPostal(cxPostalField.getText());
        params.setEmail(emailField.getText());
        params.setFone(fone1Field.getText());
        params.setFone2(fone2Field.getText());
        params.setInscEstadual(inscrEstField.getText());
        params.setRazao(razaoField.getText());
        params.setPromocao(obsPromocaoTextArea.getText());
        params.setEmailMsg(emailMsgTextArea.getText());
        params.setEmailMsgCliente(emailMsgClienteTextArea.getText());
        params.setEmailMsgCobranca(emailMsgCobrancaTextArea.getText());
        params.setMsgAniversario(emailMsgAniverTextArea.getText());
        params.setMsgCobranca(cobrancaMsgTextArea.getText());
        if (blogo != null)
            params.setArquivo(blogo);
        if (banexo != null)
            params.setAnexoPadrao(banexo);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        razaoLabel = new javax.swing.JLabel();
        fone1Label = new javax.swing.JLabel();
        fone1Field = new javax.swing.JFormattedTextField();
        cxPostalLabel = new javax.swing.JLabel();
        cxPostalField = new javax.swing.JFormattedTextField();
        inscrEstLabel = new javax.swing.JLabel();
        cnpjLabel = new javax.swing.JLabel();
        cnpjField = new javax.swing.JFormattedTextField();
        inscrEstField = new javax.swing.JFormattedTextField();
        emailLabel = new javax.swing.JLabel();
        emailField = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contasTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();
        observacaoLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obsPromocaoTextArea = new javax.swing.JTextArea();
        cxPostalLabel1 = new javax.swing.JLabel();
        fone2Field = new javax.swing.JFormattedTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        enderecoField = new javax.swing.JFormattedTextField();
        bairroField = new javax.swing.JFormattedTextField();
        cidadeField = new javax.swing.JFormattedTextField();
        cepField = new javax.swing.JFormattedTextField(Formats.createFormatter("########"));
        ufField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        ufLabel = new javax.swing.JLabel();
        cepLabel = new javax.swing.JLabel();
        cidadeLabel = new javax.swing.JLabel();
        enderecoLabel = new javax.swing.JLabel();
        bairroLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        enderecoMailField = new javax.swing.JFormattedTextField();
        ufLabel1 = new javax.swing.JLabel();
        ufMailField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        cepMailField = new javax.swing.JFormattedTextField(Formats.createFormatter("########"));
        cepLabel1 = new javax.swing.JLabel();
        cidadeMailField = new javax.swing.JFormattedTextField();
        cidadeLabel1 = new javax.swing.JLabel();
        bairroMailField = new javax.swing.JFormattedTextField();
        bairroLabel1 = new javax.swing.JLabel();
        enderecoLabel1 = new javax.swing.JLabel();
        razaoField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        emailMsgTextArea = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        emailMsgClienteTextArea = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        emailMsgCobrancaTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        emailMsgAniverTextArea = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        cobrancaMsgTextArea = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        logoButton = new javax.swing.JButton();
        anexoButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        gravarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        razaoLabel.setText(bundle.getString("razaoSocial")); // NOI18N

        fone1Label.setText(bundle.getString("fone1")); // NOI18N

        fone1Field.setColumns(12);

        cxPostalLabel.setText(bundle.getString("caixaPostal")); // NOI18N

        cxPostalField.setColumns(12);

        inscrEstLabel.setText(bundle.getString("inscrEstadual")); // NOI18N

        cnpjLabel.setText(bundle.getString("cnpj")); // NOI18N

        emailLabel.setText(bundle.getString("email")); // NOI18N

        emailField.setColumns(40);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("contas"))); // NOI18N

        contasTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(contasTable);

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        delButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(delButton, 0, 28, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addContainerGap())
        );

        observacaoLabel2.setText(bundle.getString("obsPromocao")); // NOI18N

        obsPromocaoTextArea.setColumns(20);
        obsPromocaoTextArea.setLineWrap(true);
        obsPromocaoTextArea.setRows(5);
        jScrollPane3.setViewportView(obsPromocaoTextArea);

        cxPostalLabel1.setText(bundle.getString("fone2")); // NOI18N

        fone2Field.setColumns(12);

        enderecoField.setColumns(40);
        enderecoField.setDocument(new BoundedPlainDocument(40));

        bairroField.setColumns(30);
        bairroField.setDocument(new BoundedPlainDocument(25));

        cidadeField.setColumns(30);
        cidadeField.setDocument(new BoundedPlainDocument(30));

        cepField.setColumns(8);

        ufField.setColumns(2);

        ufLabel.setText(bundle.getString("uf")); // NOI18N

        cepLabel.setText(bundle.getString("cep")); // NOI18N

        cidadeLabel.setText(bundle.getString("cidade")); // NOI18N

        enderecoLabel.setText(bundle.getString("endereco")); // NOI18N

        bairroLabel.setText(bundle.getString("bairro")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enderecoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bairroLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cepLabel)
                                    .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ufLabel)))
                            .addComponent(cidadeLabel)))
                    .addComponent(enderecoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enderecoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cidadeLabel)
                            .addComponent(bairroLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(cepLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(ufLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Endereço comercial", jPanel7);

        enderecoMailField.setDocument(new BoundedPlainDocument(50));

        ufLabel1.setText(bundle.getString("uf")); // NOI18N

        ufMailField.setColumns(2);

        cepMailField.setColumns(8);

        cepLabel1.setText(bundle.getString("cep")); // NOI18N

        cidadeMailField.setColumns(30);
        cidadeMailField.setDocument(new BoundedPlainDocument(30));

        cidadeLabel1.setText(bundle.getString("cidade")); // NOI18N

        bairroMailField.setColumns(30);
        bairroMailField.setDocument(new BoundedPlainDocument(25));

        bairroLabel1.setText(bundle.getString("bairro")); // NOI18N

        enderecoLabel1.setText(bundle.getString("endereco")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bairroMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bairroLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(cidadeMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cepLabel1)
                                            .addComponent(cepMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ufMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ufLabel1)))
                                    .addComponent(cidadeLabel1)))
                            .addComponent(enderecoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addComponent(enderecoMailField))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enderecoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoMailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cidadeLabel1)
                            .addComponent(bairroLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroMailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(cepLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ufMailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cepMailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cidadeMailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(ufLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Endereço de correspondência", jPanel8);

        razaoField.setDocument(new BoundedPlainDocument(50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(observacaoLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(emailLabel)
                                .addComponent(razaoLabel)
                                .addComponent(jTabbedPane2)
                                .addComponent(emailField))
                            .addGap(40, 40, 40)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cnpjLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(inscrEstLabel)
                                        .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(fone1Label)
                                        .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cxPostalLabel1)
                                        .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cxPostalLabel)
                                        .addComponent(cxPostalField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(razaoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(fone1Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(cxPostalLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cxPostalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(cxPostalLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cnpjLabel)
                            .addComponent(inscrEstLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(emailLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(observacaoLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        jTabbedPane1.addTab("Dados da empresa", jPanel1);

        jLabel3.setText(bundle.getString("mensagemRepres")); // NOI18N

        emailMsgTextArea.setColumns(20);
        emailMsgTextArea.setLineWrap(true);
        emailMsgTextArea.setRows(5);
        jScrollPane4.setViewportView(emailMsgTextArea);

        emailMsgClienteTextArea.setColumns(20);
        emailMsgClienteTextArea.setLineWrap(true);
        emailMsgClienteTextArea.setRows(5);
        jScrollPane6.setViewportView(emailMsgClienteTextArea);

        jLabel5.setText(bundle.getString("mensagemCliente")); // NOI18N

        emailMsgCobrancaTextArea.setColumns(20);
        emailMsgCobrancaTextArea.setLineWrap(true);
        emailMsgCobrancaTextArea.setRows(5);
        jScrollPane7.setViewportView(emailMsgCobrancaTextArea);

        jLabel6.setText(bundle.getString("mensagemCobranca")); // NOI18N

        emailMsgAniverTextArea.setColumns(20);
        emailMsgAniverTextArea.setLineWrap(true);
        emailMsgAniverTextArea.setRows(5);
        jScrollPane8.setViewportView(emailMsgAniverTextArea);

        jLabel7.setText(bundle.getString("msgAniversario")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                    .addComponent(jLabel7))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("E-mail", jPanel2);

        jLabel4.setText(bundle.getString("mensagem")); // NOI18N

        cobrancaMsgTextArea.setColumns(20);
        cobrancaMsgTextArea.setLineWrap(true);
        cobrancaMsgTextArea.setRows(5);
        jScrollPane5.setViewportView(cobrancaMsgTextArea);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(417, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cobrança", jPanel4);

        logoButton.setText(bundle.getString("definirLogo")); // NOI18N
        logoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoButtonActionPerformed(evt);
            }
        });

        anexoButton.setText(bundle.getString("definirAnexo")); // NOI18N
        anexoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anexoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(logoButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(anexoButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, Short.MAX_VALUE))
                .addContainerGap(801, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anexoButton)
                .addContainerGap(481, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mais...", jPanel5);

        gravarButton.setText(bundle.getString("save")); // NOI18N
        gravarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gravarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(886, Short.MAX_VALUE)
                .addComponent(gravarButton)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gravarButton)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        ContaRepres conta = new ContaRepres();
        Banco banco = null;
        try {
            banco = (Banco)bancoDao.findById(Banco.class, "001");
        } catch (Exception e) {
            getLogger().error(e);
        }
        conta.setBanco(banco);
        conta.setAgencia("0");
        conta.setContaCorrente("0");
        conta.setNome((razaoField.getText()));
        conta.setCpfCnpj(cnpjField.getText());
        try {
            conta.setRepres((Repres)contaDao.findById(Repres.class, -1));
            contaDao.insertRecord(conta);
            dataModel.addObject(conta);
            dataModel.fireTableDataChanged();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
        int row = contasTable.getSelectedRow();
        if (row >= 0) {
            try {
                contaDao.deleteRow(dataModel.getObject(row));
                dataModel.removeObject(row);
                dataModel.fireTableDataChanged();
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
                Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
            }
        }
    }//GEN-LAST:event_delButtonActionPerformed

    private void logoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoButtonActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            blogo = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(blogo);
                fileInputStream.close();
            } catch (Exception e) {
                blogo = null;
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }//GEN-LAST:event_logoButtonActionPerformed

    private void anexoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anexoButtonActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            banexo = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(banexo);
                fileInputStream.close();
            } catch (Exception e) {
                banexo = null;
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }//GEN-LAST:event_anexoButtonActionPerformed

    private void gravarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gravarButtonActionPerformed
        try {
            field2Object(params);
            contaDao.updateRow(params);
            Messages.infoMessage("Dados gravados.");
        } catch (DAOException ex) {
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }//GEN-LAST:event_gravarButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton anexoButton;
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JLabel bairroLabel;
    private javax.swing.JLabel bairroLabel1;
    private javax.swing.JFormattedTextField bairroMailField;
    private javax.swing.JFormattedTextField cepField;
    private javax.swing.JLabel cepLabel;
    private javax.swing.JLabel cepLabel1;
    private javax.swing.JFormattedTextField cepMailField;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel;
    private javax.swing.JLabel cidadeLabel1;
    private javax.swing.JFormattedTextField cidadeMailField;
    private javax.swing.JFormattedTextField cnpjField;
    private javax.swing.JLabel cnpjLabel;
    private javax.swing.JTextArea cobrancaMsgTextArea;
    private javax.swing.JTable contasTable;
    private javax.swing.JFormattedTextField cxPostalField;
    private javax.swing.JLabel cxPostalLabel;
    private javax.swing.JLabel cxPostalLabel1;
    private javax.swing.JButton delButton;
    private javax.swing.JFormattedTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextArea emailMsgAniverTextArea;
    private javax.swing.JTextArea emailMsgClienteTextArea;
    private javax.swing.JTextArea emailMsgCobrancaTextArea;
    private javax.swing.JTextArea emailMsgTextArea;
    private javax.swing.JFormattedTextField enderecoField;
    private javax.swing.JLabel enderecoLabel;
    private javax.swing.JLabel enderecoLabel1;
    private javax.swing.JFormattedTextField enderecoMailField;
    private javax.swing.JFormattedTextField fone1Field;
    private javax.swing.JLabel fone1Label;
    private javax.swing.JFormattedTextField fone2Field;
    private javax.swing.JButton gravarButton;
    private javax.swing.JFormattedTextField inscrEstField;
    private javax.swing.JLabel inscrEstLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton logoButton;
    private javax.swing.JTextArea obsPromocaoTextArea;
    private javax.swing.JLabel observacaoLabel2;
    private javax.swing.JFormattedTextField razaoField;
    private javax.swing.JLabel razaoLabel;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JLabel ufLabel;
    private javax.swing.JLabel ufLabel1;
    private javax.swing.JFormattedTextField ufMailField;
    // End of variables declaration//GEN-END:variables
}
