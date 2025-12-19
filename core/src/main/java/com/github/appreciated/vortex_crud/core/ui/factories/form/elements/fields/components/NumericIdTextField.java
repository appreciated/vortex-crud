package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.components;

import com.vaadin.flow.component.textfield.TextField;

/**
 * A read-only TextField component designed for displaying numeric ID values.
 * Automatically converts numeric values (Integer, Long, etc.) to strings for display.
 */
public class NumericIdTextField extends TextField {

    public NumericIdTextField() {
        super();
        setReadOnly(true);
    }

    /**
     * Sets the numeric value to display.
     * Converts the value to a string representation.
     *
     * @param value The numeric value (Integer, Long, etc.)
     */
    public void setNumericValue(Object value) {
        if (value == null) {
            setValue("");
        } else {
            setValue(value.toString());
        }
    }
}
