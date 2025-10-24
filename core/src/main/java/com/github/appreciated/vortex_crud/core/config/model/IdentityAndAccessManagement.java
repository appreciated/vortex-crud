package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.login.LoginFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    private final RepositoryType repositoryKey;
    private InternalFormElement<ModelClass, FieldType, RepositoryType> username;
    private InternalFormElement<ModelClass, FieldType, RepositoryType> password;
    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;

    public IdentityAndAccessManagement(RepositoryType repositoryKey) {
        super((Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) LoginFactory.class);
        this.repositoryKey = repositoryKey;
    }

    private Roles roles;

    private boolean signUp;

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public InternalFormElement<ModelClass, FieldType, RepositoryType> getUsername() {
        return username;
    }

    public void setUsername(InternalFormElement<ModelClass, FieldType, RepositoryType> username) {
        this.username = username;
    }

    public InternalFormElement<ModelClass, FieldType, RepositoryType> getPassword() {
        return password;
    }

    public void setPassword(InternalFormElement<ModelClass, FieldType, RepositoryType> password) {
        this.password = password;
    }

    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getSignUpFields() {
        return signUpFields;
    }

    public void setSignUpFields(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields) {
        this.signUpFields = signUpFields;
    }

    public boolean isSignUp() {
        return signUp;
    }

    public void setSignUp(boolean signUp) {
        this.signUp = signUp;
    }

    public RepositoryType getRepositoryKey() {
        return repositoryKey;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> product;

        private Builder(IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withRoles(Roles roles) {
            product.roles = roles;
            return this;
        }

        @SafeVarargs
        public final <T extends InternalFormElement<ModelClass, FieldType, RepositoryType>>  Builder<ModelClass, FieldType, RepositoryType> withSignUpFields(
                 T ... signUpFields) {
            product.signUpFields = List.of(signUpFields);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withSignUp(boolean signUp) {
            product.signUp = signUp;
            return this;
        }

        public <T extends InternalFormElement<ModelClass, FieldType, RepositoryType>> Builder<ModelClass, FieldType, RepositoryType> withUsername(T username) {
            product.username = username;
            return this;
        }

        public <T extends InternalFormElement<ModelClass, FieldType, RepositoryType>> Builder<ModelClass, FieldType, RepositoryType> withPassword(T password) {
            product.password = password;
            return this;
        }

        public IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }

    public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> of(RepositoryType keyType) {
        return new Builder<>(new IdentityAndAccessManagement<>(keyType));
    }
}
