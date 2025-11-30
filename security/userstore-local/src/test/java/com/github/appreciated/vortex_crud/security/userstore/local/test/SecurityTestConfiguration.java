package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.DefaultDialogFactoryRegistry;
import org.springframework.context.annotation.Primary;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.github.appreciated.vortex_crud.security.userstore.local.util.InMemoryDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityTestConfiguration {

    @Bean
    public InMemoryDataStore<TestUser> userDataStore() {
        return new InMemoryDataStore<>(TestUser.class);
    }

    @Bean
    public InMemoryDataStore<TestRole> roleDataStore() {
        return new InMemoryDataStore<>(TestRole.class);
    }

    @Bean
    @Primary
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DefaultDialogFactoryRegistry defaultDialogFactoryRegistry(
            VortexCrudConfigService configService,
            VortexCrudDataStoreFieldNameResolver resolver,
            VortexCrudForeignKeyResolutionStrategy foreignKeyResolutionStrategy,
            ManyToManyPersistenceStrategy manyToManyPersistenceStrategy,
            ReflectionService reflectionService,
            VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        return new DefaultDialogFactoryRegistry(
                configService,
                resolver,
                foreignKeyResolutionStrategy,
                manyToManyPersistenceStrategy,
                reflectionService,
                dataStoreUtil
        );
    }

    @Service
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class MockManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy {
        @Override
        public java.util.Collection resolveManyToMany(VortexCrudDataStore targetDataStore, ManyToMany manyToMany, Object sourceId) {
            return List.of();
        }

        @Override
        public void insert(Object sourceId, List targetObjects, ManyToMany manyToMany) {
        }

        @Override
        public void deleteAll(Object sourceId, List targetObjects, ManyToMany manyToMany) {
        }

        @Override
        public String getObjectId(Object object) {
            return null;
        }
    }

    @Service
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class MockForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy {
        @Override
        public void resolveForeignKey(Object entity, Object foreignKeyField, Object foreignKeyValue, VortexCrudDataStore dataStore, VortexCrudDataStoreFieldNameResolver fieldNameResolver) {
            // No-op for in-memory test
        }
    }

    @Service
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static class MockFieldNameResolver implements VortexCrudDataStoreFieldNameResolver {
        @Override
        public String getKeyForFieldType(Object fieldName) {
            return String.valueOf(fieldName);
        }
    }

    @Service
    public static class SecurityTestVortexCrudConfigurationProvider implements VortexCrudConfigurationProvider<Object, String, String> {

        private final InMemoryDataStore<TestUser> userDataStore;
        private final InMemoryDataStore<TestRole> roleDataStore;

        public SecurityTestVortexCrudConfigurationProvider(
                InMemoryDataStore<TestUser> userDataStore,
                InMemoryDataStore<TestRole> roleDataStore
        ) {
            this.userDataStore = userDataStore;
            this.roleDataStore = roleDataStore;
        }

        @Override
        public Application<Object, String, String> get() {

            DataStoreConfig<Object, String, String> userConfig = DataStoreConfig.<Object, String, String>builder()
                    .dataStoreInstance((VortexCrudDataStore) userDataStore)
                    .fields(Map.of(
                            "id", IdField.<Object, String, String>builder().build(),
                            "username", TextField.<Object, String, String>builder().build(),
                            "passwordHash", TextField.<Object, String, String>builder().build(),
                            "roles", TextField.<Object, String, String>builder().build(),
                            "publicField", TextField.<Object, String, String>builder().build(),
                            "adminField", TextField.<Object, String, String>builder().writeRoles(List.of("ADMIN")).readOnlyRoles(List.of("USER")).build(),
                            "secretField", TextField.<Object, String, String>builder().writeRoles(List.of("ADMIN")).readOnlyRoles(List.of("ADMIN")).build()
                    ))
                    .build();

            DataStoreConfig<Object, String, String> roleConfig = DataStoreConfig.<Object, String, String>builder()
                    .dataStoreInstance((VortexCrudDataStore) roleDataStore)
                    .fields(Map.of(
                            "id", IdField.<Object, String, String>builder().build(),
                            "name", TextField.<Object, String, String>builder().build()
                    ))
                    .build();

            FormRoute<Object, String, String> userForm = FormRoute.<Object, String, String>builder()
                    .dataStoreConfig(userConfig)
                    .title("route.users.title")
                    .formConfiguration(FormRendererConfiguration.<Object, String, String>builder()
                            .titleField("username")
                            .children(List.of(
                                    InternalFormElement.<Object, String, String>builder().field("username").label("Username").build(),
                                    InternalFormElement.<Object, String, String>builder().field("publicField").label("Public Field").build(),
                                    InternalFormElement.<Object, String, String>builder().field("adminField").label("Admin Field").build(),
                                    InternalFormElement.<Object, String, String>builder().field("secretField").label("Secret Field").build()
                            ))
                            .build())
                    .build();

            LinkedHashMap<String, RouteRenderer<Object, String, String>> routes = new LinkedHashMap<>();
            routes.put("users-grid", GridRoute.<Object, String, String>builder()
                    .dataStoreConfig(userConfig)
                    .title("route.users-grid")
                    .configuration(GridItemRendererConfiguration.<Object, String, String>builder()
                            .titleField("username")
                            .build())
                    .child(userForm)
                    .writeRoles(List.of("ADMIN", "USER"))
                    .readOnlyRoles(List.of("VIEWER"))
                    .build());

            return Application.<Object, String, String>builder()
                    .applicationName("application.name")
                    .i18nBundlePrefix("ui_test_i18n")
                    .routes(routes)
                    .identityAndAccessManagement(LocalIdentityAndAccessManagement.<Object, String, String>builder()
                            .dataStoreConfig(userConfig)
                            .username(InternalFormElement.<Object, String, String>builder().field("username").build())
                            .password(InternalFormElement.<Object, String, String>builder().field("passwordHash").build())
                            .rolesField("roles")
                            .availableRoles(new Roles(List.of("ADMIN", "USER", "VIEWER")))
                            .loginView(LoginView.class)
                            .signUpView(SignUpView.class)
                            .signUpEnabled(true)
                            .build())
                    .build();
        }
    }

    @Service
    public static class SecurityTestVortexCrudConfigService implements VortexCrudConfigService<Object, String, String> {

        private final Application<Object, String, String> configuration;

        public SecurityTestVortexCrudConfigService(VortexCrudConfigurationProvider<Object, String, String> configurationProvider) {
            this.configuration = configurationProvider.get();
        }

        @Override
        public Application<Object, String, String> configuration() {
            return configuration;
        }

        @Override
        public String applicationName() {
            return configuration.applicationName();
        }
    }
}
