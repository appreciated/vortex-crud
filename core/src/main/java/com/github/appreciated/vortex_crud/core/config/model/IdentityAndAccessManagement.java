package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class IdentityAndAccessManagement<KeyType> {

    private final KeyType repositoryKey;

    public IdentityAndAccessManagement(KeyType repositoryKey) {
        this.repositoryKey = repositoryKey;
    }

    private Roles roles;

    private boolean signUp;

    private List<AdditionalField> additionalFields;

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public boolean isSignUp() {
        return signUp;
    }

    public void setSignUp(boolean signUp) {
        this.signUp = signUp;
    }

    public List<AdditionalField> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(List<AdditionalField> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public KeyType getRepositoryKey() {
        return repositoryKey;
    }

    public static class Builder<KeyType> {

        private final IdentityAndAccessManagement<KeyType> product;

        private Builder(IdentityAndAccessManagement<KeyType> product) {
            this.product = product;
        }

        public Builder<KeyType> withRoles(Roles roles) {
            product.roles = roles;
            return this;
        }

        public Builder<KeyType> withSignUp(boolean signUp) {
            product.signUp = signUp;
            return this;
        }

        public Builder<KeyType> withAdditionalFields(List<AdditionalField> additionalFields) {
            product.additionalFields = additionalFields;
            return this;
        }

        public Builder<KeyType> addAdditionalField(AdditionalField item) {
            product.additionalFields.add(item);
            return this;
        }

        public IdentityAndAccessManagement<KeyType> build() {
            return product;
        }
    }

    public static <KeyType> Builder<KeyType> of(KeyType keyType) {
        return new Builder<>(new IdentityAndAccessManagement<>(keyType));
    }
}
