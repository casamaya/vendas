/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ritual.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 *
 * @author Sam
 */
public class FormattedFieldView extends FieldView {

    public FormattedFieldView(Element elem,
            FormattedTextField.FormatSpec formatSpec) {
        super(elem);

        this.contentBuff = new Segment();
        this.measureBuff = new Segment();
        this.workBuff = new Segment();
        this.element = elem;

        buildMapping(formatSpec);	// Build the model -> view map
        createContent();			// Update content string
    }

    // View methods start here 
    @Override
    public float getPreferredSpan(int axis) {
        int widthFormat;
        int widthContent;

        if (formatSize == 0 || axis == View.Y_AXIS) {
            return super.getPreferredSpan(axis);
        }

        widthFormat = Utilities.getTabbedTextWidth(measureBuff,
                getFontMetrics(), 0, this, 0);
        widthContent = Utilities.getTabbedTextWidth(contentBuff,
                getFontMetrics(), 0, this, 0);

        return Math.max(widthFormat, widthContent);
    }

    @Override
    public Shape modelToView(int pos, Shape a, Position.Bias b)
            throws BadLocationException {
        a = adjustAllocation(a);
        Rectangle r = new Rectangle(a.getBounds());
        FontMetrics fm = getFontMetrics();
        r.height = fm.getHeight();

        int oldCount = contentBuff.count;

        if (pos < offsets.length) {
            contentBuff.count = offsets[pos];
        } else {
            // Beyond the end: point to the location
            // after the last model position.
            contentBuff.count = offsets[offsets.length - 1] + 1;
        }

        int offset = Utilities.getTabbedTextWidth(contentBuff, metrics,
                0, this, element.getStartOffset());
        contentBuff.count = oldCount;

        r.x += offset;
        r.width = 1;

        return r;
    }

    @Override
    public int viewToModel(float fx, float fy, Shape a,
            Position.Bias[] bias) {
        a = adjustAllocation(a);
        bias[0] = Position.Bias.Forward;

        int x = (int) fx;
        int y = (int) fy;
        Rectangle r = a.getBounds();
        int startOffset = element.getStartOffset();
        int endOffset = element.getEndOffset();

        if (y < r.y || x < r.x) {
            return startOffset;
        } else if (y > r.y + r.height || x > r.x + r.width) {
            return endOffset - 1;
        }

        // The given position is within the bounds of the view.
        int offset = Utilities.getTabbedTextOffset(contentBuff,
                getFontMetrics(), r.x, x, this,
                startOffset);
        // The offset includes characters not in the model,
        // so get rid of them to return a true model offset.
        for (int i = 0; i < offsets.length; i++) {
            if (offset <= offsets[i]) {
                offset = i;
                break;
            }
        }

        // Don't return an offset beyond the data
        // actually in the model.
        if (offset > endOffset - 1) {
            offset = endOffset - 1;
        }
        return offset;
    }

