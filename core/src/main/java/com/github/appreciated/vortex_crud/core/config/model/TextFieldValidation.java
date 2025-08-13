package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

/**
 * Validation for text-based components like TextField and TextArea.
 */
@GenerateBuilder
public class TextFieldValidation implements Validation {

    private int maxLength;

    public TextFieldValidation() {
    }

    public TextFieldValidation(int maxLength) {
        this.maxLength = maxLength;
    }

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

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public static class Builder {
        private final TextFieldValidation product;

        private Builder(TextFieldValidation product) {
            this.product = product;
        }

        public Builder withMaxLength(int maxLength) {
            product.maxLength = maxLength;
            return this;
        }

        public TextFieldValidation build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new TextFieldValidation());
    }
}