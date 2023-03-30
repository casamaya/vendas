/*
 * ContasModel.java
 *
 * Created on 26/10/2007, 17:48:28
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import vendas.entity.Conta;

/**
 *
 * @author p993702
 */
public class ContasModel implements TreeModel {

    private boolean showAncestors;
    private Collection<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
    private Conta rootNode;

    public ContasModel(Conta no) {
        showAncestors = false;
        rootNode = no;
    }

    public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        Conta oldRoot = rootNode;
        if (newRoot != null) {
            rootNode = (Conta) newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }

    protected void fireTreeStructureChanged(Conta oldRoot) {
        //int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[]{oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }

    @Override
    public Object getRoot() {
        return rootNode;
    }

    @Override
    public Object getChild(Object parent, int index) {
        Conta p = (Conta) parent;
        if (showAncestors) {
            return p.getContaMaster();
        }
        return p.getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        Conta p = (Conta) parent;
        if (showAncestors) {
            int count = 0;
            if (p.getContaMaster() != null) {
                count++;
            }
            return count;
        }
        p.getContas().size();
        return p.getContas().size();
    }

    @Override
    public boolean isLeaf(Object node) {
        Conta p = (Conta) node;
        if (showAncestors) {
            return (p.getContaMaster() == null);
        }
        return p.getChildCount() == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
//        System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Conta p = (Conta) parent;
        if (showAncestors) {
            Conta father = p.getContaMaster();
            if (father != null) {
                if (father == child) {
                    return 0;
                }
            }
            return -1;
        }
        return p.getIndexOfChild((Conta) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }
}