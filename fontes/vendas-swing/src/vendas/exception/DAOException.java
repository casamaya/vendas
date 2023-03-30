/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.exception;

/**
 *
 * @author joliveira
 */
public class DAOException extends Exception {
    private static final long serialVersionUID = 2142225189340611524L;
    public DAOException() {
        super();
    }
    public DAOException(String msg) {
        super(msg);
    }
    public DAOException(Throwable e) {
        super(e);
    }
}
