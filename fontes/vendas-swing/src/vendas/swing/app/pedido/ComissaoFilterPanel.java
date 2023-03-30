/*
 * ComissaoFilterPanel.java
 *
 * Created on 30 de Novembro de 2007, 17:18
 */
package vendas.swing.app.pedido;

import java.util.Date;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.PedidoFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.GrupoProduto;
import vendas.entity.Repres;
import vendas.entity.SubGrupoProduto;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class ComissaoFilterPanel extends EditPanel {

    /** Creates new form ComissaoFilterPanel */
    public ComissaoFilterPanel() {
        initComponents();
    }

    @Override
    public void object2Field(Object obj) {
        getLogger().info("object2Field");
        PedidoFilter pedido = (PedidoFilter) obj;
        clienteComboBox.setSelectedItem(pedido.getCliente());
        responsavelComboBox.setSelectedItem(pedido.getResponsavel());
        dtNota1Field.setDate(pedido.getDtNotaIni());
        dtNota2Field.setDate(pedido.getDtNotaEnd());
        dtPgto1Field.setDate(pedido.getDtPgtoNotaIni());
        dtPgto2Field.setDate(pedido.getDtPgtoNotaEnd());
        dtPedido1Field.setDate(pedido.getDtEmissaoIni());
        dtPedido2Field.setDate(pedido.getDtEmissaoEnd());
        represComboBox.setSelectedItem(pedido.getRepres());
        vendedorComboBox.setSelectedItem(pedido.getVendedor());
        grupoComboBox.setSelectedItem(pedido.getGrupoProduto());
        String situacao = pedido.getSituacao();
        if ("T".equals(situacao)) {
            todosRadioButton.setSelected(true);
        } else if ("S".equals(situacao)) {
            saldoRadioButton.setSelected(true);
        } else if ("N".equals(situacao)) {
            naoAtendidosRadioButton.setSelected(true);
        } else {
            pgtosRadioButton.setSelected(true);
        }
    }

    @Override
    public void field2Object(Object obj) {
        StringBuilder sb = new StringBuilder();
        PedidoFilter pedido = (PedidoFilter) obj;
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setResponsavel((Cliente) responsavelComboBox.getModel().getSelectedItem());
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        pedido.setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            sb.append("Grupo: ").append(pedido.getGrupoProduto().getNomeGrupo()).append(". ");
        }
        SubGrupoProduto subGrupo = (SubGrupoProduto) subGrupoComboBox.getModel().getSelectedItem();
        pedido.setSubGrupoProduto(subGrupo);
        if ((subGrupo != null) && (subGrupo.getIdCodSubGrupo() != null)) {
            sb.append("Sub-grupo: ").append(subGrupo.getNomeGrupo()).append(". ");
        }
        if (dtPedido1Field.getDate() != null)
        try {
        pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate())));
        pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido2Field.getDate())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pedido.getDtEmissaoIni() != null) {
            sb.append("Emitidos de ").append(DateUtils.format(pedido.getDtEmissaoIni()));
            sb.append(" a ");
            sb.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            sb.append(". ");
        }
        pedido.setDtNotaIni(dtNota1Field.getDate());
        pedido.setDtNotaEnd(dtNota2Field.getDate());
        if (pedido.getDtNotaIni() != null) {
            sb.append("Notas de ").append(DateUtils.format(pedido.getDtNotaIni()));
            sb.append(" a ");
            sb.append(DateUtils.format(pedido.getDtNotaEnd()));
            sb.append(". ");
        }
        if (dtPgto1Field.getDate() != null) {
            try {
                Date d1 = DateUtils.parse(DateUtils.format(dtPgto1Field.getDate()));
                Date d2 = DateUtils.parse(DateUtils.format(dtPgto2Field.getDate()));
                pedido.setDtPgtoNotaIni(d1);
                pedido.setDtPgtoNotaEnd(d2);

                sb.append("Pagamentos de ").append(DateUtils.format(pedido.getDtPgtoNotaIni()));
                sb.append(" a ");
                sb.append(DateUtils.format(pedido.getDtPgtoNotaEnd()));
                sb.append(". ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (saldoRadioButton.isSelected()) {
            pedido.setSituacao("S");
            sb.append("Saldo. ");
        } else if (pgtosRadioButton.isSelected()) {
            pedido.setSituacao("P");
            sb.append("Pagos. ");
        } else if (naoAtendidosRadioButton.isSelected()) {
            pedido.setSituacao("N");
            sb.append("Pedidos n\u00E3o atendidos. ");
        } else {
            pedido.setSituacao("T");
        }

        if (detalheRadioButton.isSelected()) {
            pedido.setTipoRelatorio(0);
        } else {
            pedido.setTipoRelatorio(1);
        }
        
        pedido.setTitle(sb.toString());

        if (clienteRadioButton.isSelected()) {
            pedido.setOrdem(0);
        } else
        if (entregaRadioButton.isSelected()) {
            pedido.setOrdem(1);
        } else
        if (pedidoRadioButton.isSelected()) {
            pedido.setOrdem(2);
        } else
        if (dtNotaRadioButton.isSelected()) {
            pedido.setOrdem(3);
        } else
        if (dtPedidoRadioButton.isSelected()) {
            pedido.setOrdem(4);
        } else
        if (notaRadioButton.isSelected()) {
            pedido.setOrdem(5);
        }
        pedido.setFiltrarPgtos(pgtosCheckBox.isSelected());
    }

    @Override
    public void init() {
        getLogger().info("init");
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        clienteComboBox.setModel(VendasUtil.getClienteListModel());
        responsavelComboBox.setModel(VendasUtil.getClienteListModel());
        represComboBox.setModel(VendasUtil.getRepresListModel(true));
        grupoComboBox.setModel(VendasUtil.getGrupoProdutoListModel());
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        dtPgto1Field = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dtPgto2Field = new com.toedter.calendar.JDateChooser();
        pgtosCheckBox = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        subGrupoComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        todosRadioButton = new javax.swing.JRadioButton();
        naoAtendidosRadioButton = new javax.swing.JRadioButton();
        pgtosRadioButton = new javax.swing.JRadioButton();
        saldoRadioButton = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        detalheRadioButton = new javax.swing.JRadioButton();
        resumoRadioButton = new javax.swing.JRadioButton();
        ordemPanel = new javax.swing.JPanel();
        clienteRadioButton = new javax.swing.JRadioButton();
        entregaRadioButton = new javax.swing.JRadioButton();
        pedidoRadioButton = new javax.swing.JRadioButton();
        notaRadioButton = new javax.swing.JRadioButton();
        dtNotaRadioButton = new javax.swing.JRadioButton();
        dtPedidoRadioButton = new javax.swing.JRadioButton();
        clientesInativosCheckBox = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        responsavelComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtPedido")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(ComissaoFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N
        dtPedido1Field.setName("dtPedido1Field"); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N
        dtPedido2Field.setName("dtPedido2Field"); // NOI18N

        jLabel5.setText(bundle.getString("dtNota")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        dtNota1Field.setDateFormatString(resourceMap.getString("dtNota1Field.dateFormatString")); // NOI18N
        dtNota1Field.setName("dtNota1Field"); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        dtNota2Field.setDateFormatString(resourceMap.getString("dtNota2Field.dateFormatString")); // NOI18N
        dtNota2Field.setName("dtNota2Field"); // NOI18N

        jLabel7.setText(bundle.getString("pagamentos")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        dtPgto1Field.setDateFormatString(resourceMap.getString("dtPgto1Field.dateFormatString")); // NOI18N
        dtPgto1Field.setName("dtPgto1Field"); // NOI18N

        jLabel6.setText(bundle.getString("para")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        dtPgto2Field.setDateFormatString(resourceMap.getString("dtPgto2Field.dateFormatString")); // NOI18N
        dtPgto2Field.setName("dtPgto2Field"); // NOI18N

        pgtosCheckBox.setText(bundle.getString("somentepagamentos")); // NOI18N
        pgtosCheckBox.setName("pgtosCheckBox"); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel11.setText(bundle.getString("grupoProduto")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        grupoComboBox.setName("grupoComboBox"); // NOI18N
        grupoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupoComboBoxActionPerformed(evt);
            }
        });

        jLabel12.setText(bundle.getString("subgrupo")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        subGrupoComboBox.setName("subGrupoComboBox"); // NOI18N

        jLabel9.setText(bundle.getString("representada")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        clienteComboBox.setName("clienteComboBox"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("situacao"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        buttonGroup1.add(todosRadioButton);
        todosRadioButton.setText(bundle.getString("todos")); // NOI18N
        todosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        todosRadioButton.setName("todosRadioButton"); // NOI18N

        buttonGroup1.add(naoAtendidosRadioButton);
        naoAtendidosRadioButton.setText(bundle.getString("naoAtendidos")); // NOI18N
        naoAtendidosRadioButton.setName("naoAtendidosRadioButton"); // NOI18N

        buttonGroup1.add(pgtosRadioButton);
        pgtosRadioButton.setText(bundle.getString("pagamento")); // NOI18N
        pgtosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pgtosRadioButton.setName("pgtosRadioButton"); // NOI18N

        buttonGroup1.add(saldoRadioButton);
        saldoRadioButton.setSelected(true);
        saldoRadioButton.setText(bundle.getString("saldo")); // NOI18N
        saldoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saldoRadioButton.setName("saldoRadioButton"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(todosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(naoAtendidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pgtosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saldoRadioButton))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(todosRadioButton)
                    .addComponent(naoAtendidosRadioButton)
                    .addComponent(pgtosRadioButton)
                    .addComponent(saldoRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("tipoRelatorio"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        buttonGroup2.add(detalheRadioButton);
        detalheRadioButton.setSelected(true);
        detalheRadioButton.setText(bundle.getString("detalhado")); // NOI18N
        detalheRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        detalheRadioButton.setName("detalheRadioButton"); // NOI18N
        detalheRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                detalheRadioButtonStateChanged(evt);
            }
        });
        detalheRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detalheRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(resumoRadioButton);
        resumoRadioButton.setText(bundle.getString("resumo")); // NOI18N
        resumoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        resumoRadioButton.setName("resumoRadioButton"); // NOI18N
        resumoRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                resumoRadioButtonStateChanged(evt);
            }
        });
        resumoRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumoRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(detalheRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resumoRadioButton)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detalheRadioButton)
                    .addComponent(resumoRadioButton))
                .addContainerGap(6, Short.MAX_VALUE))
        );

        ordemPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N
        ordemPanel.setName("ordemPanel"); // NOI18N

        buttonGroup3.add(clienteRadioButton);
        clienteRadioButton.setSelected(true);
        clienteRadioButton.setText(bundle.getString("cliente")); // NOI18N
        clienteRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        clienteRadioButton.setName("clienteRadioButton"); // NOI18N

        buttonGroup3.add(entregaRadioButton);
        entregaRadioButton.setText(bundle.getString("dtEntrega")); // NOI18N
        entregaRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        entregaRadioButton.setName("entregaRadioButton"); // NOI18N

        buttonGroup3.add(pedidoRadioButton);
        pedidoRadioButton.setText(bundle.getString("pedido")); // NOI18N
        pedidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pedidoRadioButton.setName("pedidoRadioButton"); // NOI18N

        buttonGroup3.add(notaRadioButton);
        notaRadioButton.setText(bundle.getString("nota")); // NOI18N
        notaRadioButton.setName("notaRadioButton"); // NOI18N

        buttonGroup3.add(dtNotaRadioButton);
        dtNotaRadioButton.setText(bundle.getString("dtNota")); // NOI18N
        dtNotaRadioButton.setName("dtNotaRadioButton"); // NOI18N

        buttonGroup3.add(dtPedidoRadioButton);
        dtPedidoRadioButton.setText(bundle.getString("dtPedido")); // NOI18N
        dtPedidoRadioButton.setName("dtPedidoRadioButton"); // NOI18N

        javax.swing.GroupLayout ordemPanelLayout = new javax.swing.GroupLayout(ordemPanel);
        ordemPanel.setLayout(ordemPanelLayout);
        ordemPanelLayout.setHorizontalGroup(
            ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clienteRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entregaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtNotaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtPedidoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notaRadioButton)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        ordemPanelLayout.setVerticalGroup(
            ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordemPanelLayout.createSequentialGroup()
                .addGroup(ordemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteRadioButton)
                    .addComponent(entregaRadioButton)
                    .addComponent(pedidoRadioButton)
                    .addComponent(notaRadioButton)
                    .addComponent(dtNotaRadioButton)
                    .addComponent(dtPedidoRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        clientesInativosCheckBox.setLabel(bundle.getString("clientesInativos")); // NOI18N
        clientesInativosCheckBox.setName("clientesInativosCheckBox"); // NOI18N
        clientesInativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesInativosCheckBoxActionPerformed(evt);
            }
        });

        jLabel13.setText(bundle.getString("responsavel")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        responsavelComboBox.setName("responsavelComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ordemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(clientesInativosCheckBox))
                    .addComponent(jLabel13)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(responsavelComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dtPgto1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pgtosCheckBox))
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtNota2Field, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(dtPgto1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(dtPgto2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pgtosCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subGrupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(clientesInativosCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(responsavelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ordemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void resumoRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_resumoRadioButtonStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_resumoRadioButtonStateChanged

    private void detalheRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_detalheRadioButtonStateChanged
        // TODO add your handling code here:
        clienteRadioButton.setEnabled(detalheRadioButton.isSelected());
        entregaRadioButton.setEnabled(detalheRadioButton.isSelected());
        pedidoRadioButton.setEnabled(detalheRadioButton.isSelected());
    }//GEN-LAST:event_detalheRadioButtonStateChanged

    private void grupoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupoComboBoxActionPerformed
        GrupoProduto grupo = (GrupoProduto) grupoComboBox.getModel().getSelectedItem();
        subGrupoComboBox.setModel(VendasUtil.getSubGrupoProdutoListModel(grupo));
    }//GEN-LAST:event_grupoComboBoxActionPerformed

    private void clientesInativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesInativosCheckBoxActionPerformed
        clienteComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
        responsavelComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
}//GEN-LAST:event_clientesInativosCheckBoxActionPerformed

    private void detalheRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detalheRadioButtonActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_detalheRadioButtonActionPerformed

    private void resumoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumoRadioButtonActionPerformed

    }//GEN-LAST:event_resumoRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JRadioButton clienteRadioButton;
    private javax.swing.JCheckBox clientesInativosCheckBox;
    private javax.swing.JRadioButton detalheRadioButton;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private javax.swing.JRadioButton dtNotaRadioButton;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JRadioButton dtPedidoRadioButton;
    private com.toedter.calendar.JDateChooser dtPgto1Field;
    private com.toedter.calendar.JDateChooser dtPgto2Field;
    private javax.swing.JRadioButton entregaRadioButton;
    private javax.swing.JComboBox grupoComboBox;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton naoAtendidosRadioButton;
    private javax.swing.JRadioButton notaRadioButton;
    private javax.swing.JPanel ordemPanel;
    private javax.swing.JRadioButton pedidoRadioButton;
    private javax.swing.JCheckBox pgtosCheckBox;
    private javax.swing.JRadioButton pgtosRadioButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox responsavelComboBox;
    private javax.swing.JRadioButton resumoRadioButton;
    private javax.swing.JRadioButton saldoRadioButton;
    private javax.swing.JComboBox subGrupoComboBox;
    private javax.swing.JRadioButton todosRadioButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
