/*
 * ViewPanel.java
 * 
 * Created on 30/06/2007, 10:06:27
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import javax.swing.JPanel;

/**
 *
 * @author Sam
 */
public abstract class ViewPanel extends JPanel {
    
    public ViewPanel() {
    }
/*
    public void view(Object obj, String title) {
        String[] options = {"Selecionar", "Cancelar"};
        object2Field(obj);
        JOptionPane.showOptionDialog(this, this, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        field2Object(obj);
    }
*/
    protected void object2Field(Object obj) {}
    
    protected void field2Object(Object obj) {}
}
