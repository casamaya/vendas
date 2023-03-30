/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vendas.swing.app.cliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import ritual.swing.FractionCellRenderer;
import ritual.swing.TApplication;
import vendas.beans.CobrancaFilter;
import vendas.dao.VendedorDao;
import vendas.entity.Vendedor;
import vendas.swing.app.pedido.PgtoFilterPanel;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.SaldoClienteTableModel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author joliveira
 */
public class SaldoClienteInternalFrame extends TableViewFrame {

    TApplication app = TApplication.getInstance();

    public SaldoClienteInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle("Saldo de cobranças");
    }

    @Override
    public void initComponents() {
        super.initComponents();

        JTable jt;
        final ServiceTableModel stm = getTableModel();
        jt = new JTable(stm);
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jt.addMouseListener(new MouseAdapter() {

            /* @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            } */
        });

        setTable(jt);
        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        // setSize(400, 300);
        setPreferredSize(new Dimension(800, 400));
        setFilterObject(createFilter());
        getColumn(SaldoClienteTableModel.CLIENTE).setPreferredWidth(300);
        getColumn(SaldoClienteTableModel.VALOR).setPreferredWidth(95);
        getColumn(SaldoClienteTableModel.PERC).setPreferredWidth(90);
    }

    private CobrancaFilter createFilter() {
        CobrancaFilter clienteFilter = new CobrancaFilter();
        getLogger().info("createFilter");
        if (app.getUser().isVendedor()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                clienteFilter.setVendedor((Vendedor) dao.findById(Vendedor.class, app.getUser().getIdvendedor()));
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        clienteFilter.setSituacao("N");
        return clienteFilter;
    }

    @Override
    public void cancelFilter() {
        setFilterObject(createFilter());
        doRefresh();
    }

    public void executeFilter(CobrancaFilter filtro) {
        try {
            getTableModel().select(filtro);
            setFilterObject(filtro);
            getTableModel().fireTableDataChanged();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        PgtoFilterPanel filterPanel = new PgtoFilterPanel();
        filterPanel.init();
        filterPanel.enableTipoPgto(false);
        EditDialog edtDlg = new EditDialog("Saldo de cobranças");
        edtDlg.setEditPanel(filterPanel);
        CobrancaFilter filtro = createFilter();

        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                getTableModel().fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

}
