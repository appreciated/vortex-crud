package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class OneToMany<FieldId> {

    private FieldId referenceField;

    public FieldId getReferenceField() {
        return referenceField;
    }

    public void setReferenceField(FieldId referenceField) {
        this.referenceField = referenceField;
    }

    public OneToMany(FieldId referenceField) {
        this.referenceField = referenceField;
    }

    public static class Builder<FieldId> {

        private final OneToMany<FieldId> product;

        private Builder(OneToMany<FieldId> product) {
            this.product = product;
        }

        public static <FieldId> Builder<FieldId> of(FieldId referenceField) {
            return new Builder<>(new OneToMany<>(referenceField));
        }

        public Builder withReferenceField(FieldId referenceField) {
            product.referenceField = referenceField;
            return this;
        }

        public OneToMany build() {
            return product;
        }
    }
}
