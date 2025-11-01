package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.Builder;
import lombok.With;

/**
 * Validation for number field components.
 */
@Builder(toBuilder = true)
@With
public record NumberFieldValidation(
    Double min,
    Double max,
    Double step
) implements Validation {

    @Override
    public void applyToComponent(Component component) {
        if (component instanceof NumberField numberField) {
            if (min != null) {
                numberField.setMin(min);
            }
            if (max != null) {
                numberField.setMax(max);
            }
            if (step != null) {
                numberField.setStep(step);
            }
        }
    }

    @Override
    public boolean isApplicableToComponent(Class<? extends Component> componentClass) {
        return NumberField.class.isAssignableFrom(componentClass);
    }
}