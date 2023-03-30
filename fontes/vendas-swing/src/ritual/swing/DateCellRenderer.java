package ritual.swing;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.Date;
import ritual.util.DateUtils;

/**
 * @author sam
 * Date: Jul 10, 2003
 * Time: 11:26:23 PM
 */
public class DateCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        if (value != null && value instanceof Date) {
            setText(DateUtils.format((Date) value));
        } else {
            super.setValue(value);
        }
    }
}
