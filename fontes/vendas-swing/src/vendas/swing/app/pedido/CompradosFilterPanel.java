/*
 * PedidoFilterPanel.java
 *
 * Created on 15 de Julho de 2007, 12:36
 */
package vendas.swing.app.pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import vendas.dao.VendedorDao;
import vendas.entity.RoteiroVendedor;
import vendas.entity.SubGrupoProduto;
import vendas.swing.core.BaseTableModel;
import vendas.util.Constants;
import vendas.util.StringUtils;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class CompradosFilterPanel extends EditPanel {

    /** Creates new form PedidoFilterPanel */
    public CompradosFilterPanel() {
        initComponents();

    }

    @Override
    public void init() {
        try {
            clienteComboBox.setModel(VendasUtil.getClienteListModel());
            respComboBox.setModel(VendasUtil.getClienteListModel());
            represComboBox.setModel(VendasUtil.getRepresListModel(true));
            grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
            vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
            grupoComboBox.setModel(VendasUtil.getGrupoProdutoListModel());
            roteiroComboBox.setModel(VendasUtil.getRoteiroListModel());
            SubGrupoModel model = new SubGrupoModel(null);
            atendTable.setModel(model);
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
        pedido.setCidade(cidadeField.getText());

//        pedido.setProdutosInativos(produtoInativoCheckBox.isSelected());

        if (dtPedido1Field.getDate() != null) {
            try {
                pedido.setDtEmissaoIni(DateUtils.parse(DateUtils.format(dtPedido1Field.getDate())));
                pedido.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(dtPedido2Field.getDate())));
            } catch (Exception e) {
            }

            title.append(getBundle().getString("emitidos"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEmissaoIni()));
            title.append(" ");
            title.append(getBundle().getString("para"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEmissaoEnd()));
            title.append(". ");
        }
        //DateUtils.format(null)
        if (dtEntrega1Field.getDate() != null) {
            try {
                pedido.setDtEntregaIni(DateUtils.parse(DateUtils.format(dtEntrega1Field.getDate())));
                pedido.setDtEntregaEnd(DateUtils.parse(DateUtils.format(dtEntrega2Field.getDate())));
            } catch (Exception e) {
            }

            title.append(getBundle().getString("entregas"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEntregaIni()));
            title.append(" ");
            title.append(getBundle().getString("para"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtEntregaEnd()));
            title.append(". ");
        }
        if (dtNota1Field.getDate() != null) {
            try {
                pedido.setDtNotaIni(DateUtils.parse(DateUtils.format(dtNota1Field.getDate())));
                pedido.setDtNotaEnd(DateUtils.parse(DateUtils.format(dtNota2Field.getDate())));
            } catch (Exception e) {
            }
            title.append(getBundle().getString("notasEmitidas"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtNotaIni()));
            title.append(" ");
            title.append(getBundle().getString("para"));
            title.append(" ");
            title.append(DateUtils.format(pedido.getDtNotaEnd()));
            title.append(". ");
        }
        pedido.setGrupo((GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem());
        if ((pedido.getGrupo() != null) && (pedido.getGrupo().getIdGrupoCliente() != null)) {
            title.append("Grupo ").append(pedido.getGrupo().getNomeGrupo()).append(". ");
        }
        pedido.setGrupoProduto((GrupoProduto) grupoComboBox.getModel().getSelectedItem());
        
        if (pedido.getGrupoProduto() != null && pedido.getGrupoProduto().getIdCodGrupo() != null) {
            title.append("Grupo produto: ").append(pedido.getGrupoProduto().getNomeGrupo()).append(". ");
        }
        SubGrupoModel model = (SubGrupoModel) atendTable.getModel();
        List<SubGrupoItem> subgrupos = model.getItemList();
        
        if ((subgrupos != null) && (!subgrupos.isEmpty())) {
            //title.append("Sub-grupo: ").append(subGrupo.getNomeGrupo()).append(". ");
            List<Integer> itens = new ArrayList<>();
            List<String> sbg = new ArrayList<>();
            
            for (SubGrupoItem item : subgrupos) {
                if (item.getMarcado()) {
                    itens.add(item.getSubGrupo().getIdCodSubGrupo());
                    sbg.add(item.getSubGrupo().getNomeGrupo());
                }
            }
            if (!itens.isEmpty()) {
                title.append("Subgrupo: ").append(StringUtils.listAsString(sbg)).append(". ");
                pedido.setSubGrupos(itens);
            }
        }
        if (produtoRadioButton.isSelected()) {
            pedido.setOrdem(0);
        } else if (entregaRadioButton.isSelected()) {
            pedido.setOrdem(1);
        } else if (pedidoRadioButton.isSelected()) {
            pedido.setOrdem(2);
        }

        pedido.setPrecoCliente(precoClienteCheckBox.isSelected());

        if (produtoField.getValue() != null) {
            pedido.getProduto().setIdProduto(Integer.decode(produtoField.getValue().toString()));
        }
        pedido.setRepres((Repres) represComboBox.getModel().getSelectedItem());
        pedido.setCliente((Cliente) clienteComboBox.getModel().getSelectedItem());
        pedido.setResponsavel((Cliente) respComboBox.getModel().getSelectedItem());
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
        }
        RoteiroVendedor roteiro = (RoteiroVendedor) roteiroComboBox.getModel().getSelectedItem();
        if (roteiro.getIdRoteiro() != null) {
            title.append("Roteiro ").append(roteiro.getDescricao());
            title.append(". ");
        }
        pedido.setRoteiro(roteiro);
        pedido.setTitle(title.toString());
    }
    
    private class SubGrupoItem {
        private Boolean marcado;
        private SubGrupoProduto subGrupo;

        public Boolean getMarcado() {
            return marcado;
        }

        public void setMarcado(Boolean marcado) {
            this.marcado = marcado;
        }

        public SubGrupoProduto getSubGrupo() {
            return subGrupo;
        }

        public void setSubGrupo(SubGrupoProduto subGrupo) {
            this.subGrupo = subGrupo;
        }
    }
    
    private class SubGrupoModel extends BaseTableModel {
        final int MARCAR = 0;
        final int SUBGRUPO = 1;
        
        public SubGrupoModel(List values) {
            super(values);
        }

        @Override
        public void setColumns() {
            addColumn("Marcar");
            addColumn("Sub grupo");
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return (column == MARCAR);
        }

        @Override
        public Class getColumnClass(int column) {
            Class aClass;
            
            if (column == MARCAR) {
                aClass = Boolean.class;
            } else {
                aClass = String.class;
            }
            
            return aClass;
        }

        @Override
        public Object getValueAt(int row, int col) {
            SubGrupoItem item = (SubGrupoItem) getObject(row);
            Object obj = null;
            
            if (col == MARCAR) {
                obj = item.getMarcado();
            } else {
                obj = item.getSubGrupo().getNomeGrupo();
            }
            
            return obj;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            SubGrupoItem item = (SubGrupoItem) getObject(row);
            
            if (column == MARCAR) {
                item.setMarcado((Boolean) aValue);
                fireTableDataChanged();
            }
        }
    }
    
    private class SubGrupoComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        SubGrupoProduto grupo1 = (SubGrupoProduto) obj1;
        SubGrupoProduto grupo2 = (SubGrupoProduto) obj2;

        return grupo1.getIdCodSubGrupo().compareTo(grupo2.getIdCodSubGrupo());
    }
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        dtPedido1Field = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dtPedido2Field = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        todosRadioButton = new javax.swing.JRadioButton();
        atendidosRadioButton = new javax.swing.JRadioButton();
        naoAtendRadioButton = new javax.swing.JRadioButton();
        dtEntrega1Field = new com.toedter.calendar.JDateChooser();
        dtEntrega2Field = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        produtoField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        grupoComboBox = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        atendTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        clienteComboBox = new javax.swing.JComboBox();
        clientesInativosCheckBox = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        roteiroComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        respComboBox = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        dtNota1Field = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        dtNota2Field = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        inativosCheckBox = new javax.swing.JCheckBox();
        represComboBox = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        produtoRadioButton = new javax.swing.JRadioButton();
        entregaRadioButton = new javax.swing.JRadioButton();
        pedidoRadioButton = new javax.swing.JRadioButton();
        precoClienteCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("pedido"))); // NOI18N

        jLabel2.setText(bundle.getString("dtEmissao")); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(CompradosFilterPanel.class);
        dtPedido1Field.setDateFormatString(resourceMap.getString("dtPedido1Field.dateFormatString")); // NOI18N

        jLabel3.setText(bundle.getString("para")); // NOI18N

        dtPedido2Field.setDateFormatString(resourceMap.getString("dtPedido2Field.dateFormatString")); // NOI18N

        jLabel5.setText(bundle.getString("dtEntrega")); // NOI18N

        jLabel4.setText(bundle.getString("para")); // NOI18N

        jLabel8.setText(bundle.getString("vendedor")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("situacaoItens"))); // NOI18N

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(todosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(atendidosRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(naoAtendRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(todosRadioButton)
                .addComponent(atendidosRadioButton)
                .addComponent(naoAtendRadioButton))
        );

        dtEntrega1Field.setDateFormatString(resourceMap.getString("dtEntrega1Field.dateFormatString")); // NOI18N

        dtEntrega2Field.setDateFormatString(resourceMap.getString("dtEntrega2Field.dateFormatString")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dtPedido1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dtEntrega2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dtPedido2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dtEntrega1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Produtos"));

        jLabel17.setText(bundle.getString("produto")); // NOI18N

        produtoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel18.setText(bundle.getString("grupoProduto")); // NOI18N

        grupoComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grupoComboBoxActionPerformed(evt);
            }
        });

        jLabel20.setText(bundle.getString("subgrupo")); // NOI18N

        atendTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(atendTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(produtoField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grupoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(produtoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("cliente"))); // NOI18N

        jLabel10.setText(bundle.getString("razaoSocial")); // NOI18N

        clientesInativosCheckBox.setText(bundle.getString("clientesInativos")); // NOI18N
        clientesInativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesInativosCheckBoxActionPerformed(evt);
            }
        });

        jLabel16.setText(bundle.getString("cidade")); // NOI18N

        cidadeField.setDocument(new BoundedPlainDocument(30));

        jLabel12.setText(bundle.getString("grupoCliente")); // NOI18N

        jLabel19.setText(bundle.getString("roteiro")); // NOI18N

        jLabel11.setText(bundle.getString("responsavel")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roteiroComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 353, Short.MAX_VALUE)
                    .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 353, Short.MAX_VALUE)
                    .addComponent(respComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 353, Short.MAX_VALUE)
                    .addComponent(cidadeField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                    .addComponent(clienteComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 353, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clientesInativosCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(clientesInativosCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(respComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roteiroComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("atendimento"))); // NOI18N

        jLabel14.setText(bundle.getString("dtNota")); // NOI18N

        dtNota1Field.setDateFormatString(resourceMap.getString("dtNota1Field.dateFormatString")); // NOI18N

        jLabel15.setText(bundle.getString("para")); // NOI18N

        dtNota2Field.setDateFormatString(resourceMap.getString("dtNota2Field.dateFormatString")); // NOI18N

        jLabel13.setText(bundle.getString("representada")); // NOI18N

        inativosCheckBox.setText(bundle.getString("mostrarInativas")); // NOI18N
        inativosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inativosCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtNota2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(inativosCheckBox))
                    .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(inativosCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtNota1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dtNota2Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N

        orderGroup.add(produtoRadioButton);
        produtoRadioButton.setSelected(true);
        produtoRadioButton.setText(bundle.getString("produto")); // NOI18N
        produtoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(entregaRadioButton);
        entregaRadioButton.setText(bundle.getString("dtEntrega")); // NOI18N
        entregaRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        orderGroup.add(pedidoRadioButton);
        pedidoRadioButton.setText(bundle.getString("pedido")); // NOI18N
        pedidoRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(produtoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entregaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pedidoRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(produtoRadioButton)
                .addComponent(entregaRadioButton)
                .addComponent(pedidoRadioButton))
        );

        precoClienteCheckBox.setText(bundle.getString("precoCliente")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(precoClienteCheckBox)))
                .addGap(4, 4, 4))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(precoClienteCheckBox)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inativosCheckBoxActionPerformed
        represComboBox.setModel(VendasUtil.getRepresListModel(!inativosCheckBox.isSelected()));
}//GEN-LAST:event_inativosCheckBoxActionPerformed

    private void clientesInativosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesInativosCheckBoxActionPerformed
        clienteComboBox.setModel(VendasUtil.getClienteListModel(clientesInativosCheckBox.isSelected()));

    }//GEN-LAST:event_clientesInativosCheckBoxActionPerformed

    private void grupoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grupoComboBoxActionPerformed
        GrupoProduto grupo = (GrupoProduto) grupoComboBox.getModel().getSelectedItem();
        List<SubGrupoProduto> itens = (List)grupo.getSubGrupoProdutoList();
        Collections.sort(itens, new SubGrupoComparator());
        List<SubGrupoItem> sgi = new ArrayList<>();
        SubGrupoItem value;
        
        for (SubGrupoProduto item : itens) {
            value = new SubGrupoItem();
            value.setMarcado(Boolean.FALSE);
            value.setSubGrupo(item);
            sgi.add(value);
        }
        SubGrupoModel model = new SubGrupoModel(sgi);
        atendTable.setModel(model);
        atendTable.getColumnModel().getColumn(1).setWidth(130);
        model.fireTableDataChanged();
    }//GEN-LAST:event_grupoComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable atendTable;
    private javax.swing.JRadioButton atendidosRadioButton;
    private javax.swing.ButtonGroup atendimentoGroup;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JComboBox clienteComboBox;
    private javax.swing.JCheckBox clientesInativosCheckBox;
    private com.toedter.calendar.JDateChooser dtEntrega1Field;
    private com.toedter.calendar.JDateChooser dtEntrega2Field;
    private com.toedter.calendar.JDateChooser dtNota1Field;
    private com.toedter.calendar.JDateChooser dtNota2Field;
    private com.toedter.calendar.JDateChooser dtPedido1Field;
    private com.toedter.calendar.JDateChooser dtPedido2Field;
    private javax.swing.JRadioButton entregaRadioButton;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton naoAtendRadioButton;
    private javax.swing.ButtonGroup orderGroup;
    private javax.swing.JRadioButton pedidoRadioButton;
    private javax.swing.JCheckBox precoClienteCheckBox;
    private javax.swing.JFormattedTextField produtoField;
    private javax.swing.JRadioButton produtoRadioButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JComboBox respComboBox;
    private javax.swing.JComboBox roteiroComboBox;
    private javax.swing.ButtonGroup situacaoGroup;
    private javax.swing.JRadioButton todosRadioButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables
}
