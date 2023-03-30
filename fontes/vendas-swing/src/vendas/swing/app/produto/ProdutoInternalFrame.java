/**
 * ProdutoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.produto;

import javax.swing.SwingConstants;
import vendas.entity.Produto;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import javax.swing.table.TableColumn;
import ritual.swing.FractionCellRenderer;
import vendas.beans.ProdutoFilter;
import vendas.dao.GrupoProdutoDao;
import ritual.swing.TApplication;
import vendas.dao.UnidadeProdutoDao;
import vendas.swing.model.ProdutoTableModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author p993702
 */
public class ProdutoInternalFrame extends TableViewFrame {

    private GrupoProdutoDao grupoDao;
    private UnidadeProdutoDao unidadeDao;

    public ProdutoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("produtosTitle"));
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        ProdutoFilter filtro = new ProdutoFilter();
        ProdutoFilterPanel filterPanel = new ProdutoFilterPanel();
        filterPanel.setSelectInativos(false);
        EditDialog edtDlg = new EditDialog(getBundle().getString("filterProdutoTitle"));
        edtDlg.setEditPanel(filterPanel);
        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(ProdutoTableModel.DESCRICAO);
        col.setPreferredWidth(300);
        col = getColumn(ProdutoTableModel.GRUPO);
        col.setPreferredWidth(160);
        col = getColumn(ProdutoTableModel.FATOR);
        col.setCellRenderer(new FractionCellRenderer(8, 4, SwingConstants.RIGHT));
        grupoDao = (GrupoProdutoDao) TApplication.getInstance().lookupService("grupoProdutoDao");
        unidadeDao = (UnidadeProdutoDao) TApplication.getInstance().lookupService("undProdutoDao");/*
        JTable jt = getTable();
        jt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });*/
    }
    
    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_PRODUTO"))
            super.insert();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_PRODUTO"))
            super.remove();
    }    

    @Override
    protected void insertRow() {
        ProdutoEditPanel editPanel = new ProdutoEditPanel();
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("addProdutoTitle"));
        edtDlg.setEditPanel(editPanel);
        Produto prod = new Produto();
        while (edtDlg.edit(prod)) {
            try {
                if (TApplication.getInstance().getUser().isAdmin()) {
                    prod.setBloqueado("N");
                } else {
                    prod.setBloqueado("S");
                }
                getTableModel().insertRecord(prod);
                viewProduto(prod);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        ProdutoEditPanel editPanel = new ProdutoEditPanel();
        editPanel.setEnableCode(false);
        try {
            editPanel.setGrupo(grupoDao.findAll());
            editPanel.setUnidades(unidadeDao.findAll());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        EditDialog edtDlg = new EditDialog(getBundle().getString("editProdutoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_PRODUTO"));
        Object produto = vtm.getObject(getSelectedRow());
        while (edtDlg.edit(produto)) {
            try {
                vtm.updateRow(produto);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    private void viewProduto(Produto produto) {
        String pedidoTitle = "Visualizar produto " + produto.getIdProduto();
        if (!TApplication.getInstance().locateFrame(pedidoTitle)) {
            ProdutoViewFrame viewFrame = new ProdutoViewFrame(pedidoTitle);
            TApplication.getInstance().getDesktopPane().add(viewFrame);
            viewFrame.setVisible(true);
            viewFrame.execute(produto);
        }
    }

    @Override
    public void viewRow() {
        Produto pedido = (Produto) getTableModel().getObject(getSelectedRow());
        viewProduto(pedido);
    }

    public void executeFilter(Produto produto) {
        ProdutoFilter filtro = new ProdutoFilter();
        filtro.setProduto(produto)  ;
        setFilterObject(filtro);
        doRefresh();
        if (getTableModel().getItemList().isEmpty())
            Messages.errorMessage(getBundle().getString("emptyList"));
    }
}
