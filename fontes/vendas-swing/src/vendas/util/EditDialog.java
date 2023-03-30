/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import ritual.swing.TApplication;
import vendas.swing.core.EditPanel;

/**
 *
 * @author Sam
 */
public class EditDialog extends JDialog implements ActionListener, PropertyChangeListener {

    private String btnString1;
    private String btnString2;
    private JOptionPane optionPane;
    private EditPanel editPanel;
    private boolean result = false;
    private Object[] options;
    private Object target;
    private Map erros;
    private Logger logger;
    private TApplication app = TApplication.getInstance();

    public EditDialog(String title) {
        setTitle(title);
        init();
    }

    public EditDialog(JFrame f, String title) {
        super(f, title);
        init();
    }

    private void init() {
        logger = Logger.getLogger(getClass());

        btnString1 = app.getResourceString("confirmar");
        btnString2 = app.getResourceString("cancelar");

        options = new Object[2];
        options[0] = btnString1;
        options[1] = btnString2;

        setModal(true);
        setResizable(false);

        //Handle window closing correctly. 
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                // Instead of directly closing the window, we're going to change the JOptionPane's value property.
                optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
            }
        });
        setLocation(0, 0);
    }

    public EditPanel getEditPanel() {
        return editPanel;
    }

    public Map getErros() {
        return erros;
    }

    public void setErros(Map erros) {
        this.erros = erros;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setEditPanel(EditPanel panel) {
        editPanel = panel;
        optionPane = new JOptionPane(editPanel,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        optionPane.addPropertyChangeListener(this);
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = app.getMainFrame().getSize();
        Point loc = app.getMainFrame().getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    }

    public void setEditPanel(EditPanel panel, Boolean isReadOnly) {
        if (isReadOnly) {
            options = new Object[1];
            options[0] = app.getResourceString("cancelar");
        }
        
        editPanel = panel;
        optionPane = new JOptionPane(editPanel,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        optionPane.addPropertyChangeListener(this);
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = app.getMainFrame().getSize();
        Point loc = app.getMainFrame().getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    }
    
    public void setEditPanel(EditPanel panel, Object[] buttons) {
        editPanel = panel;
        optionPane = new JOptionPane(editPanel,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                buttons,
                buttons[0]);
        setContentPane(optionPane);
        optionPane.addPropertyChangeListener(this);
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = app.getMainFrame().getSize();
        Point loc = app.getMainFrame().getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();
        logger.info(prop);
        if (isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {

                if (editPanel.entryValidate()) {
                    //we're done; clear and dismiss the dialog
                    result = true;
                    clearAndHide();
                } else {
                    String errorText;
                    if (erros == null) {
                        errorText = app.getResourceString("invalidEntry");
                    } else {
                        errorText = editPanel.getErrors().toString();
                    }
                    Messages.errorMessage(errorText);
                }
            } else { //user closed dialog or clicked cancel
                clearAndHide();
            }
        }
    }

    public void clearAndHide() {
        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
        setVisible(false);
    }

    public boolean edit(Object value, boolean isEditMode) {
        target = value;
        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
        result = false;
        pack();
//        setLocationRelativeTo(parent);
        editPanel.object2Field(value);
        editPanel.enableControls(isEditMode);
        setVisible(true);
        if (result) {
            try {
                editPanel.field2Object(value);
            } catch (Exception e) {
                logger.error("Erro lendo campos", e);
                result = false;
            }
        }
        return result;
    }

    public boolean edit(Object value) {
        return edit(value, true);
    }
}
