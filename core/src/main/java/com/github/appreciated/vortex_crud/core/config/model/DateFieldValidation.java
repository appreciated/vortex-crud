package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Validation for date picker components.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DateFieldValidation implements Validation {

    private LocalDate min;

    private LocalDate max;

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