package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.User;
import com.github.appreciated.vortex_crud.core.service.UserProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;
import java.util.function.Supplier;

@GenerateBuilder
public class UserManagement<T extends User> {

    private boolean enabled;

    private AccessControl accessControl;

    private boolean signUp;

    private List<AdditionalField> additionalFields;
    private Class<T> userClass;
    private Supplier<T> userSupplier;
    private UserProvider<T> userProvider;

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

    public Class<T> getUserClass() {
        return userClass;
    }

    public void setUserClass(Class<T> userClass) {
        this.userClass = userClass;
    }

    public Supplier<T> getUserSupplier() {
        return userSupplier;
    }

    public void setUserSupplier(Supplier<T> userSupplier) {
        this.userSupplier = userSupplier;
    }

    public UserProvider<T> getUserProvider() {
        return userProvider;
    }

    public void setUserProvider(UserProvider<T> userProvider) {
        this.userProvider = userProvider;
    }

    public static class Builder<T extends User> {

        private final UserManagement<T> product;

        private Builder(UserManagement<T> product) {
            this.product = product;
        }

        public Builder<T> withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder<T> withAccessControl(AccessControl accessControl) {
            product.accessControl = accessControl;
            return this;
        }

        public Builder<T> withSignUp(boolean signUp) {
            product.signUp = signUp;
            return this;
        }

        public Builder<T> withAdditionalFields(List<AdditionalField> additionalFields) {
            product.additionalFields = additionalFields;
            return this;
        }

        public Builder<T> addAdditionalField(AdditionalField item) {
            product.additionalFields.add(item);
            return this;
        }

        public Builder<T> withUserClass(Class<T> userClass) {
            product.userClass = userClass;
            return this;
        }

        public Builder<T> withUserSupplier(Supplier<T> userSupplier) {
            product.userSupplier = userSupplier;
            return this;
        }

        public Builder<T> withUserProvider(UserProvider<T> userProvider) {
            product.userProvider = userProvider;
            return this;
        }

        public UserManagement<T> build() {
            return product;
        }
    }

    public static <T extends User> Builder<T> of() {
        return new Builder<>(new UserManagement<>());
    }
}
