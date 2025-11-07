package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.vaadin.flow.component.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> implements IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    private final RepositoryType repositoryKey;
    private final InternalFormElement<ModelClass, FieldType, RepositoryType> username;
    private final InternalFormElement<ModelClass, FieldType, RepositoryType> password;
    private final FieldType rolesField;
    private final List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
    private final Class<? extends Component> loginView;
    private final Class<? extends Component> signUpView;
    private final Roles roles;
    private final boolean signUpEnabled;

    // Dependencies for runtime user context resolution
    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
    private final ReflectionService<FieldType> reflectionService;

    private LocalIdentityAndAccessManagement(
            RepositoryType repositoryKey,
            InternalFormElement<ModelClass, FieldType, RepositoryType> username,
            InternalFormElement<ModelClass, FieldType, RepositoryType> password,
            FieldType rolesField,
            List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields,
            Class<? extends Component> loginView,
            Class<? extends Component> signUpView,
            Roles roles,
            boolean signUpEnabled,
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            ReflectionService<FieldType> reflectionService) {
        this.repositoryKey = repositoryKey;
        this.username = username;
        this.password = password;
        this.rolesField = rolesField;
        this.signUpFields = signUpFields;
        this.loginView = loginView;
        this.signUpView = signUpView;
        this.roles = roles;
        this.signUpEnabled = signUpEnabled;
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.reflectionService = reflectionService;
    }

    @Override
    public List<SimpleGrantedAuthority> resolveRolesForEntity(ReflectionService<FieldType> reflectionService, Object userEntity) {
        if (userEntity == null || rolesField == null) {
            return Collections.emptyList();
        }
        List<VortexCrudRoleProvider> value = (List<VortexCrudRoleProvider>) reflectionService.getValue(userEntity, rolesField);
        if (value == null) {
            return Collections.emptyList();
        }
        return value.stream().map(VortexCrudRoleProvider::getRole).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isSignUpEnabled() {
        return signUpEnabled;
    }

    @Override
    public Object getCurrentUserEntity() {
        if (dataStoreFactoryRegistry == null || reflectionService == null) {
            return null;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        String currentUsername = auth.getName();
        if (currentUsername == null) {
            return null;
        }

        try {
            VortexCrudDataStore<FieldType, Object> dataStore =
                    (VortexCrudDataStore<FieldType, Object>) dataStoreFactoryRegistry.getDataStore(repositoryKey);

            FieldType usernameField = username.field();
            List<Object> users = dataStore.getRecordsFromTableWhereColumnEquals(usernameField, currentUsername, 0, 1);

            return users.isEmpty() ? null : users.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<String> getCurrentUserRoles() {
        Object userEntity = getCurrentUserEntity();
        if (userEntity == null) {
            return Collections.emptySet();
        }

        List<SimpleGrantedAuthority> authorities = resolveRolesForEntity(reflectionService, userEntity);
        return authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean currentUserHasRole(String role) {
        if (role == null) {
            return false;
        }
        return getCurrentUserRoles().contains(role);
    }

    // Getters for all fields
    @Override
    public RepositoryType getRepositoryKey() {
        return repositoryKey;
    }

    @Override
    public InternalFormElement<ModelClass, FieldType, RepositoryType> getUsername() {
        return username;
    }

    @Override
    public InternalFormElement<ModelClass, FieldType, RepositoryType> getPassword() {
        return password;
    }

    @Override
    public FieldType getRolesField() {
        return rolesField;
    }

    @Override
    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getSignUpFields() {
        return signUpFields;
    }

    @Override
    public Class<? extends Component> getLoginView() {
        return loginView;
    }

    @Override
    public Class<? extends Component> getSignUpView() {
        return signUpView;
    }

    @Override
    public Roles getRoles() {
        return roles;
    }

    public VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> getDataStoreFactoryRegistry() {
        return dataStoreFactoryRegistry;
    }

    public ReflectionService<FieldType> getReflectionService() {
        return reflectionService;
    }

    public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> builder() {
        return new Builder<>();
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {
        private RepositoryType repositoryKey;
        private InternalFormElement<ModelClass, FieldType, RepositoryType> username;
        private InternalFormElement<ModelClass, FieldType, RepositoryType> password;
        private FieldType rolesField;
        private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields;
        private Class<? extends Component> loginView;
        private Class<? extends Component> signUpView;
        private Roles roles;
        private boolean signUpEnabled = false;
        private VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
        private ReflectionService<FieldType> reflectionService;

        public Builder<ModelClass, FieldType, RepositoryType> repositoryKey(RepositoryType repositoryKey) {
            this.repositoryKey = repositoryKey;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> username(InternalFormElement<ModelClass, FieldType, RepositoryType> username) {
            this.username = username;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> password(InternalFormElement<ModelClass, FieldType, RepositoryType> password) {
            this.password = password;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> rolesField(FieldType rolesField) {
            this.rolesField = rolesField;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> signUpFields(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields) {
            this.signUpFields = signUpFields;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> loginView(Class<? extends Component> loginView) {
            this.loginView = loginView;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> signUpView(Class<? extends Component> signUpView) {
            this.signUpView = signUpView;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> roles(Roles roles) {
            this.roles = roles;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> signUpEnabled(boolean signUpEnabled) {
            this.signUpEnabled = signUpEnabled;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry(VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry) {
            this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> reflectionService(ReflectionService<FieldType> reflectionService) {
            this.reflectionService = reflectionService;
            return this;
        }

        public LocalIdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> build() {
            return new LocalIdentityAndAccessManagement<>(
                    repositoryKey,
                    username,
                    password,
                    rolesField,
                    signUpFields,
                    loginView,
                    signUpView,
                    roles,
                    signUpEnabled,
                    dataStoreFactoryRegistry,
                    reflectionService
            );
        }
    }
}
