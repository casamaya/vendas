/*
 * ListComboBoxModel.java
 *
 * Created on 27 de Novembro de 2006, 19:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ritual.swing;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Jaime Oliveira
 */
public class ListComboBoxModel extends DefaultComboBoxModel {
    
    /** Creates a new instance of ListComboBoxModel 
     * @param items 
     */
    public ListComboBoxModel(List items) {
        super(items.toArray());
    }
    
    public ListComboBoxModel(String[] items) {
        super(items);
    }
    
    public ListComboBoxModel() {
        super();
    }
}
