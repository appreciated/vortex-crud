package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.time.LocalDate;

/**
 * Validation for date picker components.
 */
@GenerateBuilder
public class DateFieldValidation implements Validation {
    
    private LocalDate min;
    private LocalDate max;
    
    public DateFieldValidation() {
    }
    
    public DateFieldValidation(LocalDate min, LocalDate max) {
        this.min = min;
        this.max = max;
    }
    
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
    
    public LocalDate getMin() {
        return min;
    }

    public void setMin(LocalDate min) {
        this.min = min;
    }
    
    public LocalDate getMax() {
        return max;
    }

    public void setMax(LocalDate max) {
        this.max = max;
    }
    
    public static class Builder {
        private final DateFieldValidation product;

        private Builder(DateFieldValidation product) {
            this.product = product;
        }

        public Builder withMin(LocalDate min) {
            product.min = min;
            return this;
        }
        
        public Builder withMax(LocalDate max) {
            product.max = max;
            return this;
        }

        public DateFieldValidation build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new DateFieldValidation());
    }
}