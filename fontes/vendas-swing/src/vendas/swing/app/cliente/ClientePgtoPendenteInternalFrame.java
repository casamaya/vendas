/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.cliente;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.ClientePgtoPendenteTableModel;

/**
 *
 * @author jaimeoliveira
 */
public class ClientePgtoPendenteInternalFrame extends TableViewFrame {
    public ClientePgtoPendenteInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle("Clientes LC");
    }
    
    @Override
    public void initComponents() {
        super.initComponents();
        TableColumn col = getColumn(ClientePgtoPendenteTableModel.RAZAO);
        col.setPreferredWidth(320);
        col.setCellRenderer(new ColorCellRenderer());
        col = getColumn(1);
        col.setPreferredWidth(170);
        col = getColumn(2);
        col.setPreferredWidth(120);
        col = getColumn(3);
        col.setPreferredWidth(120);
        
    }
    
    @Override
    public void remove() {
    }

    @Override
    public void insert() {
    }

    @Override
    public void edit() {
    }
    
    class ColorCellRenderer extends DefaultTableCellRenderer {

        public ColorCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
                    c.setForeground(Color.RED);

            return c;
        }

    }    
    
    
}
