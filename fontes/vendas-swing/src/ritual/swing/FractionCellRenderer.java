package ritual.swing;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.NumberFormat;
import ritual.util.NumberUtils;

/**
 * @author sam
 * Date: Jul 10, 2003
 * Time: 11:26:23 PM
 */
public class FractionCellRenderer extends DefaultTableCellRenderer {

    protected int integer;
    protected int fraction;
    protected int align;
    

    public FractionCellRenderer(int integer, int fraction, int align) {
        this.integer = integer;
        this.fraction = fraction;
        this.align = align;

        setHorizontalAlignment(align);
    }

    @Override
    protected void setValue(Object value) {
        NumberFormat formatter = NumberUtils.getDecimalFormat();//NumberFormat.getInstance(new Locale("pt","BR"));
        formatter.setMaximumIntegerDigits(integer);
        formatter.setMaximumFractionDigits(fraction);
        formatter.setMinimumFractionDigits(fraction);
        if (value != null && value instanceof Number) {
            setText(formatter.format(((Number) value).floatValue()));
        } else {
            super.setValue(value);
        }
    }
}
