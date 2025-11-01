package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import lombok.Builder;
import lombok.With;

import java.time.LocalDate;

/**
 * Validation for date picker components.
 */
@Builder(toBuilder = true)
@With
public record DateFieldValidation(
    LocalDate min,
    LocalDate max
) implements Validation {

    @Override
    public void applyToComponent(Component component) {
        if (component instanceof DatePicker datePicker) {
            if (min != null) {
                datePicker.setMin(min);
            }
            if (max != null) {
                datePicker.setMax(max);
            }
        }
    }

    @Override
    public boolean isApplicableToComponent(Class<? extends Component> componentClass) {
        return DatePicker.class.isAssignableFrom(componentClass);
    }
}