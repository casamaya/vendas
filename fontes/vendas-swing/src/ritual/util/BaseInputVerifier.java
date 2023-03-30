/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ritual.util;

import javax.swing.InputVerifier;

/**
 *
 * @author Sam
 */
public abstract class BaseInputVerifier extends InputVerifier {
    
    private boolean required = false;
    
    public BaseInputVerifier(boolean required) {
        super();
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
