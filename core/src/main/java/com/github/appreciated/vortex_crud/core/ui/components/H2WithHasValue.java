package com.github.appreciated.vortex_crud.core.ui.components;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.shared.Registration;

public class H2WithHasValue extends H2 implements HasValue<HasValue.ValueChangeEvent<String>, String> {

    @Override
    public void setValue(String value) {
        setText(value);
    }

    @Override
    public String getValue() {
        return getText();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener listener) {
        return null;
    }
}
