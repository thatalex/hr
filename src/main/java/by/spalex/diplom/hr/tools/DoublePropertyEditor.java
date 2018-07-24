package by.spalex.diplom.hr.tools;

import java.beans.PropertyEditorSupport;

/**
 * Class for parsing double values from pages
 */
public class DoublePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        double value = 0.0;
        text = text.replaceAll(",", ".");
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
        }
        super.setValue(value);
    }
}
