/*
 * PedidoViewFrame.java
 *
 * Created on 19 de Agosto de 2007, 19:39
 */
package vendas.swing.app.pedido;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.DefaultCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import org.jdesktop.application.Action;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.DateCellEditor;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.NumericTextField;
import vendas.beans.UnidadeItem;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.entity.AtendimentoPedido;
import vendas.entity.Pedido;
import vendas.entity.PgtoCliente;
import ritual.swing.ViewFrame;
import ritual.util.DateUtils;
import ritual.util.DateVerifier;
import vendas.exception.DAOException;
import vendas.swing.model.ItemPedidoModel;
import vendas.swing.model.NotasPedidoModel;
import vendas.swing.model.PgtoPedidoModel;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.SimpleAuth;
import ritual.util.NumberUtils;
import vendas.beans.CobrancaVO;
import vendas.beans.EmailBean;
import vendas.beans.PedidoBean;
import vendas.dao.ClienteDao;
import vendas.dao.EmpresaDao;
import vendas.dao.RepresDao;
import vendas.entity.ArquivoPedido;
import vendas.entity.ClienteContato;
import vendas.entity.ContaRepres;
import vendas.entity.ItemPedido;
import vendas.entity.ItemPedidoAtend;
import vendas.entity.ItemPedidoPK;
import vendas.entity.Params;
import vendas.entity.Repres;
import vendas.entity.User;
import vendas.swing.model.ArquivoPedidoModel;
import vendas.swing.model.PedidoEmbarqueModel;
import vendas.util.Constants;
import vendas.util.EmailPanel;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;

/**
 *
 * @author  Sam
 */
public class PedidoViewFrame extends ViewFrame {

    PedidoDao pedidoDao;
    ListSelectionModel listSelectionModel;
    private final TApplication app;

    /** Creates new form PedidoViewFrame */
    public PedidoViewFrame(String title) {
        super(title);
        app = TApplication.getInstance();
        initComponents();
        setTitle(title);
        pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        listSelectionModel = notasTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        notasTable.setSelectionModel(listSelectionModel);
        boolean editar = false;
        if (app.getUser().isAdmin() || app.getUser().isEscritorio()) {
            editar = true;
        }

        alterarButton.setEnabled(editar);
        addNotaButton.setEnabled(!app.getUser().isVendedor());
        delNotaButton.setEnabled(!app.getUser().isVendedor());
        edtNotaButton.setEnabled(!app.getUser().isVendedor());
        delPgtoButton.setEnabled(!app.getUser().isVendedor());
        addPgtoButton.setEnabled(!app.getUser().isVendedor());
        edtPgtoButton.setEnabled(!app.getUser().isVendedor());
        edtPgtoButton1.setEnabled(app.getUser().isAdmin() || app.getUser().isEscritorio());
        repetirPgtoButton.setEnabled(!app.getUser().isVendedor());
        cobrancaButton.setEnabled(app.isGrant("EMITIR_DEMONSTRATIVO_COBRANCA"));
        planilhaButton.setEnabled(app.isGrant("EMITIR_PLANILHA_CHEQUES"));
        addButton.setEnabled(app.isGrant("ANEXAR_ARQUIVO_PEDIDO"));
        viewButton.setEnabled(app.isGrant("VISUALIZAR_ARQUIVO_PEDIDO"));
        deleteItemButton.setEnabled(app.isGrant("EXCLUIR_ARQUIVO_PEDIDO"));
        emailItemButton.setEnabled(app.isGrant("ENVIAR_ARQUIVO_PEDIDO_EMAIL"));
        posAtendimentoButton.setEnabled(app.isGrant("ATUALIZAR_POSICAO_ATENDIMENTO"));
        autorizarButton.setEnabled(app.getUser().isAdmin());
        notasTable.setRowHeight(Constants.ROWHEIGHT);
        pgtosTable.setRowHeight(Constants.ROWHEIGHT);
    }

    private void enableAutorizarCobranca(Pedido pedido) {
        //autorizarCobrancaButton.setEnabled(pedido.isPagamentos() && app.getUser().getPerfil().equals(Constants.ADMIN));
        autorizarCobrancaButton.setEnabled(false);
    }

    @Override
    protected void object2Field(Object obj) {
        Pedido pedido = (Pedido) obj;
        if (app.getUser().isVendedor() && pedido.isPrePedido()) {
            if (pedido.getVendedor().getIdVendedor().equals(app.getUser().getIdvendedor())) {
                alterarButton.setEnabled(true);
            }
        }
        enableAutorizarCobranca(pedido);

        atendimentoField.setText(pedido.getAtendimento());
        clienteField.setText(pedido.getCliente().getRazao());
        if (pedido.getClienteResponsavel() != null) {
            responsavelField.setText(pedido.getClienteResponsavel().getRazao());
        } else {
            responsavelField.setText("");
        }
        
        if ("FINANCEIRO RUIM,INATIVO,FECHOU".contains(pedido.getCliente().getSituacaoCliente().getNome())) {
            clienteField.setForeground(Color.red);
        } else {
            clienteField.setForeground(Color.BLACK);
        }

        valorOpField.setValue(pedido.getValorOp());
        comissaoField.setValue(pedido.getVlrComissaoVendedor());
        comissaoTotalField.setValue(pedido.getVlrComissaoTotal());
        comissaoVendedorField.setValue(pedido.getComissaoVendedor());
        emissaoField.setValue(pedido.getDtPedido());
        entregaField.setValue(pedido.getDtEntrega());
        formaVendaField.setValue(pedido.getFormaVenda().getNome());
        formaPgtoField.setText(pedido.getFormaPgto().getNome());
         imediatoCheckBox.setSelected(pedido.getEntregaImediata());
        
        if (pedido.getFormaPgtoCliente() != null) {
            formaPgtoClienteField.setText(pedido.getFormaPgtoCliente().getNome());
        }
        
        tipoPgtoField.setText(pedido.getTipoPgto().getNome());
        List itensPedido = (List) pedido.getItens();
        Collections.sort(itensPedido, new ItensComparator());
        ItemPedidoViewModel itemModel = new ItemPedidoViewModel(itensPedido, pedido);
        itemModel.setReadOnly(true);
        itensTable.setModel(itemModel);
        itensTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        itensTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        DecimalFormat df = NumberUtils.getDecimalFormat();
        NumericTextField comissaoEditField = new NumericTextField(8, df);
        percComissaoField.setInputVerifier(new DateVerifier(true));
        itensTable.getColumnModel().getColumn(ItemPedidoModel.COMISSAO).setPreferredWidth(45);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.COMISSAO).setCellEditor(new DefaultCellEditor(comissaoEditField));

        NotasPedidoModel notaModel = new NotasPedidoModel((List) pedido.getAtendimentos());
        //notaModel.setReadOnly(!"A".equals(pedido.getSituacao()));
        notasTable.setModel(notaModel);
        notasTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        notasTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        notasTable.getColumnModel().getColumn(NotasPedidoModel.DATAPGTO).setPreferredWidth(80);
        notasTable.getColumnModel().getColumn(NotasPedidoModel.DTDESCONTO).setPreferredWidth(100);
        notasTable.getColumnModel().getColumn(NotasPedidoModel.DTDESCONTO).setCellEditor(new DateCellEditor());
        notasTable.setSurrendersFocusOnKeystroke(true);
        
        JFormattedTextField tipoField = new javax.swing.JFormattedTextField(); 
        tipoField.setDocument(new BoundedPlainDocument(2));
        tipoField.setName("tipoField");
        DefaultCellEditor editor = new DefaultCellEditor(tipoField);
        notasTable.getColumnModel().getColumn(NotasPedidoModel.OPERADOR).setCellEditor(editor);
        
        PedidoEmbarqueModel embarqueModel = new PedidoEmbarqueModel(pedido.getPedidoEmbarqueList());
        embarquesTable.setModel(embarqueModel);
        embarquesTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        obsTextArea.setText(pedido.getObs());
        atendimentoTextArea.setText(pedido.getObsAtendimento());
        complementarTextArea.setText(pedido.getComplemento());
        obsAdicionalTextArea.setText(pedido.getObs2());
        obsClienteTextArea.setText(pedido.getObs3());
        pedidoField.setValue(pedido.getIdPedido());
        pedidoRepresField.setValue(pedido.getIdPedidoRepres());
        ArquivoPedidoModel arqModel = new ArquivoPedidoModel(pedido.getArquivos());
        anexosTable.setModel(arqModel);
        anexosTable.getColumnModel().getColumn(ArquivoPedidoModel.NOME).setPreferredWidth(250);
        
