/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.util;

import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import ritual.swing.TApplication;

/**
 *
 * @author Sam
 */
public class Messages {

    static ResourceBundle bundle = TApplication.getInstance().getBundle();

    public static boolean deleteQuestion() {
        Object[] options = {bundle.getString("excluir"), bundle.getString("naoExcluir")};
        int n = JOptionPane.showOptionDialog(null, bundle.getString("deleteConfirmMessage"), bundle.getString("deleteTitle"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        return n == 0;
    }
    
    public static int queryQuestion(Object[] options, String msg) {
        int n = JOptionPane.showOptionDialog(null, msg, bundle.getString("selecionar"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return n;
    }

    public static boolean confirmQuestion(String msg) {
        Object[] options = {bundle.getString("confirmar"), bundle.getString("cancelar")};
        int n = JOptionPane.showOptionDialog(null, msg, bundle.getString("confirmTitle"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        return n == 0;
    }

    public static void errorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, bundle.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
    }
    
    public static void warningMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, bundle.getString("warningTitle"), JOptionPane.WARNING_MESSAGE);
    }

    public static void infoMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, bundle.getString("warningTitle"), JOptionPane.INFORMATION_MESSAGE);
    }
}
