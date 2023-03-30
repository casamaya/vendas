/*
 * PedidoFilterPanel.java
 *
 * Created on 15 de Julho de 2007, 12:36
 */
package vendas.swing.app.pedido;

import java.util.Locale;
import vendas.beans.PedidoFilter;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.GrupoProduto;
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import ritual.util.NumberUtils;
import vendas.dao.VendedorDao;
import vendas.entity.FormaVenda;
import vendas.entity.SegMercado;
import vendas.entity.SubGrupoProduto;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class PedidoFilterPanel extends EditPanel {

    /** Creates new form PedidoFilterPanel */
    public PedidoFilterPanel() {
        initComponents();
        dtPedido1Field.setLocale(new Locale("pt_BR"));
        dtPedido1Field.setDateFormatString("dd/MM/yyyy");
        dtPedido2Field.setLocale(new Locale("pt_BR"));
        dtPedido2Field.setDateFormatString("dd/MM/yyyy");
    }

    @Override
    public void init() {
        try {
            clienteComboBox.setModel(VendasUtil.getClienteListModel());
            responsavelComboBox.setModel(VendasUtil.getClienteListModel());
            represComboBox.setModel(VendasUtil.getRepresListModel(true));
            grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
            segmentoComboBox.setModel(VendasUtil.getSegMercadoListModel());
            vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
            grupoComboBox.setModel(VendasUtil.getGrupoProdutoListModel());
            formaVendaComboBox.setModel(VendasUtil.getFormaVendaListModel());
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
        TApplication app = TApplication.getInstance();
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedorComboBox.setSelectedItem(dao.findById(Vendedor.class, app.getUser().getIdvendedor()));
                vendedorComboBox.setEnabled(false);
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
    }

    @Override
    public void object2Field(Object obj) {
        PedidoFilter pedido = (PedidoFilter) obj;
        atendidosRadioButton.setSelected("A".equals(pedido.getSituacao()));
        atendimentoRadioButton.setSelected("T".equals(pedido.getAtendimento()));
        todosEmitidosRadioButton.setSelected(true);
        cidadeField.setText(pedido.getCidade());
        bairroField.setText(pedido.getBairro());
        ufField.setText(pedido.getUf());
        dtEntrega1Field.setDate(pedido.getDtEntregaIni());
        dtEntrega2Field.setDate(pedido.getDtEntregaEnd());
        dtNota1Field.setDate(pedido.getDtNotaIni());
        dtNota2Field.setDate(pedido.getDtNotaEnd());
        dtPedido1Field.setDate(pedido.getDtEmissaoIni());
        dtPedido2Field.setDate(pedido.getDtEmissaoEnd());
        naoAtendRadioButton.setSelected(new Boolean("N".equals(pedido.getSituacao())));
        notaField.setText(pedido.getNota());
        parcialRadioButton.setSelected(new Boolean("P".equals(pedido.getAtendimento())));
        pedidoField.setValue(pedido.getPedido());
        produtoField.setValue(pedido.getProduto().getIdProduto());
        somenteOPCheckBox.setSelected(pedido.isOp());
        todosRadioButton.setSelected(new Boolean("T".equals(pedido.getSituacao())));
        totalRadioButton.setSelected(new Boolean("A".equals(pedido.getAtendimento())));
        clienteComboBox.setSelectedItem(pedido.getCliente());
        responsavelComboBox.setSelectedItem(pedido.getResponsavel());
        grupoComboBox.setSelectedItem(pedido.getGrupoProduto());
        grupoClienteComboBox.setSelectedItem(pedido.getGrupo());
        segmentoComboBox.setSelectedItem(pedido.getSegmento());
        represComboBox.setSelectedItem(pedido.getRepres());
        vendedorComboBox.setSelectedItem(pedido.getVendedor());
        prePedidoCheckBox.setSelected(pedido.isPrePedido());
        preCobrancaCheckBox.setSelected(pedido.isPreCobranca());
        precoClienteCheckBox.setSelected(pedido.getPrecoCliente());
        pedidosRepresCheckBox.setSelected(pedido.getFiltrarPedidosFornecedor());
        switch (pedido.getOrdem()) {
            case 0:
                represRadioButton.setSelected(true);
                break;
            case 1:
                clienteRadioButton.setSelected(true);
                break;
            case 2:
                entregaRadioButton.setSelected(true);
                break;
            case 3:
                pedidoRadioButton.setSelected(true);
                break;
            case 4:
                emissaoRadioButton.setSelected(true);
                break;
            case 5:
                pedidoRepresRadioButton.setSelected(true);
                break;
        }
    }

    @Override
    public void field2Object(Object obj) {
        PedidoFilter pedido = (PedidoFilter) obj;
        StringBuilder title = new StringBuilder();
        
        if (todosRadioButton.isSelected()) {
            pedido.setSituacao("T");
        }
        if (atendidosRadioButton.isSelected()) {
            pedido.setSituacao("A");
        }
        if (naoAtendRadioButton.isSelected()) {
            pedido.setSituacao("N");
        }
        if (atrasoRadioButton.isSelected()) {
            pedido.setSituacao("E");
        }
        if (atendimentoRadioButton.isSelected()) {
            pedido.setAtendimento("T");
        }
        if (totalRadioButton.isSelected()) {
            pedido.setAtendimento("A");
        }
        if (parcialRadioButton.isSelected()) {
            pedido.setAtendimento("P");
        }
        pedido.setCidade(cidadeField.getText());
        pedido.setBairro(bairroField.getText());
        pedido.setUf(ufField.getText());

        if (dtPedido1Field.getDate() != null) {
            try {
                pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate()))); //DateUtils.parse(dtPedido1Field.getText())
                pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido2Field.getDate())));
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        if (pedido.getDtEmissaoIni() != null) {
            title.append(getBundle().getString("emitidos"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEmissaoIni()));
            title.append(" ");
            title.append(getBundle().getString("para"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            title.append(". ");
        }
        if (dtEntrega1Field.getDate() != null) {
            try {
                pedido.setDtEntregaIni(DateUtils.parse(DateUtils.format(dtEntrega1Field.getDate()))); //DateUtils.parse(dtPedido1Field.getText())
                pedido.setDtEntregaEnd(DateUtils.parse(DateUtils.format(dtEntrega2Field.getDate())));
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        if (pedido.getDtEntregaIni() != null) {
            title.append(getBundle().getString("entregas"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEntregaIni()));
            title.append(getBundle().getString("para"));
            title.append(DateUtils.format(pedido.getDtEntregaEnd()));
            title.append(". ");
        }
        if (dtNota1Field.getDate() != null) {
            try {
                pedido.setDtNotaIni(DateUtils.parse(DateUtils.format(dtNota1Field.getDate()))); //DateUtils.parse(dtPedido1Field.getText())
                pedido.setDtNotaEnd(DateUtils.parse(DateUtils.format(dtNota2Field.getDate())));
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        }
        if (pedido.getDtNotaIni() != null) {
            title.append(getBundle().getString("notasEmitidas"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtNotaIni()));
            title.append(" a ");
            title.append(DateUtils.format(pedido.getDtNotaEnd()));
            title.append(". ");
        }
        pedido.setGrupo((GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem());
        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            title.append(getBundle().getString("grupoCliente"));
            title.append(" ");
            title.append(pedido.getGrupo().getNomeGrupo());
        }
        pedido.setSegmento((SegMercado) segmentoComboBox.getModel().getSelectedItem());
        if (pedido.getSegmento().getIdSegmento() != null) {
            title.append(getBundle().getString("segmento"));
            title.append(" ");
            title.append(pedido.getSegmento().getNome());
        }
        pedido.setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        SubGrupoProduto subGrupo = (SubGrupoProduto) subGrupoComboBox.getModel().getSelectedItem();
        pedido.setSubGrupoProduto(subGrupo);
        if ((subGrupo != null) && (subGrupo.getIdCodSubGrupo() != null)) {
            title.append("Grupo produto: ").append(pedido.getGrupoProduto().getNomeGrupo()).append(". ");
            title.append("Sub-grupo: ").append(subGrupo.getNomeGrupo()).append(". ");
        }
        pedido.setNota(notaField.getText());
        if (represRadioButton.isSelected()) {
            pedido.setOrdem(0);
        } else if (clienteRadioButton.isSelected()) {
            pedido.setOrdem(1);
        } else if (entregaRadioButton.isSelected()) {
            pedido.setOrdem(2);
        } else if (pedidoRadioButton.isSelected()) {
            pedido.setOrdem(3);
        } else if (emissaoRadioButton.isSelected()) {
            pedido.setOrdem(4);
        } else
            pedido.setOrdem(5);

        pedido.setPedido((Integer) pedidoField.getValue());
        pedido.getProduto().setIdProduto((Integer) produtoField.getValue());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setResponsavel((Cliente) responsavelComboBox.getModel().getSelectedItem());
        pedido.setFormaVenda((FormaVenda) formaVendaComboBox.getModel().getSelectedItem());
        pedido.setOp(somenteOPCheckBox.isSelected());
        
        if (pedido.getRepres() != null && pedido.getRepres().getIdRepres() != null) {
            pedido.setFiltrarPedidosFornecedor(pedidosRepresCheckBox.isSelected());
        } else 
            pedido.setFiltrarPedidosFornecedor(false);
        
        if (pedido.getReposicao()) {
            title.append(getBundle().getString("reposicao"));
            title.append(". ");
        }
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        if (!"T".equals(pedido.getSituacao())) {
            if (("N").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("naoAtendidos"));
                title.append(". ");
            }
            if (("A").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("atendidos"));
                title.append(". ");
            }
            if (("E").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("emAtraso"));
                title.append(". ");
            }
        }
        if (emitidosRadioButton.isSelected()) {
            pedido.setEmissao(1);
        } else if (naoEmitidosRadioButton.isSelected()) {
            pedido.setEmissao(2);
        }
        if (demoEmitidosRadioButton.isSelected()) {
            pedido.setDemonstrativo(1);
        } else if (demoNaoEmitidosRadioButton.isSelected()) {
            pedido.setDemonstrativo(2);
        }
        
        pedido.setInativos(inativosCheckBox.isSelected());
        
        pedido.setPrePedido(prePedidoCheckBox.isSelected());
        pedido.setPreCobranca(preCobrancaCheckBox.isSelected());
        pedido.setPrecoCliente(precoClienteCheckBox.isSelected());
        pedido.setTitle(title.toString());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        atendimentoGroup = new javax.swing.ButtonGroup();
        situacaoGroup = new javax.swing.ButtonGroup();
        orderGroup = new javax.swing.ButtonGroup();
        emitidoGroup = new javax.swing.ButtonGroup();
        demoGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pedidoField = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtEntrega1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtEntrega2Field = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        todosRadioButton = new javax.swing.JRadioButton();
        atendidosRadioButton = new javax.swing.JRadioButton();
        naoAtendRadioButton = new javax.swing.JRadioButton();
        atrasoRadioButton = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        represRadioButton = new javax.swing.JRadioButton();
        clienteRadioButton = new javax.swing.JRadioButton();
        entregaRadioButton = new javax.swing.JRadioButton();
        pedidoRadioButton = new javax.swing.JRadioButton();
        emissaoRadioButton = new javax.swing.JRadioButton();
        pedidoRepresRadioButton = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        todosEmitidosRadioButton = new javax.swing.JRadioButton();
        naoEmitidosRadioButton = new javax.swing.JRadioButton();
        emitidosRadioButton = new javax.swing.JRadioButton();
        prePedidoCheckBox = new javax.swing.JCheckBox();
        somenteOPCheckBox = new javax.swing.JCheckBox();
        preCobrancaCheckBox = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        demoTodosRadioButton = new javax.swing.JRadioButton();
        demoNaoEmitidosRadioButton = new javax.swing.JRadioButton();
        demoEmitidosRadioButton = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        formaVendaComboBox = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        produtoField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        subGrupoComboBox = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        clientesInativosCheckBox = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        segmentoComboBox = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        precoClienteCheckBox = new javax.swing.JCheckBox();
        boletoCheckBox = new javax.swing.JCheckBox();
        ufField = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        responsavelComboBox = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        notaField = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        atendimentoRadioButton = new javax.swing.JRadioButton();
        totalRadioButton = new javax.swing.JRadioButton();
        parcialRadioButton = new javax.swing.JRadioButton();
        inativosCheckBox = new javax.swing.JCheckBox();
        pedidosRepresCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("pedido"))); // NOI18N

        jLabel9.setText(bundle.getString("pedido")); // NOI18N

        pedidoField.setFormatterFactory(NumberUtils.getIntFormatterFactory());

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PedidoFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N

        jLabel5.setText(bundle.getString("dtEntrega")); // NOI18N

        dtEntrega1Field.setDateFormatString(resourceMap.getString("dtEntrega1Field.dateFormatString")); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N

        dtEntrega2Field.setDateFormatString(resourceMap.getString("dtEntrega2Field.dateFormatString")); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("situacao"))); // NOI18N

        situacaoGroup.add(todosRadioButton);
        todosRadioButton.setText(bundle.getString("todos")); // NOI18N
        todosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(atendidosRadioButton);
        atendidosRadioButton.setText(bundle.getString("atendidos")); // NOI18N
        atendidosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(naoAtendRadioButton);
        naoAtendRadioButton.setSelected(true);
        naoAtendRadioButton.setText(bundle.getString("naoAtendidos")); // NOI18N
        naoAtendRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(atrasoRadioButton);
        atrasoRadioButton.setText(bundle.getString("emAtraso")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(todosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(atendidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(naoAtendRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(atrasoRadioButton)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(todosRadioButton)
                .addComponent(atendidosRadioButton)
                .addComponent(naoAtendRadioButton)
                .addComponent(atrasoRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N

        orderGroup.add(represRadioButton);
        represRadioButton.setText(bundle.getString("representada")); // NOI18N
        represRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(clienteRadioButton);
        clienteRadioButton.setText(bundle.getString("cliente")); // NOI18N
        clienteRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(entregaRadioButton);
        entregaRadioButton.setSelected(true);
        entregaRadioButton.setText(bundle.getString("dtEntrega")); // NOI18N
        entregaRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(pedidoRadioButton);
        pedidoRadioButton.setText(bundle.getString("pedido")); // NOI18N
        pedidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(emissaoRadioButton);
        emissaoRadioButton.setText(bundle.getString("emissao")); // NOI18N

        orderGroup.add(pedidoRepresRadioButton);
        pedidoRepresRadioButton.setText(resourceMap.getString("pedidoRepresRadioButton.text")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(represRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clienteRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entregaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pedidoRepresRadioButton)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(pedidoRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(emissaoRadioButton)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(represRadioButton)
                    .addComponent(clienteRadioButton)
                    .addComponent(entregaRadioButton)
                    .addComponent(pedidoRadioButton)
                    .addComponent(emissaoRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pedidoRepresRadioButton))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("enviados"))); // NOI18N

        emitidoGroup.add(todosEmitidosRadioButton);
        todosEmitidosRadioButton.setSelected(true);
        todosEmitidosRadioButton.setText(bundle.getString("todos")); // NOI18N
        todosEmitidosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        emitidoGroup.add(naoEmitidosRadioButton);
        naoEmitidosRadioButton.setText(bundle.getString("naoEnviados")); // NOI18N

        emitidoGroup.add(emitidosRadioButton);
        emitidosRadioButton.setText(bundle.getString("enviados")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(todosEmitidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(emitidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(naoEmitidosRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emitidosRadioButton)
                    .addComponent(naoEmitidosRadioButton)
                    .addComponent(todosEmitidosRadioButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        prePedidoCheckBox.setText(bundle.getString("prePedido")); // NOI18N

        somenteOPCheckBox.setText(bundle.getString("somenteOP")); // NOI18N
        somenteOPCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        preCobrancaCheckBox.setText(bundle.getString("preCobranca")); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("demonstrativos"))); // NOI18N

        demoGroup.add(demoTodosRadioButton);
        demoTodosRadioButton.setSelected(true);
        demoTodosRadioButton.setText(bundle.getString("todos")); // NOI18N
        demoTodosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        demoGroup.add(demoNaoEmitidosRadioButton);
        demoNaoEmitidosRadioButton.setText(bundle.getString("naoEnviados")); // NOI18N

        demoGroup.add(demoEmitidosRadioButton);
        demoEmitidosRadioButton.setText(bundle.getString("enviados")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(demoTodosRadioButton)
                .addGap(7, 7, 7)
                .addComponent(demoEmitidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(demoNaoEmitidosRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(demoTodosRadioButton)
                    .addComponent(demoEmitidosRadioButton)
                    .addComponent(demoNaoEmitidosRadioButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel23.setText(bundle.getString("formaVenda")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel9)
                                .addGap(72, 72, 72)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(14, 14, 14)
                                .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(prePedidoCheckBox))
                            .addComponent(jLabel5))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(preCobrancaCheckBox)
                            .addComponent(somenteOPCheckBox)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(formaVendaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(3, 3, 3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pedidoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(prePedidoCheckBox))
                        .addGap(1, 1, 1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(preCobrancaCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(somenteOPCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formaVendaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("produtos"))); // NOI18N

        jLabel17.setText(bundle.getString("produto")); // NOI18N

        produtoField.setFormatterFactory(NumberUtils.getIntFormatterFactory());

        jLabel18.setText(bundle.getString("grupoProduto")); // NOI18N

        grupoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupoComboBoxActionPerformed(evt);
            }
        });

        jLabel20.setText(bundle.getString("subgrupo")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(produtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))))
                .addGap(174, 174, 174))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(produtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("cliente"))); // NOI18N

        jLabel10.setText(bundle.getString("razaoSocial")); // NOI18N

        clientesInativosCheckBox.setLabel(bundle.getString("clientesInativos")); // NOI18N
        clientesInativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesInativosCheckBoxActionPerformed(evt);
            }
        });

        jLabel12.setText(bundle.getString("grupoCliente")); // NOI18N

        jLabel21.setText(bundle.getString("segmento")); // NOI18N

        jLabel16.setText(bundle.getString("cidade")); // NOI18N

        cidadeField.setDocument(new BoundedPlainDocument(30));

        jLabel19.setText(bundle.getString("bairro")); // NOI18N

        bairroField.setDocument(new BoundedPlainDocument(30));

        precoClienteCheckBox.setText(bundle.getString("precoCliente")); // NOI18N

        boletoCheckBox.setText(bundle.getString("entregarBoleto")); // NOI18N

        ufField.setDocument(new BoundedPlainDocument(30));

        jLabel22.setText(bundle.getString("uf")); // NOI18N

        jLabel11.setText(bundle.getString("clienteResponsavel")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(clientesInativosCheckBox))
                            .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(segmentoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel11)
                    .addComponent(responsavelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(precoClienteCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boletoCheckBox))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(144, 144, 144)
                        .addComponent(jLabel19)
                        .addGap(185, 185, 185)
                        .addComponent(jLabel22)))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(clientesInativosCheckBox)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(segmentoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(responsavelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(precoClienteCheckBox)
                        .addComponent(boletoCheckBox))
                    .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("atendimento"))); // NOI18N

        jLabel14.setText(bundle.getString("dtNota")); // NOI18N

        dtNota1Field.setDateFormatString(resourceMap.getString("dtNota1Field.dateFormatString")); // NOI18N

        jLabel15.setText(bundle.getString("para")); // NOI18N

        dtNota2Field.setDateFormatString(resourceMap.getString("dtNota2Field.dateFormatString")); // NOI18N

        jLabel6.setText(bundle.getString("notaFiscal")); // NOI18N

        jLabel13.setText(bundle.getString("representada")); // NOI18N

        atendimentoGroup.add(atendimentoRadioButton);
        atendimentoRadioButton.setSelected(true);
        atendimentoRadioButton.setText(bundle.getString("todos")); // NOI18N
        atendimentoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        atendimentoGroup.add(totalRadioButton);
        totalRadioButton.setText(bundle.getString("total")); // NOI18N
        totalRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        atendimentoGroup.add(parcialRadioButton);
        parcialRadioButton.setText(bundle.getString("parcial")); // NOI18N
        parcialRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        inativosCheckBox.setText(bundle.getString("mostrarInativas")); // NOI18N
        inativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inativosCheckBoxActionPerformed(evt);
            }
        });

        pedidosRepresCheckBox.setText(bundle.getString("filtrarPedidosFornecedor")); // NOI18N
        pedidosRepresCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidosRepresCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addComponent(jLabel14))
                .addGap(4, 4, 4)
                .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notaField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inativosCheckBox))
                    .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(atendimentoRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parcialRadioButton))
                    .addComponent(pedidosRepresCheckBox))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel13)
                                .addComponent(inativosCheckBox)
                                .addComponent(pedidosRepresCheckBox)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtNota1Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dtNota2Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(notaField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(parcialRadioButton))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(totalRadioButton))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(atendimentoRadioButton)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inativosCheckBoxActionPerformed
        represComboBox.setModel(VendasUtil.getRepresListModel(!inativosCheckBox.isSelected()));
    }//GEN-LAST:event_inativosCheckBoxActionPerformed

    private void clientesInativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesInativosCheckBoxActionPerformed
        clienteComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
        responsavelComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
    }//GEN-LAST:event_clientesInativosCheckBoxActionPerformed

    private void grupoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupoComboBoxActionPerformed
        // TODO add your handling code here:
        GrupoProduto grupo = (GrupoProduto) grupoComboBox.getModel().getSelectedItem();
        subGrupoComboBox.setModel(VendasUtil.getSubGrupoProdutoListModel(grupo));
    }//GEN-LAST:event_grupoComboBoxActionPerformed

    private void pedidosRepresCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidosRepresCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pedidosRepresCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton atendidosRadioButton;
    private javax.swing.ButtonGroup atendimentoGroup;
    private javax.swing.JRadioButton atendimentoRadioButton;
    private javax.swing.JRadioButton atrasoRadioButton;
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JCheckBox boletoCheckBox;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JRadioButton clienteRadioButton;
    private javax.swing.JCheckBox clientesInativosCheckBox;
    private javax.swing.JRadioButton demoEmitidosRadioButton;
    private javax.swing.ButtonGroup demoGroup;
    private javax.swing.JRadioButton demoNaoEmitidosRadioButton;
    private javax.swing.JRadioButton demoTodosRadioButton;
    private com.toedter.calendar.JDateChooser dtEntrega1Field;
    private com.toedter.calendar.JDateChooser dtEntrega2Field;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JRadioButton emissaoRadioButton;
    private javax.swing.ButtonGroup emitidoGroup;
    private javax.swing.JRadioButton emitidosRadioButton;
    private javax.swing.JRadioButton entregaRadioButton;
    private javax.swing.JComboBox formaVendaComboBox;
    private javax.swing.JComboBox grupoClienteComboBox;
    private javax.swing.JComboBox grupoComboBox;
    private javax.swing.JCheckBox inativosCheckBox;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JRadioButton naoAtendRadioButton;
    private javax.swing.JRadioButton naoEmitidosRadioButton;
    private javax.swing.JFormattedTextField notaField;
    private javax.swing.ButtonGroup orderGroup;
    private javax.swing.JRadioButton parcialRadioButton;
    private javax.swing.JFormattedTextField pedidoField;
    private javax.swing.JRadioButton pedidoRadioButton;
    private javax.swing.JRadioButton pedidoRepresRadioButton;
    private javax.swing.JCheckBox pedidosRepresCheckBox;
    private javax.swing.JCheckBox preCobrancaCheckBox;
    private javax.swing.JCheckBox prePedidoCheckBox;
    private javax.swing.JCheckBox precoClienteCheckBox;
    private javax.swing.JFormattedTextField produtoField;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JRadioButton represRadioButton;
    private javax.swing.JComboBox responsavelComboBox;
    private javax.swing.JComboBox segmentoComboBox;
    private javax.swing.ButtonGroup situacaoGroup;
    private javax.swing.JCheckBox somenteOPCheckBox;
    private javax.swing.JComboBox subGrupoComboBox;
    private javax.swing.JRadioButton todosEmitidosRadioButton;
    private javax.swing.JRadioButton todosRadioButton;
    private javax.swing.JRadioButton totalRadioButton;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
