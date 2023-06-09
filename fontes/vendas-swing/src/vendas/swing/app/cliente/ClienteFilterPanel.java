/*
 * ClienteFilterPanel.java
 *
 * Created on 15 de Julho de 2007, 12:36
 */
package vendas.swing.app.cliente;

import vendas.entity.SituacaoCliente;
import vendas.entity.Vendedor;
import vendas.swing.core.EditPanel;
import ritual.swing.BoundedPlainDocument;
import ritual.swing.TApplication;
import vendas.beans.ClientesFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.GrupoCliente;
import vendas.entity.Repres;
import vendas.entity.RoteiroVendedor;
import vendas.entity.SegMercado;
import vendas.util.Constants;
import vendas.util.VendasUtil;

/**
 *
 * @author  Sam
 */
public class ClienteFilterPanel extends EditPanel {

    /** Creates new form ClienteFilterPanel */
    public ClienteFilterPanel() {
        initComponents();
    }

    public void setOrderPanel(boolean value) {
        jPanel1.setVisible(value);
    }

    @Override
    public void init() {
        vendedorComboBox.setModel(VendasUtil.getVendedoresListModel());
        situacaoComboBox.setModel(VendasUtil.getSitClienteListModel());
        roteiroComboBox.setModel(VendasUtil.getRoteiroListModel());
        segmentoComboBox.setModel(VendasUtil.getSegMercadoListModel());
        represComboBox.setModel(VendasUtil.getRepresListModel(true));
        grupoClienteComboBox.setModel(VendasUtil.getGrupoClienteListModel());
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
        ClientesFilter clienteFilter = (ClientesFilter) obj;
        clienteFilter.setTitle(null);
        Cliente cliente = clienteFilter.getCliente();
        cliente.setCidade(cidadeField.getText());
        cliente.setCnpj(cnpjField.getText());
        cliente.setRazao(razaoField.getText());
        cliente.setBairro(bairroField.getText());
        cliente.setUf(ufField.getText());
        cliente.setBloqueado(bloqueadoCheckBox.isSelected());
        SituacaoCliente situacao = (SituacaoCliente) situacaoComboBox.getModel().getSelectedItem();
        Repres repres = (Repres) represComboBox.getModel().getSelectedItem();
        
        if (clienteOrderButton.isSelected()) {
            clienteFilter.setOrder(0);
        }
        if (cidadeOrderButton.isSelected()) {
            clienteFilter.setOrder(1);
        }
        if (vendedorOrderButton.isSelected()) {
            clienteFilter.setOrder(2);
        }
        if (bairroOrderButton.isSelected()) {
            clienteFilter.setOrder(3);
        }
        if (pedidoCheckBox.isSelected()) {
            situacao.setPedido("1");
        } else {
            situacao.setPedido("T");
        }

        clienteFilter.setEntregarBoleto(entregarBoletoCheckBox.isSelected());

        cliente.setSituacaoCliente(situacao);
        clienteFilter.setRepres(repres);
        Vendedor vendedor = (Vendedor) vendedorComboBox.getModel().getSelectedItem();
        clienteFilter.setVendedor(vendedor);
        clienteFilter.setRoteiro((RoteiroVendedor) roteiroComboBox.getModel().getSelectedItem());
        clienteFilter.setSegmento((SegMercado) segmentoComboBox.getModel().getSelectedItem());
        clienteFilter.setGrupoCliente((GrupoCliente) grupoClienteComboBox.getModel().getSelectedItem());
        
        if (clienteFilter.getGrupoCliente().getIdGrupoCliente() != null) {
            clienteFilter.setTitle("Grupo de cliente :" + clienteFilter.getGrupoCliente().getNomeGrupo());
        }
        
        if (vendedor.getIdVendedor() != null) {
            clienteFilter.setTitle("Vendedor :" + vendedor.getNome());
        }
        
        if (clienteFilter.getRoteiro().getIdRoteiro() != null) {
            clienteFilter.setTitle("Roteiro :" + clienteFilter.getRoteiro().getDescricao());
        }
        
        if (clienteFilter.getRoteiro().getIdRoteiro() != null) {
            clienteFilter.setTitle("Roteiro :" + clienteFilter.getRoteiro().getDescricao());
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
        razaoLabel = new javax.swing.JLabel();
        razaoField = new javax.swing.JFormattedTextField();
        cidadeLabel = new javax.swing.JLabel();
        cidadeField = new javax.swing.JFormattedTextField();
        cidadeLabel1 = new javax.swing.JLabel();
        bairroField = new javax.swing.JFormattedTextField();
        cidadeLabel2 = new javax.swing.JLabel();
        ufField = new javax.swing.JFormattedTextField();
        vendedorLabel = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        roteiroLabel = new javax.swing.JLabel();
        roteiroComboBox = new javax.swing.JComboBox();
        cnpjLabel = new javax.swing.JLabel();
        cnpjField = new javax.swing.JFormattedTextField();
        segmentoLabel = new javax.swing.JLabel();
        segmentoComboBox = new javax.swing.JComboBox();
        segmentoLabel1 = new javax.swing.JLabel();
        grupoClienteComboBox = new javax.swing.JComboBox();
        represLabel = new javax.swing.JLabel();
        represComboBox = new javax.swing.JComboBox();
        situacaoLabel = new javax.swing.JLabel();
        situacaoComboBox = new javax.swing.JComboBox();
        pedidoCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        clienteOrderButton = new javax.swing.JRadioButton();
        cidadeOrderButton = new javax.swing.JRadioButton();
        vendedorOrderButton = new javax.swing.JRadioButton();
        bairroOrderButton = new javax.swing.JRadioButton();
        entregarBoletoCheckBox = new javax.swing.JCheckBox();
        bloqueadoCheckBox = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        razaoLabel.setText(bundle.getString("razaoSocial")); // NOI18N

        razaoField.setDocument(new BoundedPlainDocument(50));

        cidadeLabel.setText(bundle.getString("cidade")); // NOI18N

        cidadeField.setColumns(30);
        cidadeField.setDocument(new BoundedPlainDocument(30));

        cidadeLabel1.setText(bundle.getString("bairro")); // NOI18N

        bairroField.setColumns(30);
        bairroField.setDocument(new BoundedPlainDocument(30));

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/swing/app/cliente/Bundle"); // NOI18N
        cidadeLabel2.setText(bundle1.getString("ClienteFilterPanel.cidadeLabel2.text")); // NOI18N

        ufField.setColumns(2);
        ufField.setDocument(new BoundedPlainDocument(2));

        vendedorLabel.setText(bundle.getString("vendedor")); // NOI18N

        roteiroLabel.setText(bundle.getString("roteiro")); // NOI18N

        cnpjLabel.setText(bundle.getString("cnpj")); // NOI18N

        segmentoLabel.setText(bundle.getString("segmento")); // NOI18N

        segmentoLabel1.setText(bundle.getString("grupoCliente")); // NOI18N

        represLabel.setText(bundle.getString("fornecedor")); // NOI18N

        situacaoLabel.setText(bundle.getString("situacao")); // NOI18N

        pedidoCheckBox.setSelected(true);
        pedidoCheckBox.setText(bundle.getString("pedidoHabilitado")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ordem"))); // NOI18N

        buttonGroup1.add(clienteOrderButton);
        clienteOrderButton.setSelected(true);
        clienteOrderButton.setText(bundle.getString("cliente")); // NOI18N

        buttonGroup1.add(cidadeOrderButton);
        cidadeOrderButton.setText(bundle.getString("bairroCliente")); // NOI18N

        buttonGroup1.add(vendedorOrderButton);
        vendedorOrderButton.setText(bundle.getString("bairroVendedorCliente")); // NOI18N

        bairroOrderButton.setText(bundle1.getString("ClienteFilterPanel.bairroOrderButton.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(clienteOrderButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cidadeOrderButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(vendedorOrderButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bairroOrderButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteOrderButton)
                    .addComponent(cidadeOrderButton)
                    .addComponent(vendedorOrderButton)
                    .addComponent(bairroOrderButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        entregarBoletoCheckBox.setText(bundle.getString("entregarBoleto")); // NOI18N

        bloqueadoCheckBox.setText(bundle1.getString("ClienteFilterPanel.bloqueadoCheckBox.text")); // NOI18N
        bloqueadoCheckBox.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(vendedorComboBox, 0, 174, Short.MAX_VALUE)
                                .addComponent(vendedorLabel)
                                .addComponent(cidadeField, 0, 0, Short.MAX_VALUE))
                            .addComponent(cidadeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(roteiroComboBox, 0, 174, Short.MAX_VALUE)
                                    .addComponent(roteiroLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(cnpjLabel))
                                    .addComponent(cnpjField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(cidadeLabel1)
                                        .addGap(131, 131, 131))
                                    .addComponent(bairroField, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cidadeLabel2)
                                    .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(213, 213, 213))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(razaoLabel)
                            .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(represLabel)
                            .addComponent(situacaoLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(situacaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pedidoCheckBox))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(represComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(segmentoComboBox, 0, 174, Short.MAX_VALUE)
                                        .addComponent(segmentoLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(segmentoLabel1)
                                        .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(186, 186, 186)
                                    .addComponent(entregarBoletoCheckBox)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bloqueadoCheckBox))
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(razaoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(razaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cidadeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cidadeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cidadeLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bairroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cidadeLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ufField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(vendedorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(roteiroLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roteiroComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cnpjLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cnpjField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(segmentoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(segmentoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(segmentoLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grupoClienteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(represLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(represComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(situacaoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(situacaoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pedidoCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(entregarBoletoCheckBox)
                    .addComponent(bloqueadoCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField bairroField;
    private javax.swing.JRadioButton bairroOrderButton;
    private javax.swing.JCheckBox bloqueadoCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField cidadeField;
    private javax.swing.JLabel cidadeLabel;
    private javax.swing.JLabel cidadeLabel1;
    private javax.swing.JLabel cidadeLabel2;
    private javax.swing.JRadioButton cidadeOrderButton;
    private javax.swing.JRadioButton clienteOrderButton;
    private javax.swing.JFormattedTextField cnpjField;
    private javax.swing.JLabel cnpjLabel;
    private javax.swing.JCheckBox entregarBoletoCheckBox;
    private javax.swing.JComboBox grupoClienteComboBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox pedidoCheckBox;
    private javax.swing.JFormattedTextField razaoField;
    private javax.swing.JLabel razaoLabel;
    private javax.swing.JComboBox represComboBox;
    private javax.swing.JLabel represLabel;
    private javax.swing.JComboBox roteiroComboBox;
    private javax.swing.JLabel roteiroLabel;
    private javax.swing.JComboBox segmentoComboBox;
    private javax.swing.JLabel segmentoLabel;
    private javax.swing.JLabel segmentoLabel1;
    private javax.swing.JComboBox situacaoComboBox;
    private javax.swing.JLabel situacaoLabel;
    private javax.swing.JFormattedTextField ufField;
    private javax.swing.JComboBox vendedorComboBox;
    private javax.swing.JLabel vendedorLabel;
    private javax.swing.JRadioButton vendedorOrderButton;
    // End of variables declaration//GEN-END:variables
}
