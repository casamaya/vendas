/*
 * VendedorViewFrame.java
 *
 * Created on 8 de Julho de 2008, 09:59
 */
package vendas.swing.app.auxiliar;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.jdesktop.application.Action;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.FractionCellRenderer;
import ritual.swing.TApplication;
import ritual.util.NumberUtils;
import vendas.dao.VendedorDao;
import vendas.entity.RoteiroVendedor;
import vendas.entity.Vendedor;
import vendas.swing.core.Formats;
import ritual.swing.ViewFrame;
import vendas.entity.ComissaoVendedor;
import vendas.entity.ComissaoVendedorPK;
import vendas.entity.ContatoVendedor;
import vendas.swing.app.cliente.ContatoVendedorEditPanel;
import vendas.swing.model.ComissaoVendedorModel;
import vendas.swing.model.ContatoVendedorModel;
import vendas.swing.model.RoteiroVendedorModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public class VendedorViewFrame extends ViewFrame {

    private final int CADASTRO = 0;
    private final int ROTEIROS = 1;
    private final int COMISSOES = 2;
    private final int CONTATOS = 3;
    private Vendedor vendedor;
    private VendedorDao vendedorDao;

    /** Creates new form VendedorViewFrame */
    public VendedorViewFrame(String title) {
        super(title, false, true, true, true);
        initComponents();

    }

    @Override
    protected void object2Field(Object obj) {
        getLogger().info("object2Field");
        setRenderer();
        vendedor = (Vendedor) obj;
        
        List lista = (List)vendedor.getContatos();
        Collections.sort(lista, new ContatoComparator());

        roteirosTable.setModel(new RoteiroVendedorModel((List) vendedor.getRoteiros()));
        comissoesTable.setModel(new ComissaoVendedorModel((List) vendedor.getComissoesVendedor()));
        //contatosTable.setModel(new ContatoVendedorModel((List) vendedor.getContatos()));
        contatosTable.setModel(new ContatoVendedorModel(lista));
        contatosTable.getColumnModel().getColumn(ContatoVendedorModel.NOME).setPreferredWidth(80);
        contatosTable.getColumnModel().getColumn(ContatoVendedorModel.MSN).setPreferredWidth(80);
        contatosTable.getColumnModel().getColumn(ContatoVendedorModel.ANIVER).setPreferredWidth(50);
        bairroField.setText(vendedor.getBairro());
        cidadeField.setText(vendedor.getCidade());
        cepField.setText(vendedor.getCep());
        comissionadoCheckBox.setSelected(vendedor.getRecebeComissao().booleanValue());
        ativoCheckBox.setSelected(vendedor.isAtivo());
        enderecoField.setText(vendedor.getEndereco());
        fone1Field.setText(vendedor.getFone1());
        fone2Field.setText(vendedor.getFone2());
        nomeField.setText(vendedor.getNome());
        ufField.setText(vendedor.getUf());
        valComissaoField.setValue(vendedor.getValorComissao());
        aniversarioField.setText(vendedor.getAniversarioExtenso());

    }

    public VendedorDao getVendedorDao() {
        return vendedorDao;
    }

    public void setVendedorDao(VendedorDao vendedorDao) {
        this.vendedorDao = vendedorDao;
    }

    private void setRenderer() {
        roteirosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        comissoesTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
    }

    @Override
    public void view() {
        edit();
    }

    @Override
    public void edit() {
        Boolean isGrant;
        JTable table;
        switch (tabbedPane.getSelectedIndex()) {
            case CADASTRO:
                vendedorEdit();
                return;
            case ROTEIROS:
                table = roteirosTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_ROTEIRO");
                break;
            case COMISSOES:
                table = comissoesTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_COMISSAO_VENDEDOR");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_CONTATO_VENDEDOR");
                break;
            default:
                return;
        }
        
        if (!isGrant)
            return;

        int i = table.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        switch (tabbedPane.getSelectedIndex()) {
            case ROTEIROS:
                roteiroEdit();
                break;
            case COMISSOES:
                comissaoEdit();
                break;
            case CONTATOS:
                contatoEdit();
                break;
        }
    }

    @Override
    public void insert() {
        TApplication app = TApplication.getInstance();
        switch (tabbedPane.getSelectedIndex()) {
            case ROTEIROS:
                if (app.isGrant("INCLUIR_ROTEIRO"))
                    roteiroInsert();
                break;
            case COMISSOES:
                if (app.isGrant("INCLUIR_COMISSAO_VENDEDOR"))
                    comissaoInsert();
                break;
            case CONTATOS:
                if (app.isGrant("INCLUIR_CONTATO_VENDEDOR"))
                    contatoInsert();
                break;
        }
    }

    @Override
    public void remove() {
        TApplication app = TApplication.getInstance();
        JTable table;
        Boolean isGrant;
        switch (tabbedPane.getSelectedIndex()) {
            case ROTEIROS:
                table = roteirosTable;
                isGrant = app.isGrant("EXCLUIR_ROTEIRO");
                break;
            case COMISSOES:
                table = comissoesTable;
                isGrant = app.isGrant("EXCLUIR_COMISSAO_VENDEDOR");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = app.isGrant("EXCLUIR_CONTATO_VENDEDOR");
                break;
            default:
                return;
        }

        if (!isGrant)
            return;
        
        int i = table.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        if (!Messages.deleteQuestion()) {
            return;
        }
        switch (tabbedPane.getSelectedIndex()) {
            case ROTEIROS:
                roteiroRemove();
                break;
            case COMISSOES:
                comissaoRemove();
                break;
            case CONTATOS:
                contatoRemove();
                break;
        }
    }

    private void contatoRemove() {
        getLogger().info("contatoRemove");
        try {
            ContatoVendedorModel compradorModel = (ContatoVendedorModel) contatosTable.getModel();
            ContatoVendedor comprador = (ContatoVendedor) compradorModel.getObject(contatosTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getidContato());
            vendedorDao.deleteRow(comprador);
            compradorModel.removeObject(contatosTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    @Action
    public void vendedorEdit() {
        VendedorEditPanel editPanel = new VendedorEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editVendedorTitle"));
        Boolean isReadOnly;
        
        Vendedor vendedor = (Vendedor)getValueObject();
        TApplication app = TApplication.getInstance();
        
        if (app.getUser().isVendedor() && app.getUser().getIdvendedor().compareTo(vendedor.getIdVendedor()) == 0)
            isReadOnly = Boolean.FALSE;
        else
            isReadOnly = !app.isGrant("ALTERAR_VENDEDOR");

        edtDlg.setEditPanel(editPanel, isReadOnly);
        
        while (edtDlg.edit(getValueObject())) {
            try {
                if (!isReadOnly) {
                    vendedorDao.updateRow(getValueObject());
                    object2Field(getValueObject());
                }
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void roteiroEdit() {
    }

    @Action
    public void contatoEdit() {
        ContatoVendedorEditPanel editPanel = new ContatoVendedorEditPanel();
        ContatoVendedor contato = (ContatoVendedor) ((ContatoVendedorModel) contatosTable.getModel()).getObject(contatosTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editCompradorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(contato)) {
            try {
                vendedorDao.updateRow(contato);
                ((ContatoVendedorModel) contatosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void comissaoEdit() {
    }

    private void roteiroInsert() {
        RoteiroEditPanel editPanel = new RoteiroEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRoteiroTitle"));
        edtDlg.setEditPanel(editPanel);

        RoteiroVendedor roteiro = new RoteiroVendedor();
        roteiro.setVendedor(vendedor);
        while (edtDlg.edit(roteiro)) {
            try {
                vendedor.getRoteiros().add(roteiro);
                vendedorDao.updateRow(vendedor);
                ((RoteiroVendedorModel) roteirosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }

        }
    }

    private void contatoInsert() {
        ContatoVendedorEditPanel editPanel = new ContatoVendedorEditPanel();
        ContatoVendedor comprador = new ContatoVendedor();
        comprador.setVendedor(vendedor);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addContatoVendedorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                vendedor.getContatos().add(comprador);
                vendedorDao.insertRecord(comprador);
                ((ContatoVendedorModel) contatosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void comissaoInsert() {
        ComissaoVendedorEditPanel editPanel = new ComissaoVendedorEditPanel();
        editPanel.init();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addComissaoVendedorTitle"));
        edtDlg.setEditPanel(editPanel);

        ComissaoVendedor comissao = new ComissaoVendedor();
        ComissaoVendedorPK pk = new ComissaoVendedorPK();
        pk.setIdvendedor(vendedor.getIdVendedor());
        comissao.setComissaoVendedorPK(pk);
        while (edtDlg.edit(comissao)) {
            try {
                vendedor.getComissoesVendedor().add(comissao);
                vendedorDao.insertRecord(comissao);
                ((ComissaoVendedorModel) comissoesTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void roteiroRemove() {
        getLogger().info("roteiroRemove");
        try {
            RoteiroVendedorModel roteiroModel = (RoteiroVendedorModel) roteirosTable.getModel();
            RoteiroVendedor roteiroVendedor = (RoteiroVendedor) roteiroModel.getObject(roteirosTable.getSelectedRow());
            getLogger().info("removendo " + roteiroVendedor.getIdRoteiro());
            vendedorDao.deleteRow(roteiroVendedor);
            roteiroModel.removeObject(roteirosTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void comissaoRemove() {
        getLogger().info("comissaoRemove");
        try {
            ComissaoVendedorModel comissaoModel = (ComissaoVendedorModel) roteirosTable.getModel();
            ComissaoVendedor comissaoVendedor = (ComissaoVendedor) comissaoModel.getObject(roteirosTable.getSelectedRow());
            getLogger().info("removendo " + comissaoVendedor.getComissaoVendedorPK().toString());
            vendedorDao.deleteRow(comissaoVendedor);
            comissaoModel.removeObject(roteirosTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        cadastroPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        nomeLabel = new javax.swing.JLabel();
        nomeField = new javax.swing.JFormattedTextField();
        enderecoLabel = new javax.swing.JLabel();
        enderecoField = new javax.swing.JFormattedTextField();
        bairroLabel = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        cidadeLabel = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        cepLabel = new javax.swing.JLabel();
        cepField = new javax.swing.JFormattedTextField();
        ufLabel = new javax.swing.JLabel();
        ufField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        fone1Label = new javax.swing.JLabel();
        fone1Field = new javax.swing.JFormattedTextField();
        fone2Label = new javax.swing.JLabel();
        fone2Field = new javax.swing.JFormattedTextField();
        comissionadoCheckBox = new javax.swing.JCheckBox();
        valComissaoLabel = new javax.swing.JLabel();
        valComissaoField = Formats.newFloatFormat();
        jButton1 = new javax.swing.JButton();
        ativoCheckBox = new javax.swing.JCheckBox();
        ufLabel1 = new javax.swing.JLabel();
        aniversarioField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roteirosTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        comissoesTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        contatosTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        tabbedPane.setName("tabbedPane"); // NOI18N

        cadastroPanel.setName("cadastroPanel"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("dadosVendedor"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        nomeLabel.setText(bundle.getString("nome")); // NOI18N
        nomeLabel.setName("nomeLabel"); // NOI18N

        nomeField.setColumns(40);
        nomeField.setDocument(new BoundedPlainDocument(40));
        nomeField.setFocusable(false);
        nomeField.setName("nomeField"); // NOI18N

        enderecoLabel.setText(bundle.getString("endereco")); // NOI18N
        enderecoLabel.setName("enderecoLabel"); // NOI18N

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

        fone1Label.setText(bundle.getString("fone1")); // NOI18N
        fone1Label.setName("fone1Label"); // NOI18N

        fone1Field.setColumns(12);
        fone1Field.setFocusable(false);
        fone1Field.setName("fone1Field"); // NOI18N

        fone2Label.setText(bundle.getString("fone2")); // NOI18N
        fone2Label.setName("fone2Label"); // NOI18N

        fone2Field.setColumns(12);
        fone2Field.setFocusable(false);
        fone2Field.setName("fone2Field"); // NOI18N

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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(VendedorViewFrame.class, this);
        jButton1.setAction(actionMap.get("vendedorEdit")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        ativoCheckBox.setText(bundle.getString("ativo")); // NOI18N
        ativoCheckBox.setName("ativoCheckBox"); // NOI18N

        ufLabel1.setText(bundle.getString("uf")); // NOI18N
        ufLabel1.setName("ufLabel1"); // NOI18N

        aniversarioField.setFocusable(false);
        aniversarioField.setName("aniversarioField"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                                    .addComponent(ufLabel)))
                            .addComponent(ufLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valComissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aniversarioField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ativoCheckBox)))
                    .addComponent(enderecoField, 0, 0, Short.MAX_VALUE)
                    .addComponent(nomeField, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeLabel)
                    .addComponent(nomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aniversarioField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ufLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ativoCheckBox)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cadastroPanelLayout = new javax.swing.GroupLayout(cadastroPanel);
        cadastroPanel.setLayout(cadastroPanelLayout);
        cadastroPanelLayout.setHorizontalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );
        cadastroPanelLayout.setVerticalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("cadastro"), cadastroPanel); // NOI18N

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("roteiros"), jPanel2); // NOI18N

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
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("comissoes"), jPanel3); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        contatosTable.setModel(new javax.swing.table.DefaultTableModel(
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
        contatosTable.setName("contatosTable"); // NOI18N
        jScrollPane3.setViewportView(contatosTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("contatos"), jPanel4); // NOI18N

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField aniversarioField;
    private javax.swing.JCheckBox ativoCheckBox;
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JLabel bairroLabel;
    private javax.swing.JPanel cadastroPanel;
    private javax.swing.JFormattedTextField cepField;
    private javax.swing.JLabel cepLabel;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel;
    private javax.swing.JCheckBox comissionadoCheckBox;
    private javax.swing.JTable comissoesTable;
    private javax.swing.JTable contatosTable;
    private javax.swing.JFormattedTextField enderecoField;
    private javax.swing.JLabel enderecoLabel;
    private javax.swing.JFormattedTextField fone1Field;
    private javax.swing.JLabel fone1Label;
    private javax.swing.JFormattedTextField fone2Field;
    private javax.swing.JLabel fone2Label;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JFormattedTextField nomeField;
    private javax.swing.JLabel nomeLabel;
    private javax.swing.JTable roteirosTable;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JLabel ufLabel;
    private javax.swing.JLabel ufLabel1;
    private javax.swing.JFormattedTextField valComissaoField;
    private javax.swing.JLabel valComissaoLabel;
    // End of variables declaration//GEN-END:variables
}

class ContatoComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        ContatoVendedor c1 = (ContatoVendedor) obj1;
        ContatoVendedor c2 = (ContatoVendedor) obj2;
        return c1.getContato().compareTo(c2.getContato());
    }
}