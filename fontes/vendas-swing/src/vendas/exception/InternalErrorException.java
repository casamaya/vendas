/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.exception;

/**
 *
 * @author sam
 */
public class InternalErrorException extends Exception {

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String me) {
        super(me);
    }

    public InternalErrorException(Throwable e) {
        super(e);
    }
}
