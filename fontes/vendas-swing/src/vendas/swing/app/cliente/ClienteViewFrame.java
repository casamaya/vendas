/*
 * NewJInternalFrame.java
 *
 * Created on 8 de Dezembro de 2007, 17:03
 */
package vendas.swing.app.cliente;

import demo.ScrollablePicture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.jdesktop.application.Action;
import vendas.swing.model.ClienteCompradorModel;
import vendas.swing.model.ClienteRepresModel;
import vendas.swing.model.ClienteSegmentoModel;
import vendas.swing.model.ClienteVisitaModel;
import vendas.entity.Cliente;
import vendas.entity.ClienteRepres;
import vendas.entity.Comprador;
import vendas.entity.SegMercado;
import vendas.entity.VisitaCliente;
import vendas.dao.ClienteDao;
import vendas.dao.RepresDao;
import vendas.dao.SegMercadoDao;
import ritual.swing.TApplication;
import vendas.dao.SituacaoClienteDao;
import vendas.dao.VendedorDao;
import vendas.swing.core.Formats;
import ritual.swing.ViewFrame;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.util.DateUtils;
import ritual.util.DateVerifier;
import vendas.beans.ClientesFilter;
import vendas.dao.BaseDao;
import vendas.dao.GrupoClienteDao;
import vendas.dao.PedidoDao;
import vendas.entity.BancoCliente;
import vendas.entity.ClienteContato;
import vendas.entity.GrupoCliente;
import vendas.entity.ItemPedido;
import vendas.entity.ReferenciaCliente;
import vendas.entity.Vendedor;
import vendas.swing.model.BancoClienteModel;
import vendas.swing.model.ClienteContatoModel;
import vendas.swing.model.GruposClientesModel;
import vendas.swing.model.ReferenciaClienteModel;
import vendas.swing.model.RotatividadeTableModel;
import vendas.swing.model.UltimoPedidoModel;
import vendas.swing.model.VendedorClienteModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;
import vendas.util.StringUtils;

/**
 *
 * @author  Sam
 */
public class ClienteViewFrame extends ViewFrame {

    private VendedorDao vendedorDao;
    private ClienteDao clienteDao;
    private SegMercadoDao segmentoDao;
    private SituacaoClienteDao situacaoDao;
    private BaseDao baseDao;
    private PedidoDao pedidoDao;
    private RepresDao represDao;
    private Cliente cliente;
    private final int CADASTRO = 0;
    private final int VENDEDORES = 1;
    private final int SEGMERCADO = 2;
    private final int COMPRADORES = 3;
    private final int REPRES = 4;
    private final int VISITAS = 5;
    private final int MOVIMENTO = 6;
    private final int CONTATOS = 7;
    private final int REFCLIENTE = 8;
    private final int REFBANCO = 9;
    private final int GRUPOCLIENTE = 11;

    /** Creates new form NewJInternalFrame */
    public ClienteViewFrame(String title) {
        super(title, false, true, true, true);
        initComponents();
        TApplication app = TApplication.getInstance();
        segmentoDao = (SegMercadoDao) app.lookupService("segMercadoDao");
        baseDao = (BaseDao) app.lookupService("baseDao");
        represDao = (RepresDao) app.lookupService("represDao");
        pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        alterarButton.setEnabled(app.getUser().isAdmin() || app.getUser().isEscritorio());
        historicoTextArea.setEditable(false);
        historicoSaveButton.setVisible(false);

    }

    @Override
    protected void object2Field(Object obj) {
        getLogger().info("object2Field");
        setRenderer();
        cliente = (Cliente) obj;
        cliente2Field();
        bloqueadoCheckBox.setSelected(cliente.isBloqueado());
        desbloquearButton.setEnabled(TApplication.getInstance().getUser().isAdmin());
        segmentoTable.setModel(new ClienteSegmentoModel((List) cliente.getSegmentos()));
        vendedoresTable.setModel(new VendedorClienteModel((List) cliente.getVendedores()));
        compradoresTable.setModel(new ClienteCompradorModel((List) cliente.getCompradores()));
        
        Collection<VisitaCliente> visitas = cliente.getVisitas();
        
        visitasTable.setModel(new ClienteVisitaModel((List) visitas));
        represTable.setModel(new ClienteRepresModel((List) cliente.getRepresentadas()));
        refBancoTable.setModel(new BancoClienteModel((List) cliente.getReferenciasBanco()));
        refComercialTable.setModel(new ReferenciaClienteModel((List) cliente.getReferenciasCliente()));
        contatosTable.setModel(new ClienteContatoModel((List) cliente.getContatos()));
        gruposTable.setModel(new GruposClientesModel((List) cliente.getGruposCliente()));

        contatosTable.getColumnModel().getColumn(ClienteContatoModel.ENDERECO).setPreferredWidth(250);
        contatosTable.getColumnModel().getColumn(ClienteContatoModel.TIPO).setPreferredWidth(100);

        ClientesFilter filtro = new ClientesFilter();
        filtro.setCliente(cliente);
        List movimento = clienteDao.findRotatividade(filtro);
        rotatividadeTable.setModel(new RotatividadeTableModel(movimento));
        List pedidos = pedidoDao.findUltimosPedidos(cliente.getIdCliente());
        pedidosTable.setModel(new UltimoPedidoModel(pedidos));
        pedidosTable.getColumnModel().getColumn(UltimoPedidoModel.PEDIDO).setPreferredWidth(80);
        pedidosTable.getColumnModel().getColumn(UltimoPedidoModel.DATA).setPreferredWidth(80);
        pedidosTable.getColumnModel().getColumn(UltimoPedidoModel.VALOR).setPreferredWidth(80);
        pedidosTable.getColumnModel().getColumn(UltimoPedidoModel.REPRES).setPreferredWidth(230);
        pedidosTable.getColumnModel().getColumn(UltimoPedidoModel.VENDEDOR).setPreferredWidth(170);
        segmentoTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        vendedoresTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        represTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        rotatividadeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        visitasTable.getColumnModel().getColumn(ClienteVisitaModel.DT_VISITA).setPreferredWidth(90);
        visitasTable.getColumnModel().getColumn(ClienteVisitaModel.PEDIDO).setPreferredWidth(90);
        visitasTable.getColumnModel().getColumn(ClienteVisitaModel.VENDEDOR).setPreferredWidth(200);
        visitasTable.getColumnModel().getColumn(ClienteVisitaModel.OBS).setPreferredWidth(250);
    }

    private void setRenderer() {
        segmentoTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        visitasTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        pedidosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        pedidosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        represTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
    }

