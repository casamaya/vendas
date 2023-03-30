/*
 * NewJInternalFrame.java
 *
 * Created on 15 de Dezembro de 2007, 13:47
 */
package vendas.swing.app.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.TableColumn;
import ritual.swing.ListComboBoxModel;
import ritual.swing.TApplication;
import vendas.dao.ClienteDao;
import vendas.dao.RoteiroDao;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.RoteiroVendedor;
import vendas.entity.Vendedor;
import vendas.swing.app.auxiliar.RoteiroEditPanel;
import ritual.swing.ViewFrame;
import vendas.swing.model.ClienteGrupoModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public class RoteiroClienteFrame extends ViewFrame {

    private ClienteDao clienteDao;
    private VendedorDao vendedorDao;
    private RoteiroDao roteiroDao;

    /** Creates new form NewJInternalFrame */
    public RoteiroClienteFrame() {
        super();
        setTitle(getBundle().getString("roteiros"));
        initComponents();
    }

    public void init() {
        getLogger().info("init");

        vendedorComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadRoteiro();
                updateClientes();
            }
        });
        roteiroComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadClientesRoteiros();
                updateClientes();
            }
        });
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

        refresh();

    }

    private void loadRoteiro() {
        getLogger().info("loadRoteiro");
        Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
        List roteiros = (List) vendedor.getRoteiros();

        try {
            roteiroComboBox.setModel(new ListComboBoxModel(roteiros));
            if (roteiros.size() > 0) {
                roteiroComboBox.setSelectedIndex(0);
            }
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    private void loadClientesRoteiros() {
        RoteiroVendedor roteiro = (RoteiroVendedor) roteiroComboBox.getSelectedItem();
        List clientes = (List)roteiro.getClientes();
        
        if (clientes == null || clientes.isEmpty())
            clientes = new ArrayList();
        else
            Collections.sort(clientes, new NomeClienteComparator());
        
        ClienteGrupoModel clienteGrupoModel = new ClienteGrupoModel((List) clientes);
        clienteRoteiroTable.setModel(clienteGrupoModel);
        TableColumn col = clienteRoteiroTable.getColumnModel().getColumn(0);
        col.setPreferredWidth(200);
        clienteGrupoModel.fireTableDataChanged();
    }

    public RoteiroDao getRoteiroDao() {
        return roteiroDao;
    }

    public void setRoteiroDao(RoteiroDao roteiroDao) {
        this.roteiroDao = roteiroDao;
    }

    @Override
    public void refresh() {
        getLogger().info("doRefresh");
        try {
            vendedorComboBox.setModel(new ListComboBoxModel(vendedorDao.findAll()));
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
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        loadRoteiro();
        updateClientes();
    }

    private void updateClientes() {
        getLogger().info("updateClientes");
        RoteiroVendedor roteiro = (RoteiroVendedor) roteiroComboBox.getSelectedItem();
        if (roteiro != null) {
            List lista = (List) roteiroDao.findNotInRoteiro(roteiro);
            Collections.sort(lista, new NomeClienteComparator());
            ClienteGrupoModel clientesModel = new ClienteGrupoModel(lista);
            clientesTable.setModel(clientesModel);
            TableColumn col = clientesTable.getColumnModel().getColumn(0);
            col.setPreferredWidth(200);
            clientesModel.fireTableDataChanged();
        } else {
            List lista = new ArrayList();
            ClienteGrupoModel clientesModel = new ClienteGrupoModel(lista);
            clientesTable.setModel(clientesModel);
            clientesModel.fireTableDataChanged();
        }
    }

    @Override
    public void report() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        vendedorComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        roteiroComboBox = new javax.swing.JComboBox();
        addButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();
        clientesPanel = new javax.swing.JPanel();
        agrupadosPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        clienteRoteiroTable = new javax.swing.JTable();
        downButton = new javax.swing.JButton();
        disponiveisPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        clientesTable = new javax.swing.JTable();
        upButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMinimumSize(new java.awt.Dimension(668, 473));
        setName("Form"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        grupoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("roteiros"))); // NOI18N
        grupoPanel.setName("grupoPanel"); // NOI18N

        jLabel1.setText(bundle.getString("vendedor")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        vendedorComboBox.setName("vendedorComboBox"); // NOI18N

        jLabel2.setText(bundle.getString("roteiro")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        roteiroComboBox.setName("roteiroComboBox"); // NOI18N

        addButton.setText(bundle.getString("add")); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("vendas/swing/app/cliente/Bundle"); // NOI18N
        addButton.setToolTipText(bundle1.getString("RoteiroClienteFrame.addButton.toolTipText")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        delButton.setText(bundle.getString("excluir")); // NOI18N
        delButton.setToolTipText(bundle1.getString("RoteiroClienteFrame.delButton.toolTipText")); // NOI18N
        delButton.setName("delButton"); // NOI18N
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout grupoPanelLayout = new javax.swing.GroupLayout(grupoPanel);
        grupoPanel.setLayout(grupoPanelLayout);
        grupoPanelLayout.setHorizontalGroup(
            grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grupoPanelLayout.createSequentialGroup()
                .addGroup(grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(grupoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(roteiroComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 4, Short.MAX_VALUE))
            .addGroup(grupoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        grupoPanelLayout.setVerticalGroup(
            grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grupoPanelLayout.createSequentialGroup()
                .addGroup(grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(grupoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roteiroComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(grupoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vendedorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(grupoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(delButton)))
        );

        clientesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("clientes"))); // NOI18N
        clientesPanel.setName("clientesPanel"); // NOI18N

        agrupadosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("clientesNoRoteiro"))); // NOI18N
        agrupadosPanel.setName("agrupadosPanel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        clienteRoteiroTable.setName("clienteRoteiroTable"); // NOI18N
        jScrollPane2.setViewportView(clienteRoteiroTable);

        downButton.setText(bundle.getString("toLow")); // NOI18N
        downButton.setToolTipText(bundle1.getString("RoteiroClienteFrame.downButton.toolTipText")); // NOI18N
        downButton.setName("downButton"); // NOI18N
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout agrupadosPanelLayout = new javax.swing.GroupLayout(agrupadosPanel);
        agrupadosPanel.setLayout(agrupadosPanelLayout);
        agrupadosPanelLayout.setHorizontalGroup(
            agrupadosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(agrupadosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downButton)
                .addContainerGap())
        );
        agrupadosPanelLayout.setVerticalGroup(
            agrupadosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, agrupadosPanelLayout.createSequentialGroup()
                .addGroup(agrupadosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(agrupadosPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(downButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );

        disponiveisPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("clientesDisponiveis"))); // NOI18N
        disponiveisPanel.setName("disponiveisPanel"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        clientesTable.setName("clientesTable"); // NOI18N
        jScrollPane3.setViewportView(clientesTable);

        upButton.setText(bundle.getString("toUper")); // NOI18N
        upButton.setToolTipText(bundle1.getString("RoteiroClienteFrame.upButton.toolTipText")); // NOI18N
        upButton.setName("upButton"); // NOI18N
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout disponiveisPanelLayout = new javax.swing.GroupLayout(disponiveisPanel);
        disponiveisPanel.setLayout(disponiveisPanelLayout);
        disponiveisPanelLayout.setHorizontalGroup(
            disponiveisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, disponiveisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upButton)
                .addGap(12, 12, 12))
        );
        disponiveisPanelLayout.setVerticalGroup(
            disponiveisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disponiveisPanelLayout.createSequentialGroup()
                .addGroup(disponiveisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(upButton)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout clientesPanelLayout = new javax.swing.GroupLayout(clientesPanel);
        clientesPanel.setLayout(clientesPanelLayout);
        clientesPanelLayout.setHorizontalGroup(
            clientesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesPanelLayout.createSequentialGroup()
                .addGroup(clientesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(disponiveisPanel, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agrupadosPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        clientesPanelLayout.setVerticalGroup(
            clientesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientesPanelLayout.createSequentialGroup()
                .addComponent(agrupadosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disponiveisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clientesPanel, 0, 632, Short.MAX_VALUE)
                    .addComponent(grupoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grupoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        getLogger().info("upButton");
        int r = clientesTable.getSelectedRow();
        if (r < 0) {
            Messages.errorMessage(getBundle().getString("selectOne"));
            return;
        }
        ClienteGrupoModel roteiroModel = (ClienteGrupoModel) clientesTable.getModel();
        Cliente cliente = (Cliente) roteiroModel.getObject(r);
        RoteiroVendedor roteiro = (RoteiroVendedor) roteiroComboBox.getSelectedItem();
        
        if (roteiro.getClientes() == null) roteiro.setClientes(new ArrayList<Cliente>());
        
        roteiro.getClientes().add(cliente);
        
        try {
            roteiroDao.inserirCliente(roteiro, cliente);
            updateClientes();
            loadClientesRoteiros();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        getLogger().info("downButton");
        int r = clienteRoteiroTable.getSelectedRow();
        if (r < 0) {
            Messages.errorMessage(getBundle().getString("selectOne"));
            return;
        }
        ClienteGrupoModel roteiroModel = (ClienteGrupoModel) clienteRoteiroTable.getModel();
        Cliente cliente = (Cliente) roteiroModel.getObject(r);
        RoteiroVendedor roteiro = (RoteiroVendedor) roteiroComboBox.getSelectedItem();
        roteiro.getClientes().remove(cliente);
        //roteiroModel.removeObject(r);
        try {
            roteiroDao.deleteCliente(roteiro, cliente);
            updateClientes();
            loadClientesRoteiros();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("saveErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
        }

    }//GEN-LAST:event_downButtonActionPerformed

private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    Vendedor vendedor = (Vendedor) vendedorComboBox.getSelectedItem();
    RoteiroVendedor roteiro = new RoteiroVendedor();
    roteiro.setDescricao(vendedor.getNome() + " - NOVO ROTEIRO");

    RoteiroEditPanel editPanel = new RoteiroEditPanel();
    EditDialog edtDlg = new EditDialog(getBundle().getString("addRoteiroTitle"));
    edtDlg.setEditPanel(editPanel);
    boolean isValidEdit = false;
    while (edtDlg.edit(roteiro)) {
        if (roteiro.getDescricao().length() > 3) {
            isValidEdit = true;
            break;
        }
    }
    if (!isValidEdit)
        return;
    
    roteiro.setVendedor(vendedor);

    try {
        roteiroDao.insertRecord(roteiro);
        vendedor.getRoteiros().add(roteiro);
        roteiroComboBox.setModel(new ListComboBoxModel((List) vendedor.getRoteiros()));
        updateClientes();
    } catch (Exception e) {
        getLogger().error(getBundle().getString("saveErrorMessage"), e);
        Messages.errorMessage(getBundle().getString("saveErrorMessage"));
    }
//    roteiroModel.addObject(roteiro);
}//GEN-LAST:event_addButtonActionPerformed

private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
    ListComboBoxModel roteiroModel = (ListComboBoxModel) roteiroComboBox.getModel();
    RoteiroVendedor roteiro = (RoteiroVendedor) roteiroModel.getSelectedItem();
    List<Cliente> clientes = (List)roteiro.getClientes();
    try {
        roteiroDao.deleteRoteiro(roteiro);
        refresh();
    } catch (Exception e) {
        getLogger().error(getBundle().getString("deleteErrorMessage"), e);
        Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
    }


}//GEN-LAST:event_delButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel agrupadosPanel;
    private javax.swing.JTable clienteRoteiroTable;
    private javax.swing.JPanel clientesPanel;
    private javax.swing.JTable clientesTable;
    private javax.swing.JButton delButton;
    private javax.swing.JPanel disponiveisPanel;
    private javax.swing.JButton downButton;
    private javax.swing.JPanel grupoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox roteiroComboBox;
    private javax.swing.JButton upButton;
    private javax.swing.JComboBox vendedorComboBox;
    // End of variables declaration//GEN-END:variables

    public ClienteDao getClienteDao() {
        return clienteDao;
    }

    public void setClienteDao(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public VendedorDao getVendedorDao() {
        return vendedorDao;
    }

    public void setVendedorDao(VendedorDao vendedorDao) {
        this.vendedorDao = vendedorDao;
    }
}

class NomeClienteComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        Cliente c1 = (Cliente) obj1;
        Cliente c2 = (Cliente) obj2;
        return c1.getRazao().compareTo(c2.getRazao());
    }
}