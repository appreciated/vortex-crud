package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

/**
 * Validation for number field components.
 */
@GenerateBuilder
public class NumberFieldValidation implements Validation {

    private Double min;
    private Double max;
    private Double step;

    public NumberFieldValidation() {
    }

    public NumberFieldValidation(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

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

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    public static class Builder {
        private final NumberFieldValidation product;

        private Builder(NumberFieldValidation product) {
            this.product = product;
        }

        public Builder withMin(Double min) {
            product.min = min;
            return this;
        }

        public Builder withMax(Double max) {
            product.max = max;
            return this;
        }

        public Builder withStep(Double step) {
            product.step = step;
            return this;
        }

        public NumberFieldValidation build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new NumberFieldValidation());
    }
}