    private void cliente2Field() {
        bairroField.setText(cliente.getBairro());
        cepField.setText(StringUtils.formatarCep(cliente.getCep()));
        cidadeField.setText(cliente.getCidade());
        cnpjField.setText(StringUtils.formatarCnpj(cliente.getCnpj()));
        dtInclusaoField.setValue(cliente.getDtInclusao());
        enderecoField.setText(cliente.getEndereco());
        fone1Field.setText(cliente.getFone1());
        fone2Field.setText(cliente.getFone2());
        fone3Field.setText(cliente.getFone3());
        inscrEstField.setText(cliente.getInscrEstadual());
                pendenteField.setForeground(Color.red);
        pgtoPendenteField.setForeground(Color.red);
        limiteCreditoField.setForeground(Color.red);
        historicoTextArea.setText(cliente.getHistorico());
        if (TApplication.getInstance().getUser().isAdmin()) {
            historicoTextArea.setEditable(true);
            historicoSaveButton.setVisible(true);
        }
        limiteCreditoField.setValue(cliente.getLimiteCredito());
        diasAtrasoField.setValue(cliente.getNumDiasAtraso());
                PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        BigDecimal pgtosPendentes = dao.getPgtosClientePendende(cliente.getIdCliente());
        if (pgtosPendentes == null) {
            pgtosPendentes = BigDecimal.ZERO;
        }
        BigDecimal vendasPendentes = dao.getVendasClientePendende(cliente.getIdCliente());
        if (vendasPendentes == null) {
            vendasPendentes = BigDecimal.ZERO;
        }
        pendenteField.setValue(vendasPendentes);
        pgtoPendenteField.setValue(pgtosPendentes);
        razaoField.setText(cliente.getRazao());
        situacaoField.setText(cliente.getSituacaoCliente().getNome());
        ufField.setText(cliente.getUf());
        empilhadeiraCheckBox.setSelected(cliente.isEmpilhadeira());
        entregarBoletoCheckBox.setSelected(cliente.isEntregarBoleto());
        subsTributariaCheckBox.setSelected(cliente.isSubstituicaoTributaria());
        reduzidoField.setText((cliente.getFantasia()));
        siteField.setText((cliente.getSite()));
    }

    public void setVendedorDao(VendedorDao srvc) {
        vendedorDao = srvc;
    }

    public void setSituacaoDao(SituacaoClienteDao srvc) {
        situacaoDao = srvc;
    }

    public void setClienteDao(ClienteDao srvc) {
        clienteDao = srvc;
    }

    @Override
    public void view() {
        edit();
    }
    
