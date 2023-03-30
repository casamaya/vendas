/*
 * PgtoComissaoPanel.java
 *
 * Created on 30 de Novembro de 2007, 18:03
 */
package vendas.swing.app.pedido;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingConstants;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.ListComboBoxModel;
import ritual.swing.TApplication;
import vendas.dao.PedidoDao;
import vendas.dao.RepresDao;
import vendas.dao.VendedorDao;
import vendas.entity.AtendimentoPedido;
import vendas.entity.Repres;
import vendas.entity.Vendedor;
import vendas.swing.core.BaseTableModel;
import ritual.swing.ViewFrame;
import ritual.util.DateUtils;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public class PgtoComissaoFrame extends ViewFrame {

    private RepresDao represDao;
    private VendedorDao vendedorDao;
    private PedidoDao pedidoDao;
    private int marcados;

    /** Creates new form PgtoComissaoPanel */
    public PgtoComissaoFrame() {
        super("Pagamento de comissão");
        initComponents();
    }

    private void loadCombos() {
        represComboBox.setModel(new ListComboBoxModel(represDao.findRepresPgto()));
        List lista = new ArrayList();
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(" -- Selecione -- ");
        lista.add(vendedor);
        lista.addAll(vendedorDao.findVendedorPgto());
        vendedorComboBox.setModel(new ListComboBoxModel(lista));

    }

    public void init() {
        loadCombos();
        represComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doRefresh();
            }
        });
        vendedorComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doRefresh();
            }
        });
        atendTable.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        atendTable.setDefaultRenderer(Date.class, new DateCellRenderer());
        TApplication app = TApplication.getInstance();
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao)app.lookupService("vendedorDao");
            try {
            vendedorComboBox.setSelectedItem(dao.findById(Vendedor.class, app.getUser().getIdvendedor()));
            vendedorComboBox.setEnabled(false);
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        doRefresh();
    }

    private void doRefresh() {
        Repres repres = (Repres) represComboBox.getSelectedItem();
        Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
        List lista = pedidoDao.findAPagar(repres, vendedor);
        atendTable.setModel(new ComissaoModel(lista));
        atendTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        saldoField.setValue(repres.getTotal());
        disponivelField.setValue(repres.getTotal().doubleValue());
        pgtoButton.setEnabled(false);
        marcados = 0;
        atualiza();
    }

    private void marcar(AtendimentoPedido nota, boolean value) {
        String s = DateUtils.format(DateUtils.getDate());
        Date d = null;
        try {
            d = DateUtils.parse(s);
        } catch (Exception e) {
        }
        if (value) {
            if (nota.getDtPgtoComissao() == null) {
            nota.setDtPgtoComissao(d);
            nota.setRecibo("1");
            marcados++;
            }
        } else {
            nota.setDtPgtoComissao(null);
            nota.setRecibo("0");
            if (marcados > 0) {
                marcados--;
            }
        }
    }

    private void marcarAll() {
        ComissaoModel model = (ComissaoModel) atendTable.getModel();
        List<AtendimentoPedido> atendimentos = model.getItemList();
        double disponivel = (Double) disponivelField.getValue();
        for (AtendimentoPedido nota : atendimentos) {
            if (disponivel >= nota.getComissaoTotal().doubleValue()) {
                disponivel = disponivel - nota.getComissaoTotal().doubleValue();
                marcar(nota, true);
            }
        }
        pgtoButton.setEnabled(marcados > 0);
        disponivelField.setValue(disponivel);
        model.fireTableDataChanged();
    }

    @Override
    public void refresh() {
        loadCombos();
        doRefresh();
    }

    private void atualiza() {
        ComissaoModel model = (ComissaoModel) atendTable.getModel();
        List<AtendimentoPedido> atendimentos = model.getItemList();
        double disponivel = 0;
        for (AtendimentoPedido nota : atendimentos) {
                disponivel += nota.getComissaoTotal().doubleValue();
        }
        restanteField.setValue(disponivel);
    }

    private void pagar() {
        Repres repres = (Repres) represComboBox.getSelectedItem();
        double disponivel = (Double) disponivelField.getValue();
        BigDecimal value = new BigDecimal(disponivel);
        repres.setTotal(value);

        ComissaoModel model = (ComissaoModel) atendTable.getModel();
        List<AtendimentoPedido> atendimentos = model.getItemList();
        try {
            pedidoDao.updateAtendimentos(atendimentos, repres);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
        doRefresh();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        selectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        atendTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        pgtoButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        saldoField = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        disponivelField = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        restanteField = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setName("Form"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        jLabel1.setText(bundle.getString("representada")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        represComboBox.setName("represComboBox"); // NOI18N

        jLabel2.setText(bundle.getString("vendedor")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        selectButton.setText(bundle.getString("marcar")); // NOI18N
        selectButton.setName("selectButton"); // NOI18N
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectButton))
                    .addComponent(jLabel2))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectButton)
                    .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        atendTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        atendTable.setName("atendTable"); // NOI18N
        jScrollPane1.setViewportView(atendTable);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        pgtoButton.setText(bundle.getString("pagar")); // NOI18N
        pgtoButton.setEnabled(false);
        pgtoButton.setName("pgtoButton"); // NOI18N
        pgtoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pgtoButtonActionPerformed(evt);
            }
        });

        jLabel3.setText(bundle.getString("saldoFornecedor")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        saldoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldoField.setFocusable(false);
        saldoField.setName("saldoField"); // NOI18N

        jLabel4.setText(bundle.getString("disponivel")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        disponivelField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        disponivelField.setFocusable(false);
        disponivelField.setName("disponivelField"); // NOI18N

        jLabel5.setText(bundle.getString("saldo")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        restanteField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        restanteField.setFocusable(false);
        restanteField.setName("restanteField"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldoField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(disponivelField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(restanteField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
                .addComponent(pgtoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pgtoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(saldoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(disponivelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(restanteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 937, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        marcarAll();
    }//GEN-LAST:event_selectButtonActionPerformed

    private void pgtoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pgtoButtonActionPerformed
        if (Messages.confirmQuestion(getBundle().getString("confirmarPgto"))) {
            pagar();
        }
    }//GEN-LAST:event_pgtoButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable atendTable;
    private javax.swing.JFormattedTextField disponivelField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pgtoButton;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JFormattedTextField restanteField;
    private javax.swing.JFormattedTextField saldoField;
    private javax.swing.JButton selectButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables

    public RepresDao getRepresDao() {
        return represDao;
    }

    public void setRepresDao(RepresDao represDao) {
        this.represDao = represDao;
    }

    public VendedorDao getVendedorDao() {
        return vendedorDao;
    }

    public void setVendedorDao(VendedorDao vendedorDao) {
        this.vendedorDao = vendedorDao;
    }

    public PedidoDao getPedidoDao() {
        return pedidoDao;
    }

    public void setPedidoDao(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    private class ComissaoModel extends BaseTableModel {

        final int PAGAR = 0;
        final int PEDIDO = 1;
        final int DTPEDIDO = 2;
        final int VALOR = 3;
        final int CLIENTE = 4;
        final int NOTA = 5;
        final int DATANF = 6;
        final int VALORNF = 7;
        final int COMVENDEDOR = 8;
        final int COMTOTAL = 9;

        public ComissaoModel(List values) {
            super(values);
        }

        @Override
        public void setColumns() {
            addColumn("Pagar");
            addColumn("Pedido");
            addColumn("Dt. pedido");
            addColumn("Valor");
            addColumn("Cliente");
            addColumn("Nota");
            addColumn("Data nota");
            addColumn("Valor nota");
            addColumn("Com. vendedor");
            addColumn("Com. total");
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == PAGAR) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Class getColumnClass(int column) {
            Class aClass = null;
            switch (column) {
                case PAGAR:
                    aClass = Boolean.class;
                    break;
                case PEDIDO:
                    aClass = Integer.class;
                    break;
                case DTPEDIDO:
                    aClass = Date.class;
                    break;
                case VALOR:
                    aClass = BigDecimal.class;
                    break;
                case CLIENTE:
                    aClass = String.class;
                    break;
                case NOTA:
                    aClass = String.class;
                    break;
                case DATANF:
                    aClass = Date.class;
                    break;
                case VALORNF:
                    aClass = BigDecimal.class;
                    break;
                case COMVENDEDOR:
                    aClass = BigDecimal.class;
                    break;
                case COMTOTAL:
                    aClass = BigDecimal.class;
                    break;
            }
            return aClass;
        }

        @Override
        public Object getValueAt(int row, int col) {
            AtendimentoPedido item = (AtendimentoPedido) getObject(row);
            Object obj = null;
            switch (col) {
                case PAGAR:
                    obj = new Boolean(item.getDtPgtoComissao() != null);
                    break;
                case PEDIDO:
                    obj = item.getPedido().getIdPedido();
                    break;
                case DTPEDIDO:
                    obj = item.getPedido().getDtPedido();
                    break;
                case VALOR:
                    obj = item.getPedido().getValor();
                    break;
                case CLIENTE:
                    obj = item.getPedido().getCliente().getRazao();
                    break;
                case NOTA:
                    obj = item.getAtendimentoPedidoPK().getNf();
                    break;
                case DATANF:
                    obj = item.getDtNota();
                    break;
                case VALORNF:
                    obj = item.getValor();
                    break;
                case COMVENDEDOR:
                    obj = item.getComissaoVendedor();
                    break;
                case COMTOTAL:
                    obj = item.getValorComissao();
                    break;
            }
            return obj;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            AtendimentoPedido item = (AtendimentoPedido) getObject(row);
            if (column == 0) {
                double disponivel = (Double) disponivelField.getValue();
                if ((Boolean) aValue) {
                    if (disponivel >= item.getComissaoTotal().doubleValue()) {
                        marcar(item, true);
                        disponivel = disponivel - item.getComissaoTotal().doubleValue();
                        disponivelField.setValue(disponivel);
                    }
                } else {
                    marcar(item, false);
                    disponivel = disponivel + item.getComissaoTotal().doubleValue();
                    disponivelField.setValue(disponivel);
                }
                pgtoButton.setEnabled(marcados > 0);
                fireTableDataChanged();
            }
        }
    }
}