        if ((pedido.getValorOp() != null) && (pedido.getValorOp().intValue() > 0)) {
            pedidoField.setForeground(Color.blue);
        } else {
            pedidoField.setForeground(Color.black);
        }
        
        percComissaoField.setValue(pedido.getComissao());
        Collection<AtendimentoPedido> notas = pedido.getAtendimentos();
        
        if (notas == null) {
            notas = new ArrayList();
        }
        
        List<PgtoCliente> pgtos = new ArrayList<>();
   
        for (AtendimentoPedido nota : notas) {
            for (int i = 0; i < nota.getPgtos().size(); i++) {
                pgtos.add(null);
            }
            Collections.copy(pgtos, (List) nota.getPgtos());
            Collections.sort(pgtos, new TpPgtoComparator());
            break;
        }
        
        PgtoPedidoModel pgtoModel = new PgtoPedidoModel(pgtos);
        pgtosTable.setModel(pgtoModel);
        pgtosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        pgtosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.CONTA).setPreferredWidth(200);
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.PREVPGTO).setCellEditor(new DateCellEditor());
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.VALOR).setPreferredWidth(80);
        JFormattedTextField valorCobrancaField = new JFormattedTextField();
        valorCobrancaField.setInputVerifier(new DateVerifier(true));
        valorCobrancaField.setFormatterFactory(NumberUtils.getFormatterFactory());
