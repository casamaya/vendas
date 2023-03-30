package ritual.swing;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

public class NumericTextField extends JTextField
        implements NumericPlainDocument.InsertErrorListener {

    public NumericTextField() {
        this(null, 0, null);
    }

    public NumericTextField(String text, int columns, DecimalFormat format) {
        super(null, text, columns);

        NumericPlainDocument numericDoc = (NumericPlainDocument) getDocument();
        if (format != null) {
            numericDoc.setFormat(format);
        }

        numericDoc.addInsertErrorListener(this);
    }

    public NumericTextField(int columns, DecimalFormat format) {
        this(null, columns, format);
    }

    public NumericTextField(String text) {
        this(text, 0, null);
    }

    public NumericTextField(String text, int columns) {
        this(text, columns, null);
    }

    public void setFormat(DecimalFormat format) {
        ((NumericPlainDocument) getDocument()).setFormat(format);
    }

    public DecimalFormat getFormat() {
        return ((NumericPlainDocument) getDocument()).getFormat();
    }

    public void formatChanged() {
        // Notify change of format attributes.
        setFormat(getFormat());
    }

    // Methods to get the field value
    public Long getLongValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getLongValue();
    }

    public BigDecimal getBigDecimalValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getBigDecimalValue();
    }

    public Double getDoubleValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getDoubleValue();
    }

    public Number getNumberValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getNumberValue();
    }

    // Methods to install numeric values
    public void setValue(Number number) {
        setText(getFormat().format(number));
    }

    public void setValue(BigDecimal number) {
        setText(getFormat().format(number));
    }

    public void setValue(long l) {
        setText(getFormat().format(l));
    }

    public void setValue(double d) {
        setText(getFormat().format(d));
    }

    public void normalize() throws ParseException {
        // format the value according to the format string
        setText(getFormat().format(getNumberValue()));
    }

    // Override to handle insertion error
    public void insertFailed(NumericPlainDocument doc, int offset,
            String str, AttributeSet a) {
        // By default, just beep
        Toolkit.getDefaultToolkit().beep();
    }

    // Method to create default model
    @Override
    protected Document createDefaultModel() {
        return new NumericPlainDocument();
    }

    // Test code
    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        format.setGroupingUsed(true);
        format.setGroupingSize(3);
        format.setParseIntegerOnly(false);

        JFrame f = new JFrame("Numeric Text Field Example");
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        final NumericTextField tf = new NumericTextField(10, format);

        tf.setValue((double) 123456.789);

        JLabel lbl = new JLabel("Type a number: ");
        f.getContentPane().add(tf, "East");
        f.getContentPane().add(lbl, "West");
        
        javax.swing.JButton b = new javax.swing.JButton("Ok");
        f.getContentPane().add(b, "South");

        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    tf.normalize();
                    Long l = tf.getLongValue();
                    System.out.println("Value is (Long)" + l);
                } catch (ParseException e1) {
                    try {
                        Double d = tf.getDoubleValue();
                        System.out.println("Value is (Double)" + d);
                    } catch (ParseException e2) {
                        System.out.println(e2);
                    }
                }
            }
        });
        f.pack();
        f.setVisible(true);
    }
}
