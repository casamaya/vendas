/*
 * PedidoEditPanel.java
 *
 * Created on 30 de Junho de 2007, 11:24
 */
package vendas.swing.app.pedido;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.ListComboBoxModel;
import ritual.swing.NumericPlainDocument;
import ritual.swing.NumericTextField;
import vendas.entity.Cliente;
import vendas.entity.FormaPgto;
import vendas.entity.Pedido;
import vendas.entity.Repres;
import vendas.entity.Transportador;
import vendas.entity.Vendedor;
import vendas.dao.ClienteDao;
import vendas.dao.FormaPgtoDao;
import vendas.dao.RepresDao;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.dao.TransportadorDao;
import vendas.dao.VendedorDao;
import vendas.entity.ItemPedido;
import vendas.swing.core.EditPanel;
import vendas.swing.model.ItemPedidoModel;
import ritual.util.DateVerifier;
import ritual.util.NumberUtils;
import vendas.beans.PedidoBean;
import vendas.beans.ProdutoFilter;
import vendas.dao.FormaVendaDao;
import vendas.dao.PedidoDao;
import vendas.dao.ProdutoDao;
import vendas.dao.TipoPgtoDao;
import vendas.entity.FormaVenda;
import vendas.entity.ItemPedidoPK;
import vendas.entity.PedidoEmbarque;
import vendas.entity.RepresProduto;
import vendas.entity.RepresProdutoPK;
import vendas.entity.TipoPgto;
import vendas.exception.DAOException;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class PedidoEditPanel extends EditPanel {

    DateVerifier dateVerifier = new DateVerifier();
    JComboBox produtoComboBox = new JComboBox();
    boolean editMode = false;
    boolean updateItens = false;
    boolean updateVendedor = false;
    //PedidoBean current;

    /** Creates new form PedidoEditPanel */
    public PedidoEditPanel() {
        super();
        initComponents();
        init();
        DecimalFormat df = new DecimalFormat("#0.00");
        valorOpField.setDocument(new NumericPlainDocument(df));
        comissaoVendedorField.setDocument(new NumericPlainDocument(df));
    }

    @Override
    public void enableControls(boolean isEditMode) {
        pedidoField.setFocusable(!isEditMode);
        emissaoField.setFocusable(!isEditMode);
        if (isEditMode) {
            entregaField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

                @Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    Date dt = null;
                    if (entregaField.getDate() != null) {
                        try {
                            dt = DateUtils.parse(DateUtils.format(entregaField.getDate()));
                        } catch (Exception e) {
                            getLogger().error(e.getMessage(), e);
                        }
                    }
                    if (dt == null) {
                        return;
                    }
                    ItemPedidoModel itp = (ItemPedidoModel) itensTable.getModel();
                    List<ItemPedido> itens = itp.getItemList();
                    for (ItemPedido item : itens) {
                        if ("N".equals(item.getSituacao())) {
                            item.setDtEntrega(dt);
                        }
                    }
                    itp.fireTableDataChanged();

                }
            });
        }
    }

    public void updateProductList(Repres repres) {
        getLogger().info("updateProductList");
        ProdutoDao produtodao = new ProdutoDao();
        ProdutoFilter prodFilter = new ProdutoFilter();
        prodFilter.setRepres(repres);
        if (repres != null) {
            produtoComboBox.setModel(new ListComboBoxModel((List) produtodao.findProdutoRepres(prodFilter)));
            TableColumn col = itensTable.getColumnModel().getColumn(ItemPedidoModel.DESCRICAO);
            DefaultCellEditor editor = new DefaultCellEditor(produtoComboBox);
            col.setCellEditor(editor);
            if (updateItens) {
                MyItemModel model = (MyItemModel) itensTable.getModel();
                model.setItemList(new ArrayList(1));
                model.fireTableDataChanged();
            } else {
                updateItens = true;
            }
            if (obsTextArea.getText().trim().length() == 0) {
                obsTextArea.setText(repres.getObservacao());
            }
            if (obs3TextArea.getText().trim().length() == 0) {
                obs3TextArea.setText(repres.getObservacaoCliente());
            }
        }
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoBean bean = (PedidoBean) obj;
        Pedido pedido = bean.getPedido();

        clienteComboBox.setSelectedItem(pedido.getCliente());
        responsavelComboBox.setSelectedItem(pedido.getClienteResponsavel());
        formaVendaComboBox.setSelectedItem(pedido.getFormaVenda());
        valorOpField.setValue(pedido.getValorOp());
        if (pedido.getComissaoVendedor() != null) {
            comissaoVendedorField.setValue(pedido.getComissaoVendedor());
        }
        emissaoField.setDate(pedido.getDtPedido());
        entregaField.setDate(pedido.getDtEntrega());
        imediatoCheckBox.setSelected(pedido.getEntregaImediata());
        obsTextArea.setText(pedido.getObs());
        obs2TextArea.setText(pedido.getObs2());
        obs3TextArea.setText(pedido.getObs3());
        complementarTextArea.setText(pedido.getComplemento());
        pedidoField.setValue(pedido.getIdPedido());
        pedidoRepresField.setValue(pedido.getIdPedidoRepres());
        pgtoComboBox.setSelectedItem(pedido.getFormaPgto());
        pgtoClienteComboBox.setSelectedItem(pedido.getFormaPgtoCliente());
        tipoPgtoComboBox.setSelectedItem(pedido.getTipoPgto());
        represComboBox.setSelectedItem(pedido.getRepres());
        if (pedido.getVendedor() != null) {
            vendedorComboBox.setSelectedItem(pedido.getVendedor());
        } else {
            if (TApplication.getInstance().getUser().isVendedor()) {
                PedidoDao dao = new PedidoDao();
                try  {
                    vendedorComboBox.setSelectedItem(dao.findById(Vendedor.class, TApplication.getInstance().getUser().getIdvendedor()));
                } catch (DAOException ex) {
                    getLogger().error(ex);
                }
                Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
                comissaoVendedorField.setValue(vendedor.getValorComissao());
            }
        }
        
        if (TApplication.getInstance().getUser().isVendedor()) {
            vendedorComboBox.setEnabled(false);
            comissaoVendedorField.setEnabled(false);
        }
        
        List<ItemPedido> itens = new ArrayList<>();
        
        for (ItemPedido itemPedido : pedido.getItens()) {
            ItemPedido novo = new ItemPedido();
            try {
                novo.setDtEntrega(itemPedido.getDtEntrega());
                novo.setIpi(itemPedido.getIpi());
                ItemPedidoPK pk = new ItemPedidoPK();
                pk.setIdPedido(itemPedido.getItemPedidoPK().getIdPedido());
                pk.setIdProduto(itemPedido.getItemPedidoPK().getIdProduto());
                novo.setItemPedidoPK(pk);
                if (itemPedido.getValorCliente() == null) {
                    novo.setValorCliente(itemPedido.getValor());
                } else {
                    novo.setValorCliente(itemPedido.getValorCliente());
                }
                novo.setPedido(itemPedido.getPedido());
                novo.setProduto(itemPedido.getProduto());
                novo.setQtd(itemPedido.getQtd());
                novo.setSituacao(itemPedido.getSituacao());
                novo.setValor(itemPedido.getValor());
                novo.setEmbalagem(itemPedido.getEmbalagem());
                novo.setPercComissao(itemPedido.getPercComissao());
                itens.add(novo);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        unidadesField.setValue(pedido.getVlrUnidades());

        totalField.setValue(pedido.getValor());
        totalClienteField.setValue(pedido.getValorCliente());
        MyItemModel itemModel = new MyItemModel(itens);
        itemModel.setReadOnly(false);
        itensTable.setModel(itemModel);
        updateProductList(pedido.getRepres());
        setColumnsEditor();
        clienteComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente rep = (Cliente) clienteComboBox.getSelectedItem();
                if (vendedorComboBox.isEnabled()) {
                if (!rep.getVendedores().isEmpty())
                    vendedorComboBox.setSelectedItem(rep.getVendedores().get(0));
                }
            }
        });
        represComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Repres rep = (Repres) represComboBox.getSelectedItem();
                obsTextArea.setText(rep.getObservacao());
                obs3TextArea.setText(rep.getObservacaoCliente());
                entregaField.setDate(DateUtils.getNextDate(new Date(), rep.getDiasAtendimento()));
                updateProductList(rep);
                updateTransportador(rep);
                
                Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
                if (vendedor == null)
                    return;
                if (represComboBox.getSelectedIndex() == -1) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                    return;
                }
