/*
 * ContasFrame.java
 *
 * Created on 4 de Novembro de 2007, 21:52
 */
package vendas.security;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import ritual.swing.ListComboBoxModel;
import ritual.swing.ViewFrame;
import vendas.dao.UserDao;
import vendas.entity.Perfil;
import vendas.entity.PerfilRecurso;
import vendas.entity.Recurso;
import vendas.exception.DAOException;
import vendas.util.Messages;

/**
 *
 * @author  Sam
 */
public final class RecursosFrame extends ViewFrame {

    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private Logger logger;
    UserDao dao;

    /** Creates new form ContasFrame */
    @SuppressWarnings("Convert2Lambda")
    public RecursosFrame() {
        super("Permissões", true, true, true, true);
        dao = new UserDao();
        logger = Logger.getLogger(getClass());

        List<Perfil> perfis = dao.getPerfis();
        loadTree(perfis.get(0));
        initComponents();

        carregarPerfil(perfis);
        tree.setRootVisible(false);
        tree.invalidate();
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode == null) {
                    permissaoCheckBox.setEnabled(false);
                    return;
                }
                Recurso recurso = (Recurso)selectedNode.getUserObject();
                Perfil perfil = (Perfil)perfilComboBox.getSelectedItem();
                PerfilRecurso perfilRecurso = dao.getPerfilRecurso(perfil, recurso);
                
                if ("C".equals(perfilRecurso.getChecked().toString())) {
                    permissaoCheckBox.setSelected(true);
                } else {
                    permissaoCheckBox.setSelected(false);
                }
                permissaoCheckBox.setEnabled(true);
                permissaoCheckBox.invalidate();
            }
        });

        perfilComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        expandAll(tree, true);
        tree.fireTreeExpanded(tree.getPathForRow(0));
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new MyTreeCellRenderer());
        permissaoCheckBox.setEnabled(false);
    }
    
    private void carregarPerfil(List<Perfil> perfis) {
        try {
            perfilComboBox.setModel(new ListComboBoxModel(perfis));
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        perfilComboBox.setSelectedIndex(0);
    }
    
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    private void loadTree(Perfil perfil) {
        if (perfil == null)
            return;
        
        rootNode = new DefaultMutableTreeNode();

        Recurso recurso = new Recurso();
        recurso.setNome(getBundle().getString("permissoes"));
        DefaultMutableTreeNode recursoNode = new DefaultMutableTreeNode("Permissões");
        List<Recurso> recursos = null;
        
        try {
            recursos = dao.findAllMaster(perfil);
        } catch (Exception e) {
            logger.error(e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        for (Recurso resource : recursos) {
            recursoNode.add(loadContas(new DefaultMutableTreeNode(resource)));
        }
        rootNode.add(recursoNode);
        treeModel = new DefaultTreeModel(rootNode);

    }

    public void expandAll(JTree tree, boolean expand) {
        if (tree == null)
            return;
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    private DefaultMutableTreeNode loadContas(DefaultMutableTreeNode node) {
        Recurso recursoPai = (Recurso) node.getUserObject();
        List<Recurso> recursos = (List) recursoPai.getRecursos();
        for (Recurso recurso : recursos) {
            node.add(loadContas(new DefaultMutableTreeNode(recurso)));
        }
        return node;
    }

    @Override
    public void view() {

    }

    @Override
    public void edit() {
        
    }

    private DefaultMutableTreeNode getCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
        return currentNode;
    }

    @Override
    public void remove() {

    }

    public void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    @Override
    public void insert() {
    }

    @Override
    public void refresh() {
        //Perfil perfil = (Perfil)perfilComboBox.getSelectedItem();
        //loadTree(perfil);
        DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode)tree.getModel().getRoot()).getFirstLeaf();
        tree.setSelectionPath(new TreePath(firstLeaf.getPath()));
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        model.reload(root);
        expandAll(tree, true);
        tree.fireTreeExpanded(tree.getPathForRow(0));
    }

    @Override
    public void report() {

    }
    
    private class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            
            if (leaf) {
                Recurso node = (Recurso) ((DefaultMutableTreeNode) value).getUserObject();
                Perfil perfil = (Perfil) perfilComboBox.getSelectedItem();
                PerfilRecurso perfilRecurso = dao.getPerfilRecurso(perfil, node);

                if ("U".equals(perfilRecurso.getChecked().toString()))
                    setForeground(Color.RED);
            }

            return this;
        }
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
        perfilComboBox = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new JTree(rootNode);
        jPanel2 = new javax.swing.JPanel();
        permissaoCheckBox = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        setTitle(bundle.getString("contasTitle")); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 400));

        jLabel1.setText("Perfil");
        jPanel1.add(jLabel1);

        perfilComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(perfilComboBox);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        tree.setModel(treeModel);
        tree.setAutoscrolls(true);
        tree.setMinimumSize(new java.awt.Dimension(300, 400));
        jScrollPane1.setViewportView(tree);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        permissaoCheckBox.setText("Permitir acesso");
        jPanel2.add(permissaoCheckBox);

        saveButton.setText("Gravar");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel2.add(saveButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if (!permissaoCheckBox.isEnabled())
            return;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }
        Recurso recurso = (Recurso)selectedNode.getUserObject();
        Perfil perfil = (Perfil)perfilComboBox.getSelectedItem();
        PerfilRecurso perfilRecurso = dao.getPerfilRecurso(perfil, recurso);
        if (permissaoCheckBox.isSelected())
            perfilRecurso.setChecked('C');
        else
            perfilRecurso.setChecked('U');
        try {
            dao.updateRow(perfilRecurso);
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            model.nodeChanged(selectedNode);

        } catch (DAOException ex) {
            Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            java.util.logging.Logger.getLogger(RecursosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox perfilComboBox;
    private javax.swing.JCheckBox permissaoCheckBox;
    private javax.swing.JButton saveButton;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
}

