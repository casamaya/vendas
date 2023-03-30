/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.swing;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Sam
 */
public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
    // This is the component that will handle the editing of the cell value

    JComponent component = getDate();

    private JDateChooser getDate() {
        JDateChooser component = new JDateChooser();
        component.setDateFormatString("dd/MM/yyyy");
        return component;

    }
    // This method is called when a cell value is edited by the user.

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)

        if (isSelected) {
            // cell (and perhaps other cells) are selected
        }

        // Configure the component with the specified value
        ((JDateChooser) component).setDate((Date) value);

        // Return the configured component
        return component;
    }
    // This method is called when editing is completed.
    // It must return the new value to be stored in the cell.

    public Object getCellEditorValue() {
        return ((JDateChooser) component).getDate();
    }
}
