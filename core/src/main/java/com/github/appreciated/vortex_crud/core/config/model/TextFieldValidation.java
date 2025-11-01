package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Builder;
import lombok.With;

/**
 * Validation for text-based components like TextField and TextArea.
 */
@Builder(toBuilder = true)
@With
public record TextFieldValidation(
    int maxLength
) implements Validation {

    @Override
    public void applyToComponent(Component component) {
        if (component instanceof TextField textField && maxLength > 0) {
            textField.setMaxLength(maxLength);
        } else if (component instanceof TextArea textArea && maxLength > 0) {
            textArea.setMaxLength(maxLength);
        }
    }

    @Override
    public boolean isApplicableToComponent(Class<? extends Component> componentClass) {
        return TextField.class.isAssignableFrom(componentClass) ||
               TextArea.class.isAssignableFrom(componentClass);
    }
}