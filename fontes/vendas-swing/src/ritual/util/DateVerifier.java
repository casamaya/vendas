/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.util;

import java.text.ParseException;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Sam
 */
public class DateVerifier extends BaseInputVerifier {


    public DateVerifier() {
        super(false);
    }

    public DateVerifier(boolean required) {
        super(required);
    }

    public boolean verify(JComponent input) {
        if (input instanceof JFormattedTextField) {
            JFormattedTextField ftf = (JFormattedTextField) input;
            JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
            String texto = ftf.getText();
            if (formatter != null) {
                if (texto.length() > 0) {
                    try {
                        formatter.stringToValue(texto);
                        return true;
                    } catch (ParseException pe) {
                        //Messages.errorMessage(bundle.getString("dataMsgError"));
                        ftf.setText("");
                        ftf.requestFocus();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        return verify(input);
    }
}