//        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.VALORCOBRANCA).setPreferredWidth(80);
//        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.VALORCOBRANCA).setCellEditor(new DefaultCellEditor(valorCobrancaField));

        if (pedido.getFormaPgtoTransp() != null) {
            pgtoTransField.setText(pedido.getFormaPgtoTransp().getNome());
        }
        
        emitidoClienteCheckBox.setSelected("1".equals(pedido.getEmitidoCliente()));
        emitidoCobrancaCheckBox.setSelected("1".equals(pedido.getEmitidoCobranca()));
        emitidoRepresCheckBox.setSelected("1".equals(pedido.getEmitido()));
        prePedidoCheckBox.setSelected(pedido.isPrePedido());
        emitidoClienteCheckBox.setEnabled(app.isGrant("ALTERAR_INDICADORES"));
        emitidoCobrancaCheckBox.setEnabled(app.isGrant("ALTERAR_INDICADORES"));
        emitidoRepresCheckBox.setEnabled(app.isGrant("ALTERAR_INDICADORES"));
        prePedidoCheckBox.setEnabled(app.isGrant("ALTERAR_INDICADORES"));
        //preCobrancaCheckBox.setEnabled(app.isGrant("ALTERAR_INDICADORES"));

        represField.setText(pedido.getRepres().getRazao());
        situacaoField.setText(pedido.getSituacao());
        transportadorField.setText(pedido.getTransportador().getNome());
        
        StringBuilder unidades = new StringBuilder();
        Collection<UnidadeItem> unidadesItens = pedidoDao.findUnidadesByPedido(pedido.getIdPedido());
        
        for (UnidadeItem und : unidadesItens) {
            unidades.append(und.getUnidade());
            unidades.append(" ");
            unidades.append(NumberUtils.format(und.getValor()));
            unidades.append(" ");
        }
        
        unidadesTextArea.setText(unidades.toString());
        
        unidades = new StringBuilder();
        unidadesItens = pedidoDao.findUndSaldoByPedido(pedido.getIdPedido());
        
        for (UnidadeItem und : unidadesItens) {
            unidades.append(und.getUnidade());
            unidades.append(" ");
            unidades.append(NumberUtils.format(und.getValor()));
            unidades.append(" ");
        }
        
        undSaldoTextArea.setText(unidades.toString());
        vendedorField.setText(pedido.getVendedor().getNome());
        valorField.setValue(pedido.getValor());
        valorClienteField.setValue(pedido.getValorCliente());

        PedidoDao dao = (PedidoDao) app.lookupService("pedidoDao");
        BigDecimal pgtosPendentes = dao.getPgtosClientePendende(pedido.getCliente().getIdCliente());
        
        if (pgtosPendentes == null) {
            pgtosPendentes = BigDecimal.ZERO;
        }
        
        BigDecimal vendasPendentes = dao.getVendasClientePendende(pedido.getCliente().getIdCliente());
        
        if (vendasPendentes == null) {
            vendasPendentes = BigDecimal.ZERO;
        }
        
        pgtoPendenteField.setValue(pgtosPendentes);
        diasAtrasoField.setValue(pedido.getCliente().getNumDiasAtraso());
        pendenteField.setValue(vendasPendentes);
        naoAtendidoField.setValue(pedido.getCliente().getLimiteCredito());
        pendenteField.setForeground(Color.red);
        pgtoPendenteField.setForeground(Color.red);
        naoAtendidoField.setForeground(Color.red);
        diasAtrasoField.setForeground(Color.red);

        TableColumn col = itensTable.getColumnModel().getColumn(ItemPedidoModel.DESCRICAO);
        col.setPreferredWidth(280);

        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.CODIGO);
        col.setPreferredWidth(70);

        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.QUANT);
        col.setPreferredWidth(80);
        col.setCellRenderer(new FractionCellRenderer(8, 3, SwingConstants.RIGHT));


        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.COMISSAO);
        col.setPreferredWidth(45);

        itensTable.getColumnModel().getColumn(ItemPedidoModel.EMBALAGEM).setPreferredWidth(55);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.VALORCLIENTE).setPreferredWidth(80);
        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.VALOR);
        col.setPreferredWidth(80);

        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.UNIDADE);
        col.setPreferredWidth(36);

        col = itensTable.getColumnModel().getColumn(ItemPedidoModel.SITUACAO);
        col.setPreferredWidth(20);

    }

    @Action
    public void autorizar() {
        Pedido pedido = (Pedido) getValueObject();

        if (!Messages.confirmQuestion("Confirma altera\u00E7\u00E3o")) {
            return;
        }

        pedido.setPrePedido(!pedido.isPrePedido());
        try {
            pedidoDao.updatePedido(pedido);
            prePedidoCheckBox.setSelected(pedido.isPrePedido());
            Messages.infoMessage(pedido.isPrePedido() ? "Pedido bloqueado." : "Pedido autorizado.");
        } catch (DAOException ex) {
            Logger.getLogger(PedidoViewFrame.class.getName()).log(Level.SEVERE, null, ex);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }

    @Action
    public void autorizarCobranca() {
        Pedido pedido = (Pedido) getValueObject();
        if (!pedido.isPreCobranca()) {
            return;
        }
        if (!Messages.confirmQuestion("Confirma altera\u00E7\u00E3o")) {
            return;
        }

        pedido.setPreCobranca(!pedido.isPreCobranca());

        try {
            pedidoDao.updatePedido(pedido);
        } catch (DAOException ex) {
            Logger.getLogger(PedidoViewFrame.class.getName()).log(Level.SEVERE, null, ex);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }

    @Action
    public void editPedido() {
        
        if (!app.isGrant("ALTERAR_PEDIDO"))
            return;

        Pedido pedido = (Pedido) getValueObject();
        if ("A".equals(pedido.getSituacao())) {
            Messages.warningMessage(getBundle().getString("naoEditarPedido"));
            return;
        }
        PedidoEditPanel editPanel = new PedidoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setPedido(pedido);
        while (edtDlg.edit(pedidoBean)) {
            try {
                pedidoDao.updatePedido(pedido);
                object2Field(pedido);
                if (getParentViewFrame() != null) {
                    getParentViewFrame().refresh();
                }
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
        Pedido pedido = (Pedido) getValueObject();
        try {
            util.imprimirPedido(pedido);
            emitidoClienteCheckBox.setSelected("1".equals(pedido.getEmitidoCliente()));
            emitidoCobrancaCheckBox.setSelected("1".equals(pedido.getEmitidoCobranca()));
            emitidoCobrancaCheckBox.invalidate();
            emitidoRepresCheckBox.setSelected("1".equals(pedido.getEmitido()));
            emitidoClienteCheckBox.invalidate();
            emitidoRepresCheckBox.invalidate();

        } catch (Exception e) {
            getLogger().error(e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    private class SharedListSelectionHandler implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            
            if (!lsm.isSelectionEmpty()) {
                carregarPagamentos();
            }
        }
    }

    private void carregarPagamentos() {
        int row = notasTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);
        List<PgtoCliente> pgtos = new ArrayList<>();
        for (int i = 0; i < atend.getPgtos().size(); i++) {
            pgtos.add(null);
        }
        Collections.copy(pgtos, (List) atend.getPgtos());
        Collections.sort(pgtos, new TpPgtoComparator());
        //PgtoPedidoModel pgtoModel = new PgtoPedidoModel((List) atend.getPgtos());
        PgtoPedidoModel pgtoModel = new PgtoPedidoModel(pgtos);


        pgtosTable.setModel(pgtoModel);
        pgtosTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        pgtosTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.CONTA).setPreferredWidth(250);
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.PREVPGTO).setCellEditor(new DateCellEditor());
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.PREVPGTO).setPreferredWidth(80);
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.VALOR).setPreferredWidth(80);
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.DIAS).setPreferredWidth(50);
        pgtosTable.getColumnModel().getColumn(PgtoPedidoModel.OBS).setPreferredWidth(200);

        pgtoModel.fireTableDataChanged();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        remessaMenuItem = new javax.swing.JMenuItem();
        pgtoPopupMenu = new javax.swing.JPopupMenu();
        planilhaMenuItem = new javax.swing.JMenuItem();
        itemPopupMenu = new javax.swing.JPopupMenu();
        itemMenuItem = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        pedidoField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        emissaoField = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        valorField = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        situacaoField = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        atendimentoField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        clienteField = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        formaPgtoClienteField = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        represField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        percComissaoField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        formaPgtoField = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        vendedorField = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        comissaoVendedorField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        entregaField = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        transportadorField = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        pgtoTransField = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        comissaoField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        comissaoTotalField = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        valorOpField = new javax.swing.JFormattedTextField();
        tipoPgtoField = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        grupoClienteField = new javax.swing.JFormattedTextField();
        alterarButton = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        pedidoRepresField = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        repetirButton = new javax.swing.JButton();
        valorClienteField = new javax.swing.JFormattedTextField();
        autorizarButton = new javax.swing.JButton();
        pgtoPendenteField = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        naoAtendidoField = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        autorizarCobrancaButton = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        formaVendaField = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        emitidoClienteCheckBox = new javax.swing.JCheckBox();
        emitidoRepresCheckBox = new javax.swing.JCheckBox();
        emitidoCobrancaCheckBox = new javax.swing.JCheckBox();
        prePedidoCheckBox = new javax.swing.JCheckBox();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itensTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        unidadesTextArea = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsTextArea = new javax.swing.JTextArea();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        obsAdicionalTextArea = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        obsClienteTextArea = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        complementarTextArea = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        undSaldoTextArea = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        anexosTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        emailItemButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        atendimentoTextArea = new javax.swing.JTextArea();
        posAtendimentoButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        notasTable = new javax.swing.JTable();
        addNotaButton = new javax.swing.JButton();
        delNotaButton = new javax.swing.JButton();
        edtNotaButton = new javax.swing.JButton();
        cobrancaButton = new javax.swing.JButton();
        planilhaButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        pgtosTable = new javax.swing.JTable();
        addPgtoButton = new javax.swing.JButton();
        delPgtoButton = new javax.swing.JButton();
        edtPgtoButton = new javax.swing.JButton();
        edtPgtoButton1 = new javax.swing.JButton();
        repetirPgtoButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        responsavelField = new javax.swing.JFormattedTextField();
        pendenteField = new javax.swing.JFormattedTextField();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        embarquesTable = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        diasAtrasoField = new javax.swing.JFormattedTextField();
        imediatoCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        remessaMenuItem.setText(bundle.getString("marcarRemessa")); // NOI18N
        remessaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remessaMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(remessaMenuItem);

        planilhaMenuItem.setLabel(bundle.getString("planilhaCheque")); // NOI18N
        planilhaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planilhaMenuItemActionPerformed(evt);
            }
        });
        pgtoPopupMenu.add(planilhaMenuItem);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PedidoViewFrame.class);
        itemMenuItem.setText(resourceMap.getString("itemMenuItem.text")); // NOI18N
        itemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuItemActionPerformed(evt);
            }
        });
        itemPopupMenu.add(itemMenuItem);

        setClosable(true);
        setIconifiable(true);
        setTitle(bundle.getString("viewPedido")); // NOI18N

        jLabel1.setText(bundle.getString("pedido")); // NOI18N

        pedidoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        pedidoField.setFocusable(false);
        pedidoField.setFont(resourceMap.getFont("pedidoField.font")); // NOI18N

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        emissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        emissaoField.setFocusable(false);

        jLabel20.setText(bundle.getString("valor")); // NOI18N

        valorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorField.setFocusable(false);

        jLabel15.setText(bundle.getString("situacao")); // NOI18N

        situacaoField.setFocusable(false);

        jLabel16.setText(bundle.getString("atendimento")); // NOI18N

        atendimentoField.setFocusable(false);

        jLabel3.setText(bundle.getString("cliente")); // NOI18N

        clienteField.setFocusable(false);

        jLabel5.setText(bundle.getString("representada")); // NOI18N

        formaPgtoClienteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        formaPgtoClienteField.setFocusable(false);

        jLabel23.setText(bundle.getString("formaPgtoCliente")); // NOI18N

        represField.setFocusable(false);

        jLabel6.setText(bundle.getString("percComissao")); // NOI18N

        percComissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        percComissaoField.setFocusable(false);

        jLabel7.setText(bundle.getString("formaPgtoRepres")); // NOI18N

        formaPgtoField.setFocusable(false);

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        vendedorField.setFocusable(false);

        jLabel9.setText(bundle.getString("percComissao")); // NOI18N

        comissaoVendedorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        comissaoVendedorField.setFocusable(false);

        jLabel10.setText(bundle.getString("embarque")); // NOI18N

        entregaField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        entregaField.setFocusable(false);

        jLabel11.setText(bundle.getString("transportador")); // NOI18N

        transportadorField.setFocusable(false);

        jLabel14.setText(bundle.getString("formaPgtoTransp")); // NOI18N

        pgtoTransField.setFocusable(false);

        jLabel17.setText(bundle.getString("valorCliente")); // NOI18N

        comissaoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        comissaoField.setFocusable(false);

        jLabel18.setText(bundle.getString("comissaoTotal")); // NOI18N

        comissaoTotalField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        comissaoTotalField.setFocusable(false);

        jLabel12.setText(bundle.getString("valorOp")); // NOI18N

        valorOpField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorOpField.setFocusable(false);

        tipoPgtoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        tipoPgtoField.setFocusable(false);

        jLabel21.setText(bundle.getString("tipoPgto")); // NOI18N

        jLabel4.setText(bundle.getString("grupoCliente")); // NOI18N

        grupoClienteField.setFocusable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(PedidoViewFrame.class, this);
        alterarButton.setAction(actionMap.get("editPedido")); // NOI18N

        jLabel22.setText(bundle.getString("codigoRepres")); // NOI18N

        pedidoRepresField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        pedidoRepresField.setFocusable(false);
        pedidoRepresField.setFont(resourceMap.getFont("pedidoRepresField.font")); // NOI18N

        jLabel24.setText(bundle.getString("comissaoVendedor")); // NOI18N

        repetirButton.setText(bundle.getString("repetir")); // NOI18N
        repetirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repetirButtonActionPerformed(evt);
            }
        });

        valorClienteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorClienteField.setFocusable(false);

        autorizarButton.setAction(actionMap.get("autorizar")); // NOI18N

        pgtoPendenteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pgtoPendenteField.setFocusable(false);

        jLabel25.setText(bundle.getString("pgtoPendente")); // NOI18N

        naoAtendidoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        naoAtendidoField.setFocusable(false);

        jLabel26.setText(bundle.getString("lc")); // NOI18N

        autorizarCobrancaButton.setAction(actionMap.get("autorizarCobranca")); // NOI18N

        jLabel27.setText(bundle.getString("formaVenda")); // NOI18N

        formaVendaField.setFocusable(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        emitidoClienteCheckBox.setText(bundle.getString("emitidoCliente")); // NOI18N
        emitidoClienteCheckBox.setFocusable(false);

        emitidoRepresCheckBox.setText(bundle.getString("emitidoFornec")); // NOI18N
        emitidoRepresCheckBox.setFocusable(false);

        emitidoCobrancaCheckBox.setText(bundle.getString("emitidoCobranca")); // NOI18N
        emitidoCobrancaCheckBox.setFocusable(false);

        prePedidoCheckBox.setText(bundle.getString("prePedido")); // NOI18N
        prePedidoCheckBox.setFocusable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emitidoRepresCheckBox)
                    .addComponent(emitidoCobrancaCheckBox)
                    .addComponent(emitidoClienteCheckBox)
                    .addComponent(prePedidoCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prePedidoCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(emitidoClienteCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emitidoRepresCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emitidoCobrancaCheckBox)
                .addGap(44, 44, 44))
        );

        itensTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itensTable.setComponentPopupMenu(itemPopupMenu);
        jScrollPane1.setViewportView(itensTable);

        unidadesTextArea.setLineWrap(true);
        unidadesTextArea.setRows(5);
        unidadesTextArea.setFocusable(false);
        jScrollPane5.setViewportView(unidadesTextArea);

        jLabel19.setText(bundle.getString("unidades")); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("observacoes"))); // NOI18N

        obsTextArea.setLineWrap(true);
        obsTextArea.setRows(5);
        obsTextArea.setFocusable(false);
        jScrollPane2.setViewportView(obsTextArea);

        jLabel29.setText(bundle.getString("observacaoFornecedor")); // NOI18N

        obsAdicionalTextArea.setLineWrap(true);
        obsAdicionalTextArea.setRows(5);
        obsAdicionalTextArea.setFocusable(false);
        jScrollPane8.setViewportView(obsAdicionalTextArea);

        jLabel28.setText(bundle.getString("observacao2")); // NOI18N

        obsClienteTextArea.setLineWrap(true);
        obsClienteTextArea.setRows(5);
        obsClienteTextArea.setFocusable(false);
        jScrollPane9.setViewportView(obsClienteTextArea);

        jLabel30.setText(bundle.getString("obsCliente")); // NOI18N

        jLabel31.setText(bundle.getString("complementar")); // NOI18N

        complementarTextArea.setForeground(resourceMap.getColor("complementarTextArea.foreground")); // NOI18N
        complementarTextArea.setLineWrap(true);
        complementarTextArea.setRows(5);
        complementarTextArea.setFocusable(false);
        jScrollPane6.setViewportView(complementarTextArea);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        undSaldoTextArea.setBackground(resourceMap.getColor("undSaldoTextArea.background")); // NOI18N
        undSaldoTextArea.setLineWrap(true);
        undSaldoTextArea.setRows(5);
        undSaldoTextArea.setFocusable(false);
        jScrollPane12.setViewportView(undSaldoTextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1178, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Itens", jPanel1);

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewButton)
                    .addComponent(addButton)
                    .addComponent(deleteItemButton)
                    .addComponent(emailItemButton))
                .addGap(923, 923, 923))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteItemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailItemButton))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("anexos"), jPanel3); // NOI18N

        jLabel33.setText(bundle.getString("obs")); // NOI18N

        atendimentoTextArea.setLineWrap(true);
        atendimentoTextArea.setRows(5);
        atendimentoTextArea.setFocusable(false);
        jScrollPane10.setViewportView(atendimentoTextArea);

        posAtendimentoButton.setText(bundle.getString("atualizar")); // NOI18N
        posAtendimentoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                posAtendimentoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(posAtendimentoButton))
                    .addComponent(jLabel33))
                .addContainerGap(952, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(posAtendimentoButton)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("posicaoAtendimento"), jPanel5); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("notas"))); // NOI18N

        notasTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(notasTable);

        addNotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N
        addNotaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNotaButtonActionPerformed(evt);
            }
        });

        delNotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        delNotaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delNotaButtonActionPerformed(evt);
            }
        });

        edtNotaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Edit16.gif"))); // NOI18N
        edtNotaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtNotaButtonActionPerformed(evt);
            }
        });

        cobrancaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/paste.png"))); // NOI18N
        cobrancaButton.setToolTipText(bundle.getString("controleCobranca")); // NOI18N
        cobrancaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cobrancaButtonActionPerformed(evt);
            }
        });

        planilhaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/paste.png"))); // NOI18N
        planilhaButton.setToolTipText(bundle.getString("planilhaCheque")); // NOI18N
        planilhaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planilhaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(delNotaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edtNotaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addNotaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cobrancaButton)
                    .addComponent(planilhaButton))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cobrancaButton)
                            .addComponent(addNotaButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(planilhaButton)
                            .addComponent(edtNotaButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delNotaButton)))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("pagamentos"))); // NOI18N

        pgtosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane4.setViewportView(pgtosTable);

        addPgtoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N
        addPgtoButton.setToolTipText(resourceMap.getString("addPgtoButton.toolTipText")); // NOI18N
        addPgtoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPgtoButtonActionPerformed(evt);
            }
        });

        delPgtoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        delPgtoButton.setToolTipText(resourceMap.getString("delPgtoButton.toolTipText")); // NOI18N
        delPgtoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPgtoButtonActionPerformed(evt);
            }
        });

        edtPgtoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Edit16.gif"))); // NOI18N
        edtPgtoButton.setToolTipText(resourceMap.getString("edtPgtoButton.toolTipText")); // NOI18N
        edtPgtoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtPgtoButtonActionPerformed(evt);
            }
        });

        edtPgtoButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/Edit16.gif"))); // NOI18N
        edtPgtoButton1.setToolTipText(resourceMap.getString("edtPgtoButton1.toolTipText")); // NOI18N
        edtPgtoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtPgtoButton1ActionPerformed(evt);
            }
        });

        repetirPgtoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/copy.png"))); // NOI18N
        repetirPgtoButton.setToolTipText(resourceMap.getString("repetirPgtoButton.toolTipText")); // NOI18N
        repetirPgtoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repetirPgtoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1119, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(repetirPgtoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(edtPgtoButton1, 0, 0, Short.MAX_VALUE)
                    .addComponent(edtPgtoButton, 0, 0, Short.MAX_VALUE)
                    .addComponent(delPgtoButton, 0, 0, Short.MAX_VALUE)
                    .addComponent(addPgtoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(addPgtoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delPgtoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtPgtoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(repetirPgtoButton)
                            .addComponent(edtPgtoButton1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("Atendimento", jPanel2);

        jLabel13.setText(bundle.getString("clienteResponsavel")); // NOI18N

        responsavelField.setFocusable(false);

        pendenteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pendenteField.setFocusable(false);

        jLabel32.setText(bundle.getString("pendente")); // NOI18N

        embarquesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane11.setViewportView(embarquesTable);

        jLabel34.setText(bundle.getString("diasAtraso")); // NOI18N

        diasAtrasoField.setEditable(false);

        imediatoCheckBox.setText(bundle.getString("imediato")); // NOI18N
        imediatoCheckBox.setFocusable(false);
        imediatoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imediatoCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane2, 0, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel20)))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)
                                .addGap(14, 14, 14)
                                .addComponent(jLabel18)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(valorClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(comissaoTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(situacaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(atendimentoField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(valorOpField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(alterarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autorizarButton)
                                .addGap(4, 4, 4)
                                .addComponent(autorizarCobrancaButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(repetirButton))
                            .addComponent(jLabel12)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(represField, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addGap(11, 11, 11)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(percComissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(formaPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel7))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(responsavelField, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel27)
                                            .addComponent(formaVendaField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel22)
                                            .addComponent(pedidoRepresField, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(39, 39, 39)
                                        .addComponent(imediatoCheckBox)))
                                .addGap(78, 78, 78)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(vendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(11, 11, 11)
                                        .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(transportadorField, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pgtoTransField, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(18, 18, 18)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(303, 303, 303)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel23)
                                .addGap(103, 103, 103)
                                .addComponent(jLabel21))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(clienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(grupoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(formaPgtoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tipoPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(pendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel26))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(naoAtendidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jLabel17))
                    .addComponent(jLabel18)
                    .addComponent(jLabel15)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jLabel12)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valorClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comissaoTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(situacaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(atendimentoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(valorOpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(alterarButton)
                    .addComponent(autorizarButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(autorizarCobrancaButton)
                        .addComponent(repetirButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel23)
                                .addComponent(jLabel21))
                            .addGap(8, 8, 8)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(clienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(grupoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(formaPgtoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tipoPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel25)
                                .addComponent(jLabel26))
                            .addGap(8, 8, 8)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(naoAtendidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel32)
                            .addGap(8, 8, 8)
                            .addComponent(pendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(imediatoCheckBox)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel27))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(responsavelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(formaVendaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(8, 8, 8)
                                        .addComponent(pedidoRepresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(formaPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(percComissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(represField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(transportadorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pgtoTransField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void atualizaValorItemAtend(ItemPedidoAtend item, Collection<ItemPedido> lista) {
        for (ItemPedido i : lista) {
            if (item.getProduto().getIdProduto().equals(i.getProduto().getIdProduto())) {
                item.setValor(i.getValorCliente());
            }
        }
    }

    private void remessaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remessaMenuItemActionPerformed
        Pedido pedido = (Pedido) getValueObject();
        if ("A".equals(pedido.getSituacao())) {
            Messages.warningMessage(getBundle().getString("naoEditarPedido"));
            return;
        }
        int row = itensTable.getSelectedRow();
        if (row >= 0) {
            ItemPedidoModel model = (ItemPedidoModel) itensTable.getModel();
            ItemPedido itemPedido = (ItemPedido) model.getObject(row);
            if ("N".equals(itemPedido.getSituacao()))
                itemPedido.setSituacao("R");
            else if ("R".equals(itemPedido.getSituacao()))
                 itemPedido.setSituacao("N");
            try {
                pedidoDao.updateRow(itemPedido);
                model.fireTableRowsUpdated(row, row);
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }//GEN-LAST:event_remessaMenuItemActionPerformed
    private boolean editObsCobranca(CobrancaVO cobranca) {
        ObsCobrancaEdtPanel editPanel = new ObsCobrancaEdtPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("ctrlPgtosTitle"));
        editPanel.setEditMode(true);
        edtDlg.setEditPanel(editPanel);
        boolean result = false;
        while (edtDlg.edit(cobranca)) {
            try {
                if (!app.getUser().isVendedor()) {
                    pedidoDao.updateRow(cobranca.getAtend());
                }
                result = true;
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
        return result;
    }
    private void planilhaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planilhaMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_planilhaMenuItemActionPerformed

    private void repetirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repetirButtonActionPerformed
        if (!Messages.confirmQuestion("Confirma repetir")) {
            return;
        }
        Pedido pedido = (Pedido) getObject();
        PedidoDao dao = (PedidoDao) app.lookupService("pedidoDao");
        Integer id = dao.getNextValue();
        Pedido novo = new Pedido();
        novo.setIdPedido(id);
        novo.setAcompanhar(pedido.getAcompanhar());
        novo.setAtendimento("N");
        novo.setCliente(pedido.getCliente());
        novo.setClienteResponsavel(pedido.getClienteResponsavel());
        novo.setComissao(pedido.getComissao());
        novo.setComissaoVendedor(pedido.getComissaoVendedor());
        novo.setComplemento(pedido.getComplemento());
        novo.setDtEntrega(pedido.getDtEntrega());
        novo.setDtPedido(pedido.getDtPedido());
        novo.setDtcancel(pedido.getDtcancel());
        novo.setEmitido("0");
        novo.setEmitidoCliente("0");
        novo.setEmitidoCobranca("0");
        novo.setEmbarqueImediato("0");
        novo.setFormaPgto(pedido.getFormaPgto());
        novo.setFormaPgtoCliente(pedido.getFormaPgtoCliente());
        novo.setFormaPgtoTransp(pedido.getFormaPgtoTransp());
        novo.setFormaVenda(pedido.getFormaVenda());
        novo.setFrete(pedido.getFrete());
        novo.setIpi(pedido.getIpi());
        novo.setIsromaneio(pedido.getIsromaneio());
        novo.setObs(pedido.getObs());
        novo.setObs2(pedido.getObs2());
        novo.setObs3(pedido.getObs3());
        //novo.setPreCobranca("1");
        novo.setPreCobranca("0");
        novo.setPrePedido("1");
        novo.setReposicao(pedido.getReposicao());
        novo.setRepres(pedido.getRepres());
        novo.setSeguro(pedido.getSeguro());
        novo.setSituacao("N");
        novo.setTipoPedido(pedido.getTipoPedido());
        novo.setTipoPgto(pedido.getTipoPgto());
        novo.setTpentrega(pedido.getTpentrega());
        novo.setTransportador(pedido.getTransportador());
        novo.setValor(pedido.getValor());
        novo.setValorCliente(pedido.getValorCliente());
        novo.setValorComissao(pedido.getValorComissao());
        novo.setValorOp(pedido.getValorOp());
        novo.setValorPendente(pedido.getValorPendente());
        novo.setVendedor(pedido.getVendedor());
        ItemPedido item;
        List<ItemPedido> itens = new ArrayList<ItemPedido>();
        for (ItemPedido o : pedido.getItens()) {
            item = new ItemPedido();
            item.setDtEntrega(o.getDtEntrega());
            item.setIpi(o.getIpi());
            ItemPedidoPK pk = new ItemPedidoPK();
            pk.setIdPedido(id);
            pk.setIdProduto(o.getItemPedidoPK().getIdProduto());
            item.setItemPedidoPK(pk);
            item.setValorCliente(o.getValorCliente());
            item.setPedido(o.getPedido());
            item.setProduto(o.getProduto());
            item.setQtd(o.getQtd());
            item.setSituacao("N");
            item.setValor(o.getValor());
            item.setEmbalagem(o.getEmbalagem());
            item.setPercComissao(o.getPercComissao());
            itens.add(item);
        }
        novo.setItens(itens);
        PedidoUtil util = new PedidoUtil();
        try {
            dao.insertRecord(novo);
            dao.setNextValue(novo.getIdPedido());
            Messages.infoMessage("Pedido criado: " + novo.getIdPedido());
            util.viewPedido(novo);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.errorMessage(app.getResourceString("saveErrorMessage"));
        }
    }//GEN-LAST:event_repetirButtonActionPerformed

    private void addNotaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNotaButtonActionPerformed
        Pedido pedido = (Pedido) getValueObject();
        if ("A".equals(pedido.getSituacao())) {
            Messages.warningMessage(getBundle().getString("naoIncluirNota"));
            return;
        }
        if(pedido.isPrePedido()) {
            Messages.warningMessage("Falha ao incluir nota: pedido deve estar autorizado!");
            return;
        }
        AtendimentoPedido atend = new AtendimentoPedido();

        Collection<ItemPedido> lista = pedido.getItens();
        for (ItemPedido item : lista) {
            ItemPedidoAtend itemA = new ItemPedidoAtend();
            itemA.setAtendimentoPedido(atend);
            itemA.setDtEntrega(item.getDtEntrega());
            itemA.setProduto(item.getProduto());
            itemA.setQtd(item.getQtd());
            itemA.setValor(item.getValorCliente());
            itemA.setEmbalagem(item.getEmbalagem());
            itemA.getItemPedidoAtendPK().setIdPedido(pedido.getIdPedido());
            itemA.getItemPedidoAtendPK().setIdProduto(item.getProduto().getIdProduto());
            itemA.setPercComissao(item.getPercComissao());
            atend.getItens().add(itemA);

        }

        atend.setPedido(pedido);
        atend.getAtendimentoPedidoPK().setIdPedido(pedido.getIdPedido());

        if ((pedido.getAtendimentos() != null) && (pedido.getAtendimentos().size() > 0)) {
            try {
                atend.setItens(pedidoDao.findItensPedidoPendentes(pedido.getIdPedido()));
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }

        NotaEdtPanel editPanel = new NotaEdtPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addNotaPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(atend)) {
            try {
                pedidoDao.insertRecord(atend);
                pedidoDao.updSituacaoItem(pedido.getItens());
                pedidoDao.updateSituacaoPedido(pedido);
                pedido.getAtendimentos().add(atend);
                situacaoField.setText(pedido.getSituacao());
                atendimentoField.setText(pedido.getAtendimento());
                ((NotasPedidoModel) notasTable.getModel()).fireTableDataChanged();
                ((ItemPedidoModel) itensTable.getModel()).fireTableDataChanged();
                enableAutorizarCobranca(pedido);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
}//GEN-LAST:event_addNotaButtonActionPerformed

    private void delNotaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delNotaButtonActionPerformed
        int row = notasTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecioneNota"));
            return;
        }

        Pedido pedido = (Pedido) getValueObject();
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);
        if ("A".equals(pedido.getSituacao()) || (atend.getDtPgtoComissao() != null)) {
            Messages.warningMessage(getBundle().getString("naoEditarPedido"));
            return;
        }

        if (atend.getDtPgtoComissao() != null) {
            Messages.warningMessage("Não pode excluir nota: comissão atendida.");
            return;
        }
        try {
            pedidoDao.deleteAtendimento(atend);
            model.removeObject(row);
            List<PgtoCliente> pgtos = new ArrayList<>();
            PgtoPedidoModel pgtoModel = new PgtoPedidoModel(pgtos);
            pgtosTable.setModel(pgtoModel);
            pgtoModel.fireTableDataChanged();
            pedidoDao.updSituacaoItem(pedido.getItens());
            ((ItemPedidoModel) itensTable.getModel()).fireTableDataChanged();
            int n = model.getRowCount() - 1;
            if (n >= 0) {
                notasTable.setRowSelectionInterval(n, n);
                notasTable.scrollRectToVisible(notasTable.getCellRect(n, 0, true));
            }
            //preCobrancaCheckBox.setSelected(pedido.isPreCobranca());
            enableAutorizarCobranca(pedido);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
}//GEN-LAST:event_delNotaButtonActionPerformed

    private void edtNotaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtNotaButtonActionPerformed
        Pedido pedido = (Pedido) getValueObject();
        //if ("A".equals(pedido.getSituacao())) {
        //    Messages.warningMessage(getBundle().getString("naoIncluirNota"));
        //}
        int row = notasTable.getSelectedRow();
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecioneNota"));
            return;
        } else {
            Collection<ItemPedido> lista = pedido.getItens();
            Collection<ItemPedidoAtend> itens = atend.getItens();
            for (ItemPedidoAtend item : itens) {
                atualizaValorItemAtend(item, lista);
            }
        }
        atend.setPedido(pedido);
        boolean atualizarNota = true;
        if (!app.getUser().isAdmin()) {
            if (atend.getDtPgtoComissao() != null) {
                Messages.warningMessage(getBundle().getString("naoAlterarNota"));
                atualizarNota = false;
            }
        }
        NotaEdtPanel editPanel = new NotaEdtPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editNotaPedidoTitle"));
        editPanel.setEditMode(atualizarNota);
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(atend)) {
            if (atualizarNota) {
                try {
                    pedidoDao.updateAtendimento(atend);
                    //pedidoDao.updateRow(pedido);
                    pedidoDao.updSituacaoItem(pedido.getItens());
                    pedidoDao.updateSituacaoPedido(pedido);
                    //pedido.getAtendimentos().add(atend);
                    situacaoField.setText(pedido.getSituacao());
                    atendimentoField.setText(pedido.getAtendimento());
                    //pedido.getAtendimentos().add(atend);
                    model.fireTableDataChanged();
                    ((ItemPedidoModel) itensTable.getModel()).fireTableDataChanged();
                    break;
                } catch (Exception e) {
                    getLogger().error(getBundle().getString("saveErrorMessage"), e);
                    Messages.errorMessage(getBundle().getString("saveErrorMessage"));
                }
            } else {
                break;

            }
        }
}//GEN-LAST:event_edtNotaButtonActionPerformed

    private void cobrancaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cobrancaButtonActionPerformed
        int row = notasTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecioneNota"));
            return;
        }
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);
        Pedido pedido = atend.getPedido();
        CobrancaVO cobranca = new CobrancaVO();
        cobranca.setAtend(atend);
        cobranca.setAnexos(pedido.getArquivos());
        cobranca.setIncluirAnexo(false);
        
        if (!editObsCobranca(cobranca)) {
            return;
        }
        
        URL url;
        url = getClass().getResource(Constants.JRCOBRANCA);
        Map map = app.getDefaultMap(app.getResourceString("demonstrativoCobranca"));
        map.put("ipi", atend.getValorIpi());
        map.put("seguro", atend.getValorSeguro());
        map.put("icms", atend.getValorIcms());
        map.put("frete", atend.getValorFrete());
        map.put("subsTrib", atend.getSubsTrib());

        EmpresaDao empresaDao = (EmpresaDao) app.lookupService("empresaDao");
        Object o = new Integer(-1);

        List lista = new ArrayList();
        List<PgtoCliente> pgtos = new ArrayList<PgtoCliente>();
        for (int i = 0; i < atend.getPgtos().size(); i++) {
            pgtos.add(null);
        }
        Collections.copy(pgtos, (List) atend.getPgtos());
        Collections.sort(pgtos, new TpPgtoComparator());
        atend.setPgtos(pgtos);

        lista.add(atend);
        try {
            Params value = (Params) empresaDao.findById(Params.class, o);
            map.put("fone", value.getFone());
            map.put("fone2", value.getFone2());
            map.put("email", value.getEmail());
            map.put("msgCobranca", value.getMsgCobranca());
            
            ReportViewFrame rvf = Reports.showReport(url, map, new CobrancaDataSource(lista));
            EmailBean eb = rvf.getEmail();
            List<String> to = new ArrayList();
            
            rvf.setCobranca(pedido);
            rvf.setParentViewFrame(this);
            rvf.setIncluirAnexo(cobranca.getIncluirAnexo());
            if (cobranca.getIncluirAnexo()) {
                rvf.setAnexos(cobranca.getAnexosSelecionados());
            }
            for (ClienteContato contato : pedido.getClienteResponsavel().getContatos()) {
                if (ClienteContato.EMAILCOMERCIAL.equals(contato.getTipoContato()) || ClienteContato.EMAILFINANCEIRO.equals(contato.getTipoContato())) {
                    to.add(contato.getEndereco());
                }
            }
            //n\u00E3o enviar para fornecedor
            //for (RepresContato contato : pedido.getRepres().getContatos()) {
            //    if (RepresContato.EMAIL.equals(contato.getTipoContato())) {
            //        to.add(contato.getEndereco());
            //    }
            //}
            eb.setTo(to);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

}//GEN-LAST:event_cobrancaButtonActionPerformed

    public void atualizaEmitidoCobranca() {
        emitidoCobrancaCheckBox.invalidate();
    }
    
    private void planilhaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planilhaButtonActionPerformed
        // TODO add your handling code here:
        int row = notasTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecioneNota"));
            return;
        }
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);
        URL url;
        url = getClass().getResource(Constants.JRPLANILHACHEQUES);
        Map map = app.getDefaultMap(app.getResourceString("planilhaCheque"));
        map.put("ipi", atend.getValorIpi());
        map.put("seguro", atend.getValorSeguro());
        map.put("icms", atend.getValorIcms());
        map.put("frete", atend.getValorFrete());

        EmpresaDao empresaDao = (EmpresaDao) app.lookupService("empresaDao");
        Object o = -1;

        List lista = new ArrayList();
        List<PgtoCliente> pgtos = new ArrayList<>();
        for (int i = 0; i < atend.getPgtos().size(); i++) {
            pgtos.add(null);
        }
        Collections.copy(pgtos, (List) atend.getPgtos());
        Collections.sort(pgtos, new TpPgtoComparator());
        atend.setPgtos(pgtos);

        lista.add(atend);
        try {
            Params value = (Params) empresaDao.findById(Params.class, o);
            map.put("fone", value.getFone());
            map.put("msgCobranca", value.getMsgCobranca());
            ReportViewFrame rvf = Reports.showReport(url, map, new CobrancaDataSource(lista));
            EmailBean eb = rvf.getEmail();

        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }//GEN-LAST:event_planilhaButtonActionPerformed

    private void repetirLancamento() {
        int rows[] = pgtosTable.getSelectedRows();
        if (rows.length <= 0) {
            Messages.warningMessage(getBundle().getString("selecionePgto"));
            return;
        }
                
        String[] options = {getBundle().getString("repetir"), getBundle().getString("cancelar")};
        int qtd;
        int numDias;
        RepetirLancPanel panel = new RepetirLancPanel();
        if (JOptionPane.showOptionDialog(this, panel, getBundle().getString("repetirLancamento"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == 0) {
            qtd = panel.getQuantidade();
            numDias = panel.getNumDias();
            List<PgtoCliente> lista = new ArrayList();
            
            PgtoPedidoModel ppm = (PgtoPedidoModel)pgtosTable.getModel();
            for (int i : rows) {
                lista.add((PgtoCliente) ppm.getObject(i));
            }
            try {
                //pedidoDao.exportarPgtoCliente(lista, qtd, numDias);
                for (PgtoCliente pgc : lista) {
                    Date dtPrevPgto = pgc.getDtPrevisaoPgto();
                    Date dtVencimento = pgc.getDtVencimento();
                    for (int i = 0; i < qtd; i++) {
                        if (dtPrevPgto != null)
                            dtPrevPgto = DateUtils.getNextDate(dtPrevPgto, numDias);
                        if (dtVencimento != null)
                            dtVencimento = DateUtils.getNextDate(dtVencimento, numDias);
                        PgtoCliente value = new PgtoCliente();
                        value.setAtendimentoPedido(pgc.getAtendimentoPedido());
                        value.setCobranca(pgc.getCobranca());
                        value.setComplemento(pgc.getComplemento());
                        value.setContaRepres(pgc.getContaRepres());
                        value.setDtPrevisaoPgto(dtPrevPgto);
                        value.setDtVencimento(dtVencimento);
                        value.setObservacao(pgc.getObservacao());
                        value.setTipoPgto(pgc.getTipoPgto());
                        value.setValor(pgc.getValor());
                        
                        pedidoDao.incluirPagamento(value);
                        ppm.addObject(value);

                    }
                }
                ppm.fireTableDataChanged();
            } catch (Exception e) {
                getLogger().error("Erro ao repetir lancamento", e);
                JOptionPane.showMessageDialog(this, getBundle().getString("exportError"), getBundle().getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    private void addPgtoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPgtoButtonActionPerformed
        Pedido pedido = (Pedido) getValueObject();
        int row = notasTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecioneNota"));
            return;
        }
        NotasPedidoModel model = (NotasPedidoModel) notasTable.getModel();
        AtendimentoPedido atend = (AtendimentoPedido) model.getObject(row);

        PgtoCliente pgto = new PgtoCliente();
        pgto.setAtendimentoPedido(atend);
        PgtoEditPanel editPanel = new PgtoEditPanel();
        RepresDao represDao = (RepresDao) app.lookupService("represDao");
        Repres r = null;
        try {
            r = (Repres) represDao.findById(Repres.class, -1);
        } catch (Exception e) {
        }

        List<ContaRepres> contas = new ArrayList<ContaRepres>();
        contas.addAll(pedido.getRepres().getContasAtivas());
        if (r != null) {
            contas.addAll(r.getContas());
        }
        editPanel.setContas(contas);
        EditDialog edtDlg = new EditDialog(getBundle().getString("addPgtoPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(pgto)) {
            try {
                pedidoDao.incluirPagamento(pgto);
                PgtoPedidoModel modelPgto = (PgtoPedidoModel) pgtosTable.getModel();
                //                modelPgto.addObject(pgto);
                atend.getPgtos().add(pgto);
                carregarPagamentos();
                //preCobrancaCheckBox.setSelected(pedido.isPreCobranca());
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
        ((NotasPedidoModel) notasTable.getModel()).fireTableDataChanged();
}//GEN-LAST:event_addPgtoButtonActionPerformed

    private void delPgtoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delPgtoButtonActionPerformed
        int row = pgtosTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecionePgto"));
            return;
        }
        PgtoPedidoModel model = (PgtoPedidoModel) pgtosTable.getModel();
        PgtoCliente pgto = (PgtoCliente) model.getObject(row);
        Pedido pedido = (Pedido) getValueObject();
        try {
            pedidoDao.deleteRow(pgto);
            //preCobrancaCheckBox.setSelected(pedido.isPreCobranca());
            model.removeObject(row);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
        model.fireTableDataChanged();
}//GEN-LAST:event_delPgtoButtonActionPerformed

    private void edtPgtoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtPgtoButtonActionPerformed
        getLogger().info("edtPgtoButtonActionPerformed");
        int row = pgtosTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecionePgto"));
            return;
        }
        Pedido pedido = (Pedido) getValueObject();
        PgtoPedidoModel model = (PgtoPedidoModel) pgtosTable.getModel();
        NotasPedidoModel notasModel = (NotasPedidoModel) notasTable.getModel();
        PgtoCliente pgto = (PgtoCliente) model.getObject(row);
        if (pgto.getDtPgto() != null) {
            Messages.warningMessage(getBundle().getString("vencimentoNaoAlterar"));
            return;
        }

        PgtoEditPanel editPanel = new PgtoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editPgtoPedidoTitle"));
        edtDlg.setEditPanel(editPanel);
        RepresDao represDao = (RepresDao) app.lookupService("represDao");
        Repres r = null;
        try {
            r = (Repres) represDao.findById(Repres.class, -1);
        } catch (Exception e) {
        }

        List<ContaRepres> contas = new ArrayList<ContaRepres>();
        contas.addAll(pedido.getRepres().getContasAtivas());
        if (r != null) {
            contas.addAll(r.getContas());
        }
        editPanel.setContas(contas);


        while (edtDlg.edit(pgto)) {
            try {
                pedidoDao.atualizarPagamento(pgto);
                ClienteDao clidao = (ClienteDao)app.lookupService("clienteDao");
                clidao.atualizaAtrasoCliente(pedido.getCliente().getIdCliente());
                model.fireTableRowsUpdated(row, row);
                notasModel.fireTableDataChanged();
                break;

            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
}//GEN-LAST:event_edtPgtoButtonActionPerformed

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
            Pedido pedido = (Pedido) getObject();
            ArquivoPedido arq = new ArquivoPedido();
            arq.setPedido(pedido);
            arq.setDescricao(file.getName());
            byte[] bFile = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(bFile);
                fileInputStream.close();
                arq.setArquivo(bFile);
                pedidoDao.insertRecord(arq);
                ArquivoPedidoModel model = (ArquivoPedidoModel) anexosTable.getModel();
                model.addObject(arq);
                model.fireTableDataChanged();
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
}//GEN-LAST:event_addButtonActionPerformed

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        // TODO add your handling code here:
        int i = anexosTable.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage("Selecione um arquivo");
            return;
        }
        ArquivoPedidoModel model = (ArquivoPedidoModel) anexosTable.getModel();
        ArquivoPedido arq = (ArquivoPedido) model.getObject(i);
        try {
            InputStream is = arq.getBlob().getBinaryStream();
            Reports.showReportStream(is, arq.getDescricao());
        } catch (Exception e) {
            getLogger().error("Falha ao abrir", e);
            Messages.errorMessage("Falha ao abrir");
        }
}//GEN-LAST:event_viewButtonActionPerformed

    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
        if (!app.getUser().isAdmin() && !app.getUser().isEscritorio()) {
            return;                    
        }
        
        int i = anexosTable.getSelectedRow();
        if (i < 0) {
            Messages.warningMessage("Selecione um arquivo");
            return;
        }
        if (i >= 0) {
            ArquivoPedidoModel model = (ArquivoPedidoModel) anexosTable.getModel();
            ArquivoPedido arq = (ArquivoPedido) model.getObject(i);
            try {
                pedidoDao.deleteRow(arq);
                model.removeObject(i);
            } catch (Exception e) {
                getLogger().error(getBundle().getString("deleteErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
            }
        }
}//GEN-LAST:event_deleteItemButtonActionPerformed

    private void emailItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailItemButtonActionPerformed
        int rows[] = anexosTable.getSelectedRows();
        if (rows.length <= 0) {
            Messages.warningMessage(getBundle().getString("selectMany"));
            return;
        }
        Pedido pedido = (Pedido) getObject();
        //String fileName = pedido.getIdPedido() + "_" + pedido.getCliente().getFantasia() + ".pdf";
        EmailBean email = (EmailBean) app.lookupService("emailBean");
        loadProperties(email);
        EmailPanel editPanel = new EmailPanel();
        User user = (User) app.getUser();

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
                Messages.errorMessage("Inclua um destinatário.");
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
            session.setDebug(false); //Habilita o LOG das ações executadas durante o envio do email

            //Objeto que contém a mensagem
            Message msg = new MimeMessage(session);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            try {
                messageBodyPart.setText(email.getText() + '\n' + user.getAssinatura());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                DataSource source;
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

                //Setando o destinatário
                for (String s : email.getTo()) {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
                }
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
                EmpresaDao empresaDao = (EmpresaDao) app.lookupService("empresaDao");
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

    private void edtPgtoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtPgtoButton1ActionPerformed
       getLogger().info("edtPgtoButton1ActionPerformed");
        if (!(app.getUser().isAdmin() || app.getUser().isEscritorio())) {
            return;
        }
        int row = pgtosTable.getSelectedRow();
        if (row < 0) {
            Messages.warningMessage(getBundle().getString("selecionePgto"));
            return;
        }
        PgtoPedidoModel model = (PgtoPedidoModel) pgtosTable.getModel();
        PgtoCliente pgto = (PgtoCliente) model.getObject(row);
        
        if ("P".equals(pgto.getTipoPgto()) && app.getUser().isEscritorio()) {
            return;
        }
        

        Date d = pgto.getDtPgto();
        BigDecimal b = pgto.getValorPgto();
        pgto.setDtPgto(null);
        pgto.setValorPgto(null);
        
        try {
            pedidoDao.updateRow(pgto);
            ClienteDao clidao = (ClienteDao)app.lookupService("clienteDao");
            Pedido pedido = (Pedido) getValueObject();
            clidao.atualizaAtrasoCliente(pedido.getCliente().getIdCliente());
            model.fireTableDataChanged();
        } catch (Exception e) {
            pgto.setDtPgto(d);
            pgto.setValorPgto(b);
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
        }
    }//GEN-LAST:event_edtPgtoButton1ActionPerformed

    private void posAtendimentoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_posAtendimentoButtonActionPerformed
        Pedido pedido = (Pedido) getValueObject();
        PedidoUtil util = new PedidoUtil();
        util.obsAtendimentoPedido(pedido);
        atendimentoTextArea.setText(pedido.getObsAtendimento());
        atendimentoTextArea.invalidate();
    }//GEN-LAST:event_posAtendimentoButtonActionPerformed

    private void repetirPgtoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repetirPgtoButtonActionPerformed
        repetirLancamento();
    }//GEN-LAST:event_repetirPgtoButtonActionPerformed

    private void igualarPreco() {
        Pedido pedido = (Pedido) getValueObject();
        BigDecimal value = new BigDecimal(0);
        BigDecimal valueCliente = new BigDecimal(0);
        BigDecimal valorComissao = new BigDecimal(0);
        BigDecimal divisor = new BigDecimal(100);
        
        List<ItemPedido> itens = (List)pedido.getItens();
        
        for (ItemPedido item : itens) {
            item.setValor(item.getValorCliente());
        }
        
        for (ItemPedido item : itens) {
            value = value.add(item.getTotal());
            if (item.getTotalCliente() != null) {
                valueCliente = valueCliente.add(item.getTotalCliente());
            }
            valorComissao = valorComissao.add(item.getQtd().multiply(item.getValor()).divide(divisor).multiply(item.getPercComissao()));
        }

        pedido.setValor(value);
        pedido.setValorCliente(valueCliente);
        pedido.setValorComissao(valorComissao);
        
        try {
            pedidoDao.updatePedido(pedido);
            object2Field(pedido);
        } catch (DAOException ex) {
            Logger.getLogger(PedidoViewFrame.class.getName()).log(Level.SEVERE, null, ex);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }
    
    private void itemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuItemActionPerformed
        if (app.getUser().isAdmin() || app.getUser().isEscritorio()) {
        
            Pedido pedido = (Pedido) getValueObject();
            if ("A".equals(pedido.getSituacao())) {
                Messages.warningMessage(getBundle().getString("naoEditarPedido"));
                return;
            }

            igualarPreco();

        }
    }//GEN-LAST:event_itemMenuItemActionPerformed

    private void imediatoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imediatoCheckBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_imediatoCheckBoxActionPerformed

    private void loadProperties(EmailBean email) {
        User user = (User) app.getUser();
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
        EmpresaDao empresaDao = (EmpresaDao) app.lookupService("empresaDao");
        Params value = null;
        
        try {
            value = (Params) empresaDao.findById(Params.class, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return value;

    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private BigDecimal calculaComissao() {
        Pedido pedido = (Pedido) getObject();
        BigDecimal valorComissao = BigDecimal.ZERO;
        BigDecimal divisor = new BigDecimal(100);
        for (ItemPedido item : pedido.getItens()) {
            valorComissao = valorComissao.add(item.getQtd().multiply(item.getValor()).divide(divisor).multiply(item.getPercComissao()));
        }
        return valorComissao;

    }

    class ItemPedidoViewModel extends ItemPedidoModel {

        Pedido pedido;

        public ItemPedidoViewModel(List values, Pedido p) {
            super(values);
            pedido = p;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (app.getUser().isAdmin()) {
                return (column == COMISSAO);
            } else {
                return false;
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            ItemPedido itemPedido = (ItemPedido) getObject(row);
            if (column == COMISSAO) {
                PedidoDao dao = (PedidoDao) app.lookupService("pedidoDao");
                itemPedido.setPercComissao(new BigDecimal((String) aValue));
                BigDecimal valorComissao;
                try {
                    valorComissao = calculaComissao();
                    dao.updateComissao(itemPedido, valorComissao);
                    fireTableRowsUpdated(row, row);
                    pedido.setValorComissao(valorComissao);
                    comissaoField.setValue(pedido.getVlrComissaoVendedor());
                    comissaoTotalField.setValue(pedido.getVlrComissaoTotal());
                    comissaoField.invalidate();
                    comissaoTotalField.invalidate();
                } catch (Exception e) {
                    getLogger().error(e.getMessage(), e);
                    Messages.errorMessage(app.getResourceString("saveErrorMessage"));
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton addNotaButton;
    private javax.swing.JButton addPgtoButton;
    private javax.swing.JButton alterarButton;
    private javax.swing.JTable anexosTable;
    private javax.swing.JFormattedTextField atendimentoField;
    private javax.swing.JTextArea atendimentoTextArea;
    private javax.swing.JButton autorizarButton;
    private javax.swing.JButton autorizarCobrancaButton;
    private javax.swing.JFormattedTextField clienteField;
    private javax.swing.JButton cobrancaButton;
    private javax.swing.JFormattedTextField comissaoField;
    private javax.swing.JFormattedTextField comissaoTotalField;
    private javax.swing.JFormattedTextField comissaoVendedorField;
    private javax.swing.JTextArea complementarTextArea;
    private javax.swing.JButton delNotaButton;
    private javax.swing.JButton delPgtoButton;
    private javax.swing.JButton deleteItemButton;
    private javax.swing.JFormattedTextField diasAtrasoField;
    private javax.swing.JButton edtNotaButton;
    private javax.swing.JButton edtPgtoButton;
    private javax.swing.JButton edtPgtoButton1;
    private javax.swing.JButton emailItemButton;
    private javax.swing.JTable embarquesTable;
    private javax.swing.JFormattedTextField emissaoField;
    private javax.swing.JCheckBox emitidoClienteCheckBox;
    private javax.swing.JCheckBox emitidoCobrancaCheckBox;
    private javax.swing.JCheckBox emitidoRepresCheckBox;
    private javax.swing.JFormattedTextField entregaField;
    private javax.swing.JFormattedTextField formaPgtoClienteField;
    private javax.swing.JFormattedTextField formaPgtoField;
    private javax.swing.JFormattedTextField formaVendaField;
    private javax.swing.JFormattedTextField grupoClienteField;
    private javax.swing.JCheckBox imediatoCheckBox;
    private javax.swing.JMenuItem itemMenuItem;
    private javax.swing.JPopupMenu itemPopupMenu;
    private javax.swing.JTable itensTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JFormattedTextField naoAtendidoField;
    private javax.swing.JTable notasTable;
    private javax.swing.JTextArea obsAdicionalTextArea;
    private javax.swing.JTextArea obsClienteTextArea;
    private javax.swing.JTextArea obsTextArea;
    private javax.swing.JFormattedTextField pedidoField;
    private javax.swing.JFormattedTextField pedidoRepresField;
    private javax.swing.JFormattedTextField pendenteField;
    private javax.swing.JFormattedTextField percComissaoField;
    private javax.swing.JFormattedTextField pgtoPendenteField;
    private javax.swing.JPopupMenu pgtoPopupMenu;
    private javax.swing.JFormattedTextField pgtoTransField;
    private javax.swing.JTable pgtosTable;
    private javax.swing.JButton planilhaButton;
    private javax.swing.JMenuItem planilhaMenuItem;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JButton posAtendimentoButton;
    private javax.swing.JCheckBox prePedidoCheckBox;
    private javax.swing.JMenuItem remessaMenuItem;
    private javax.swing.JButton repetirButton;
    private javax.swing.JButton repetirPgtoButton;
    private javax.swing.JFormattedTextField represField;
    private javax.swing.JFormattedTextField responsavelField;
    private javax.swing.JFormattedTextField situacaoField;
    private javax.swing.JFormattedTextField tipoPgtoField;
    private javax.swing.JFormattedTextField transportadorField;
    private javax.swing.JTextArea undSaldoTextArea;
    private javax.swing.JTextArea unidadesTextArea;
    private javax.swing.JFormattedTextField valorClienteField;
    private javax.swing.JFormattedTextField valorField;
    private javax.swing.JFormattedTextField valorOpField;
    private javax.swing.JFormattedTextField vendedorField;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}

class TpPgtoComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        PgtoCliente grupo1 = (PgtoCliente) obj1;
        PgtoCliente grupo2 = (PgtoCliente) obj2;

        return grupo1.getDtVencimento().compareTo(grupo2.getDtVencimento());
    }
}

class ItensComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        ItemPedido grupo1 = (ItemPedido) obj1;
        ItemPedido grupo2 = (ItemPedido) obj2;

        return grupo1.getProduto().getDescricao().compareTo(grupo2.getProduto().getDescricao());
    }
}