    @Override
    public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        super.insertUpdate(changes, adjustAllocation(a), f);
        createContent();		// Update content string
    }

    @Override
    public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
        super.removeUpdate(changes, adjustAllocation(a), f);
        createContent();		// Update content string
    }
    // End of View methods

    // View drawing methods: overridden from PlainView
    @Override
    protected void drawLine(int line, Graphics g, int x, int y) {
        // Set the colors
        JTextComponent host = (JTextComponent) getContainer();
        unselected = (host.isEnabled()) ? host.getForeground() : host.getDisabledTextColor();
        Caret c = host.getCaret();
        selected = c.isSelectionVisible() ? host.getSelectedTextColor() : unselected;

        int p0 = element.getStartOffset();
        int p1 = element.getEndOffset() - 1;
        int sel0 = ((JTextComponent) getContainer()).getSelectionStart();
        int sel1 = ((JTextComponent) getContainer()).getSelectionEnd();

        try {
            // If the element is empty or there is no selection
            // in this view, just draw the whole thing in one go.
            if (p0 == p1 || sel0 == sel1 ||
                    inView(p0, p1, sel0, sel1) == false) {
                drawUnselectedText(g, x, y, 0, contentBuff.count);
                return;
            }

            // There is a selection in this view. Draw up to three regions:
            //	(a)	The unselected region before the selection.
            //	(b)	The selected region.
            //	(c)	The unselected region after the selection.
            // First, map the selected region offsets to be relative
            // to the start of the region and then map them to view
            // offsets so that they take into account characters not
            // present in the model.
            int mappedSel0 = mapOffset(Math.max(sel0 - p0, 0));
            int mappedSel1 = mapOffset(Math.min(sel1 - p0, p1 - p0));

            if (mappedSel0 > 0) {
                // Draw an initial unselected region
                x = drawUnselectedText(g, x, y, 0, mappedSel0);
            }
            x = drawSelectedText(g, x, y, mappedSel0, mappedSel1);

            if (mappedSel1 < contentBuff.count) {
                drawUnselectedText(g, x, y, mappedSel1, contentBuff.count);
            }
        } catch (BadLocationException e) {
        // Should not happen!
        }
    }

    @Override
    protected int drawUnselectedText(Graphics g, int x, int y,
            int p0, int p1) throws BadLocationException {
        g.setColor(unselected);
        workBuff.array = contentBuff.array;
        workBuff.offset = p0;
        workBuff.count = p1 - p0;
        return Utilities.drawTabbedText(workBuff, x, y, g, this, p0);
    }

    @Override
    protected int drawSelectedText(Graphics g, int x,
            int y, int p0, int p1) throws BadLocationException {
        workBuff.array = contentBuff.array;
        workBuff.offset = p0;
        workBuff.count = p1 - p0;
        g.setColor(selected);
        return Utilities.drawTabbedText(workBuff, x, y, g, this, p0);
    }
    // End of View drawing methods

    // Build the model-to-view mapping
    protected void buildMapping(
            FormattedTextField.FormatSpec formatSpec) {
        formatSize = formatSpec != null ? formatSpec.getFormatSize() : 0;

        if (formatSize != 0) {
            // Save the format string as a character array
            formatChars = formatSpec.getFormat().toCharArray();

            // Allocate a buffer to store the formatted string
            formattedContent = new char[formatSize];
            contentBuff.offset = 0;
            contentBuff.count = formatSize;
            contentBuff.array = formattedContent;

            // Keep the mask for computing
            // the preferred horizontal span, but use 
            // a wide character for measurement
            char[] maskChars = formatSpec.getMask().toCharArray();
            measureBuff.offset = 0;
            measureBuff.array = maskChars;
            measureBuff.count = formatSize;

            // Get the number of markers
            markerCount = formatSpec.getMarkerCount();

            // Allocate an array to hold the offsets
            offsets = new int[markerCount];

            // Create the offset array
            markerCount = 0;
            for (int i = 0; i < formatSize; i++) {
                if (maskChars[i] ==
                        FormattedTextField.FormatSpec.MARKER_CHAR) {
                    offsets[markerCount++] = i;

                    // Replace marker with a wide character
                    // in the array used for measurement.
                    maskChars[i] = WIDE_CHARACTER;
                }
            }
        }
    }

    // Use the document content and the format
    // string to build the display content		
    protected void createContent() {
        try {
            Document doc = getDocument();
            int startOffset = element.getStartOffset();
            int endOffset = element.getEndOffset();
            int length = endOffset - startOffset - 1;

            // If there is no format, use the raw data.
            if (formatSize != 0) {
                // Get the document content
                doc.getText(startOffset, length, workBuff);

                // Initialize the output buffer with the
                // format string.
                System.arraycopy(formatChars, 0, formattedContent, 0,
                        formatSize);

                // Insert the model content into
                // the target string.
                int count = Math.min(length, markerCount);
                int firstOffset = workBuff.offset;

                // Place the model data into the output array
                for (int i = 0; i < count; i++) {
                    formattedContent[offsets[i]] = workBuff.array[i + firstOffset];
                }
            } else {
                doc.getText(startOffset, length, contentBuff);
            }
        } catch (BadLocationException bl) {
            contentBuff.count = 0;
        }
    }

    // Map a document offset to a view offset.
    protected int mapOffset(int pos) {
        pos -= element.getStartOffset();
        if (pos >= offsets.length) {
            return contentBuff.count;
        } else {
            return offsets[pos];
        }
    }

    // Determines whether the selection intersects
    // a given range of model offsets.
    protected boolean inView(int p0, int p1, int sel0, int sel1) {
        if (sel0 >= p0 && sel0 < p1) {
            return true;
        }

        if (sel0 < p0 && sel1 >= p0) {
            return true;
        }

        return false;
    }
    protected char[] formattedContent;	// The formatted content for display
    protected char[] formatChars;		// The format string as characters
    protected Segment contentBuff;		// Segment pointing to formatted content
    protected Segment measureBuff;		// Segment pointing to mask string
    protected Segment workBuff;			// Segment used for scratch purposes
    protected Element element;			// The mapped element
    protected int[] offsets;			// Model-to-view offsets
    protected Color selected;			// Selected text color
    protected Color unselected;			// Unselected text color
    protected int formatSize;			// Length of the formatting string
    protected int markerCount;			// Number of markers in the format
    protected static final char WIDE_CHARACTER = 'm';
}