//                if (updateVendedor) {
                VendedorDao dao = new VendedorDao();
                BigDecimal valComissao = dao.findComissao(vendedor, (Repres) represComboBox.getSelectedItem());
                if (valComissao == null) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                } else {
                    comissaoVendedorField.setValue(valComissao);
                }
                

            }
        });
        pgtoClienteComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FormaPgto rep = (FormaPgto) pgtoClienteComboBox.getSelectedItem();
                pgtoComboBox.setSelectedItem(rep);
            }
        });
        vendedorComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
                if (vendedor == null)
                    return;
                if (represComboBox.getSelectedIndex() == -1) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                    return;
                }
//                if (updateVendedor) {
                VendedorDao dao = new VendedorDao();
                BigDecimal valComissao = dao.findComissao(vendedor, (Repres) represComboBox.getSelectedItem());
                if (valComissao == null) {
                    comissaoVendedorField.setValue(vendedor.getValorComissao());
                } else {
                    comissaoVendedorField.setValue(valComissao);
                }
//                } else {
//                    updateVendedor = true;
//                }
            }
        });
        transportadorComboBox.setSelectedItem(pedido.getTransportador());
        pgtoTranspComboBox.setSelectedItem(pedido.getFormaPgtoTransp());

    }

    private void updateTransportador(Repres rep) {
        TransportadorDao transportadorDao = (TransportadorDao) TApplication.getInstance().lookupService("transportadorDao");
        List<Transportador> transpList = rep.getTransportadores();
        if ((transpList == null) || (transpList.isEmpty())) {
            transpList = transportadorDao.findAll();
        }

        transportadorComboBox.setModel(new ListComboBoxModel(transpList));
        Transportador t = transpList.get(0);
        pgtoTranspComboBox.setSelectedItem(t.getFormaPgto());
        transportadorComboBox.setSelectedItem(transpList.get(0)); 
        transportadorComboBox.invalidate();
        pgtoTranspComboBox.invalidate();
    }
    
    private void setColumnsEditor() {
        TableColumn col;
        FocusListener focus = new FocusListener() {
            @Override
           public void focusGained(FocusEvent e) {
               
               String t = ((NumericTextField) e.getSource()).getText();
               if (t != null && t.length() > 0) {
                   
                    ((NumericTextField) e.getSource()).setText(t.substring(t.length() - 1));
               }
           } 
            @Override
           public void focusLost(FocusEvent e) {
               
           }
        };

        itensTable.getColumnModel().getColumn(ItemPedidoModel.DESCRICAO).setPreferredWidth(280);

        itensTable.getColumnModel().getColumn(ItemPedidoModel.CODIGO).setPreferredWidth(70);

        itensTable.getColumnModel().getColumn(ItemPedidoModel.UNIDADE).setPreferredWidth(36);

        itensTable.getColumnModel().getColumn(ItemPedidoModel.SITUACAO).setPreferredWidth(20);
        DecimalFormat df = NumberUtils.getDecimalFormat();
        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        
        NumericTextField vlField = new NumericTextField(8, df);
        vlField.setInputVerifier(new DateVerifier(true));
        vlField.addFocusListener(focus);

        itensTable.getColumnModel().getColumn(ItemPedidoModel.QUANT).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.QUANT).setCellEditor(new DefaultCellEditor(vlField));
        itensTable.getColumnModel().getColumn(ItemPedidoModel.QUANT).setCellRenderer(new FractionCellRenderer(8, 3, SwingConstants.RIGHT));

        DecimalFormat dfp = NumberUtils.getDecimalFormat();
        dfp.setMinimumFractionDigits(2);
        dfp.setMaximumFractionDigits(2);
        NumericTextField precoField = new NumericTextField(8, dfp);
        precoField.setInputVerifier(new DateVerifier(true));
        precoField.addFocusListener(focus);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.VALOR).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.VALOR).setCellEditor(new DefaultCellEditor(precoField));

        DecimalFormat dfe = NumberUtils.getDecimalFormat();
        dfe.setMinimumFractionDigits(4);
        dfe.setMaximumFractionDigits(4);
        NumericTextField embalagemField = new NumericTextField(8, dfe);
        embalagemField.setInputVerifier(new DateVerifier(true));
        embalagemField.addFocusListener(focus);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.EMBALAGEM).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.EMBALAGEM).setCellEditor(new DefaultCellEditor(embalagemField));

        NumericTextField precoClienteField = new NumericTextField(8, dfp);
        precoClienteField.setInputVerifier(new DateVerifier(true));
        precoClienteField.addFocusListener(focus);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.VALORCLIENTE).setPreferredWidth(80);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.VALORCLIENTE).setCellEditor(new DefaultCellEditor(precoClienteField));

        NumericTextField percComissaoField = new NumericTextField(8, df);
        percComissaoField.setInputVerifier(new DateVerifier(true));
        itensTable.getColumnModel().getColumn(ItemPedidoModel.COMISSAO).setPreferredWidth(45);
        itensTable.getColumnModel().getColumn(ItemPedidoModel.COMISSAO).setCellEditor(new DefaultCellEditor(percComissaoField));

        df = NumberUtils.getIntegerFormat();
        NumericTextField codigo = new NumericTextField(8, df);
        codigo.addFocusListener(focus);
        codigo.setInputVerifier(new DateVerifier(true));
        itensTable.getColumnModel().getColumn(ItemPedidoModel.CODIGO).setCellEditor(new DefaultCellEditor(codigo));

    }

    @Override
    public void field2Object(Object obj) {
        PedidoBean bean = (PedidoBean) obj;
        Pedido pedido = bean.getPedido();

        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        Cliente cli = (Cliente) responsavelComboBox.getModel().getSelectedItem();
        if ((cli != null) && " -- Selecione -- ".equals(cli.getRazao()))
            pedido.setClienteResponsavel(null);
        else
            pedido.setClienteResponsavel(cli);
        
        pedido.setSubstituicaoTributaria(cli.isSubstituicaoTributaria() ? "1" : "0");
        pedido.setFormaVenda((FormaVenda) formaVendaComboBox.getModel().getSelectedItem());
        pedido.setValorOp(NumberUtils.getBigDecimal(valorOpField.getValue()));

        pedido.setComissaoVendedor(NumberUtils.getBigDecimal(comissaoVendedorField.getValue()));
        pedido.setDtEmbarqueAnterior(pedido.getDtEntrega());
        pedido.setEntregaImediata(imediatoCheckBox.isSelected());

        try {
            pedido.setDtPedido(DateUtils.parse(DateUtils.format(emissaoField.getDate())));
            if ((entregaField.getDate() != null)) {
                pedido.setDtEntrega(DateUtils.parse(DateUtils.format(entregaField.getDate())));
            }
        } catch (Exception e) {
        }
        
        pedido.setFormaPgto((FormaPgto) pgtoComboBox.getModel().getSelectedItem());
        pedido.setFormaPgtoCliente((FormaPgto) pgtoClienteComboBox.getModel().getSelectedItem());
        pedido.setTipoPgto((TipoPgto) tipoPgtoComboBox.getModel().getSelectedItem());
        pedido.setFormaPgtoTransp((FormaPgto) pgtoTranspComboBox.getModel().getSelectedItem());
        if (pedidoField.getText() != null && pedidoField.getText().length() > 0) {
            pedido.setIdPedido(Integer.decode(pedidoField.getText()));
        }
        pedido.setObs(obsTextArea.getText());
        pedido.setObs2(obs2TextArea.getText());
        pedido.setObs3(obs3TextArea.getText());
        pedido.setComplemento(complementarTextArea.getText());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setComissao(pedido.getRepres().getComissao());
        pedido.setTransportador((Transportador) transportadorComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        List<ItemPedido> itens = ((MyItemModel) itensTable.getModel()).getItemList();
        BigDecimal value = new BigDecimal(0);
        BigDecimal valueCliente = new BigDecimal(0);
        BigDecimal valorComissao = new BigDecimal(0);
        BigDecimal divisor = new BigDecimal(100);
        
        if (pedidoRepresField.getText() != null && pedidoRepresField.getText().length() > 0) {
            pedido.setIdPedidoRepres(Integer.decode(pedidoRepresField.getText()));
        }
        
        if (pedido.getIdPedido() == null) {
            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
            Integer id = dao.getNextValue();
            pedido.setIdPedido(id);
        } else if ((pedido.getDtEntrega() != null) && (pedido.getDtEmbarqueAnterior() != null)  && !pedido.isPrePedido()) {
            if (pedido.getDtEmbarqueAnterior().compareTo(pedido.getDtEntrega()) != 0) {
                List<PedidoEmbarque> embarques = pedido.getPedidoEmbarqueList();
                if (embarques == null) {
                    pedido.setPedidoEmbarqueList(new ArrayList<PedidoEmbarque>());
                }
                PedidoEmbarque pe = new PedidoEmbarque();
                pe.setPedido(pedido);
                pe.setDtEmbarque(pedido.getDtEmbarqueAnterior());
                pedido.getPedidoEmbarqueList().add(pe);            
            }
        }
        
        for (ItemPedido item : itens) {
            item.setPedido(pedido);
            if (pedido.getIdPedido() != null)
                item.getItemPedidoPK().setIdPedido(pedido.getIdPedido());
            value = value.add(item.getTotal());
            if (item.getTotalCliente() != null) {
                valueCliente = valueCliente.add(item.getTotalCliente());
            }
            valorComissao = valorComissao.add(item.getQtd().multiply(item.getValor()).divide(divisor).multiply(item.getPercComissao()));
        }
        pedido.setItens(null);
        pedido.setItens(itens);
        pedido.setValor(value);
        pedido.setValorCliente(valueCliente);
        pedido.setValorComissao(valorComissao);
    }

    @Override
    public boolean entryValidate() {
        List<ItemPedido> itens = ((MyItemModel) itensTable.getModel()).getItemList();
        if ((itens == null) || itens.isEmpty())
            return false;
        BigDecimal value = new BigDecimal(0);
        for (ItemPedido item : itens) {
            value = value.add(item.getTotal());
        }
        return value.doubleValue() != 0;
    }

    @Override
    public void init() {
        getLogger().info("init");
        saldoClienteField.setForeground(Color.red);
        limiteCreditoField.setForeground(Color.red);
        pgtoPendenteField.setForeground(Color.red);
        diasAtrasoField.setForeground(Color.red);
        ClienteDao clienteDao;
        FormaVendaDao formaVendaDao;
        FormaPgtoDao formaPgtoDao;
        TipoPgtoDao tipoPgtoDao;
        RepresDao represDao;
        TransportadorDao transportadorDao;
        VendedorDao vendedorDao;
        clienteDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        formaVendaDao = (FormaVendaDao) TApplication.getInstance().lookupService("formaVendaDao");
        formaPgtoDao = (FormaPgtoDao) TApplication.getInstance().lookupService("formaPgtoDao");
        tipoPgtoDao = (TipoPgtoDao) TApplication.getInstance().lookupService("tipoPgtoDao");
        represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        vendedorDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        transportadorDao = (TransportadorDao) TApplication.getInstance().lookupService("transportadorDao");

        itensTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        itensTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        itensTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        itensTable.setRowHeight(Constants.ROWHEIGHT);
////////////////
//        itensTable.setRowSelectionAllowed(false);
        itensTable.setSurrendersFocusOnKeystroke(true);

        ///////////
        try {
            clienteComboBox.setModel(new ListComboBoxModel(clienteDao.findAllAtivos()));
            responsavelComboBox.setModel(VendasUtil.getClienteListModel());
            formaVendaComboBox.setModel(new ListComboBoxModel(formaVendaDao.findAll()));
            represComboBox.setModel(new ListComboBoxModel(represDao.findAll()));
            pgtoComboBox.setModel(new ListComboBoxModel(formaPgtoDao.findAll()));
            pgtoClienteComboBox.setModel(new ListComboBoxModel(formaPgtoDao.findAll()));
            tipoPgtoComboBox.setModel(new ListComboBoxModel(tipoPgtoDao.findAll()));
            pgtoTranspComboBox.setModel(new ListComboBoxModel(formaPgtoDao.findAll()));
            vendedorComboBox.setModel(new ListComboBoxModel(vendedorDao.findAllAtivos()));
            transportadorComboBox.setModel(new ListComboBoxModel(transportadorDao.findAll()));
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
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
        emissaoField = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        pgtoClienteComboBox = new javax.swing.JComboBox();
        pedidoRepresField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        tipoPgtoComboBox = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        formaVendaComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        saldoClienteField = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        limiteCreditoField = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        responsavelComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        pgtoComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        valorOpField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        entregaField = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        comissaoVendedorField = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        transportadorComboBox = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        pgtoTranspComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itensTable = new javax.swing.JTable();
        newItemButton = new javax.swing.JButton();
        deleteItemButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        unidadesField = new javax.swing.JFormattedTextField();
        totalField = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        totalClienteField = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsTextArea = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obs2TextArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        obs3TextArea = new javax.swing.JTextArea();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        complementarTextArea = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        pgtoPendenteField = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        nfCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        diasAtrasoField = new javax.swing.JFormattedTextField();
        imediatoCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("pedido")); // NOI18N

        pedidoField.setEditable(false);
        pedidoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        pedidoField.setInputVerifier(dateVerifier);
        pedidoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pedidoFieldFocusLost(evt);
            }
        });

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PedidoEditPanel.class);
        emissaoField.setDateFormatString(resourceMap.getString("emissaoField.dateFormatString")); // NOI18N

        jLabel19.setText(bundle.getString("formaPgtoCliente")); // NOI18N

        pgtoClienteComboBox.setInputVerifier(dateVerifier);

        pedidoRepresField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        pedidoRepresField.setInputVerifier(dateVerifier);

        jLabel18.setText(bundle.getString("tipoPgto")); // NOI18N

        tipoPgtoComboBox.setInputVerifier(dateVerifier);

        jLabel23.setText(bundle.getString("formaVenda")); // NOI18N

        jLabel3.setText(bundle.getString("cliente")); // NOI18N

        clienteComboBox.setInputVerifier(dateVerifier);
        clienteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clienteComboBoxActionPerformed(evt);
            }
        });

        jLabel6.setText(bundle.getString("pedidosNaoAtendidos")); // NOI18N

        saldoClienteField.setEditable(false);
        saldoClienteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        saldoClienteField.setInputVerifier(dateVerifier);

        jLabel9.setText(bundle.getString("lc")); // NOI18N

        limiteCreditoField.setEditable(false);
        limiteCreditoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        limiteCreditoField.setInputVerifier(dateVerifier);

        jLabel24.setText(bundle.getString("clienteResponsavel")); // NOI18N

        responsavelComboBox.setInputVerifier(dateVerifier);

        jLabel7.setText(bundle.getString("formaPgtoRepres")); // NOI18N

        pgtoComboBox.setInputVerifier(dateVerifier);

        jLabel5.setText(bundle.getString("representada")); // NOI18N

        represComboBox.setInputVerifier(dateVerifier);

        jLabel12.setText(bundle.getString("valorOp")); // NOI18N

        valorOpField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel10.setText(bundle.getString("embarque")); // NOI18N

        entregaField.setDateFormatString(resourceMap.getString("entregaField.dateFormatString")); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        vendedorComboBox.setInputVerifier(dateVerifier);

        comissaoVendedorField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel11.setText(bundle.getString("transportador")); // NOI18N

        transportadorComboBox.setInputVerifier(dateVerifier);
        transportadorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportadorComboBoxActionPerformed(evt);
            }
        });

        jLabel15.setText(bundle.getString("formaPgtoTransp")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Itens"));

        itensTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(itensTable);

        newItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/new.png"))); // NOI18N
        newItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemButtonActionPerformed(evt);
            }
        });

        deleteItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vendas/resources/cut.png"))); // NOI18N
        deleteItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemButtonActionPerformed(evt);
            }
        });

        jLabel16.setText(bundle.getString("unidades")); // NOI18N

        jLabel17.setText(bundle.getString("total")); // NOI18N

        unidadesField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        unidadesField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unidadesField.setInputVerifier(dateVerifier);

        totalField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalField.setInputVerifier(dateVerifier);

        jLabel20.setText(bundle.getString("totalCliente")); // NOI18N

        totalClienteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalClienteField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalClienteField.setInputVerifier(dateVerifier);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteItemButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(newItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(unidadesField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(unidadesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(totalClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(newItemButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteItemButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel13.setText(bundle.getString("observacaoFornecedor")); // NOI18N

        obsTextArea.setLineWrap(true);
        obsTextArea.setRows(5);
        jScrollPane2.setViewportView(obsTextArea);

        jLabel14.setText(bundle.getString("observacao2")); // NOI18N

        obs2TextArea.setLineWrap(true);
        obs2TextArea.setRows(5);
        jScrollPane3.setViewportView(obs2TextArea);

        obs3TextArea.setLineWrap(true);
        obs3TextArea.setRows(5);
        jScrollPane4.setViewportView(obs3TextArea);

        jLabel21.setText(bundle.getString("obsCliente")); // NOI18N

        complementarTextArea.setLineWrap(true);
        complementarTextArea.setRows(5);
        jScrollPane5.setViewportView(complementarTextArea);

        jLabel22.setText(bundle.getString("complementar")); // NOI18N

        pgtoPendenteField.setEditable(false);
        pgtoPendenteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        pgtoPendenteField.setInputVerifier(dateVerifier);

        jLabel25.setText(bundle.getString("valoresAVencer")); // NOI18N

        nfCheckBox.setText(bundle.getString("nf")); // NOI18N
        nfCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nfCheckBoxActionPerformed(evt);
            }
        });

        jLabel4.setText(bundle.getString("codigoRepres")); // NOI18N

        jLabel26.setText(bundle.getString("diasAtraso")); // NOI18N

        diasAtrasoField.setEditable(false);
        diasAtrasoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        diasAtrasoField.setInputVerifier(dateVerifier);

        imediatoCheckBox.setText(bundle.getString("imediato")); // NOI18N
        imediatoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imediatoCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel24)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(responsavelComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 359, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nfCheckBox)
                                        .addGap(37, 37, 37)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(valorOpField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(68, 68, 68)
                                                .addComponent(imediatoCheckBox))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(40, 40, 40)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(saldoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel25)
                                            .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pgtoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(limiteCreditoField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel9))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel26)))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(47, 47, 47))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pgtoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tipoPgtoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(pedidoRepresField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(formaVendaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(202, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(269, 269, 269))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(vendedorComboBox, 0, 356, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(transportadorComboBox, 0, 351, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(107, 107, 107))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pgtoTranspComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel21))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addComponent(jLabel19)
                        .addComponent(jLabel18))
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(emissaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pgtoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tipoPgtoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(formaVendaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(responsavelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(diasAtrasoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(saldoClienteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(limiteCreditoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel7)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pedidoRepresField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pgtoPendenteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pgtoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(valorOpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nfCheckBox))
                            .addComponent(entregaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imediatoCheckBox))))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comissaoVendedorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportadorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pgtoTranspComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane5, 0, 0, Short.MAX_VALUE))))
                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void newItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemButtonActionPerformed
        Repres rep = (Repres) represComboBox.getSelectedItem();
        if (rep == null) {
            return;
        }

        MyItemModel itp = (MyItemModel) itensTable.getModel();
        /*if (itp.getItemList().size() == 23) {
            Messages.errorMessage("Número de itens já está completo.");
            return;
        }*/

        ItemPedido itemPedido = new ItemPedido();
        //itemPedido.setPedido(current);
        if (entregaField.getDate() != null) {
            try {
                itemPedido.setDtEntrega(DateUtils.parse(DateUtils.format(entregaField.getDate())));
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }

        //itemPedido.setPercComissao((BigDecimal)comissaoField.getValue());
        //itemPedido.getItemPedidoPK().setIdPedido(Integer.decode(pedidoField.getText()));
        editMode = true;
        itp.addObject(itemPedido);
        //itp.fireTableDataChanged();
        int n = itp.getRowCount() - 1;
        itensTable.setRowSelectionInterval(n, n);
        itensTable.scrollRectToVisible(itensTable.getCellRect(n, 0, true));
    }//GEN-LAST:event_newItemButtonActionPerformed

    private class MyItemModel extends ItemPedidoModel {

        public MyItemModel() throws Exception {
            super();
        }

        public MyItemModel(List values) {
            super(values);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            RepresProduto rp = null;
            ItemPedido itemPedido = (ItemPedido) getObject(row);
            ProdutoDao produtodao = new ProdutoDao();
            
            if (column == CODIGO)
                super.setValueAt(Integer.decode((String) aValue), row, column);
            else
                super.setValueAt(aValue, row, column);
            
            switch (column) {
                case CODIGO:
                    try {
                        RepresProdutoPK pk = new RepresProdutoPK();
                        Repres r = (Repres) represComboBox.getModel().getSelectedItem();
                        pk.setIdProduto(Integer.decode((String) aValue));
                        pk.setIdRepres(r.getIdRepres());
                        rp = (RepresProduto) produtodao.findById(RepresProduto.class, pk);
                    } catch (Exception e) {
                        getLogger().error(e);
                    }
                    if (rp == null) {
                        Messages.errorMessage("Produto n\u00E3o localizado");
                    } else {
                        itemPedido.setProduto(rp.getProduto());
                        itemPedido.getItemPedidoPK().setIdProduto(rp.getProduto().getIdProduto());
                        itemPedido.setValor(rp.getPreco());
                        BigDecimal valor = rp.getPrecoFinal();
                        if (valor == null || valor.intValue() == 0) {
                            valor = rp.getPreco();
                        }
                        itemPedido.setValorCliente(valor);
                        itemPedido.setPercComissao(rp.getPercComissao());
                        itemPedido.setIpi(rp.getIpi());
                        if (rp.getEmbalagem() == null)
                        itemPedido.setQtd(BigDecimal.ZERO);
                        else
                        itemPedido.setQtd(rp.getEmbalagem());
                    }
                    break;
                case EMBALAGEM:
                    try {
                        RepresProdutoPK pk = new RepresProdutoPK();
                        Repres r = (Repres) represComboBox.getModel().getSelectedItem();
                        pk.setIdProduto(itemPedido.getItemPedidoPK().getIdProduto());
                        pk.setIdRepres(r.getIdRepres());
                        rp = (RepresProduto) produtodao.findById(RepresProduto.class, pk);
                    } catch (Exception e) {
                        getLogger().error(e);
                    }
                    if (rp == null) {
                        Messages.errorMessage("Produto n\u00E3o localizado");
                    } else {
                        if (rp.getEmbalagem() != null) {
                            itemPedido.setQtd(rp.getEmbalagem().multiply(new BigDecimal((String)aValue)));
                        }
                    }
                    break;
                case QUANT:
                    break;
                case VALOR:
                    break;
                case VALORCLIENTE:
                    break;
                case COMISSAO:
                    break;
                default:
                    return;
            }
            List<ItemPedido> o = getItemList();
            BigDecimal vlrUnidades = new BigDecimal(0);
            BigDecimal vlrCliente = new BigDecimal(0);
            BigDecimal vlr = new BigDecimal(0);
            for (ItemPedido item : o) {
                vlrUnidades = vlrUnidades.add(item.getQtd().multiply(item.getProduto().getFatorConversao()));
                vlr = vlr.add(item.getQtd().multiply(item.getValor()));
                vlrCliente = vlrCliente.add(item.getQtd().multiply(item.getValorCliente()));
            }
            
            if (vlrCliente.compareTo((BigDecimal)limiteCreditoField.getValue()) > 0) {
                Messages.errorMessage("Valor do pedido ultrapassa o limite de crédito.");
            }
            
            unidadesField.setValue(vlrUnidades);
            totalField.setValue(vlr);
            totalClienteField.setValue(vlrCliente);
         
        }
    }

    private void deleteItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemButtonActionPerformed
        int n = itensTable.getSelectedRow();
        if (n >= 0) {
            ItemPedidoModel itp = (ItemPedidoModel) itensTable.getModel();
            //ItemPedido item = (ItemPedido) itp.getObject(n);
            //itp.getItemList().remove(item);
            //itp.fireTableDataChanged();
            ItemPedido item = (ItemPedido) itp.getObject(n);
            itp.removeObject(n);

            //current.getItens().remove(item);
            int i = itp.getRowCount();
            if (i > 0) {
                if (n >= i) {
                    n--;
                }
                itensTable.setRowSelectionInterval(n, n);
                itensTable.scrollRectToVisible(itensTable.getCellRect(n, 0, true));
            }
        }
    }//GEN-LAST:event_deleteItemButtonActionPerformed

    private void pedidoFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pedidoFieldFocusLost
        // TODO add your handling code here:
        String s = pedidoField.getText();
        if ((s == null) || (s.length() == 0)) {
            return;
        }
        PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        try {
            if (dao.findById(Pedido.class, Integer.decode(s)) != null) {
                Messages.infoMessage("Pedido já existe.");
                pedidoField.setValue(null);
                pedidoField.requestFocus();
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.infoMessage("Falha ao localizar.");
            pedidoField.setValue(null);
            pedidoField.requestFocus();
        }
    }//GEN-LAST:event_pedidoFieldFocusLost

    private void transportadorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportadorComboBoxActionPerformed
        // TODO add your handling code here:
        Transportador transp = (Transportador) transportadorComboBox.getModel().getSelectedItem();
        if (transp == null) {
            return;
        }
        FormaPgto formaPgto = transp.getFormaPgto();
        pgtoTranspComboBox.setSelectedItem(formaPgto);
        pgtoTranspComboBox.invalidate();

    }//GEN-LAST:event_transportadorComboBoxActionPerformed

    private void clienteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clienteComboBoxActionPerformed
        // TODO add your handling code here:
        Cliente cli = (Cliente) clienteComboBox.getModel().getSelectedItem();
        if (cli == null) {
            return;
        }

        PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
                BigDecimal pgtosPendentes = dao.getPgtosClientePendende(cli.getIdCliente());
        if (pgtosPendentes == null)
            pgtosPendentes = BigDecimal.ZERO;
        BigDecimal vendasPendentes = dao.getVendasClientePendende(cli.getIdCliente());
        if (vendasPendentes == null)
            vendasPendentes = BigDecimal.ZERO;
        BigDecimal saldo = pgtosPendentes.add(vendasPendentes);
        saldoClienteField.setValue(vendasPendentes);
        pgtoPendenteField.setValue(pgtosPendentes);
        limiteCreditoField.setValue(cli.getLimiteCredito());
        diasAtrasoField.setValue(cli.getNumDiasAtraso());
        saldoClienteField.invalidate();
        limiteCreditoField.invalidate();
        diasAtrasoField.invalidate();
        pgtoPendenteField.invalidate();

        FormaPgto fp = (FormaPgto) pgtoClienteComboBox.getModel().getSelectedItem();
        if ((fp != null) && !("ANTECIPADO").equals(fp.getNome())) {
        if (cli.getLimiteCredito() != null)
            if (saldo.compareTo(cli.getLimiteCredito()) >= 0) {
                Messages.errorMessage("O cliente n\u00E3o possui saldo disponível e n\u00E3o poderá ter pedido emitido. Selecione outro cliente.");
                clienteComboBox.setSelectedItem(null);
                return;
            }
        }
        String obsText = "Cliente n\u00E3o sujeito a substitui\u00E7\u00E3o tributária. ";
        String obs = obs2TextArea.getText();
        
        if (obs.contains(obsText)) {
            obs = obs.replace(obsText, "");
        }
        
        if (cli.isSubstituicaoTributaria()) {
            obs = obsText + obs;
        }
        
        obs2TextArea.setText(obs);
        obs2TextArea.invalidate();
        
        /*
        String obs = obs2TextArea.getText();
        if (cli.getIdTipo() != null) {
            if (cli.getIdTipo() == 1) {
                if (obs.contains("Indústria"))
                    obs = obs.replace("Indústria", "Atacadista");
                else
                    obs = "Atacadista. " + obs;
            } else {
                if (obs.contains("Atacadista"))
                    obs = obs.replace("Atacadista", "Indústria");
                else
                    obs = "Indústria. " + obs;            
            }
            obs2TextArea.setText(obs);
            obs2TextArea.invalidate();
        }
        */
        responsavelComboBox.setSelectedItem(cli);

    }//GEN-LAST:event_clienteComboBoxActionPerformed

    private void nfCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nfCheckBoxActionPerformed
        if (nfCheckBox.isSelected()) {
            obs2TextArea.setText("NF + CONHECIMENTO DE FRETE = VALOR TOTAL DO PEDIDO");
        }
    }//GEN-LAST:event_nfCheckBoxActionPerformed

    private void imediatoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imediatoCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_imediatoCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JFormattedTextField comissaoVendedorField;
    private javax.swing.JTextArea complementarTextArea;
    private javax.swing.JButton deleteItemButton;
    private javax.swing.JFormattedTextField diasAtrasoField;
    private com.toedter.calendar.JDateChooser emissaoField;
    private com.toedter.calendar.JDateChooser entregaField;
    private javax.swing.JComboBox formaVendaComboBox;
    private javax.swing.JCheckBox imediatoCheckBox;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JFormattedTextField limiteCreditoField;
    private javax.swing.JButton newItemButton;
    private javax.swing.JCheckBox nfCheckBox;
    private javax.swing.JTextArea obs2TextArea;
    private javax.swing.JTextArea obs3TextArea;
    private javax.swing.JTextArea obsTextArea;
    private javax.swing.JFormattedTextField pedidoField;
    private javax.swing.JFormattedTextField pedidoRepresField;
    private javax.swing.JComboBox pgtoClienteComboBox;
    private javax.swing.JComboBox pgtoComboBox;
    private javax.swing.JFormattedTextField pgtoPendenteField;
    private javax.swing.JComboBox pgtoTranspComboBox;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox responsavelComboBox;
    private javax.swing.JFormattedTextField saldoClienteField;
    private javax.swing.JComboBox tipoPgtoComboBox;
    private javax.swing.JFormattedTextField totalClienteField;
    private javax.swing.JFormattedTextField totalField;
    private javax.swing.JComboBox transportadorComboBox;
    private javax.swing.JFormattedTextField unidadesField;
    private javax.swing.JFormattedTextField valorOpField;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
