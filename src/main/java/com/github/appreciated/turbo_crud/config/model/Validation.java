package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class Validation {

    private int maxLength;

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public static class Builder {

        private Validation product;

        private Builder(Validation product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Validation());
        }

        public Builder withMaxLength(int maxLength) {
            product.maxLength = maxLength;
            return this;
        }

        public Validation build() {
            return product;
        }
    }
}
