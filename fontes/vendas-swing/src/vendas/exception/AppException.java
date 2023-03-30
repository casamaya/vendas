/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.exception;

/**
 *
 * @author joliveira
 */
public class AppException extends Exception {
    private static final long serialVersionUID = 2142225189340611524L;
    public AppException() {
        super();
    }
    public AppException(String msg) {
        super(msg);
    }
    public AppException(Throwable e) {
        super(e);
    }
}