    @Override
    public void edit() {
        JTable table;
        Boolean isGrant;
        switch (tabbedPane.getSelectedIndex()) {
            case CADASTRO:
                if (TApplication.getInstance().isGrant("ALTERAR_CLIENTE"))
                clienteEdit();
                return;
            case VENDEDORES:
                table = vendedoresTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_VENDEDOR_CLIENTE");
                break;
            case SEGMERCADO:
                table = segmentoTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_SEGMENTO_MERCADO_CLIENTE");
                break;
            case COMPRADORES:
                table = compradoresTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_COMPRADOR_CLIENTE");
                break;
            case REPRES:
                table = represTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_FORNECEDOR_CLIENTE");
                break;
            case VISITAS:
                table = visitasTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_VISITA");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_COMPRADOR_CLIENTE");
                break;
            case REFCLIENTE:
                table = refComercialTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_REFERENCIA_COMERCIAL");
                break;
            case REFBANCO:
                table = refBancoTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_REFERENCIA_BANCARIA");
                break;
            case GRUPOCLIENTE:
                table = gruposTable;
                isGrant = TApplication.getInstance().isGrant("ALTERAR_REFERENCIA_BANCARIA");
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
            case COMPRADORES:
                compradorEdit();
                break;
            case REPRES:
                represEdit();
                break;
            case CONTATOS:
                contatoEdit();
                break;
            case REFCLIENTE:
                refComercialEdit();
                break;
            case REFBANCO:
                refBancoEdit();
                break;
            case VISITAS:
                visitaEdit();
                break;
        }
    }

    @Override
    public void insert() {
        TApplication app = TApplication.getInstance();
        switch (tabbedPane.getSelectedIndex()) {
            case VENDEDORES:
                if (app.isGrant("INCLUIR_VENDEDOR_CLIENTE"))
                    vendedorInsert();
                break;
            case SEGMERCADO:
                if (app.isGrant("INCLUIR_SEGMENTO_MERCADO_CLIENTE"))
                    segmentoInsert();
                break;
            case COMPRADORES:
                if (app.isGrant("INCLUIR_COMPRADOR_CLIENTE"))
                    compradorInsert();
                break;
            case REPRES:
                if (app.isGrant("INCLUIR_FORNECEDOR_CLIENTE"))
                    represInsert();
                break;
            case VISITAS:
                if (app.isGrant("INCLUIR_VISITA"))
                    visitaInsert();
                break;
            case CONTATOS:
                if (app.isGrant("INCLUIR_COMPRADOR_CLIENTE"))
                    contatoInsert();
                break;
            case REFCLIENTE:
                if (app.isGrant("INCLUIR_REFERENCIA_COMERCIAL"))
                    refComercialInsert();
                break;
            case REFBANCO:
                if (app.isGrant("INCLUIR_REFERENCIA_BANCARIA"))
                    refBancoInsert();
                break;
            case GRUPOCLIENTE:
                if (app.isGrant("INCLUIR_GRUPO_CLIENTE"))
                    grupoClienteInsert();
                break;
        }
    }

    private void grupoClienteInsert() {
        GrupoClientePanel editPanel = new GrupoClientePanel();
        GrupoClienteDao grupoClienteDao = new GrupoClienteDao();
        try {
            editPanel.setGruposClientes(grupoClienteDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog("Incluir grupo de cliente");
        edtDlg.setEditPanel(editPanel);

        GrupoCliente segmento = new GrupoCliente();
        while (edtDlg.edit(segmento)) {
            try {
                cliente.getGruposCliente().add(segmento);
                clienteDao.updateRow(cliente);
                ((GruposClientesModel) gruposTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void remove() {
        TApplication app = TApplication.getInstance();
        Boolean isGrant;
        JTable table;
        switch (tabbedPane.getSelectedIndex()) {
            case VENDEDORES:
                table = vendedoresTable;
                isGrant = app.isGrant("EXCLUIR_VENDEDOR_CLIENTE");
                break;
            case SEGMERCADO:
                table = segmentoTable;
                isGrant = app.isGrant("EXCLUIR_SEGMENTO_MERCADO_CLIENTE");
                break;
            case COMPRADORES:
                table = compradoresTable;
                isGrant = app.isGrant("EXCLUIR_COMPRADOR_CLIENTE");
                break;
            case REPRES:
                table = represTable;
                isGrant = app.isGrant("EXCLUIR_FORNECEDOR_CLIENTE");
                break;
            case VISITAS:
                table = visitasTable;
                isGrant = app.isGrant("EXCLUIR_VISITA");
                break;
            case CONTATOS:
                table = contatosTable;
                isGrant = app.isGrant("EXCLUIR_COMPRADOR_CLIENTE");
                break;
            case REFCLIENTE:
                table = refComercialTable;
                isGrant = app.isGrant("EXCLUIR_REFERENCIA_COMERCIAL");
                break;
            case REFBANCO:
                table = refBancoTable;
                isGrant = app.isGrant("EXCLUIR_REFERENCIA_BANCARIA");
                break;
            case GRUPOCLIENTE:
                table = gruposTable;
                isGrant = app.isGrant("EXCLUIR_GRUPO_CLIENTE");
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
        
        if (tabbedPane.getSelectedIndex() == VISITAS) {
            VisitaCliente visita = (VisitaCliente) ((ClienteVisitaModel) visitasTable.getModel()).getObject(visitasTable.getSelectedRow());
            if (visita.getDtVisita().compareTo(DateUtils.getCurrentDate()) < 0) {
                return;
            }
        }
        
        if (!Messages.deleteQuestion()) {
            return;
        }
        
        switch (tabbedPane.getSelectedIndex()) {
            case VENDEDORES:
                vendedorRemove();
                break;
            case SEGMERCADO:
                segmentoRemove();
                break;
            case COMPRADORES:
                compradorRemove();
                break;
            case REPRES:
                represRemove();
                break;
            case VISITAS:
                visitaRemove();
                break;
            case CONTATOS:
                contatoRemove();
                break;
            case REFCLIENTE:
                refComercialRemove();
                break;
            case REFBANCO:
                refBancoRemove();
                break;
            case GRUPOCLIENTE:
                grupoClienteRemove();
                break;
        }
    }

    private void grupoClienteRemove() {
        getLogger().info("grupoClienteRemove");
        try {
            GruposClientesModel segModel = (GruposClientesModel) gruposTable.getModel();
            segModel.removeObject(gruposTable.getSelectedRow());
            clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void refComercialInsert() {
        RefComercialEditPanel editPanel = new RefComercialEditPanel();
        ReferenciaCliente referencia = new ReferenciaCliente();
        referencia.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRefClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(referencia)) {
            try {
                clienteDao.insertRecord(referencia);
                cliente.getReferenciasCliente().add(referencia);
                ((ReferenciaClienteModel) refComercialTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void refComercialEdit() {
        RefComercialEditPanel editPanel = new RefComercialEditPanel();
        ReferenciaCliente referencia = new ReferenciaCliente();
        referencia.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRefClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(referencia)) {
            try {
                clienteDao.updateRow(referencia);
                ((ReferenciaClienteModel) refComercialTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    private void refComercialRemove() {
        getLogger().info("refComercialRemove");
        try {
            ReferenciaClienteModel compradorModel = (ReferenciaClienteModel) refComercialTable.getModel();
            ReferenciaCliente comprador = (ReferenciaCliente) compradorModel.getObject(refComercialTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getIdReferenciaCliente());
            baseDao.deleteRow(comprador);
            compradorModel.removeObject(refComercialTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }
    
    private void refBancoInsert() {
        RefBancoEditPanel editPanel = new RefBancoEditPanel();
        BancoCliente referencia = new BancoCliente();
        referencia.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRefBancoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(referencia)) {
            try {
                clienteDao.insertRecord(referencia);
                cliente.getReferenciasBanco().add(referencia);
                ((BancoClienteModel) refBancoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void refBancoEdit() {
        RefBancoEditPanel editPanel = new RefBancoEditPanel();
        BancoCliente referencia = new BancoCliente();
        referencia.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRefBancoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(referencia)) {
            try {
                clienteDao.updateRow(referencia);
                ((BancoClienteModel) refBancoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    private void refBancoRemove() {
        getLogger().info("refBancoRemove");
        try {
            BancoClienteModel compradorModel = (BancoClienteModel) refBancoTable.getModel();
            BancoCliente comprador = (BancoCliente) compradorModel.getObject(refBancoTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getIdReferencia());
            baseDao.deleteRow(comprador);
            compradorModel.removeObject(refBancoTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void segmentoInsert() {
        ClienteSegmentoPanel editPanel = new ClienteSegmentoPanel();
        try {
            editPanel.setSegmentos(segmentoDao.findAll());
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
                cliente.getSegmentos().add(segmento);
                clienteDao.updateRow(cliente);
                ((ClienteSegmentoModel) segmentoTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void vendedorInsert() {
        VendedorClientePanel editPanel = new VendedorClientePanel();
        try {
            editPanel.setSegmentos(vendedorDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVendedorTitle"));
        edtDlg.setEditPanel(editPanel);

        Vendedor vendedor = new Vendedor();
        while (edtDlg.edit(vendedor)) {
            try {
                cliente.getVendedores().add(vendedor);
                clienteDao.updateRow(cliente);
                ((VendedorClienteModel) vendedoresTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void compradorInsert() {
        CompradorEditPanel editPanel = new CompradorEditPanel();
        Comprador comprador = new Comprador();
        comprador.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addCompradorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                cliente.getCompradores().add(comprador);
                clienteDao.insertRecord(comprador);
                ((ClienteCompradorModel) compradoresTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void represInsert() {
        ClienteRepresPanel editPanel = new ClienteRepresPanel();
        try {
            editPanel.setRepres(represDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addRepresTitle"));
        edtDlg.setEditPanel(editPanel);

        ClienteRepres repres = new ClienteRepres(0, cliente.getIdCliente());
        while (edtDlg.edit(repres)) {
            try {
                cliente.getRepresentadas().add(repres);
                clienteDao.insertRecord(repres);
                ((ClienteRepresModel) represTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void visitaInsert() {
        ClienteVisitaPanel editPanel = new ClienteVisitaPanel();
        editPanel.init();

        try {
            editPanel.setVendedores(vendedorDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVisitaTitle"));
        edtDlg.setEditPanel(editPanel);

        VisitaCliente visita = new VisitaCliente();
        visita.setCliente(cliente);
        visita.setDtVisita(new Date());
        
        TApplication app = TApplication.getInstance();
        Vendedor vendedor = null;
        
        if (app.getUser().isVendedor() || app.getUser().isAdmin()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        
        visita.setVendedor(vendedor);
        
        while (edtDlg.edit(visita)) {
            try {
                cliente.getVisitas().add(visita);
                clienteDao.insertRecord(visita);
                ((ClienteVisitaModel) visitasTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    private void visitaEdit() {
        VisitaCliente visita = (VisitaCliente) ((ClienteVisitaModel) visitasTable.getModel()).getObject(visitasTable.getSelectedRow());

        if (visita.getDtVisita().compareTo(DateUtils.getCurrentDate()) < 0) {
            Messages.errorMessage("Visita n\u00E3o pode ser alterada.");
            return;
        }
        
        ClienteVisitaPanel editPanel = new ClienteVisitaPanel();
        editPanel.setNaoVisita(visita.getTipoVisita().equals("N"));
        
        try {
            editPanel.setVendedores(vendedorDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("editVisitaTitle"));
        edtDlg.setEditPanel(editPanel);

        while (edtDlg.edit(visita)) {
            try {
                clienteDao.updateRow(visita);
                ((ClienteVisitaModel) visitasTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void compradorEdit() {
        CompradorEditPanel editPanel = new CompradorEditPanel();
        Comprador comprador = (Comprador) ((ClienteCompradorModel) compradoresTable.getModel()).getObject(compradoresTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editCompradorTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                clienteDao.updateRow(comprador);
                ((ClienteCompradorModel) compradoresTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void represEdit() {
        ClienteRepresPanel editPanel = new ClienteRepresPanel();
        try {
            editPanel.setRepres(represDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("editRepresTitle"));
        edtDlg.setEditPanel(editPanel);
        ClienteRepres repres = (ClienteRepres) ((ClienteRepresModel) represTable.getModel()).getObject(represTable.getSelectedRow());
        while (edtDlg.edit(repres)) {
            try {
                clienteDao.updateRow(repres);
                ((ClienteRepresModel) represTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void segmentoRemove() {
        getLogger().info("segmentoRemove");
        try {
            ClienteSegmentoModel segModel = (ClienteSegmentoModel) segmentoTable.getModel();
            segModel.removeObject(segmentoTable.getSelectedRow());
            clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void vendedorRemove() {
        getLogger().info("vendedorRemove");
        try {
            VendedorClienteModel segModel = (VendedorClienteModel) vendedoresTable.getModel();
            segModel.removeObject(vendedoresTable.getSelectedRow());
            clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void compradorRemove() {
        getLogger().info("compradorRemove");
        try {
            ClienteCompradorModel compradorModel = (ClienteCompradorModel) compradoresTable.getModel();
            Comprador comprador = (Comprador) compradorModel.getObject(compradoresTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getIdComprador());
            baseDao.deleteRow(comprador);
            compradorModel.removeObject(compradoresTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void represRemove() {
        try {
            getLogger().info("represRemove");
            ClienteRepresModel repres = (ClienteRepresModel) represTable.getModel();
            repres.removeObject(represTable.getSelectedRow());
            clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void visitaRemove() {
        if(!TApplication.getInstance().isGrant("EXCLUIR_VISITA")) {
            return;
        }
        try {
            getLogger().info("visitaRemove");
            ClienteVisitaModel visita = (ClienteVisitaModel) visitasTable.getModel();
            VisitaCliente o = (VisitaCliente)visita.getObject(visitasTable.getSelectedRow());

                visita.removeObject(visitasTable.getSelectedRow());
                baseDao.deleteRow(o);

            //clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    @Action
    public void clienteEdit() {
        getLogger().info("clienteEdit");
        if(!TApplication.getInstance().isGrant("ALTERAR_CLIENTE"))
            return;        
        ClienteEditPanel editPanel = new ClienteEditPanel();
        try {
            editPanel.setSituacoes(situacaoDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("editClienteTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(cliente)) {
            try {
                if (!valida(cliente)) {
                    Messages.errorMessage("Cliente possui pedidos pendentes e n�o pode ser inativado.");
                    continue;
                }
                clienteDao.updateRow(cliente);
                cliente2Field();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
    private Boolean valida(Cliente cliente) {
        Boolean result = true;
        if (cliente.getSituacaoCliente().getNome().equals("INATIVO")) {
                ClienteDao dao = (ClienteDao)TApplication.getInstance().lookupService("clienteDao");
                if (dao.isClientePossuiPedidoPendente(cliente.getIdCliente())) {
                    result = false;
                }
            }
        return result;
    }

    @Override
    public void report() {
        URL url = getClass().getResource(Constants.JRFICHACLIENTE);
        Map model = TApplication.getInstance().getDefaultMap(getBundle().getString("fichaCliente"));
        List lista = new ArrayList();
        lista.add(cliente);
        FichaClienteDataSource ds = new FichaClienteDataSource(lista);
        try {
            Reports.showReport(url, model, ds);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("reportError"), e);
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

        tabbedPane = new javax.swing.JTabbedPane();
        cadastroPanel = new javax.swing.JPanel();
        razaoLabel1 = new javax.swing.JLabel();
        razaoField = new javax.swing.JFormattedTextField();
        cnpjLabel1 = new javax.swing.JLabel();
        cnpjField = new javax.swing.JFormattedTextField();
        inscrEstLabel1 = new javax.swing.JLabel();
        inscrEstField = new javax.swing.JFormattedTextField();
        dtInclusaoLabel1 = new javax.swing.JLabel();
        dtInclusaoField = new javax.swing.JFormattedTextField();
        enderecoLabel1 = new javax.swing.JLabel();
        enderecoField = new javax.swing.JFormattedTextField();
        bairroLabel1 = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        cidadeLabel1 = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        cepLabel1 = new javax.swing.JLabel();
        ufLabel1 = new javax.swing.JLabel();
        ufField = new javax.swing.JFormattedTextField(Formats.createFormatter("UU"));
        fone1Label1 = new javax.swing.JLabel();
        fone1Field = new javax.swing.JFormattedTextField();
        fone2Label1 = new javax.swing.JLabel();
        fone2Field = new javax.swing.JFormattedTextField();
        fone3Label1 = new javax.swing.JLabel();
        fone3Field = new javax.swing.JFormattedTextField();
        vendedorLabel1 = new javax.swing.JLabel();
        vendedorField = new javax.swing.JFormattedTextField();
        situacaoLabel1 = new javax.swing.JLabel();
        situacaoField = new javax.swing.JFormattedTextField();
        empilhadeiraCheckBox = new javax.swing.JCheckBox();
        cepField = new javax.swing.JTextField();
        alterarButton = new javax.swing.JButton();
        bairroLabel2 = new javax.swing.JLabel();
        reduzidoField = new javax.swing.JFormattedTextField();
        entregarBoletoCheckBox = new javax.swing.JCheckBox();
        addButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        faxLabel2 = new javax.swing.JLabel();
        limiteCreditoField = new javax.swing.JFormattedTextField();
        faxLabel3 = new javax.swing.JLabel();
        pendenteField = new javax.swing.JFormattedTextField();
        faxLabel4 = new javax.swing.JLabel();
        pgtoPendenteField = new javax.swing.JFormattedTextField();
        subsTributariaCheckBox = new javax.swing.JCheckBox();
        siteLabel = new javax.swing.JLabel();
        siteField = new javax.swing.JFormattedTextField();
        diasAtrasoField = new javax.swing.JFormattedTextField();
        faxLabel5 = new javax.swing.JLabel();
        bloqueadoCheckBox = new javax.swing.JCheckBox();
        desbloquearButton = new javax.swing.JButton();
        vendedoresPanel = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        vendedoresTable = new javax.swing.JTable();
        segMercadoPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        segmentoTable = new javax.swing.JTable();
        compradoresPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        compradoresTable = new javax.swing.JTable();
        represPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        represTable = new javax.swing.JTable();
        visitasPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        visitasTable = new javax.swing.JTable();
        movimentoPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        rotatividadeTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        pedidosTable = new javax.swing.JTable();
        contatoPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        contatosTable = new javax.swing.JTable();
        refComercial = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        refComercialTable = new javax.swing.JTable();
        refBanco = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        refBancoTable = new javax.swing.JTable();
        historicoPanel = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        historicoTextArea = new javax.swing.JTextArea();
        historicoSaveButton = new javax.swing.JButton();
        gruposPanel = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        gruposTable = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(733, 600));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        tabbedPane.setName("tabbedPane"); // NOI18N

        cadastroPanel.setName("cadastroPanel"); // NOI18N
        cadastroPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        razaoLabel1.setText(bundle.getString("razaoSocial")); // NOI18N
        razaoLabel1.setName("razaoLabel1"); // NOI18N

        razaoField.setDocument(new BoundedPlainDocument(50));
        razaoField.setFocusable(false);
        razaoField.setName("razaoField"); // NOI18N

        cnpjLabel1.setText(bundle.getString("cnpj")); // NOI18N
        cnpjLabel1.setName("cnpjLabel1"); // NOI18N

        cnpjField.setFocusable(false);
        cnpjField.setName("cnpjField"); // NOI18N

        inscrEstLabel1.setText(bundle.getString("inscrEstadual")); // NOI18N
        inscrEstLabel1.setName("inscrEstLabel1"); // NOI18N

        inscrEstField.setFocusable(false);
        inscrEstField.setName("inscrEstField"); // NOI18N

        dtInclusaoLabel1.setText(bundle.getString("dtInclusao")); // NOI18N
        dtInclusaoLabel1.setName("dtInclusaoLabel1"); // NOI18N

        dtInclusaoField.setFocusable(false);
        dtInclusaoField.setInputVerifier(new DateVerifier());
        dtInclusaoField.setName("dtInclusaoField"); // NOI18N

        enderecoLabel1.setText(bundle.getString("endereco")); // NOI18N
        enderecoLabel1.setName("enderecoLabel1"); // NOI18N

        enderecoField.setFocusable(false);
        enderecoField.setName("enderecoField"); // NOI18N

        bairroLabel1.setText(bundle.getString("bairro")); // NOI18N
        bairroLabel1.setName("bairroLabel1"); // NOI18N

        bairroField.setColumns(30);
        bairroField.setDocument(new BoundedPlainDocument(25));
        bairroField.setFocusable(false);
        bairroField.setName("bairroField"); // NOI18N

        cidadeLabel1.setText(bundle.getString("cidade")); // NOI18N
        cidadeLabel1.setName("cidadeLabel1"); // NOI18N

        cidadeField.setColumns(30);
        cidadeField.setDocument(new BoundedPlainDocument(30));
        cidadeField.setFocusable(false);
        cidadeField.setName("cidadeField"); // NOI18N

        cepLabel1.setText(bundle.getString("cep")); // NOI18N
        cepLabel1.setName("cepLabel1"); // NOI18N

        ufLabel1.setText(bundle.getString("uf")); // NOI18N
        ufLabel1.setName("ufLabel1"); // NOI18N

        ufField.setColumns(2);
        ufField.setFocusable(false);
        ufField.setName("ufField"); // NOI18N

        fone1Label1.setText(bundle.getString("fone1")); // NOI18N
        fone1Label1.setName("fone1Label1"); // NOI18N

        fone1Field.setColumns(20);
        fone1Field.setFocusable(false);
        fone1Field.setName("fone1Field"); // NOI18N

        fone2Label1.setText(bundle.getString("fone2")); // NOI18N
        fone2Label1.setName("fone2Label1"); // NOI18N

        fone2Field.setColumns(20);
        fone2Field.setFocusable(false);
        fone2Field.setName("fone2Field"); // NOI18N

        fone3Label1.setText(bundle.getString("fone3")); // NOI18N
        fone3Label1.setName("fone3Label1"); // NOI18N

        fone3Field.setColumns(20);
        fone3Field.setFocusable(false);
        fone3Field.setName("fone3Field"); // NOI18N

        vendedorLabel1.setText(bundle.getString("vendedor")); // NOI18N
        vendedorLabel1.setName("vendedorLabel1"); // NOI18N

        vendedorField.setFocusable(false);
        vendedorField.setName("vendedorField"); // NOI18N

        situacaoLabel1.setText(bundle.getString("situacaoCliente")); // NOI18N
        situacaoLabel1.setName("situacaoLabel1"); // NOI18N

        situacaoField.setFocusable(false);
        situacaoField.setName("situacaoField"); // NOI18N

        empilhadeiraCheckBox.setText(bundle.getString("empilhadeira")); // NOI18N
        empilhadeiraCheckBox.setFocusable(false);
        empilhadeiraCheckBox.setName("empilhadeiraCheckBox"); // NOI18N
        empilhadeiraCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empilhadeiraCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(ClienteViewFrame.class);
        cepField.setText(resourceMap.getString("cepField.text")); // NOI18N
        cepField.setName("cepField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(ClienteViewFrame.class, this);
        alterarButton.setAction(actionMap.get("clienteEdit")); // NOI18N
        alterarButton.setName("alterarButton"); // NOI18N

        bairroLabel2.setText(bundle.getString("nomeReduzido")); // NOI18N
        bairroLabel2.setName("bairroLabel2"); // NOI18N

        reduzidoField.setColumns(30);
        reduzidoField.setDocument(new BoundedPlainDocument(30));
        reduzidoField.setEditable(false);
        reduzidoField.setName("reduzidoField"); // NOI18N

        entregarBoletoCheckBox.setText(bundle.getString("entregarBoleto")); // NOI18N
        entregarBoletoCheckBox.setFocusable(false);
        entregarBoletoCheckBox.setName("entregarBoletoCheckBox"); // NOI18N

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/New16.gif"))); // NOI18N
        addButton.setToolTipText(bundle.getString("novaFoto")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        viewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Open16.gif"))); // NOI18N
        viewButton.setToolTipText(bundle.getString("abrirFoto")); // NOI18N
        viewButton.setName("viewButton"); // NOI18N
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("foto")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        faxLabel2.setText(bundle.getString("limiteCredito")); // NOI18N
        faxLabel2.setName("faxLabel2"); // NOI18N

        limiteCreditoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        limiteCreditoField.setFocusable(false);
        limiteCreditoField.setName("limiteCreditoField"); // NOI18N

        faxLabel3.setText(bundle.getString("pendente")); // NOI18N
        faxLabel3.setName("faxLabel3"); // NOI18N

        pendenteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        pendenteField.setFocusable(false);
        pendenteField.setName("pendenteField"); // NOI18N

        faxLabel4.setText(bundle.getString("pgtoPendente")); // NOI18N
        faxLabel4.setName("faxLabel4"); // NOI18N

        pgtoPendenteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        pgtoPendenteField.setFocusable(false);
        pgtoPendenteField.setName("pgtoPendenteField"); // NOI18N

        subsTributariaCheckBox.setText(resourceMap.getString("subsTributariaCheckBox.text")); // NOI18N
        subsTributariaCheckBox.setFocusable(false);
        subsTributariaCheckBox.setName("subsTributariaCheckBox"); // NOI18N

        siteLabel.setText(bundle.getString("site")); // NOI18N
        siteLabel.setName("siteLabel"); // NOI18N

        siteField.setFocusable(false);
        siteField.setName("siteField"); // NOI18N

        diasAtrasoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        diasAtrasoField.setFocusable(false);
        diasAtrasoField.setName("diasAtrasoField"); // NOI18N

        faxLabel5.setText(bundle.getString("diasAtraso")); // NOI18N
        faxLabel5.setName("faxLabel5"); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/resources/Main"); // NOI18N
        bloqueadoCheckBox.setText(bundle1.getString("bloqueado")); // NOI18N
        bloqueadoCheckBox.setFocusable(false);
        bloqueadoCheckBox.setName("bloqueadoCheckBox"); // NOI18N

        desbloquearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/autorizar16.png"))); // NOI18N
        desbloquearButton.setToolTipText(resourceMap.getString("desbloquearButton.toolTipText")); // NOI18N
        desbloquearButton.setName("desbloquearButton"); // NOI18N
        desbloquearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desbloquearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cadastroPanelLayout = new javax.swing.GroupLayout(cadastroPanel);
        cadastroPanel.setLayout(cadastroPanelLayout);
        cadastroPanelLayout.setHorizontalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(razaoLabel1)
                            .addGroup(cadastroPanelLayout.createSequentialGroup()
                                .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(alterarButton))
                            .addGroup(cadastroPanelLayout.createSequentialGroup()
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cnpjLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inscrEstLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dtInclusaoField)
                                    .addComponent(dtInclusaoLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bairroLabel2)
                                    .addComponent(reduzidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(160, Short.MAX_VALUE))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addComponent(siteLabel)
                        .addGap(837, 837, 837))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addComponent(enderecoLabel1)
                        .addContainerGap(804, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(siteField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                            .addComponent(enderecoField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cadastroPanelLayout.createSequentialGroup()
                                .addComponent(empilhadeiraCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(entregarBoletoCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(subsTributariaCheckBox))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cadastroPanelLayout.createSequentialGroup()
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fone1Label1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fone2Label1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fone3Field, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fone3Label1))
                                .addGap(18, 18, 18)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(pendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(faxLabel3)
                                        .addGap(70, 70, 70)
                                        .addComponent(faxLabel4))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cadastroPanelLayout.createSequentialGroup()
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(vendedorLabel1)
                                            .addComponent(vendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(situacaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(situacaoLabel1)))
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(bloqueadoCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(desbloquearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(addButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(viewButton))
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE))))
                            .addGroup(cadastroPanelLayout.createSequentialGroup()
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(bairroLabel1)
                                        .addGap(153, 153, 153)
                                        .addComponent(cepLabel1)
                                        .addGap(77, 77, 77)
                                        .addComponent(cidadeLabel1))
                                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                                        .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ufLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(faxLabel2)
                                            .addComponent(limiteCreditoField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(faxLabel5)
                                    .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(137, 137, 137))))
        );
        cadastroPanelLayout.setVerticalGroup(
            cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cadastroPanelLayout.createSequentialGroup()
                .addComponent(razaoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alterarButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cnpjLabel1)
                            .addComponent(inscrEstLabel1)
                            .addComponent(dtInclusaoLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(inscrEstField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dtInclusaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addComponent(bairroLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reduzidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(siteLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(siteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enderecoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bairroLabel1)
                            .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cidadeLabel1)
                                .addComponent(cepLabel1)
                                .addComponent(ufLabel1)
                                .addComponent(faxLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(limiteCreditoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cepField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addComponent(faxLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fone1Label1)
                            .addComponent(fone2Label1)
                            .addComponent(fone3Label1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fone1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fone3Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(faxLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(faxLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vendedorLabel1)
                            .addComponent(situacaoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(situacaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cadastroPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton))
                    .addComponent(viewButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(empilhadeiraCheckBox)
                    .addComponent(entregarBoletoCheckBox)
                    .addComponent(subsTributariaCheckBox))
                .addGap(32, 32, 32)
                .addGroup(cadastroPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bloqueadoCheckBox)
                    .addComponent(desbloquearButton))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("cadastro"), cadastroPanel); // NOI18N

        vendedoresPanel.setName("vendedoresPanel"); // NOI18N

        jScrollPane12.setName("jScrollPane12"); // NOI18N

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
        jScrollPane12.setViewportView(vendedoresTable);

        javax.swing.GroupLayout vendedoresPanelLayout = new javax.swing.GroupLayout(vendedoresPanel);
        vendedoresPanel.setLayout(vendedoresPanelLayout);
        vendedoresPanelLayout.setHorizontalGroup(
            vendedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendedoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        vendedoresPanelLayout.setVerticalGroup(
            vendedoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendedoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(180, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("vendedores"), vendedoresPanel); // NOI18N

        segMercadoPanel.setName("segMercadoPanel"); // NOI18N
        segMercadoPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

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
        jScrollPane1.setViewportView(segmentoTable);

        javax.swing.GroupLayout segMercadoPanelLayout = new javax.swing.GroupLayout(segMercadoPanel);
        segMercadoPanel.setLayout(segMercadoPanelLayout);
        segMercadoPanelLayout.setHorizontalGroup(
            segMercadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(segMercadoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        segMercadoPanelLayout.setVerticalGroup(
            segMercadoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(segMercadoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(180, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("segmentos"), segMercadoPanel); // NOI18N

        compradoresPanel.setName("compradoresPanel"); // NOI18N
        compradoresPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        compradoresTable.setModel(new javax.swing.table.DefaultTableModel(
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
        compradoresTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        compradoresTable.setName("compradoresTable"); // NOI18N
        jScrollPane2.setViewportView(compradoresTable);

        javax.swing.GroupLayout compradoresPanelLayout = new javax.swing.GroupLayout(compradoresPanel);
        compradoresPanel.setLayout(compradoresPanelLayout);
        compradoresPanelLayout.setHorizontalGroup(
            compradoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compradoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        compradoresPanelLayout.setVerticalGroup(
            compradoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compradoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("compradores"), compradoresPanel); // NOI18N

        represPanel.setName("represPanel"); // NOI18N
        represPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        represTable.setModel(new javax.swing.table.DefaultTableModel(
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
        represTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        represTable.setName("represTable"); // NOI18N
        jScrollPane3.setViewportView(represTable);

        javax.swing.GroupLayout represPanelLayout = new javax.swing.GroupLayout(represPanel);
        represPanel.setLayout(represPanelLayout);
        represPanelLayout.setHorizontalGroup(
            represPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(represPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        represPanelLayout.setVerticalGroup(
            represPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(represPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(144, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("representadas"), represPanel); // NOI18N

        visitasPanel.setName("visitasPanel"); // NOI18N
        visitasPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        visitasTable.setModel(new javax.swing.table.DefaultTableModel(
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
        visitasTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        visitasTable.setName("visitasTable"); // NOI18N
        jScrollPane4.setViewportView(visitasTable);

        javax.swing.GroupLayout visitasPanelLayout = new javax.swing.GroupLayout(visitasPanel);
        visitasPanel.setLayout(visitasPanelLayout);
        visitasPanelLayout.setHorizontalGroup(
            visitasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visitasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        visitasPanelLayout.setVerticalGroup(
            visitasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visitasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("visitas"), visitasPanel); // NOI18N

        movimentoPanel.setName("movimentoPanel"); // NOI18N
        movimentoPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("rotatividadeVendas"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        rotatividadeTable.setModel(new javax.swing.table.DefaultTableModel(
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
        rotatividadeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        rotatividadeTable.setName("rotatividadeTable"); // NOI18N
        jScrollPane5.setViewportView(rotatividadeTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ultimosPedidos"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        pedidosTable.setModel(new javax.swing.table.DefaultTableModel(
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
        pedidosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        pedidosTable.setName("pedidosTable"); // NOI18N
        jScrollPane6.setViewportView(pedidosTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout movimentoPanelLayout = new javax.swing.GroupLayout(movimentoPanel);
        movimentoPanel.setLayout(movimentoPanelLayout);
        movimentoPanelLayout.setHorizontalGroup(
            movimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, movimentoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(movimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        movimentoPanelLayout.setVerticalGroup(
            movimentoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movimentoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("movimento"), movimentoPanel); // NOI18N

        contatoPanel.setName("contatoPanel"); // NOI18N
        contatoPanel.setPreferredSize(new java.awt.Dimension(570, 540));

        jScrollPane9.setName("jScrollPane9"); // NOI18N

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
        jScrollPane9.setViewportView(contatosTable);

        javax.swing.GroupLayout contatoPanelLayout = new javax.swing.GroupLayout(contatoPanel);
        contatoPanel.setLayout(contatoPanelLayout);
        contatoPanelLayout.setHorizontalGroup(
            contatoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contatoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        contatoPanelLayout.setVerticalGroup(
            contatoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contatoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("email"), contatoPanel); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        refComercialTable.setModel(new javax.swing.table.DefaultTableModel(
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
        refComercialTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        refComercialTable.setName("refComercialTable"); // NOI18N
        jScrollPane7.setViewportView(refComercialTable);

        javax.swing.GroupLayout refComercialLayout = new javax.swing.GroupLayout(refComercial);
        refComercial.setLayout(refComercialLayout);
        refComercialLayout.setHorizontalGroup(
            refComercialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refComercialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        refComercialLayout.setVerticalGroup(
            refComercialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refComercialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("refComercial"), refComercial); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        refBancoTable.setModel(new javax.swing.table.DefaultTableModel(
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
        refBancoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        refBancoTable.setName("refBancoTable"); // NOI18N
        jScrollPane8.setViewportView(refBancoTable);

        javax.swing.GroupLayout refBancoLayout = new javax.swing.GroupLayout(refBanco);
        refBanco.setLayout(refBancoLayout);
        refBancoLayout.setHorizontalGroup(
            refBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refBancoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        refBancoLayout.setVerticalGroup(
            refBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(refBancoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );

        tabbedPane.addTab(bundle.getString("refBanco"), refBanco); // NOI18N

        historicoPanel.setName("historicoPanel"); // NOI18N

        jScrollPane13.setName("jScrollPane13"); // NOI18N

        historicoTextArea.setColumns(20);
        historicoTextArea.setRows(5);
        historicoTextArea.setName("historicoTextArea"); // NOI18N
        jScrollPane13.setViewportView(historicoTextArea);

        historicoSaveButton.setLabel(bundle.getString("save")); // NOI18N
        historicoSaveButton.setName("historicoSaveButton"); // NOI18N
        historicoSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historicoSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historicoPanelLayout = new javax.swing.GroupLayout(historicoPanel);
        historicoPanel.setLayout(historicoPanelLayout);
        historicoPanelLayout.setHorizontalGroup(
            historicoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historicoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historicoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(historicoSaveButton))
                .addContainerGap())
        );
        historicoPanelLayout.setVerticalGroup(
            historicoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historicoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(historicoSaveButton)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("historico"), historicoPanel); // NOI18N

        gruposPanel.setName("gruposPanel"); // NOI18N

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        gruposTable.setModel(new javax.swing.table.DefaultTableModel(
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
        gruposTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        gruposTable.setName("gruposTable"); // NOI18N
        jScrollPane10.setViewportView(gruposTable);

        javax.swing.GroupLayout gruposPanelLayout = new javax.swing.GroupLayout(gruposPanel);
        gruposPanel.setLayout(gruposPanelLayout);
        gruposPanelLayout.setHorizontalGroup(
            gruposPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gruposPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        gruposPanelLayout.setVerticalGroup(
            gruposPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gruposPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(180, Short.MAX_VALUE))
        );

        tabbedPane.addTab(resourceMap.getString("gruposPanel.TabConstraints.tabTitle"), gruposPanel); // NOI18N

        getContentPane().add(tabbedPane);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void empilhadeiraCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empilhadeiraCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empilhadeiraCheckBoxActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!("jpg").equals(StringUtils.getExtension(file))) {
                Messages.errorMessage("Arquivo deve ser jpg.");
                return;
            }
            if (file.length() > 1024 * 1024) {
                Messages.errorMessage("Arquivo muito grande.");
                return;
            }
            Cliente pedido = (Cliente)getObject();

            byte[] bFile = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(bFile);
                fileInputStream.close();
                cliente.setFoto(bFile);
                clienteDao.updateRow(cliente);
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
}//GEN-LAST:event_addButtonActionPerformed

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        // TODO add your handling code here:
        if (cliente.getFoto() == null) {
            Messages.errorMessage("Sem imagem definida");
            return;
        }
        Image image = null;
        try {
            InputStream is = cliente.getBlob().getBinaryStream();
            image = ImageIO.read(is);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("imageErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("imageErrorMessage"));
        }
        JInternalFrame frame = new JInternalFrame(title, true, true, true, true);
        //frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ScrollablePicture picture;
        //frame.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        picture = new ScrollablePicture(new ImageIcon(image), 1);
        JScrollPane pictureScrollPane = new JScrollPane(picture);
        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
        pictureScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        frame.getContentPane().add(pictureScrollPane);
        TApplication.getInstance().getDesktopPane().add(frame);
                frame.setVisible(true);
}//GEN-LAST:event_viewButtonActionPerformed

    private void historicoSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historicoSaveButtonActionPerformed
        // TODO add your handling code here:
        Cliente cliente = (Cliente)getValueObject();
        cliente.setHistorico(historicoTextArea.getText());
        try {
        clienteDao.updateRow(cliente);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }//GEN-LAST:event_historicoSaveButtonActionPerformed

private void desbloquearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desbloquearButtonActionPerformed
    ClienteDao produtoDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
    Cliente cliente = (Cliente)getValueObject();
    try {
        boolean bloqueado = !cliente.isBloqueado();   
        desbloquearButton.setEnabled(bloqueado);
        desbloquearButton.invalidate();
        cliente.setBloqueado(bloqueado);
        produtoDao.updateRow(cliente); 
                
        Messages.infoMessage("Cliente " + (bloqueado ? "bloqueado." : "desbloqueado."));
        object2Field(cliente);
        
        if (getParentViewFrame() != null) {
            getParentViewFrame().refresh();
        }
    } catch (Exception e) {
        getLogger().error(e);
        Messages.errorMessage("Falha ao desbloquear");
    }
}//GEN-LAST:event_desbloquearButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton alterarButton;
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JLabel bairroLabel1;
    private javax.swing.JLabel bairroLabel2;
    private javax.swing.JCheckBox bloqueadoCheckBox;
    private javax.swing.JPanel cadastroPanel;
    private javax.swing.JTextField cepField;
    private javax.swing.JLabel cepLabel1;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel1;
    private javax.swing.JFormattedTextField cnpjField;
    private javax.swing.JLabel cnpjLabel1;
    private javax.swing.JPanel compradoresPanel;
    private javax.swing.JTable compradoresTable;
    private javax.swing.JPanel contatoPanel;
    private javax.swing.JTable contatosTable;
    private javax.swing.JButton desbloquearButton;
    private javax.swing.JFormattedTextField diasAtrasoField;
    private javax.swing.JFormattedTextField dtInclusaoField;
    private javax.swing.JLabel dtInclusaoLabel1;
    private javax.swing.JCheckBox empilhadeiraCheckBox;
    private javax.swing.JFormattedTextField enderecoField;
    private javax.swing.JLabel enderecoLabel1;
    private javax.swing.JCheckBox entregarBoletoCheckBox;
    private javax.swing.JLabel faxLabel2;
    private javax.swing.JLabel faxLabel3;
    private javax.swing.JLabel faxLabel4;
    private javax.swing.JLabel faxLabel5;
    private javax.swing.JFormattedTextField fone1Field;
    private javax.swing.JLabel fone1Label1;
    private javax.swing.JFormattedTextField fone2Field;
    private javax.swing.JLabel fone2Label1;
    private javax.swing.JFormattedTextField fone3Field;
    private javax.swing.JLabel fone3Label1;
    private javax.swing.JPanel gruposPanel;
    private javax.swing.JTable gruposTable;
    private javax.swing.JPanel historicoPanel;
    private javax.swing.JButton historicoSaveButton;
    private javax.swing.JTextArea historicoTextArea;
    private javax.swing.JFormattedTextField inscrEstField;
    private javax.swing.JLabel inscrEstLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
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
    private javax.swing.JFormattedTextField limiteCreditoField;
    private javax.swing.JPanel movimentoPanel;
    private javax.swing.JTable pedidosTable;
    private javax.swing.JFormattedTextField pendenteField;
    private javax.swing.JFormattedTextField pgtoPendenteField;
    private javax.swing.JFormattedTextField razaoField;
    private javax.swing.JLabel razaoLabel1;
    private javax.swing.JFormattedTextField reduzidoField;
    private javax.swing.JPanel refBanco;
    private javax.swing.JTable refBancoTable;
    private javax.swing.JPanel refComercial;
    private javax.swing.JTable refComercialTable;
    private javax.swing.JPanel represPanel;
    private javax.swing.JTable represTable;
    private javax.swing.JTable rotatividadeTable;
    private javax.swing.JPanel segMercadoPanel;
    private javax.swing.JTable segmentoTable;
    private javax.swing.JFormattedTextField siteField;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JFormattedTextField situacaoField;
    private javax.swing.JLabel situacaoLabel1;
    private javax.swing.JCheckBox subsTributariaCheckBox;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JLabel ufLabel1;
    private javax.swing.JFormattedTextField vendedorField;
    private javax.swing.JLabel vendedorLabel1;
    private javax.swing.JPanel vendedoresPanel;
    private javax.swing.JTable vendedoresTable;
    private javax.swing.JButton viewButton;
    private javax.swing.JPanel visitasPanel;
    private javax.swing.JTable visitasTable;
    // End of variables declaration//GEN-END:variables

    private void contatoInsert() {
        ClienteContatoPanel editPanel = new ClienteContatoPanel();
        ClienteContato comprador = new ClienteContato();
        comprador.getClienteContatoPK().setIdCliente(cliente.getIdCliente());
        comprador.setCliente(cliente);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addContatoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                int num = cliente.getContatos().size();
                num++;
                comprador.getClienteContatoPK().setNumItem(num);
                cliente.getContatos().add(comprador);
                clienteDao.insertRecord(comprador);
                ((ClienteContatoModel) contatosTable.getModel()).fireTableDataChanged();
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
            ClienteContatoModel compradorModel = (ClienteContatoModel) contatosTable.getModel();
            ClienteContato comprador = (ClienteContato) compradorModel.getObject(contatosTable.getSelectedRow());
            getLogger().info("removendo " + comprador.getEndereco());
            baseDao.deleteRow(comprador);
            compradorModel.removeObject(contatosTable.getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    private void contatoEdit() {
        ClienteContatoPanel editPanel = new ClienteContatoPanel();
        editPanel.enableControls(false);
        ClienteContatoModel compradorModel = (ClienteContatoModel) contatosTable.getModel();
        ClienteContato comprador = (ClienteContato) compradorModel.getObject(contatosTable.getSelectedRow());
        EditDialog edtDlg = new EditDialog(getBundle().getString("editContatoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(comprador)) {
            try {
                clienteDao.updateRow(comprador);
                ((ClienteContatoModel) contatosTable.getModel()).fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
}

class VisitaComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        VisitaCliente grupo1 = (VisitaCliente) obj1;
        VisitaCliente grupo2 = (VisitaCliente) obj2;

        return grupo1.getDtVisita().compareTo(grupo2.getDtVisita());
    }
}