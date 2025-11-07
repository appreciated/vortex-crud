package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Validation for number field components.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NumberFieldValidation implements Validation {

    private Double min;

    private Double max;

    private Double step;

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