/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.core;

import org.apache.log4j.Logger;

/**
 *
 * @author p993702
 */
public class DesktopHelper {
    
    private Logger logger;

    public DesktopHelper() {
        logger = Logger.getLogger(getClass());        
    }
    
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
