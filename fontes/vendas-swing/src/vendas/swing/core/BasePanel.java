/*
 * BasePanel.java
 * 
 * Created on 15/07/2007, 15:07:15
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *
 * @author Sam
 */
public abstract class BasePanel extends JPanel {
    
    private Logger logger;

    public BasePanel() {
        logger = Logger.getLogger(getClass());
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public void init() {}

    public void object2Field(Object obj) {}
    
    public void field2Object(Object obj) {}

}
