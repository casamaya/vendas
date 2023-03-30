/*
 * ViewFrame.java
 * 
 * Created on 12/07/2007, 15:16:12
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ritual.swing;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.apache.log4j.Logger;

/**
 *
 * @author p993702
 */
public class BaseFrame extends JInternalFrame implements InternalFrameListener {
    
    private Logger logger;

    public BaseFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        logger = Logger.getLogger(getClass());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public BaseFrame(String title) {
        super(title);
        logger = Logger.getLogger(getClass());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public BaseFrame() {
        super();
        logger = Logger.getLogger(getClass());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
    }
    
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
