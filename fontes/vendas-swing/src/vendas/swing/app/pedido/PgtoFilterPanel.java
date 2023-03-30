/*
 * PedidoFilterPanel.java
 *
 * Created on 15 de Julho de 2007, 12:36
 */
package vendas.swing.app.pedido;

import ritual.swing.BoundedPlainDocument;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.CobrancaFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class PgtoFilterPanel extends EditPanel {

    /** Creates new form PedidoFilterPanel */
    public PgtoFilterPanel() {
        initComponents();
    }

    @Override
    public void init() {
        try {
            clienteComboBox.setModel(VendasUtil.getClienteListModel());
            responsavelComboBox.setModel(VendasUtil.getClienteListModel());
            represComboBox.setModel(VendasUtil.getRepresListModel(true));
            grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
            vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
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
    public void field2Object(Object obj) {
        CobrancaFilter pedido = (CobrancaFilter) obj;
        StringBuilder title = new StringBuilder();

        if (todosRadioButton.isSelected()) {
            pedido.setSituacao("T");
        }
        if (pagosRadioButton.isSelected()) {
            pedido.setSituacao("P");
        }
        if (naoPagosRadioButton.isSelected()) {
            pedido.setSituacao("N");
        }
        if (atrasoRadioButton.isSelected()) {
            pedido.setSituacao("E");
        }

        if (dtPedido1Field.getDate() != null) {
            try {
                pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate()))); //DateUtils.parse(dtPedido1Field.getText())
                pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate())));
            } catch (Exception e) {
            }
        }
        if (pedido.getDtEmissaoIni() != null) {
            title.append(getBundle().getString("emitidos"));
            title.append(": ");
            title.append(DateUtils.format(pedido.getDtEmissaoIni()));
            title.append(getBundle().getString("para"));
            title.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            title.append(". ");
        }
        if (dtVencimento1Field.getDate() != null) {
            try {
                pedido.setDtVencimentoIni(DateUtils.parse(DateUtils.format(dtVencimento1Field.getDate())));
                pedido.setDtVencimentoEnd(DateUtils.parse(DateUtils.format(dtVencimento2Field.getDate())));
            } catch (Exception e) {
            }
            title.append(getBundle().getString("vencimentos"));
            title.append(": ");
            title.append(DateUtils.format(pedido.getDtVencimentoIni()));
            title.append(getBundle().getString("para"));
            title.append(DateUtils.format(pedido.getDtVencimentoEnd()));
            title.append(". ");
        } else
        if (dtVencimento2Field.getDate() != null) {
            try {
                pedido.setDtVencimentoEnd(DateUtils.parse(DateUtils.format(dtVencimento2Field.getDate())));
            } catch (Exception e) {
            }
            title.append(getBundle().getString("vencimentos"));
            title.append(" até ");
            title.append(DateUtils.format(pedido.getDtVencimentoEnd()));
            title.append(". ");
        }
        if (dtPgtoIniField.getDate() != null) {
            try {
                pedido.setDtPgtoIni(DateUtils.parse(DateUtils.format(dtPgtoIniField.getDate())));
                pedido.setDtPgtoEnd(DateUtils.parse(DateUtils.format(dtPgtoEndField.getDate())));
            } catch (Exception e) {
            }
            title.append(getBundle().getString("pagamentos"));
            title.append(": ");
            title.append(DateUtils.format(pedido.getDtPgtoIni()));
            title.append(getBundle().getString("para"));
            title.append(DateUtils.format(pedido.getDtPgtoEnd()));
            title.append(". ");
        }
        if (dtDescontoIniField.getDate() != null) {
            try {
                pedido.setDtDescontoIni(DateUtils.parse(DateUtils.format(dtDescontoIniField.getDate())));
                pedido.setDtDescontoEnd(DateUtils.parse(DateUtils.format(dtDescontoEndField.getDate())));
            } catch (Exception e) {
            }
            title.append(getBundle().getString("descontos"));
            title.append(": ");
            title.append(DateUtils.format(pedido.getDtDescontoIni()));
            title.append(getBundle().getString("para"));
            title.append(DateUtils.format(pedido.getDtDescontoEnd()));
            title.append(". ");
        }
        pedido.setDtPrevPgtoIni(dtPrevPgtoIniField.getDate());
        pedido.setDtPrevPgtoEnd(dtPrevPgtoEndField.getDate());
        if (pedido.getDtPrevPgtoIni() != null) {
            title.append(getBundle().getString("prevPgtos"));
            title.append(": ");
            title.append(DateUtils.format(pedido.getDtPrevPgtoIni()));
            title.append("para");
            title.append(DateUtils.format(pedido.getDtPrevPgtoEnd()));
            title.append(". ");
        }
        pedido.setGrupo((GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem());
        if (tipoPgtoField.getText() != null) {
            pedido.setTipoPgto(tipoPgtoField.getText());
        }
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setResponsavel((Cliente) responsavelComboBox.getModel().getSelectedItem());
        pedido.setVendedor((Vendedor) vendedorComboBox.getModel().getSelectedItem());
        if (!"T".equals(pedido.getSituacao())) {
            if (("N").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("naoPagos"));
                title.append(". ");
            }
            if (("A").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("pagos"));
                title.append(". ");
            }
            
            if (("E").equals(pedido.getSituacao())) {
                title.append(getBundle().getString("emAtraso"));
                title.append(". ");
            }
        }
        if (vencRadioButton.isSelected()) {
            pedido.setOrdem(0);
        } else if (pgtoRadioButton.isSelected()) {
            pedido.setOrdem(1);
        } else {
            pedido.setOrdem(2);
        }

        pedido.setTitle(title.toString());

        pedido.setOperador(operadorField.getText().toUpperCase());
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
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dtVencimento1Field = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtVencimento2Field = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        dtPgtoIniField = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        dtPgtoEndField = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        dtPrevPgtoIniField = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        dtPrevPgtoEndField = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        tipoPgtoField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        dtDescontoIniField = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        dtDescontoEndField = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        operadorField = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        todosRadioButton = new javax.swing.JRadioButton();
        pagosRadioButton = new javax.swing.JRadioButton();
        naoPagosRadioButton = new javax.swing.JRadioButton();
        atrasoRadioButton = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        vencRadioButton = new javax.swing.JRadioButton();
        pgtoRadioButton = new javax.swing.JRadioButton();
        clienteButton = new javax.swing.JRadioButton();
        clientesInativosCheckBox = new javax.swing.JCheckBox();
        responsavelComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PgtoFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N

        jLabel5.setText(bundle.getString("dtVencimento")); // NOI18N

        dtVencimento1Field.setDateFormatString(resourceMap.getString("dtVencimento1Field.dateFormatString")); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N

        dtVencimento2Field.setDateFormatString(resourceMap.getString("dtVencimento2Field.dateFormatString")); // NOI18N

        jLabel7.setText(bundle.getString("dtPgto")); // NOI18N

        dtPgtoIniField.setDateFormatString(resourceMap.getString("dtPgtoIniField.dateFormatString")); // NOI18N

        jLabel9.setText(bundle.getString("para")); // NOI18N

        dtPgtoEndField.setDateFormatString(resourceMap.getString("dtPgtoEndField.dateFormatString")); // NOI18N

        jLabel16.setText(bundle.getString("dtPrevisaoPgto")); // NOI18N

        dtPrevPgtoIniField.setDateFormatString(resourceMap.getString("dtPrevPgtoIniField.dateFormatString")); // NOI18N

        jLabel17.setText(bundle.getString("para")); // NOI18N

        dtPrevPgtoEndField.setDateFormatString(resourceMap.getString("dtPrevPgtoEndField.dateFormatString")); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        jLabel1.setText(bundle.getString("tipoPgto")); // NOI18N

        tipoPgtoField.setColumns(10);
        tipoPgtoField.setDocument(new BoundedPlainDocument(10));

        jLabel18.setText(bundle.getString("dtDesconto")); // NOI18N

        dtDescontoIniField.setDateFormatString(resourceMap.getString("dtDescontoIniField.dateFormatString")); // NOI18N

        jLabel19.setText(bundle.getString("para")); // NOI18N

        dtDescontoEndField.setDateFormatString(resourceMap.getString("dtDescontoEndField.dateFormatString")); // NOI18N

        jLabel10.setText(bundle.getString("cliente")); // NOI18N

        jLabel6.setText(bundle.getString("operador")); // NOI18N

        operadorField.setColumns(2);
        operadorField.setDocument(new BoundedPlainDocument(2));

        jLabel12.setText(bundle.getString("grupoClientes")); // NOI18N

        jLabel13.setText(bundle.getString("representada")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("pagamentos"))); // NOI18N

        situacaoGroup.add(todosRadioButton);
        todosRadioButton.setText(bundle.getString("todos")); // NOI18N
        todosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(pagosRadioButton);
        pagosRadioButton.setText(bundle.getString("pagos")); // NOI18N
        pagosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(naoPagosRadioButton);
        naoPagosRadioButton.setSelected(true);
        naoPagosRadioButton.setText(bundle.getString("naoPagos")); // NOI18N
        naoPagosRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        situacaoGroup.add(atrasoRadioButton);
        atrasoRadioButton.setText(bundle.getString("emAtraso")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(todosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pagosRadioButton)
                .addGap(18, 18, 18)
                .addComponent(naoPagosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(atrasoRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(naoPagosRadioButton)
                    .addComponent(pagosRadioButton)
                    .addComponent(todosRadioButton)
                    .addComponent(atrasoRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N

        orderGroup.add(vencRadioButton);
        vencRadioButton.setSelected(true);
        vencRadioButton.setText(bundle.getString("vencimento")); // NOI18N
        vencRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(pgtoRadioButton);
        pgtoRadioButton.setText(bundle.getString("pagamento")); // NOI18N
        pgtoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(clienteButton);
        clienteButton.setLabel(bundle.getString("cliente")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(vencRadioButton)
                .addGap(18, 18, 18)
                .addComponent(pgtoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clienteButton))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteButton)
                    .addComponent(pgtoRadioButton)
                    .addComponent(vencRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        clientesInativosCheckBox.setLabel(bundle.getString("clientesInativos")); // NOI18N
        clientesInativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesInativosCheckBoxActionPerformed(evt);
            }
        });

        jLabel11.setText(bundle.getString("responsavel")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(191, 191, 191))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(tipoPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtDescontoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)
                                .addGap(10, 10, 10)
                                .addComponent(dtDescontoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addGap(12, 12, 12)
                                .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(dtPgtoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dtPgtoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(dtVencimento1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addGap(10, 10, 10)
                                .addComponent(dtVencimento2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(dtPrevPgtoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel17)
                                        .addGap(10, 10, 10)
                                        .addComponent(dtPrevPgtoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel16)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(responsavelComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 500, Short.MAX_VALUE)
                            .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 500, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clientesInativosCheckBox)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(operadorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(148, 148, 148)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3)
                                            .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                            .addComponent(jLabel9))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(20, 20, 20)
                                            .addComponent(dtPgtoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(dtPgtoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dtVencimento1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dtVencimento2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dtPrevPgtoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel17))
                                    .addComponent(dtPrevPgtoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tipoPgtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(clientesInativosCheckBox)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dtDescontoIniField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19)
                                    .addComponent(dtDescontoEndField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(operadorField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(responsavelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void clientesInativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesInativosCheckBoxActionPerformed
        clienteComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
        //responsavelComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));
}//GEN-LAST:event_clientesInativosCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup atendimentoGroup;
    private javax.swing.JRadioButton atrasoRadioButton;
    private javax.swing.JRadioButton clienteButton;
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JCheckBox clientesInativosCheckBox;
    private com.toedter.calendar.JDateChooser dtDescontoEndField;
    private com.toedter.calendar.JDateChooser dtDescontoIniField;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private com.toedter.calendar.JDateChooser dtPgtoEndField;
    private com.toedter.calendar.JDateChooser dtPgtoIniField;
    private com.toedter.calendar.JDateChooser dtPrevPgtoEndField;
    private com.toedter.calendar.JDateChooser dtPrevPgtoIniField;
    private com.toedter.calendar.JDateChooser dtVencimento1Field;
    private com.toedter.calendar.JDateChooser dtVencimento2Field;
    private javax.swing.JComboBox grupoClienteComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton naoPagosRadioButton;
    private javax.swing.JFormattedTextField operadorField;
    private javax.swing.ButtonGroup orderGroup;
    private javax.swing.JRadioButton pagosRadioButton;
    private javax.swing.JRadioButton pgtoRadioButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox responsavelComboBox;
    private javax.swing.ButtonGroup situacaoGroup;
    private javax.swing.JFormattedTextField tipoPgtoField;
    private javax.swing.JRadioButton todosRadioButton;
    private javax.swing.JRadioButton vencRadioButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables

    public void enableTipoPgto(boolean b) {
        jPanel5.setVisible(b);
        jPanel3.setVisible(b);
    }
}
