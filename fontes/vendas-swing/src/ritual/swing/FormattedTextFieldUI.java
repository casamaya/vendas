/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.Element;
import javax.swing.text.View;

/**
 *
 * @author Sam
 */
public class FormattedTextFieldUI extends MetalTextFieldUI
        implements PropertyChangeListener {

    public static ComponentUI createUI(JComponent c) {
        return new FormattedTextFieldUI();
    }

    public FormattedTextFieldUI() {
        super();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        if (c instanceof FormattedTextField) {
            c.addPropertyChangeListener(this);
            editor = (FormattedTextField) c;
            formatSpec = editor.getFormatSpec();
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        c.removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(
                FormattedTextField.FORMAT_PROPERTY)) {
            // Install the new format specification
            formatSpec = editor.getFormatSpec();

            // Recreate the View hierarchy
            modelChanged();
        }
    }

    // ViewFactory method - creates a view 
    @Override
    public View create(Element elem) {
        return new FormattedFieldView(elem, formatSpec);
    }
    protected FormattedTextField.FormatSpec formatSpec;
    protected FormattedTextField editor;
}
