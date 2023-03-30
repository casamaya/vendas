/*
 * ViewFrame.java
 * 
 * Created on 12/07/2007, 15:16:12
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ritual.swing;

import java.util.ResourceBundle;

/**
 *
 * @author p993702
 */
public abstract class ViewFrame extends BaseFrame {
    
    Object vo;
    ViewFrame parentViewFrame;
    
    private ResourceBundle bundle = TApplication.getInstance().getBundle();

    public ViewFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        setLocation(0, 0);
    }
    
    public ViewFrame(String title) {
        super(title);
        setLocation(0, 0);
    }
    
    public ViewFrame() {
        super();
        setLocation(0, 0);
    }

    public Object getObject() {
        return vo;
    }

    public ViewFrame getParentViewFrame() {
        return parentViewFrame;
    }

    public void setParentViewFrame(ViewFrame parentViewFrame) {
        this.parentViewFrame = parentViewFrame;
    }

    public void execute(Object value) {
        vo = value;
        object2Field(vo);
    }
    
    protected void object2Field(Object obj) {}
    
    protected void field2Object(Object obj) {}
    
    public void insert() {}
    public void view() {}
    public void edit() {}
    public void remove() {}
    public void refresh() {}
    public void filter() {}
    public void cancelFilter() {}
    public void report() {}

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public Object getValueObject() {
        return vo;
    }

}
