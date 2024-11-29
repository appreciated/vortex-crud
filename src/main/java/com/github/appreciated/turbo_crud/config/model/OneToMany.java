package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class OneToMany {

    private String referenceField;

    public String getReferenceField() {
        return referenceField;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }

    public static class Builder {

        private OneToMany product;

        private Builder(OneToMany product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new OneToMany());
        }

        public Builder withReferenceField(String referenceField) {
            product.referenceField = referenceField;
            return this;
        }

        public OneToMany build() {
            return product;
        }
    }
}
