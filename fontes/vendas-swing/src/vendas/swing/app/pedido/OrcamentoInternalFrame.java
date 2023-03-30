/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.app.pedido;

import java.net.URL;
import ritual.swing.TApplication;
import vendas.beans.PedidoBean;
import vendas.beans.PedidoFilter;
import vendas.dao.OrcamentoDao;
import vendas.dao.RepresDao;
import vendas.entity.Orcamento;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.OrcamentoTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author sam
 */
public class OrcamentoInternalFrame extends TableViewFrame {

    private RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");

    public OrcamentoInternalFrame(ServiceTableModel tableModel) throws Exception {
        super("Orçamentos", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        getLogger().info("initComponents ini");
        getColumn(OrcamentoTableModel.REPRES).setPreferredWidth(150);
        getColumn(OrcamentoTableModel.CLIENTE).setPreferredWidth(150);
        getColumn(OrcamentoTableModel.VENDEDOR).setPreferredWidth(150);
        getColumn(OrcamentoTableModel.EMISSAO).setPreferredWidth(80);
        getColumn(OrcamentoTableModel.FORMAPGTO).setPreferredWidth(150);
        setFilterObject(new PedidoFilter());
    }

    @Override
    public void report() {
        URL url;
        url = getClass().getResource(Constants.JRORCAMENTOS);
        String reportTitle = this.getTitle();
        OrcamentoDataSource ds = new OrcamentoDataSource(getTableModel().getItemList());
        try {
            Reports.showReport(reportTitle, "", url, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }


    @Override
    protected void insertRow() {
        Orcamento orcamento = new Orcamento();
        OrcamentoEditPanel editPanel = new OrcamentoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addOrcamentoTitle"));
        edtDlg.setEditPanel(editPanel);
        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setOrcamento(orcamento);
        while (edtDlg.edit(pedidoBean, false)) {
            try {
                getTableModel().insertRecord(pedidoBean.getOrcamento());
                OrcamentoDao dao = (OrcamentoDao) TApplication.getInstance().lookupService("orcamentoDao");
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_ORCAMENTO"))
            super.insert();
    }
    
    @Override
    public void remove() {
        if (TApplication.getInstance().isGrant("EXCLUIR_PEDIDO"))
            super.remove();
    }    

    @Override
    protected void editRow() {
        ServiceTableModel vtm = getTableModel();
        Orcamento pedido = (Orcamento) vtm.getObject(getSelectedRow());
        OrcamentoEditPanel editPanel = new OrcamentoEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editOrcamentoTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_ORCAMENTO"));

        PedidoBean pedidoBean = new PedidoBean();
        pedidoBean.setOrcamento(pedido);
        while (edtDlg.edit(pedidoBean)) {
            try {
                OrcamentoDao pedidoDao = (OrcamentoDao) vtm.getDao();
                pedidoDao.updateOrcamento(pedidoBean.getOrcamento());
                //vtm.updateRow(pedidoBean.getOrcamento());
                vtm.fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    
    @Override
    public void resetFilterObject() {
        PedidoFilter filter = new PedidoFilter();
        setFilterObject(filter);
    }

    @Override
    public void viewRow() {
        Orcamento orcamento = (Orcamento) getTableModel().getObject(getSelectedRow());
        OrcamentoViewFrame viewFrame = new OrcamentoViewFrame("Visualizar " + orcamento.getIdorcamento());
        TApplication.getInstance().getDesktopPane().add(viewFrame);
        viewFrame.setVisible(true);
        viewFrame.execute(orcamento);
    }
    @Override
    public void filter() {
        getLogger().info("filter");
        OrcamentoFilterPanel filterPanel = new OrcamentoFilterPanel();
        filterPanel.init();
        EditDialog edtDlg = new EditDialog(getBundle().getString("findOrcamentoTitle"));
        edtDlg.setEditPanel(filterPanel);
        Object filtro = getFilterObject();
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

}
