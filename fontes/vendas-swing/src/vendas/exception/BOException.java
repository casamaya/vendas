/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.exception;

/**
 *
 * @author sam
 */
public class BOException extends Exception {

    public BOException() {
        super();
    }

    public BOException(String me) {
        super(me);
    }

    public BOException(Throwable e) {
        super(e);
    }
}
