/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.swing;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Sam
 */
public class BoundedPlainDocument extends PlainDocument {
    
    private int mode = 2; //0 normal, 1 lowercase, 2 uppercase

    public BoundedPlainDocument() {
        // Default constructor - must use setMaxLength later
        this.maxLength = 0;
    }

    public BoundedPlainDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    public BoundedPlainDocument(AbstractDocument.Content content,
            int maxLength) {
        super(content);
        if (content.length() > maxLength) {
            throw new IllegalArgumentException("Initial content larger than maximum size");
        }
        this.maxLength = maxLength;
    }

    public void setMaxLength(int maxLength) {
        if (getLength() > maxLength) {
            throw new IllegalArgumentException("Current content larger than new maximum size");
        }

        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a)
            throws BadLocationException {
        if (str == null) {
            return;
        }

        // Note: be careful here - the content always has a
        // trailing newline, which should not be counted!
        int capacity = maxLength + 1 - getContent().length();
        if (capacity >= str.length()) {
            // It all fits
            switch (mode){
                case 1: str = str.toLowerCase(); break;
                case 2: str = str.toUpperCase(); break;
            }
            super.insertString(offset, str, a);
        } else {
            // It doesn't all fit. Add as much as we can.
            if (capacity > 0) {
                super.insertString(offset, str.substring(0, capacity), a);
            }

            // Finally, signal an error.
            if (errorListener != null) {
                errorListener.insertFailed(this, offset, str, a);
            }
        }
    }

    public void addInsertErrorListener(InsertErrorListener l) {
        if (errorListener == null) {
            errorListener = l;
            return;
        }
        throw new IllegalArgumentException(
                "InsertErrorListener already registered");
    }

    public void removeInsertErrorListener(InsertErrorListener l) {
        if (errorListener == l) {
            errorListener = null;
        }
    }

    public interface InsertErrorListener {

        public abstract void insertFailed(BoundedPlainDocument doc,
                int offset, String str, AttributeSet a);
    }
    protected InsertErrorListener errorListener;	// Unicast listener
    protected int maxLength;
}
