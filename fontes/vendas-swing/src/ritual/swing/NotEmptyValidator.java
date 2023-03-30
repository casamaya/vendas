/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ritual.swing;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JDialog;
 
/**
 * A class for performing basic validation on text fields. All it does is make 
 * sure that they are not null.
 * 
 * @author Michael Urban
 */
 
public class NotEmptyValidator extends AbstractValidator {
    public NotEmptyValidator(JDialog parent, JTextField c, String message) {
        super(parent, c, message);
    }
	
    protected boolean validationCriteria(JComponent c) {
        if (((JTextField)c).getText().equals(""))
            return false;
        return true;
    }
}

//JTextField textField = new JTextField();
//textField.setInputVerifier(new NotEmptyValidator(parent, textField, "Field cannot be null."));