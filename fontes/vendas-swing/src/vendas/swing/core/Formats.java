/*
 * Formats.java
 *
 * Created on 24/06/2007, 11:03:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Sam
 */
public class Formats {
    
    public Formats() {
    }
    
    public static JFormattedTextField newFloatFormat() {
        NumberFormat amountFormat = NumberFormat.getNumberInstance(new Locale("pt","BR"));
        amountFormat.setMinimumFractionDigits(2);
        amountFormat.setMaximumFractionDigits(2);
        amountFormat.setMaximumIntegerDigits(3);
        return new JFormattedTextField(amountFormat);
    }
    
    public static MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
        }
        return formatter;        
    }
}
