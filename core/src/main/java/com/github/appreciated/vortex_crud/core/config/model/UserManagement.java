package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class UserManagement {

    private boolean enabled;

    private AccessControl accessControl;

    private boolean signUp;

    private List<AdditionalField> additionalFields;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControl accessControl) {
        this.accessControl = accessControl;
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

    public static class Builder {

        private final UserManagement product;

        private Builder(UserManagement product) {
            this.product = product;
        }

        public Builder withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder withAccessControl(AccessControl accessControl) {
            product.accessControl = accessControl;
            return this;
        }

        public Builder withSignUp(boolean signUp) {
            product.signUp = signUp;
            return this;
        }

        public Builder withAdditionalFields(List<AdditionalField> additionalFields) {
            product.additionalFields = additionalFields;
            return this;
        }

        public Builder addAdditionalField(AdditionalField item) {
            product.additionalFields.add(item);
            return this;
        }

        public UserManagement build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new UserManagement());
    }
}
