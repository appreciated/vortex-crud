package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityIntegrationTestConfiguration implements VortexCrudConfigurationProvider<Object, String, String> {

    private final VortexCrudDataStore<String, Object> userDataStore;
    private final VortexCrudDataStore<String, Object> roleDataStore;

    public SecurityIntegrationTestConfiguration(
            @Qualifier("userDataStore") VortexCrudDataStore<String, Object> userDataStore,
            @Qualifier("roleDataStore") VortexCrudDataStore<String, Object> roleDataStore
    ) {
        this.userDataStore = userDataStore;
        this.roleDataStore = roleDataStore;
    }

    @Override
    public Application<Object, String, String> get() {

        DataStoreConfig<Object, String, String> userConfig = DataStoreConfig.<Object, String, String>builder()
                .dataStoreInstance(userDataStore)
                .fields(Map.of(
                        "id", NumericIdField.<Object, String, String>builder().build(),
                        "username", TextField.<Object, String, String>builder().build(),
                        "passwordHash", TextField.<Object, String, String>builder().build(),
                        "roles", TextField.<Object, String, String>builder().build(),
                        "publicField", TextField.<Object, String, String>builder().build(),
                        "adminField", TextField.<Object, String, String>builder().writeRoles(List.of("ADMIN")).readOnlyRoles(List.of("USER")).build(),
                        "secretField", TextField.<Object, String, String>builder().writeRoles(List.of("ADMIN")).readOnlyRoles(List.of("ADMIN")).build()
                ))
                .build();

        DataStoreConfig<Object, String, String> roleConfig = DataStoreConfig.<Object, String, String>builder()
                .dataStoreInstance(roleDataStore)
                .fields(Map.of(
                        "id", NumericIdField.<Object, String, String>builder().build(),
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
                .writeRoles(List.of("ADMIN", "USER"))
                .readOnlyRoles(List.of("VIEWER"))
                .build();

        LinkedHashMap<String, RouteRenderer<Object, String, String>> routes = new LinkedHashMap<>();
        routes.put("users-grid", GridRoute.<Object, String, String>builder()
                .dataStoreConfig(userConfig)
                .isDefaultRoute(true)
                .title("route.users-grid")
                .configuration(GridItemRendererConfiguration.<Object, String, String>builder()
                        .titleField("username")
                        .factory(new CardFactory())
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
