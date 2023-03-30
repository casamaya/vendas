/*
 * RepresFrame.java
 *
 * Created on 11 de Dezembro de 2007, 23:23
 */
package vendas.swing.app.repres;

import java.math.BigDecimal;
import vendas.swing.core.Formats;
import ritual.swing.BoundedPlainDocument;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import org.jdesktop.application.Action;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import vendas.beans.AlteraPreco;
import vendas.beans.ClientesFilter;
import vendas.beans.ProdutoFilter;
import vendas.swing.model.ClienteRepresModel;
import vendas.swing.model.ContaRepresModel;
import vendas.swing.model.EntradaRepresModel;
import vendas.swing.model.FormaPgtoRepresModel;
import vendas.swing.model.RepresClienteModel;
import vendas.swing.model.VendedorRepresModel;
import vendas.entity.ClienteRepres;
import vendas.entity.ContaRepres;
import vendas.entity.EntradaRepres;
import vendas.entity.FormaPgtoRepres;
import vendas.entity.Repres;
import vendas.entity.VendedorRepres;
import vendas.dao.BancoDao;
import vendas.dao.ClienteDao;
import vendas.dao.FormaPgtoDao;
import vendas.dao.GrupoProdutoDao;
import vendas.dao.ProdutoDao;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import vendas.dao.UnidadeProdutoDao;
import vendas.entity.Produto;
import vendas.entity.RepresProduto;
import vendas.swing.app.produto.AlterarPrecoEditPanel;
import vendas.swing.app.produto.ProdutoRepresEditPanel;
import vendas.swing.app.produto.RepresProdutoDataSource;
import ritual.swing.ViewFrame;
import ritual.util.DateUtils;
import ritual.util.DateVerifier;
import ritual.util.NumberUtils;
import vendas.beans.RepresFilter;
import vendas.dao.SegMercadoDao;
import vendas.dao.TransportadorDao;
import vendas.entity.RepresContato;
import vendas.entity.SegMercado;
import vendas.entity.Transportador;
import vendas.swing.app.cliente.ClienteSegmentoPanel;
import vendas.swing.app.cliente.RepresTranspPanel;
import vendas.swing.model.ProdutoRepresTableModel;
import vendas.swing.model.RepresContatoModel;
import vendas.swing.model.RepresSegmentoModel;
import vendas.swing.model.RepresTranspModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;
import vendas.util.StringUtils;

/**
 *
 * @author  Sam
 */
public class RepresViewFrame extends ViewFrame {

    private ClienteDao clienteDao;
    private FormaPgtoDao formaPgtoDao;
    private RepresDao represDao;
    private Repres repres;
    private BancoDao bancoDao;
    private ProdutoDao produtoDao;
    private GrupoProdutoDao grupoDao;
    private UnidadeProdutoDao unidadeDao;
    private SegMercadoDao segDao;
    private TransportadorDao transpDao;
    private final int CADASTRO = 0;
    private final int FORMAPGTO = 1;
    private final int PRODUTOS = 2;
    private final int VENDEDORES = 3;
    private final int RECEBIMENTOS = 4;
    private final int CONTAS = 5;
    private final int CLIENTES = 6;
    private final int CONTATOS = 7;
    private final int SEGMERCADO = 8;
    private final int TRANSPORTADOR = 9;

    /** Creates new form RepresFrame */
    public RepresViewFrame(String title) {
        super(title);
        TApplication app = TApplication.getInstance();
        initComponents();
        represDao = (RepresDao) app.lookupService("represDao");
        formaPgtoDao = (FormaPgtoDao) app.lookupService("formaPgtoDao");
        clienteDao = (ClienteDao) app.lookupService("clienteDao");
        bancoDao = (BancoDao) app.lookupService("bancoDao");
        produtoDao = (ProdutoDao) app.lookupService("produtoDao");
        grupoDao = (GrupoProdutoDao) app.lookupService("grupoProdutoDao");
        unidadeDao = (UnidadeProdutoDao) app.lookupService("undProdutoDao");
        segDao = (SegMercadoDao) app.lookupService("segMercadoDao");
        transpDao = (TransportadorDao) app.lookupService("transportadorDao");
        alterarButton.setEnabled(app.getUser().isAdmin() || TApplication.getInstance().getUser().isEscritorio());
        precoButton.setEnabled(app.getUser().isAdmin());
        zerarButton.setEnabled(app.getUser().isAdmin());
    }

    public void setRepresDao(RepresDao srvc) {
        represDao = srvc;
    }

