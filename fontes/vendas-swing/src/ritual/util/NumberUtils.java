/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ritual.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Sam
 */
public class NumberUtils {
    
    static DefaultFormatterFactory fmtFactory;
    static DefaultFormatterFactory fmtConvertFactory;
    static DefaultFormatterFactory intfmtFactory;
    static DefaultFormatter fmt;
    static DefaultFormatter convertfmt;
    static DefaultFormatter intfmt;
    //static DecimalFormat decformat;
    static DecimalFormat convertFormat;
    static DecimalFormat intFormat;
    static NumberUtils nu;
    static {
        nu = new NumberUtils();
    }
    
    private NumberUtils() {
        //decformat = new DecimalFormat("#,###.##");
        convertFormat = new DecimalFormat("#,###.####");
        //decformat.setGroupingUsed(true);
        //decformat.setGroupingSize(3);
        //decformat.setParseIntegerOnly(false);
        //decformat.setMinimumFractionDigits(2);
        //decformat.setMaximumFractionDigits(2);
        intFormat = new DecimalFormat("########");
        intFormat.setGroupingUsed(false);
        intFormat.setParseIntegerOnly(true);
        intFormat.setMinimumFractionDigits(0);
        intFormat.setMaximumFractionDigits(0);

        fmt = new NumberFormatter(getDecimalFormat());
        fmt.setValueClass(BigDecimal.class);
        convertfmt = new NumberFormatter(convertFormat);
        convertfmt.setValueClass(BigDecimal.class);
        intfmt = new NumberFormatter(intFormat);
        intfmt.setValueClass(Integer.class);

        fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
        fmtConvertFactory = new DefaultFormatterFactory(convertfmt, convertfmt, convertfmt);
        intfmtFactory = new DefaultFormatterFactory(intfmt, intfmt, intfmt);
    }

    public static DefaultFormatterFactory getFmtConvertFactory() {
        return fmtConvertFactory;
    }

    public static void setFmtConvertFactory(DefaultFormatterFactory fmtConvertFactory) {
        NumberUtils.fmtConvertFactory = fmtConvertFactory;
    }
    
    public static DefaultFormatterFactory getFormatterFactory() {
        return fmtFactory;
    }
    
    public static DefaultFormatterFactory getIntFormatterFactory() {
        return intfmtFactory;
    }

    public static DecimalFormat getDecimalFormat() {
        DecimalFormat  decformat = new DecimalFormat("#0.00");
        decformat.setGroupingUsed(true);
        decformat.setGroupingSize(3);
        decformat.setParseIntegerOnly(false);
        decformat.setMinimumFractionDigits(2);
        decformat.setMaximumFractionDigits(2);
        return decformat;
    }
    
    public static DecimalFormat getIntegerFormat() {
        return intFormat;
    }

    public static String format(Number n) {
        return format(n, 8, 2);
    }
    
    public static String format(Number n, int ints, int fraction) {
        System.out.println("v:" + n);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("pt","BR"));
        formatter.setMaximumIntegerDigits(ints);
        formatter.setMaximumFractionDigits(fraction);
        formatter.setMinimumFractionDigits(fraction);
        return formatter.format(n.floatValue());
    }

    public static Integer getInteger(Object value) {
        if (value == null)
            return null;
        if (value instanceof Long)
            return ((Long)value).intValue();
        else if (value instanceof Integer)
            return (Integer)value;
        else
            return null;
    }

    public static BigDecimal getBigDecimal (Object value) {
        if (value == null)
            return null;
        if (value instanceof Long)
            return BigDecimal.valueOf((Long)value);
        else if (value instanceof Double)
            return BigDecimal.valueOf((Double)value);
        else if (value instanceof BigDecimal)
            return (BigDecimal)value;
        else if (value instanceof String) {
            String valor = (String)value;
            if (valor.length() <= 0)
                return null;
            else
                return new BigDecimal((String)value);
        }
        else
            return null;
    }
}
