package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@GenerateBuilder
public class LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> implements IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    private final RepositoryType repositoryKey;
    private InternalFormElement<ModelClass, FieldType, RepositoryType> username;
    private InternalFormElement<ModelClass, FieldType, RepositoryType> password;
    private FieldType rolesField;
    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
    private Class<? extends Component> loginView;
    private Class<? extends Component> signUpView;

    public LocalIdentityAndAccessManagement(RepositoryType repositoryKey) {
        this.repositoryKey = repositoryKey;
    }

    private Roles roles;

    private boolean isSignUpEnabled;

    public Roles getRoles() {
        return roles;
    }

    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) reflectionService.getValue(userEntity, rolesField);
        return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
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

    @Override
    public boolean isSignUpEnabled() {
        return isSignUpEnabled;
    }

    public void setSignUpEnabled(boolean signUpEnabled) {
        this.isSignUpEnabled = signUpEnabled;
    }

    public RepositoryType getRepositoryKey() {
        return repositoryKey;
    }

    public Class<? extends Component> getLoginView() {
        return loginView;
    }

    public Class<? extends Component> getSignUpView() {
        return signUpView;
    }

    @Override
    public FieldType getRolesField() {
        return rolesField;
    }

    public void setRolesField(FieldType rolesField) {
        this.rolesField = rolesField;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> product;

        private Builder(LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withRoles(Roles roles) {
            product.roles = roles;
            return this;
        }

        public <T extends InternalFormElement<ModelClass, FieldType, RepositoryType>> Builder<ModelClass, FieldType, RepositoryType> withRolesField(FieldType rolesField) {
            product.rolesField = rolesField;
            return this;
        }

        @SafeVarargs
        public final Builder<ModelClass, FieldType, RepositoryType> withSignUpFields(
                InternalFormElement<ModelClass, FieldType, RepositoryType>... signUpFields) {
            product.signUpFields = List.of(signUpFields);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withSignUp(boolean signUp) {
            product.isSignUpEnabled = signUp;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withSignUpView(Class<? extends Component> signUpView) {
            product.signUpView = signUpView;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withLoginView(Class<? extends Component> loginView) {
            product.loginView = loginView;
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

        public LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }

    public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> of(RepositoryType keyType) {
        return new Builder<>(new LocalIdentityAndAccessManagement<>(keyType));
    }
}
