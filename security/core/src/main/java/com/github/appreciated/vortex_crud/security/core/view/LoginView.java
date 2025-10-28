package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.ArrayList;
import java.util.List;

@Route("login")
public class LoginView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final LoginForm login = new LoginForm();

    public LoginView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            ReflectionService<FieldType> reflectionService,
            PasswordEncoder passwordEncoder
    ) {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config =
            configService.getConfiguration().getUserManagement();

        if (config == null) {
            Notification.show("User management not configured").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Handle login event using vortex-crud pattern
        login.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            try {
                // Get user DataStore (vortex-crud pattern)
                VortexCrudDataStore<FieldType, Object> dataStore =
                    (VortexCrudDataStore<FieldType, Object>) dataStoreFactoryRegistry.getDataStore(config.getRepositoryKey());

                // Query for user by username field
                FieldType usernameField = config.getUsername().getField();
                List<Object> users = dataStore.getRecordsFromTableWhereColumnEquals(usernameField, username, 0, 1);

                if (users.isEmpty()) {
                    login.setError(true);
                    return;
                }

                Object userEntity = users.get(0);

                // Get password hash using ReflectionService
                FieldType passwordField = config.getPassword().getField();
                Object passwordValue = reflectionService.getValue(userEntity, passwordField);

                if (passwordValue == null || !passwordEncoder.matches(password, passwordValue.toString())) {
                    login.setError(true);
                    return;
                }

                // Authentication successful - create Spring Security session
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (config.getRoles() != null && config.getRoles().getRoles() != null) {
                    for (String role : config.getRoles().getRoles()) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    }
                }

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password, authorities);

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authToken);

                // Save security context to session
                VaadinServletRequest request = VaadinServletRequest.getCurrent();
                if (request != null) {
                    request.getHttpServletRequest().getSession()
                        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                }

                Notification.show("Login successful!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate("/");

            } catch (Exception e) {
                login.setError(true);
                Notification.show("Login failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(new H1("Vortex CRUD"), login);

        if (config.isSignUp()) {
            Button signUpButton = new Button("Sign Up", event -> UI.getCurrent().navigate(SignUpView.class));
            add(signUpButton);
        }
    }
}