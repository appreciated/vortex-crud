package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaSecurityVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSecurityUserRepository userRepository;
    private final JpaSecurityRoleRepository roleRepository;

    public JpaSecurityVortexCrudConfiguration(JpaSecurityUserRepository userRepository, JpaSecurityRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {

        Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> userFields = Map.of(
                "id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                "username", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                "passwordHash", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                "roles", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(), // Configuration for roles field, though it's ManyToMany
                "publicField", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                "adminField", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .writeRoles(List.of("ADMIN"))
                        .readOnlyRoles(List.of("USER"))
                        .build(),
                "secretField", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .writeRoles(List.of("ADMIN"))
                        .readOnlyRoles(List.of("ADMIN"))
                        .build()
        );

        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                userRepository, JpaDataStoreConfig.builder(userRepository)
                        .fields(userFields)
                        .build(),
                roleRepository, JpaDataStoreConfig.builder(roleRepository)
                        .fields(Map.of(
                                "id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "name", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()
                        ))
                        .build()
        );

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> userForm = FormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(userRepository)
                .title("route.users.title")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("username")
                        .children(List.of(
                                JpaFieldElement.builder("username", "route.users.labels.username").build(),
                                JpaFieldElement.builder("publicField", "route.users.labels.public").build(),
                                JpaFieldElement.builder("adminField", "route.users.labels.admin").build(),
                                JpaFieldElement.builder("secretField", "route.users.labels.secret").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("users-grid", GridRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(userRepository)
                .title("route.users-grid")
                .configuration(GridItemRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .titleField("username")
                        .build())
                .child(userForm)
                .writeRoles(List.of("ADMIN", "USER")) // Only ADMIN and USER can access the route (create/edit)
                .readOnlyRoles(List.of("VIEWER")) // VIEWER can read
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .repositoryKey(userRepository)
                        .username(JpaFieldElement.builder("username", "Username").build())
                        .password(JpaFieldElement.builder("passwordHash", "Password").build())
                        .rolesField("roles")
                        .availableRoles(new Roles(List.of("ADMIN", "USER", "VIEWER")))
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .signUpEnabled(true)
                        .build())
                .build();
    }
}
