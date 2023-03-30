package vendas.util;

import javax.swing.JInternalFrame;

public class ViewFrame extends JInternalFrame {
    
    public ViewFrame() {
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
    }
    
    public ViewFrame(String title) {
        super(title, true, true, true, true);
    }

    public void insertRecord() {
    }

    public void editRecord() {
    }

    public void deleteRecord() {
    }

    public void refreshRecord() {
    }

    public void report() {
    }
    
}