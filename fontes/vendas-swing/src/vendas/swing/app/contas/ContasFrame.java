/*
 * ContasFrame.java
 *
 * Created on 4 de Novembro de 2007, 21:52
 */
package vendas.swing.app.contas;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import ritual.swing.TApplication;
import ritual.swing.ViewFrame;
import vendas.dao.PlanoDao;
import vendas.entity.Conta;
import vendas.entity.Vendedor;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author  Sam
 */
public class ContasFrame extends ViewFrame {

    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private Logger logger;
    private Vendedor vendedor;
    PlanoDao dao;

    /** Creates new form ContasFrame */
    public ContasFrame() {
        super("Plano de contas", true, true, true, true);
        dao = new PlanoDao();
        logger = Logger.getLogger(getClass());
        Integer id = TApplication.getInstance().getUser().getIdvendedor();
        if (id == null)
            id = 1;
        try {
            vendedor = (Vendedor)dao.findById(Vendedor.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha geral");
        }
        loadTree();
        initComponents();

        expandAll(tree, true);
        tree.fireTreeExpanded(tree.getPathForRow(0));

    }
    
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    private void loadTree() {
        rootNode = new DefaultMutableTreeNode();

        Conta compromisso = new Conta();
        compromisso.setNome(getBundle().getString("aPagarTitle"));
        compromisso.setTipo(Short.parseShort("1"));
        DefaultMutableTreeNode aPagar = new DefaultMutableTreeNode(compromisso);
        compromisso = new Conta();
        compromisso.setNome(getBundle().getString("aReceberTitle"));
        compromisso.setTipo(Short.parseShort("2"));
        DefaultMutableTreeNode aReceber = new DefaultMutableTreeNode(compromisso);
        List<Conta> contas = null;
        
        try {
            contas = dao.findAllMaster(vendedor.getIdVendedor());
            Collections.sort(contas, new ContasComparator());
        } catch (Exception e) {
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        for (Conta conta : contas) {
            if (conta.getTipo() == 1) {

                aPagar.add(loadContas(new DefaultMutableTreeNode(conta)));
            } else {
                aReceber.add(loadContas(new DefaultMutableTreeNode(conta)));
            }
        }
        rootNode.add(aPagar);
        rootNode.add(aReceber);
        treeModel = new DefaultTreeModel(rootNode);

    }

    public void expandAll(JTree tree, boolean expand) {
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
        Conta contaMaster = (Conta) node.getUserObject();
        List<Conta> contas = (List) contaMaster.getContas();
        Collections.sort(contas, new ContasComparator());
        for (Conta conta : contas) {
            node.add(new DefaultMutableTreeNode(conta));
        }
        return node;
    }

    @Override
    public void view() {
        edit();
    }



    @Override
    public void edit() {
        if (!TApplication.getInstance().isGrant("ALTERAR_PLANO_CONTA"))
            return;
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //JOptionPane.showMessageDialog(this, "parentPath == null", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
        int level = parentNode.getLevel();
        if ((level < 2) || (level > 3)) {
            Messages.errorMessage(getBundle().getString("invalidNode"));
            return;
        }

        DefaultMutableTreeNode node = getCurrentNode();
        Conta conta = (Conta) node.getUserObject();

        EditDialog edtDlg = new EditDialog(getBundle().getString("editContaTitle"));
        PlanoContaEditPanel editPanel = new PlanoContaEditPanel();
        edtDlg.setEditPanel(editPanel);

        while (edtDlg.edit(conta)) {
            try {
                dao.updateRow(conta);
                node.setUserObject(conta);
                treeModel.reload(node);
                break;
            } catch (Exception e) {
                logger.error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private DefaultMutableTreeNode getCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
        return currentNode;
    }

    @Override
    public void remove() {
        if (!TApplication.getInstance().isGrant("EXCLUIR_PLANO_CONTA"))
            return;
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            int level = currentNode.getLevel();
            logger.info("level: " + level);
            if ((level < 2) || (level > 3)) {
                Messages.errorMessage(getBundle().getString("invalidNode"));
                return;
            }
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                if (!Messages.deleteQuestion()) {
                    return;
                }
                try {
                    Conta conta = (Conta) currentNode.getUserObject();
                    dao.deleteRow(conta);
                    treeModel.removeNodeFromParent(currentNode);
                    return;
                } catch (Exception e) {
                    logger.error(getBundle().getString("deleteErrorMessage"), e);
                    Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
                }
            }
        }

        // Either there was no selection, or the root was selected.
        //toolkit.beep();
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
        if (!TApplication.getInstance().isGrant("INCLUIR_PLANO_CONTA"))
            return;
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //JOptionPane.showMessageDialog(this, "parentPath == null", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
        int level = parentNode.getLevel();
        logger.info("level: " + level);
        if ((level < 1) || (level >= 3)) {
            Messages.errorMessage(getBundle().getString("invalidNode"));
            return;
        }
        Conta baseconta;
        DefaultMutableTreeNode node = getCurrentNode();
        //baseconta = (Conta) parentNode.getUserObject();
        baseconta = (Conta) node.getUserObject();
        Conta conta = new Conta();
        conta.setVendedor(vendedor);
        conta.setTipo(baseconta.getTipo());
        if (level == 2) {
            conta.setContaMaster(baseconta);
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addContaTitle"));
        PlanoContaEditPanel editPanel = new PlanoContaEditPanel();
        edtDlg.setEditPanel(editPanel);

        while (edtDlg.edit(conta)) {
            try {
                dao.insertRecord(conta);
                addObject(parentNode, conta, true);
                break;
            } catch (Exception e) {
                logger.error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    @Override
    public void refresh() {
        loadTree();
    }

    @Override
    public void report() {
        URL url = getClass().getResource("/vendas/report/ContasReport.jasper");
        Map hm = new HashMap();
        hm.put("ReportTitle", getTitle());
        hm.put("LogoURL", getClass().getResource("/vendas/resources/logo.png"));
        try {
            Reports.showReport(url, hm, dao.findAll());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new JTree(rootNode);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vendas/resources/Vendas"); // NOI18N
        setTitle(bundle.getString("contasTitle")); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 400));
        setPreferredSize(new java.awt.Dimension(300, 400));

        tree.setModel(treeModel);
        tree.setAutoscrolls(true);
        tree.setMinimumSize(new java.awt.Dimension(300, 400));
        tree.setScrollsOnExpand(true);
        jScrollPane1.setViewportView(tree);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
}

class ContasComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Conta c1 = (Conta) o1;
        Conta c2 = (Conta) o2;
        return c1.getNome().compareTo(c2.getNome());
    }
}
