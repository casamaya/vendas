/*
 * EditPanel.java
 * 
 * Created on 24/06/2007, 11:08:14
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import java.util.Map;
import java.util.ResourceBundle;
import ritual.swing.TApplication;

/**
 *
 * @author Sam
 */
public abstract class EditPanel extends BasePanel {
    
    private Map errors;
    private ResourceBundle bundle = TApplication.getInstance().getBundle();

    public EditPanel() {
    }

    public Map getErrors() {
        return errors;
    }

    public void setErrors(Map errors) {
        this.errors = errors;
    }

    public boolean entryValidate() {
        return true;
    }
    
    public void enableControls(boolean enabled) {
        
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
}
