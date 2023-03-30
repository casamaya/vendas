/*
 * JTableInternalFrame.java
 *
 * Created on 24/06/2007, 11:34:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import ritual.swing.ViewFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import vendas.beans.FilterBean;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public abstract class TableViewFrame extends ViewFrame {
    
    private ServiceTableModel tableModel;
    private JTable jt;
    private Object filterObject;
    private String reportFile;
    
    public TableViewFrame(String title) throws Exception {
        this(title, null);
    }
    
    public TableViewFrame(String title,ServiceTableModel tm) throws Exception {
        super(title, true, true, true, true);
        tableModel = tm;
    }
    
    public TableViewFrame(ServiceTableModel tm) throws Exception {
        this(null, tm);
    }

    public void setTable(JTable value) {
        jt = value;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        jt.setComponentPopupMenu(popupMenu);
    }
    public JTable getTable() {
        return jt;
    }
    
    public void setReportFile(String fileName) {
        reportFile = fileName;
    }
    
    public String getReportFile() {
        return reportFile;
    }
    
    public void resetFilterObject() {
        filterObject = null;
    }
    public void setFilterObject(Object obj) {
        filterObject = obj;
    }
    
    public Object getFilterObject() {
        return filterObject;
    }
    
    public void setTableModel(ServiceTableModel tm) {
        tableModel = tm;
    }

    public TableColumn getColumn(int col) {
        return jt.getColumnModel().getColumn(col);
    }
    
    public ServiceTableModel getTableModel() {
        return tableModel;
    }
    
    public int getSelectedRow() {
        return jt.getSelectedRow();
    }
    
    public void initComponents() {
        final ServiceTableModel stm = getTableModel();
        if (stm != null) {
            jt = new JTable(stm);            
        } else {
            jt = new JTable();
        }
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        //jt.setPreferredScrollableViewportSize(new Dimension(400, 300));
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        //jt.setDefaultRenderer(Float.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setDefaultRenderer(Date.class, new DateCellRenderer());
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        //setSize(400, 300);
        setPreferredSize(new Dimension(800, 400));
    }
            
    public void init() {
        getLogger().info("init ini");
        initComponents();
        doRefresh();
        getLogger().info("init end");
    }
    
    @Override
    public void refresh() {
        doRefresh();
    }
        
    public void doRefresh() {
        getLogger().info("doRefresh ini");
        try {
            getTableModel().select(filterObject);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
        getLogger().info("doRefresh end");
    }

    @Override
    public void remove() {
        int i = getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        if (!Messages.deleteQuestion())
            return;
        removeRow();
    }
    
    protected void removeRow() {
        try {
            getTableModel().deleteRow(getSelectedRow());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("deleteErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("deleteErrorMessage"));
        }
    }

    @Override
    public void insert() {
        insertRow();
    }
    
    protected void insertRow() {}
    
    @Override
    public void edit() {
        int i = getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        editRow();
    }
    
    protected void editRow() {}
    protected void viewRow() {}

    @Override
    public void view() {
        int i = getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        viewRow();
    }
    
    @Override
    public void report() {
        
        int i = getTable().getRowCount();
        if (i < 1) {
            Messages.errorMessage(getBundle().getString("noDataReport"));
            return;
        }
        try {
            
            showReport();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }
    
    @Override
    public void cancelFilter() {
        resetFilterObject();
        try {
            if (getTableModel() != null)
            getTableModel().select(filterObject);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }
    
    @Override
    public void filter() {
        getLogger().info("filter");
        String s;
        s = (String)JOptionPane.showInputDialog(this, getBundle().getString("inputNameLabel"), getBundle().getString("informar"), JOptionPane.QUESTION_MESSAGE);
        if ((s == null) || (s.length() == 0))
            return;
        try {
            getTableModel().select(s);
            setFilterObject(s);
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }    

    public void showReport() {
        URL url = getClass().getResource("/vendas/report/" + reportFile + ".jasper");
        String reportTitle = this.getTitle();
        Object filter = getFilterObject();
        String subTitle = "";
        if (filter != null) {
            if (filter instanceof FilterBean)
                subTitle = ((FilterBean)filter).getTitle();
        }
        try {
            Reports.showReport(reportTitle, subTitle, url, getTableModel().getItemList());
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    public void customizeTable(JTable jt) {
        
    }
    
}
