/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.swing;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

/**
 *
 * @author Sam
 */
public class FormattedTextField extends JTextField {

    public FormattedTextField() {
        this(null, null, 0, null);
    }

    public FormattedTextField(String text, FormatSpec spec) {
        this(null, text, 0, spec);
    }

    public FormattedTextField(int columns, FormatSpec spec) {
        this(null, null, columns, spec);
    }

    public FormattedTextField(String text, int columns, FormatSpec spec) {
        this(null, text, columns, spec);
    }

    public FormattedTextField(Document doc, String text, int columns, FormatSpec spec) {
        super(doc, text, columns);
        setFont(new Font("monospaced", Font.PLAIN, 14));
        if (spec != null) {
            setFormatSpec(spec);
        }
    }

    @Override
    public void updateUI() {
        setUI(new FormattedTextFieldUI());
    }

    public FormatSpec getFormatSpec() {
        return formatSpec;
    }

    public void setFormatSpec(FormattedTextField.FormatSpec formatSpec) {
        FormatSpec oldFormatSpec = this.formatSpec;

        // Do nothing if no change to the format specification
        if (formatSpec.equals(oldFormatSpec) == false) {
            this.formatSpec = formatSpec;

            // Limit the input to the number of markers.
            Document doc = getDocument();
            if (doc instanceof BoundedPlainDocument) {
                ((BoundedPlainDocument) doc).setMaxLength(formatSpec.getMarkerCount());
            }

            // Notify a change in the format spec
            firePropertyChange(FORMAT_PROPERTY, oldFormatSpec, formatSpec);
        }
    }

    // Use a model that bounds the input length
    @Override
    protected Document createDefaultModel() {
        BoundedPlainDocument doc = new BoundedPlainDocument();

        doc.addInsertErrorListener(
                new BoundedPlainDocument.InsertErrorListener() {

                    public void insertFailed(BoundedPlainDocument doc, int offset,
                            String str, AttributeSet a) {
                        // Beep when the field is full
                        Toolkit.getDefaultToolkit().beep();
                    }
                });
        return doc;
    }

    public static class FormatSpec {

        public FormatSpec(String format, String mask) {
            this.format = format;
            this.mask = mask;
            this.formatSize = format.length();
            if (formatSize != mask.length()) {
                throw new IllegalArgumentException(
                        "Format and mask must be the same size");
            }

            for (int i = 0; i < formatSize; i++) {
                if (mask.charAt(i) == MARKER_CHAR) {
                    markerCount++;
                }
            }
        }

        public String getFormat() {
            return format;
        }

        public String getMask() {
            return mask;
        }

        public int getFormatSize() {
            return formatSize;
        }

        public int getMarkerCount() {
            return markerCount;
        }

        public boolean equals(Object fmt) {
            return fmt != null &&
                    (fmt instanceof FormatSpec) &&
                    ((FormatSpec) fmt).getFormat().equals(format) &&
                    ((FormatSpec) fmt).getMask().equals(mask);
        }

        public String toString() {
            return "FormatSpec with format <" + format + ">, mask <" + mask + ">";
        }
        private String format;
        private String mask;
        private int formatSize;
        private int markerCount;
        public static final char MARKER_CHAR = '*';
    }
    protected FormatSpec formatSpec;
    public static final String FORMAT_PROPERTY = "format";
}