    public void setClienteService(ClienteDao srvc) {
        clienteDao = srvc;
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        TableColumn col;
        repres = (Repres) obj;
        repres2Field();
        ClientesFilter filter = new ClientesFilter();
        filter.setRepres(repres);
        filter.setOrder(1);
        List clientes = represDao.findClientes(filter);
        clientesTable.setModel(new RepresClienteModel(clientes));
        col = clientesTable.getColumnModel().getColumn(RepresClienteModel.NOME);
        col.setPreferredWidth(200);

        formaPgtoTable.setModel(new FormaPgtoRepresModel((List) repres.getFormasPgto()));
        col = formaPgtoTable.getColumnModel().getColumn(FormaPgtoRepresModel.FORMAPGTO);
        col.setPreferredWidth(150);

        vendedoresTable.setModel(new VendedorRepresModel((List) repres.getVendedores()));
        col = vendedoresTable.getColumnModel().getColumn(VendedorRepresModel.NOME);
        col.setPreferredWidth(150);

        RepresFilter represFilter = new RepresFilter();
        represFilter.setRepres(repres);
        represFilter.setDtRecebimentoIni(DateUtils.getNextDate(DateUtils.getDate(), -30));
        represFilter.setDtRecebimentoEnd(DateUtils.getDate());
        
        dtEntrada1Field.setDate(DateUtils.getNextDate(DateUtils.getDate(), -30));
        dtEntrada2Field.setDate(DateUtils.getDate());

        recebimentosTable.setModel(new EntradaRepresModel(represDao.findRecebimentosByRepres(represFilter)));
        recebimentosTable.getColumnModel().getColumn(EntradaRepresModel.DATA).setPreferredWidth(80);
        recebimentosTable.getColumnModel().getColumn(EntradaRepresModel.OBS).setPreferredWidth(200);

        contatosTable.setModel(new RepresContatoModel((List) repres.getContatos()));
        col = contatosTable.getColumnModel().getColumn(RepresContatoModel.ENDERECO);
        col.setPreferredWidth(200);

        contasTable.setModel(new ContaRepresModel((List) repres.getContas()));
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.TIPOCONTA);
        col.setPreferredWidth(90);
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.BANCO);
        col.setPreferredWidth(150);
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.CONTA);
        col.setPreferredWidth(95);
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.NOME);
        col.setPreferredWidth(170);
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.CPFCNPJ);
        col.setPreferredWidth(100);
        col = contasTable.getColumnModel().getColumn(ContaRepresModel.CPFCNPJ);
        col.setPreferredWidth(95);

        segmentoTable.setModel(new RepresSegmentoModel((List) repres.getSegmentos()));
        col = segmentoTable.getColumnModel().getColumn(0);
        col.setPreferredWidth(150);

        transpTable.setModel(new RepresTranspModel((List) repres.getTransportadores()));
        col = transpTable.getColumnModel().getColumn(1);
        col.setPreferredWidth(150);
        col = transpTable.getColumnModel().getColumn(2);
        col.setPreferredWidth(150);

        updateProduto();
        col = produtosTable.getColumnModel().getColumn(1);
        col.setPreferredWidth(150);
        formaPgtoTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        formaPgtoTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        produtosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        produtosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        vendedoresTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        produtosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        recebimentosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        recebimentosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        contasTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        contasTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        clientesTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        clientesTable.setDefaultRenderer(Date.class, new DateCellRenderer());
    }

    private void repres2Field() {
        ativoCheckBox.setSelected(repres.getAtivo());
        bairroField.setText(repres.getBairro());
        cepField.setText(StringUtils.formatarCep(repres.getCep()));
        cidadeField.setText(repres.getCidade());
        cnpjField.setText(StringUtils.formatarCnpj(repres.getCnpj()));
        comissaoTextField.setValue(repres.getComissao());
        dtInclusaoField.setValue(repres.getDtInclusao());
        enderecoField.setText(repres.getEndereco());
        fone1Field.setText(repres.getFone1());
        fone2Field.setText(repres.getFone2());
        fone3Field.setText(repres.getFone3());
        inscrEstField.setText(repres.getInscrEstadual());
        obsPedidoTextArea.setText(repres.getObservacao());
        obsPedidoClienteTextArea.setText(repres.getObservacaoCliente());
        obsProdutoTextArea.setText(repres.getObsProduto());
        //obsPromocaoTextArea.setText(repres.getObsPromocao());
        metaVendaTextArea.setText(repres.getMetaVenda());
        razaoField.setText(repres.getRazao());
        ufField.setText(repres.getUf());
        saldoField.setValue(repres.getTotal());
        diasTextField.setValue(repres.getDiasAtendimento());
        reduzidoField.setText(repres.getFantasia());
        siteField.setText(repres.getSite());
        freteTextField.setValue(repres.getValorFrete());
        if (repres.getDataTabela() != null) {
            dataTabelaTextField.setText(DateUtils.format(repres.getDataTabela()));
        } else {
            dataTabelaTextField.setText("");
        }
    }

    @Override
    public void remove() {
        getLogger().info("remove " + tabbedPane.getSelectedIndex());
        TApplication app = TApplication.getInstance();
        Boolean isGrant;
        JTable table;
        switch (tabbedPane.getSelectedIndex()) {
            case FORMAPGTO:
                table = formaPgtoTable;
                isGrant = app.isGrant("EXCLUIR_FORMA_PAGTO_FORNECEDOR");
                break;
            case PRODUTOS:
                table = produtosTable;
                isGrant = app.isGrant("EXCLUIR_PRODUTO_FORNECEDOR");
                break;
            case VENDEDORES:
                table = vendedoresTable;
                isGrant = app.isGrant("EXCLUIR_CONTATO_FORNECEDOR");
                break;
            case RECEBIMENTOS:
                table = recebimentosTable;
                isGrant = app.isGrant("EXCLUIR_RECEBIMENTO_FORNECEDOR");
                break;
            case CONTAS:
                table = contasTable;
                isGrant = app.isGrant("EXCLUIR_CONTA_FORNECEDOR");
                break;
            case CLIENTES:
                table = clientesTable;
                isGrant = app.isGrant("EXCLUIR_CLIENTE_FORNECEDOR");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = app.isGrant("EXCLUIR_EMAIL_FORNECEDOR");
                break;
            case SEGMERCADO:
                table = segmentoTable;
                isGrant = app.isGrant("EXCLUIR_SEG_MERCADO_FORNECEDOR");
                break;
            case TRANSPORTADOR:
                table = transpTable;
                isGrant = app.isGrant("EXCLUIR_TRANSPORTADOR_FORNECEDOR");
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
            case FORMAPGTO:
                formaPgtoRemove();
                break;
            case PRODUTOS:
                produtoRemove();
                break;
            case VENDEDORES:
                vendedorRemove();
                break;
            case RECEBIMENTOS:
                recebimentoRemove();
                break;
            case CONTAS:
                contaRemove();
                break;
            case CLIENTES:
                clienteRemove();
                break;
            case CONTATOS:
                contatoRemove();
                break;
            case SEGMERCADO:
                segmentoRemove();
                break;
            case TRANSPORTADOR:
                transpRemove();
                break;
        }
    }

    private void formaPgtoRemove() {
        getLogger().info("formaPgtoRemove");
        try {
            FormaPgtoRepresModel model = (FormaPgtoRepresModel) formaPgtoTable.getModel();
            int obj = formaPgtoTable.getSelectedRow();
            Object item = model.getObject(obj);
            model.removeObject(obj);
            represDao.deleteRow(item);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void produtoRemove() {
        getLogger().info("produtoRemove");
        try {
            ProdutoRepresTableModel model = (ProdutoRepresTableModel) produtosTable.getModel();
            int obj = produtosTable.getSelectedRow();
            Object item = model.getObject(obj);
            model.removeObject(obj);
            represDao.deleteRow(item);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void vendedorRemove() {
        getLogger().info("vendedoresRemove");
        try {
            VendedorRepresModel model = (VendedorRepresModel) vendedoresTable.getModel();
            VendedorRepres vendedor = (VendedorRepres) model.getObject(vendedoresTable.getSelectedRow());
            model.removeObject(vendedoresTable.getSelectedRow());
            represDao.deleteRow(vendedor);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void recebimentoRemove() {
        getLogger().info("recebimentoRemove");
        try {
            EntradaRepresModel model = (EntradaRepresModel) recebimentosTable.getModel();
            EntradaRepres entrada = (EntradaRepres) model.getObject(recebimentosTable.getSelectedRow());
            BigDecimal total = repres.getTotal();
            total = total.subtract(entrada.getValor());
            if (total.doubleValue() < 0.00) {
                total = new BigDecimal(0);
            }
            int selectedRow = recebimentosTable.getSelectedRow();
            getLogger().info("selectedRow " + selectedRow);
            model.removeObject(selectedRow);
            represDao.deleteRow(entrada);
            repres.setTotal(total);
            saldoField.setValue(repres.getTotal());
            represDao.updateRow(repres);
            totaliza();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void contaRemove() {
        getLogger().info("contaRemove");
            ContaRepres entrada = (ContaRepres) ((ContaRepresModel) contasTable.getModel()).getObject(contasTable.getSelectedRow());
        try {
            ContaRepresModel model = (ContaRepresModel) contasTable.getModel();
            represDao.deleteRow(entrada);
            model.removeObject(contasTable.getSelectedRow());
        } catch (Exception e) {
            entrada.setAtivo("I");
            try {
                represDao.updateRow(entrada);
            } catch (Exception ex) {
                getLogger().error(getBundle().getString("deleteErrorMessage"), ex);
                Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
            }
        }
        ((ContaRepresModel) contasTable.getModel()).fireTableDataChanged();
    }

    private void clienteRemove() {
        getLogger().info("clienteRemove");
        try {
            ClienteRepresModel model = (ClienteRepresModel) clientesTable.getModel();
            int obj = clientesTable.getSelectedRow();
            Object item = model.getObject(obj);
            model.removeObject(obj);
            represDao.deleteRow(item);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void segmentoRemove() {
        getLogger().info("segmentoRemove");
        try {
            RepresSegmentoModel model = (RepresSegmentoModel) segmentoTable.getModel();
            int obj = segmentoTable.getSelectedRow();
            Object item = model.getObject(obj);
            model.removeObject(obj);
            represDao.deleteRow(item);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }
    private void transpRemove() {
        getLogger().info("transpRemove");
        try {
            RepresTranspModel model = (RepresTranspModel) transpTable.getModel();
            int obj = transpTable.getSelectedRow();
            Object item = model.getObject(obj);
            model.removeObject(obj);
            represDao.deleteRow(item);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    @Override
    public void insert() {
        getLogger().info("insert " + tabbedPane.getSelectedIndex());
        TApplication app = TApplication.getInstance();
        Boolean isGrant;
        JTable table;
        switch (tabbedPane.getSelectedIndex()) {
            case PRODUTOS:
                table = produtosTable;
                isGrant = app.isGrant("INCLUIR_PRODUTO_FORNECEDOR");
                break;
            case FORMAPGTO:
                table = formaPgtoTable;
                isGrant = app.isGrant("INCLUIR_FORMA_PAGTO_FORNECEDOR");
                break;
            case VENDEDORES:
                table = vendedoresTable;
                isGrant = app.isGrant("INCLUIR_CONTATO_FORNECEDOR");
                break;
            case RECEBIMENTOS:
                table = recebimentosTable;
                isGrant = app.isGrant("INCLUIR_RECEBIMENTO_FORNECEDOR");
                break;
            case CONTAS:
                table = contasTable;
                isGrant = app.isGrant("INCLUIR_CONTA_FORNECEDOR");
                break;
            case CLIENTES:
                table = clientesTable;
                isGrant = app.isGrant("INCLUIR_CLIENTE_FORNECEDOR");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = app.isGrant("INCLUIR_EMAIL_FORNECEDOR");
                break;
            case SEGMERCADO:
                table = segmentoTable;
                isGrant = app.isGrant("INCLUIR_SEG_MERCADO_FORNECEDOR");
                break;
            case TRANSPORTADOR:
                table = transpTable;
                isGrant = app.isGrant("INCLUIR_TRANSPORTADOR_FORNECEDOR");
                break;
            default:
                return;
        }
        
        if (!isGrant)
            return;

        switch (tabbedPane.getSelectedIndex()) {
            case PRODUTOS:
                produtoInsert();
                break;
            case FORMAPGTO:
                formaPgtoInsert();
                break;
            case VENDEDORES:
                vendedorInsert();
                break;
            case RECEBIMENTOS:
                recebimentoInsert();
                break;
            case CONTAS:
                contaInsert();
                break;
            case CLIENTES:
                clienteInsert();
                break;
            case CONTATOS:
                contatoInsert();
                break;
            case SEGMERCADO:
                segmentoInsert();
                break;
            case TRANSPORTADOR:
                transpInsert();
                break;
        }
    }

    private void formaPgtoInsert() {
        getLogger().info("formaPgtoInsert");
        FormaPgtoRepresPanel editPanel = new FormaPgtoRepresPanel();
        try {
            editPanel.setFormaPgto(formaPgtoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        FormaPgtoRepres formaPgto = new FormaPgtoRepres();
        formaPgto.setRepres(repres);
        formaPgto.getFormaPgtoRepresPK().setIdRepres(repres.getIdRepres());
        EditDialog edtDlg = new EditDialog(getBundle().getString("addFormaPgtoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(formaPgto)) {
            try {
                repres.getFormasPgto().add(formaPgto);
                represDao.insertRecord(formaPgto);
                ((FormaPgtoRepresModel) formaPgtoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void vendedorInsert() {
        getLogger().info("vendedorInsert");
        VendedorRepresPanel editPanel = new VendedorRepresPanel();
        VendedorRepres vendedor = new VendedorRepres();
        vendedor.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVendedorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(vendedor)) {
            try {
                repres.getVendedores().add(vendedor);
                represDao.insertRecord(vendedor);
                ((VendedorRepresModel) vendedoresTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void recebimentoInsert() {
        getLogger().info("recebimentoInsert");
        EntradaRepresPanel editPanel = new EntradaRepresPanel();
        EntradaRepres entrada = new EntradaRepres();
        entrada.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRecebimentoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(entrada)) {
            try {
                BigDecimal total = repres.getTotal();
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
                total = total.add(entrada.getValor());
                repres.setTotal(total);
                saldoField.setValue(repres.getTotal());
                represDao.updateRow(repres);
                repres.getRecebimentos().add(entrada);
                represDao.insertRecord(entrada);
                ((EntradaRepresModel) recebimentosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void contaInsert() {
        getLogger().info("contaInsert");
        ContaRepresPanel editPanel = new ContaRepresPanel();
        try {
            editPanel.setBanco(bancoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        ContaRepres entrada = new ContaRepres();
        entrada.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addContaTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(entrada)) {
            try {
                if (repres.getContas() == null) {
                    repres.setContas(new ArrayList<ContaRepres>());
                }
                repres.getContas().add(entrada);
                represDao.insertRecord(entrada);
                ((ContaRepresModel) contasTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void clienteInsert() {
        getLogger().info("clienteInsert");
        RepresClientePanel editPanel = new RepresClientePanel();
        try {
            editPanel.setCliente(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        ClienteRepres cliente = new ClienteRepres(repres.getIdRepres(), 0);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(cliente)) {
            try {
                //repres.getClientes().add(cliente);
                RepresClienteModel m = (RepresClienteModel) clientesTable.getModel();
                m.addObject(cliente);
                represDao.insertRecord(cliente);
                ((RepresClienteModel) clientesTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void produtoInsert() {
        getLogger().info("produtoInsert");
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) produtosTable.getModel();
        ProdutoRepresEditPanel editPanel = new ProdutoRepresEditPanel();
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        RepresProduto represProduto = new RepresProduto();
        represProduto.setRepres(repres);
        represProduto.getRepresProdutoPK().setIdRepres(repres.getIdRepres());
        EditDialog edtDlg = new EditDialog(getBundle().getString("addProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(represProduto)) {
            try {
                Produto produto = (Produto) produtoDao.findById(Produto.class, represProduto.getProduto().getIdProduto());
                if (produto == null) {
                    produtoDao.insertRecord(represProduto.getProduto());
                } else {
                    produtoDao.updateRow(produto);
                }

                vtm.addObject(represProduto);
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void segmentoInsert() {
        ClienteSegmentoPanel editPanel = new ClienteSegmentoPanel();
        try {
            editPanel.setSegmentos(segDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addSegMercadoTitle"));
        edtDlg.setEditPanel(editPanel);

        SegMercado segmento = new SegMercado();
        while (edtDlg.edit(segmento)) {
            try {
                repres.getSegmentos().add(segmento);
                clienteDao.updateRow(repres);
                ((RepresSegmentoModel) segmentoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
    private void transpInsert() {
        RepresTranspPanel editPanel = new RepresTranspPanel();
        try {
            editPanel.setTransportadores(transpDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addTransportadorTitle"));
        edtDlg.setEditPanel(editPanel);

        Transportador transp = new Transportador();
        while (edtDlg.edit(transp)) {
            try {
                repres.getTransportadores().add(transp);
                represDao.updateRow(repres);
                ((RepresTranspModel) transpTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void view() {
        edit();
    }

    @Override
    public void edit() {
        getLogger().info("edit " + tabbedPane.getSelectedIndex());
        TApplication app = TApplication.getInstance();
        Boolean isGrant;
        JTable table;
        switch (tabbedPane.getSelectedIndex()) {
            case CADASTRO:
                if (app.isGrant("ALTERAR_FORNECEDORE"))
                represEdit();
                return;
            case FORMAPGTO:
                table = formaPgtoTable;
                isGrant = app.isGrant("INCLUIR_FORMA_PAGTO_FORNECEDOR");
                break;
            case PRODUTOS:
                table = produtosTable;
                isGrant = app.isGrant("INCLUIR_PRODUTO_FORNECEDOR");
                break;
            case VENDEDORES:
                table = vendedoresTable;
                isGrant = app.isGrant("INCLUIR_CONTATO_FORNECEDOR");
                break;
            case RECEBIMENTOS:
                table = recebimentosTable;
                isGrant = app.isGrant("INCLUIR_RECEBIMENTO_FORNECEDOR");
                break;
            case CONTAS:
                table = contasTable;
                isGrant = app.isGrant("INCLUIR_CONTA_FORNECEDOR");
                break;
            case CLIENTES:
                table = clientesTable;
                isGrant = app.isGrant("INCLUIR_CLIENTE_FORNECEDOR");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = app.isGrant("INCLUIR_EMAIL_FORNECEDOR");
                break;
            case SEGMERCADO:
                table = segmentoTable;
                isGrant = app.isGrant("INCLUIR_SEG_MERCADO_FORNECEDOR");
                break;
            case TRANSPORTADOR:
                table = transpTable;
                isGrant = app.isGrant("INCLUIR_TRANSPORTADOR_FORNECEDOR");
                break;
            default:
                return;
        }

        if (!isGrant) {
            return;
        }
        int i = table.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        switch (tabbedPane.getSelectedIndex()) {
            case FORMAPGTO:
                formaPgtoEdit();
                break;
            case PRODUTOS:
                produtoEdit();
                break;
            case VENDEDORES:
                vendedorEdit();
                break;
            case RECEBIMENTOS:
                recebimentoEdit();
                break;
            case CONTAS:
                contaEdit();
                break;
            case CLIENTES:
                clienteEdit();
                break;
            case CONTATOS:
                contatoEdit();
                break;
        }
    }

    @Action
    public void represEdit() {
        getLogger().info("represEdit");
        if(!TApplication.getInstance().isGrant("ALTERAR_FORNECEDOR"))
            return;
        RepresEditPanel editPanel = new RepresEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRepresTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(repres)) {
            try {
                represDao.updateRow(repres);
                repres2Field();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void formaPgtoEdit() {
        getLogger().info("formaPgtoEdit");
        FormaPgtoRepresPanel editPanel = new FormaPgtoRepresPanel();
        try {
            editPanel.setFormaPgto(formaPgtoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        FormaPgtoRepres formaPgto = (FormaPgtoRepres) ((FormaPgtoRepresModel) formaPgtoTable.getModel()).getObject(formaPgtoTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editFormaPgtoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(formaPgto)) {
            try {
                represDao.updateRow(formaPgto);
                ((FormaPgtoRepresModel) formaPgtoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void vendedorEdit() {
        getLogger().info("vendedorEdit");
        VendedorRepresPanel editPanel = new VendedorRepresPanel();
        VendedorRepres vendedor = (VendedorRepres) ((VendedorRepresModel) vendedoresTable.getModel()).getObject(vendedoresTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editVendedorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(vendedor)) {
            try {
                represDao.updateRow(vendedor);
                ((VendedorRepresModel) vendedoresTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void recebimentoEdit() {
        getLogger().info("recebimentoEdit");
        EntradaRepresPanel editPanel = new EntradaRepresPanel();
        EntradaRepres entrada = (EntradaRepres) ((EntradaRepresModel) recebimentosTable.getModel()).getObject(recebimentosTable.getSelectedRow());
        BigDecimal valor = entrada.getValor();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRecebimentoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(entrada)) {
            try {
                BigDecimal total = repres.getTotal();
                total = total.subtract(valor).add(entrada.getValor());
                repres.setTotal(total);
                saldoField.setValue(repres.getTotal());
                represDao.updateRow(repres);
                represDao.updateRow(entrada);
                ((EntradaRepresModel) recebimentosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void contaEdit() {
        getLogger().info("contaEdit");
        ContaRepresPanel editPanel = new ContaRepresPanel();
        try {
            editPanel.setBanco(bancoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        ContaRepres entrada = (ContaRepres) ((ContaRepresModel) contasTable.getModel()).getObject(contasTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editContaTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(entrada)) {
            try {
                represDao.updateRow(entrada);
                ((ContaRepresModel) contasTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void clienteEdit() {
        getLogger().info("clienteEdit");
        RepresClientePanel editPanel = new RepresClientePanel();
        try {
            editPanel.setCliente(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        ClienteRepres cliente = (ClienteRepres) ((RepresClienteModel) clientesTable.getModel()).getObject(clientesTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(cliente)) {
            try {
                represDao.updateRow(cliente);
                ((RepresClienteModel) clientesTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void produtoEdit() {
        getLogger().info("produtoEdit");
        ProdutoRepresTableModel vtm = (ProdutoRepresTableModel) produtosTable.getModel();
        ProdutoRepresEditPanel editPanel = new ProdutoRepresEditPanel();
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        RepresProduto produto = (RepresProduto) vtm.getObject(produtosTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(produto)) {
            try {
                produtoDao.updateRow(produto.getProduto());
                produtoDao.updateRow(produto);
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void zerarSaldo() {
        repres.setTotal(new BigDecimal(0));
        saldoField.setValue(repres.getTotal());
        try {
            represDao.updateRow(repres);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }

    private void printFicha() {
        getLogger().info("printFicha");
        URL url = getClass().getResource(Constants.JRFICHAREPRES);
        List lista = new ArrayList();
        lista.add(repres);
        FichaRepresDataSource ds = new FichaRepresDataSource(lista);
        try {
            Reports.showReport(TApplication.getInstance().getResourceString("fichaRepres"), null, url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    private void printProdutos() {
        getLogger().info("printProdutos");
        URL url = getClass().getResource(Constants.JRREPRESPRODUTO);
        ProdutoRepresTableModel modelo = (ProdutoRepresTableModel) produtosTable.getModel();
        RepresProdutoDataSource ds = new RepresProdutoDataSource(modelo.getItemList());
        try {
            Reports.showReport(TApplication.getInstance().getResourceString("fichaRepres"), null, url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    @Override
    public void report() {

        switch (tabbedPane.getSelectedIndex()) {
            case CADASTRO:
                printFicha();
                break;
            case PRODUTOS:
                printProdutos();
                break;
        }
    }

    private void alterarPreco() {
        getLogger().info("clienteEdit");
        AlterarPrecoEditPanel editPanel = new AlterarPrecoEditPanel();
        AlteraPreco value = new AlteraPreco();
        value.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("alterarPrecoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(value)) {
            try {
                boolean result = false;
                result = produtoDao.alterarPreco(value);
                if (result) {
                    updateProduto();
                    //((ProdutoRepresTableModel)produtosTable.getModel()).fireTableDataChanged();
                }
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void updateProduto() {
        ProdutoFilter filter = new ProdutoFilter();
        filter.setInativo(inativosCheckBox.isSelected());
        filter.setRepres(repres);
        List lista = produtoDao.findProdutoRepres(filter);
        ProdutoRepresTableModel model = new ProdutoRepresTableModel(lista);
        produtosTable.setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        cadastroPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        razaoLabel = new javax.swing.JLabel();
        razaoField = new javax.swing.JFormattedTextField();
        cnpjLabel = new javax.swing.JLabel();
        cnpjField = new javax.swing.JFormattedTextField();
        inscrEstLabel = new javax.swing.JLabel();
        inscrEstField = new javax.swing.JFormattedTextField();
        dtInclusaoLabel = new javax.swing.JLabel();
        dtInclusaoField = new javax.swing.JFormattedTextField();
        enderecoLabel = new javax.swing.JLabel();
        enderecoField = new javax.swing.JFormattedTextField();
        bairroLabel = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        cepLabel = new javax.swing.JLabel();
        cidadeLabel = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        ufLabel = new javax.swing.JLabel();
        ufField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        fone1Label = new javax.swing.JLabel();
        fone1Field = new javax.swing.JFormattedTextField();
        fone2Label = new javax.swing.JLabel();
        fone2Field = new javax.swing.JFormattedTextField();
        fone3Label = new javax.swing.JLabel();
        fone3Field = new javax.swing.JFormattedTextField();
        observacaoLabel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        obsPedidoTextArea = new javax.swing.JTextArea();
        observacaoLabel1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        obsProdutoTextArea = new javax.swing.JTextArea();
        comissaoLabel = new javax.swing.JLabel();
        comissaoTextField = new javax.swing.JFormattedTextField();
        ativoCheckBox = new javax.swing.JCheckBox();
        cepField = new javax.swing.JTextField();
        observacaoLabel3 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        metaVendaTextArea = new javax.swing.JTextArea();
        comissaoLabel1 = new javax.swing.JLabel();
        diasTextField = new javax.swing.JFormattedTextField();
        cepLabel1 = new javax.swing.JLabel();
        reduzidoField = new javax.swing.JTextField();
        comissaoLabel2 = new javax.swing.JLabel();
        freteTextField = new javax.swing.JFormattedTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        obsPedidoClienteTextArea = new javax.swing.JTextArea();
        observacaoLabel2 = new javax.swing.JLabel();
        enderecoLabel1 = new javax.swing.JLabel();
        siteField = new javax.swing.JFormattedTextField();
        alterarButton = new javax.swing.JButton();
        formaPgtoPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        formaPgtoTable = new javax.swing.JTable();
        produtosPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        precoButton = new javax.swing.JButton();
        inativosCheckBox = new javax.swing.JCheckBox();
        dataTabelaTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        produtosTable = new javax.swing.JTable();
        vendedoresPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        vendedoresTable = new javax.swing.JTable();
        recebimentosPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        zerarButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        saldoField = new javax.swing.JFormattedTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        recebimentosTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        dtEntrada1Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtEntrada2Field = new com.toedter.calendar.JDateChooser();
        recebimentosButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        totalField = new javax.swing.JFormattedTextField();
        contasPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        contasTable = new javax.swing.JTable();
        clientesPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        clientesTable = new javax.swing.JTable();
        contatosPanel = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        contatosTable = new javax.swing.JTable();
        segMercadoPanel = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        segmentoTable = new javax.swing.JTable();
        transpPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        transpTable = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setName("Form"); // NOI18N

        tabbedPane.setName("tabbedPane"); // NOI18N
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        cadastroPanel.setName("cadastroPanel"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("dadosRepres"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        razaoLabel.setText(bundle.getString("razaoSocial")); // NOI18N
        razaoLabel.setName("razaoLabel"); // NOI18N

        razaoField.setDocument(new BoundedPlainDocument(50));
        razaoField.setFocusable(false);
        razaoField.setName("razaoField"); // NOI18N

        cnpjLabel.setText(bundle.getString("cnpj")); // NOI18N
        cnpjLabel.setName("cnpjLabel"); // NOI18N

        cnpjField.setFocusable(false);
        cnpjField.setName("cnpjField"); // NOI18N

        inscrEstLabel.setText(bundle.getString("inscrEstadual")); // NOI18N
        inscrEstLabel.setName("inscrEstLabel"); // NOI18N

        inscrEstField.setFocusable(false);
        inscrEstField.setName("inscrEstField"); // NOI18N

        dtInclusaoLabel.setText(bundle.getString("dtInclusao")); // NOI18N
        dtInclusaoLabel.setName("dtInclusaoLabel"); // NOI18N

        dtInclusaoField.setFocusable(false);
        dtInclusaoField.setName("dtInclusaoField"); // NOI18N

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

        cepLabel.setText(bundle.getString("cep")); // NOI18N
        cepLabel.setName("cepLabel"); // NOI18N

        cidadeLabel.setText(bundle.getString("cidade")); // NOI18N
        cidadeLabel.setName("cidadeLabel"); // NOI18N

        cidadeField.setColumns(30);
        cidadeField.setDocument(new BoundedPlainDocument(30));
        cidadeField.setFocusable(false);
        cidadeField.setName("cidadeField"); // NOI18N

        ufLabel.setText(bundle.getString("uf")); // NOI18N
        ufLabel.setName("ufLabel"); // NOI18N

        ufField.setColumns(2);
        ufField.setFocusable(false);
        ufField.setName("ufField"); // NOI18N
        ufField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ufFieldActionPerformed(evt);
            }
        });

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

        fone3Label.setText(bundle.getString("fone3")); // NOI18N
        fone3Label.setName("fone3Label"); // NOI18N

        fone3Field.setColumns(12);
        fone3Field.setFocusable(false);
        fone3Field.setName("fone3Field"); // NOI18N

        observacaoLabel.setText(bundle.getString("obsPedidoFornecedor")); // NOI18N
        observacaoLabel.setName("observacaoLabel"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        obsPedidoTextArea.setColumns(20);
        obsPedidoTextArea.setLineWrap(true);
        obsPedidoTextArea.setRows(5);
        obsPedidoTextArea.setFocusable(false);
        obsPedidoTextArea.setName("obsPedidoTextArea"); // NOI18N
        jScrollPane5.setViewportView(obsPedidoTextArea);

        observacaoLabel1.setText(bundle.getString("obsProduto")); // NOI18N
        observacaoLabel1.setName("observacaoLabel1"); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        obsProdutoTextArea.setColumns(20);
        obsProdutoTextArea.setLineWrap(true);
        obsProdutoTextArea.setRows(5);
        obsProdutoTextArea.setFocusable(false);
        obsProdutoTextArea.setName("obsProdutoTextArea"); // NOI18N
        jScrollPane8.setViewportView(obsProdutoTextArea);

        comissaoLabel.setText(bundle.getString("percComissao")); // NOI18N
        comissaoLabel.setName("comissaoLabel"); // NOI18N

        comissaoTextField.setFormatterFactory(NumberUtils.getFormatterFactory());
        comissaoTextField.setFocusable(false);
        comissaoTextField.setInputVerifier(new DateVerifier(true));
        comissaoTextField.setName("comissaoTextField"); // NOI18N

        ativoCheckBox.setText(bundle.getString("ativo")); // NOI18N
        ativoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ativoCheckBox.setFocusable(false);
        ativoCheckBox.setName("ativoCheckBox"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(RepresViewFrame.class);
        cepField.setText(resourceMap.getString("cepField.text")); // NOI18N
        cepField.setName("cepField"); // NOI18N

        observacaoLabel3.setText(bundle.getString("metaVenda")); // NOI18N
        observacaoLabel3.setName("observacaoLabel3"); // NOI18N

        jScrollPane12.setName("jScrollPane12"); // NOI18N

        metaVendaTextArea.setColumns(20);
        metaVendaTextArea.setLineWrap(true);
        metaVendaTextArea.setRows(5);
        metaVendaTextArea.setFocusable(false);
        metaVendaTextArea.setName("metaVendaTextArea"); // NOI18N
        jScrollPane12.setViewportView(metaVendaTextArea);

        comissaoLabel1.setText(bundle.getString("diasAtendimento")); // NOI18N
        comissaoLabel1.setName("comissaoLabel1"); // NOI18N

        diasTextField.setEditable(false);
        diasTextField.setInputVerifier(new DateVerifier(true));
        diasTextField.setName("diasTextField"); // NOI18N

        cepLabel1.setText(bundle.getString("nomeReduzido")); // NOI18N
        cepLabel1.setName("cepLabel1"); // NOI18N

        reduzidoField.setEditable(false);
        reduzidoField.setText(resourceMap.getString("reduzidoField.text")); // NOI18N
        reduzidoField.setName("reduzidoField"); // NOI18N

        comissaoLabel2.setText(bundle.getString("frete")); // NOI18N
        comissaoLabel2.setName("comissaoLabel2"); // NOI18N

        freteTextField.setFormatterFactory(NumberUtils.getFormatterFactory());
        freteTextField.setFocusable(false);
        freteTextField.setInputVerifier(new DateVerifier(true));
        freteTextField.setName("freteTextField"); // NOI18N

        jScrollPane13.setName("jScrollPane13"); // NOI18N

        obsPedidoClienteTextArea.setColumns(20);
        obsPedidoClienteTextArea.setLineWrap(true);
        obsPedidoClienteTextArea.setRows(5);
        obsPedidoClienteTextArea.setFocusable(false);
        obsPedidoClienteTextArea.setName("obsPedidoClienteTextArea"); // NOI18N
        jScrollPane13.setViewportView(obsPedidoClienteTextArea);

        observacaoLabel2.setText(bundle.getString("obsPedidoCliente")); // NOI18N
        observacaoLabel2.setName("observacaoLabel2"); // NOI18N

        enderecoLabel1.setText(bundle.getString("site")); // NOI18N
        enderecoLabel1.setName("enderecoLabel1"); // NOI18N

        siteField.setFocusable(false);
        siteField.setName("siteField"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cnpjLabel)
                            .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inscrEstLabel)
                            .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtInclusaoLabel)
                            .addComponent(dtInclusaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cepLabel1)
                                .addGap(24, 24, 24))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(reduzidoField, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(razaoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(553, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(enderecoLabel1)
                        .addContainerGap(629, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(enderecoField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                            .addComponent(siteField, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(enderecoLabel)
                        .addContainerGap(599, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bairroLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cepLabel)
                            .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cidadeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ufLabel))
                        .addContainerGap(178, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fone1Label)
                            .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone2Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fone3Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone3Label))
                        .addContainerGap(296, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(observacaoLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(observacaoLabel3)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(observacaoLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(observacaoLabel1)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(69, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(comissaoLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comissaoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comissaoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comissaoLabel2)
                        .addGap(27, 27, 27)
                        .addComponent(freteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(ativoCheckBox)
                        .addContainerGap(97, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(razaoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cnpjLabel)
                            .addComponent(inscrEstLabel))
                        .addComponent(dtInclusaoLabel))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(cepLabel1)
                            .addGap(32, 32, 32))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(reduzidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtInclusaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoLabel1)
                .addGap(8, 8, 8)
                .addComponent(siteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cepLabel)
                            .addComponent(cidadeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bairroLabel))
                        .addGap(32, 32, 32))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ufLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fone1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fone2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fone3Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fone3Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(observacaoLabel)
                    .addComponent(observacaoLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(observacaoLabel3)
                            .addComponent(observacaoLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comissaoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comissaoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comissaoLabel)
                    .addComponent(comissaoLabel2)
                    .addComponent(freteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ativoCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(RepresViewFrame.class, this);
        alterarButton.setAction(actionMap.get("represEdit")); // NOI18N
        alterarButton.setName("alterarButton"); // NOI18N

        javax.swing.GroupLayout cadastroPanelLayout = new javax.swing.GroupLayout(cadastroPanel);
        cadastroPanel.setLayout(cadastroPanelLayout);
        cadastroPanelLayout.setHorizontalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(alterarButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cadastroPanelLayout.setVerticalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroPanelLayout.createSequentialGroup()
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(alterarButton))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("cadastro"), cadastroPanel); // NOI18N

        formaPgtoPanel.setName("formaPgtoPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        formaPgtoTable.setModel(new javax.swing.table.DefaultTableModel(
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
        formaPgtoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        formaPgtoTable.setName("formaPgtoTable"); // NOI18N
        jScrollPane1.setViewportView(formaPgtoTable);

        javax.swing.GroupLayout formaPgtoPanelLayout = new javax.swing.GroupLayout(formaPgtoPanel);
        formaPgtoPanel.setLayout(formaPgtoPanelLayout);
        formaPgtoPanelLayout.setHorizontalGroup(
            formaPgtoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaPgtoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        formaPgtoPanelLayout.setVerticalGroup(
            formaPgtoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formaPgtoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("formasPgto"), formaPgtoPanel); // NOI18N

        produtosPanel.setName("produtosPanel"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        precoButton.setText(bundle.getString("alterarPrecoPerc")); // NOI18N
        precoButton.setName("precoButton"); // NOI18N
        precoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precoButtonActionPerformed(evt);
            }
        });

        inativosCheckBox.setText(bundle.getString("mostrarProdutoInativo")); // NOI18N
        inativosCheckBox.setName("inativosCheckBox"); // NOI18N
        inativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inativosCheckBoxActionPerformed(evt);
            }
        });

        dataTabelaTextField.setText(resourceMap.getString("dataTabelaTextField.text")); // NOI18N
        dataTabelaTextField.setName("dataTabelaTextField"); // NOI18N

        jLabel3.setText(bundle.getString("ultimaAtualizacao")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inativosCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(precoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(dataTabelaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inativosCheckBox)
                    .addComponent(precoButton)
                    .addComponent(dataTabelaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        produtosTable.setModel(new javax.swing.table.DefaultTableModel(
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
        produtosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        produtosTable.setName("produtosTable"); // NOI18N
        jScrollPane7.setViewportView(produtosTable);

        javax.swing.GroupLayout produtosPanelLayout = new javax.swing.GroupLayout(produtosPanel);
        produtosPanel.setLayout(produtosPanelLayout);
        produtosPanelLayout.setHorizontalGroup(
            produtosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, produtosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(produtosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        produtosPanelLayout.setVerticalGroup(
            produtosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(produtosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("produtos"), produtosPanel); // NOI18N

        vendedoresPanel.setName("vendedoresPanel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        vendedoresTable.setModel(new javax.swing.table.DefaultTableModel(
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
        vendedoresTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        vendedoresTable.setName("vendedoresTable"); // NOI18N
        jScrollPane2.setViewportView(vendedoresTable);

        javax.swing.GroupLayout vendedoresPanelLayout = new javax.swing.GroupLayout(vendedoresPanel);
        vendedoresPanel.setLayout(vendedoresPanelLayout);
        vendedoresPanelLayout.setHorizontalGroup(
            vendedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendedoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        vendedoresPanelLayout.setVerticalGroup(
            vendedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendedoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("contatos"), vendedoresPanel); // NOI18N

        recebimentosPanel.setName("recebimentosPanel"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        zerarButton.setText(bundle.getString("zerarSaldo")); // NOI18N
        zerarButton.setName("zerarButton"); // NOI18N
        zerarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zerarButtonActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("saldo")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        saldoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldoField.setFocusable(false);
        saldoField.setName("saldoField"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(saldoField, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(zerarButton))
                    .addComponent(jLabel1))
                .addContainerGap(506, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(saldoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(zerarButton))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        recebimentosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        recebimentosTable.setName("recebimentosTable"); // NOI18N
        jScrollPane3.setViewportView(recebimentosTable);

        jLabel4.setText(bundle.getString("dtInclusao")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        dtEntrada1Field.setDateFormatString(resourceMap.getString("dtEntrada1Field.dateFormatString")); // NOI18N
        dtEntrada1Field.setName("dtEntrada1Field"); // NOI18N

        jLabel5.setText(bundle.getString("para")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        dtEntrada2Field.setDateFormatString(resourceMap.getString("dtEntrada2Field.dateFormatString")); // NOI18N
        dtEntrada2Field.setName("dtEntrada2Field"); // NOI18N

        recebimentosButton.setText(bundle.getString("pesquisar")); // NOI18N
        recebimentosButton.setName("recebimentosButton"); // NOI18N
        recebimentosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recebimentosButtonActionPerformed(evt);
            }
        });

        jLabel6.setText(bundle.getString("total")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        totalField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalField.setFocusable(false);
        totalField.setName("totalField"); // NOI18N

        javax.swing.GroupLayout recebimentosPanelLayout = new javax.swing.GroupLayout(recebimentosPanel);
        recebimentosPanel.setLayout(recebimentosPanelLayout);
        recebimentosPanelLayout.setHorizontalGroup(
            recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recebimentosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(recebimentosPanelLayout.createSequentialGroup()
                        .addComponent(dtEntrada1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtEntrada2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recebimentosButton))
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recebimentosPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        recebimentosPanelLayout.setVerticalGroup(
            recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recebimentosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(recebimentosPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtEntrada1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5)
                    .addGroup(recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(recebimentosButton)
                        .addComponent(dtEntrada2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                .addGroup(recebimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("recebimentos"), recebimentosPanel); // NOI18N

        contasPanel.setName("contasPanel"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

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
        contasTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        contasTable.setName("contasTable"); // NOI18N
        jScrollPane4.setViewportView(contasTable);

        javax.swing.GroupLayout contasPanelLayout = new javax.swing.GroupLayout(contasPanel);
        contasPanel.setLayout(contasPanelLayout);
        contasPanelLayout.setHorizontalGroup(
            contasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        contasPanelLayout.setVerticalGroup(
            contasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("contas"), contasPanel); // NOI18N

        clientesPanel.setName("clientesPanel"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        clientesTable.setModel(new javax.swing.table.DefaultTableModel(
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
        clientesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        clientesTable.setName("clientesTable"); // NOI18N
        jScrollPane6.setViewportView(clientesTable);

        javax.swing.GroupLayout clientesPanelLayout = new javax.swing.GroupLayout(clientesPanel);
        clientesPanel.setLayout(clientesPanelLayout);
        clientesPanelLayout.setHorizontalGroup(
            clientesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        clientesPanelLayout.setVerticalGroup(
            clientesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("clientes"), clientesPanel); // NOI18N

        contatosPanel.setName("contatosPanel"); // NOI18N

        jScrollPane10.setName("jScrollPane10"); // NOI18N

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
        contatosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        contatosTable.setName("contatosTable"); // NOI18N
        jScrollPane10.setViewportView(contatosTable);

        javax.swing.GroupLayout contatosPanelLayout = new javax.swing.GroupLayout(contatosPanel);
        contatosPanel.setLayout(contatosPanelLayout);
        contatosPanelLayout.setHorizontalGroup(
            contatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contatosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        contatosPanelLayout.setVerticalGroup(
            contatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contatosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("email"), contatosPanel); // NOI18N

        segMercadoPanel.setName("segMercadoPanel"); // NOI18N

        jScrollPane11.setName("jScrollPane11"); // NOI18N

        segmentoTable.setModel(new javax.swing.table.DefaultTableModel(
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
        segmentoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        segmentoTable.setName("segmentoTable"); // NOI18N
        jScrollPane11.setViewportView(segmentoTable);

        javax.swing.GroupLayout segMercadoPanelLayout = new javax.swing.GroupLayout(segMercadoPanel);
        segMercadoPanel.setLayout(segMercadoPanelLayout);
        segMercadoPanelLayout.setHorizontalGroup(
            segMercadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(segMercadoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        segMercadoPanelLayout.setVerticalGroup(
            segMercadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(segMercadoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Segmentos de mercado", segMercadoPanel);

        transpPanel.setName("transpPanel"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        transpTable.setName("transpTable"); // NOI18N
        jScrollPane9.setViewportView(transpTable);

        javax.swing.GroupLayout transpPanelLayout = new javax.swing.GroupLayout(transpPanel);
        transpPanel.setLayout(transpPanelLayout);
        transpPanelLayout.setHorizontalGroup(
            transpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        transpPanelLayout.setVerticalGroup(
            transpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("transportadores"), transpPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void zerarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zerarButtonActionPerformed
        zerarSaldo();
    }//GEN-LAST:event_zerarButtonActionPerformed

    private void precoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precoButtonActionPerformed
        alterarPreco();
    }//GEN-LAST:event_precoButtonActionPerformed

    private void inativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inativosCheckBoxActionPerformed
        updateProduto();
    }//GEN-LAST:event_inativosCheckBoxActionPerformed

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void ufFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ufFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ufFieldActionPerformed

    private void totaliza() {
        RepresFilter represFilter = new RepresFilter();
        represFilter.setRepres(repres);
        if (dtEntrada1Field.getDate() != null) {
            represFilter.setDtRecebimentoIni(DateUtils.parse(DateUtils.format(dtEntrada1Field.getDate())));
            represFilter.setDtRecebimentoEnd(DateUtils.parse(DateUtils.format(dtEntrada2Field.getDate())));
            List<EntradaRepres> lista = represDao.findRecebimentosByRepres(represFilter);
            BigDecimal bd = BigDecimal.ZERO;
            for (EntradaRepres er : lista) {
                bd = bd.add(er.getValor());
            }
            totalField.setValue(bd);
            totalField.invalidate();
            recebimentosTable.setModel(new EntradaRepresModel(lista));
            ((EntradaRepresModel) recebimentosTable.getModel()).fireTableDataChanged();
        }
    }
    private void recebimentosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recebimentosButtonActionPerformed
        totaliza();
    }//GEN-LAST:event_recebimentosButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton alterarButton;
    private javax.swing.JCheckBox ativoCheckBox;
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JLabel bairroLabel;
    private javax.swing.JPanel cadastroPanel;
    private javax.swing.JTextField cepField;
    private javax.swing.JLabel cepLabel;
    private javax.swing.JLabel cepLabel1;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel;
    private javax.swing.JPanel clientesPanel;
    private javax.swing.JTable clientesTable;
    private javax.swing.JFormattedTextField cnpjField;
    private javax.swing.JLabel cnpjLabel;
    private javax.swing.JLabel comissaoLabel;
    private javax.swing.JLabel comissaoLabel1;
    private javax.swing.JLabel comissaoLabel2;
    private javax.swing.JFormattedTextField comissaoTextField;
    private javax.swing.JPanel contasPanel;
    private javax.swing.JTable contasTable;
    private javax.swing.JPanel contatosPanel;
    private javax.swing.JTable contatosTable;
    private javax.swing.JTextField dataTabelaTextField;
    private javax.swing.JFormattedTextField diasTextField;
    private com.toedter.calendar.JDateChooser dtEntrada1Field;
    private com.toedter.calendar.JDateChooser dtEntrada2Field;
    private javax.swing.JFormattedTextField dtInclusaoField;
    private javax.swing.JLabel dtInclusaoLabel;
    private javax.swing.JFormattedTextField enderecoField;
    private javax.swing.JLabel enderecoLabel;
    private javax.swing.JLabel enderecoLabel1;
    private javax.swing.JFormattedTextField fone1Field;
    private javax.swing.JLabel fone1Label;
    private javax.swing.JFormattedTextField fone2Field;
    private javax.swing.JLabel fone2Label;
    private javax.swing.JFormattedTextField fone3Field;
    private javax.swing.JLabel fone3Label;
    private javax.swing.JPanel formaPgtoPanel;
    private javax.swing.JTable formaPgtoTable;
    private javax.swing.JFormattedTextField freteTextField;
    private javax.swing.JCheckBox inativosCheckBox;
    private javax.swing.JFormattedTextField inscrEstField;
    private javax.swing.JLabel inscrEstLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextArea metaVendaTextArea;
    private javax.swing.JTextArea obsPedidoClienteTextArea;
    private javax.swing.JTextArea obsPedidoTextArea;
    private javax.swing.JTextArea obsProdutoTextArea;
    private javax.swing.JLabel observacaoLabel;
    private javax.swing.JLabel observacaoLabel1;
    private javax.swing.JLabel observacaoLabel2;
    private javax.swing.JLabel observacaoLabel3;
    private javax.swing.JButton precoButton;
    private javax.swing.JPanel produtosPanel;
    private javax.swing.JTable produtosTable;
    private javax.swing.JFormattedTextField razaoField;
    private javax.swing.JLabel razaoLabel;
    private javax.swing.JButton recebimentosButton;
    private javax.swing.JPanel recebimentosPanel;
    private javax.swing.JTable recebimentosTable;
    private javax.swing.JTextField reduzidoField;
    private javax.swing.JFormattedTextField saldoField;
    private javax.swing.JPanel segMercadoPanel;
    private javax.swing.JTable segmentoTable;
    private javax.swing.JFormattedTextField siteField;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JFormattedTextField totalField;
    private javax.swing.JPanel transpPanel;
    private javax.swing.JTable transpTable;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JLabel ufLabel;
    private javax.swing.JPanel vendedoresPanel;
    private javax.swing.JTable vendedoresTable;
    private javax.swing.JButton zerarButton;
    // End of variables declaration//GEN-END:variables

    private void contatoEdit() {
        RepresContatoPanel editPanel = new RepresContatoPanel();
        editPanel.enableControls(false);
        RepresContatoModel compradorModel = (RepresContatoModel) contatosTable.getModel();
        RepresContato comprador = (RepresContato) compradorModel.getObject(contatosTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editContatoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                clienteDao.updateRow(comprador);
                ((RepresContatoModel) contatosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void contatoInsert() {
        RepresContatoPanel editPanel = new RepresContatoPanel();
        RepresContato comprador = new RepresContato();
        comprador.getRepresContatoPK().setIdRepres(repres.getIdRepres());
        comprador.setRepres(repres);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addContatoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                int num = repres.getContatos().size();
                num++;
                comprador.getRepresContatoPK().setNumItem(num);
                repres.getContatos().add(comprador);
                clienteDao.insertRecord(comprador);
                ((RepresContatoModel) contatosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void contatoRemove() {
        getLogger().info("compradorRemove");
        try {
            RepresContatoModel compradorModel = (RepresContatoModel) contatosTable.getModel();
            RepresContato comprador = (RepresContato) compradorModel.getObject(contatosTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getEndereco());
            represDao.deleteRow(comprador);
            compradorModel.removeObject(contatosTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }
}
