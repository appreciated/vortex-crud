package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;

@Route("login")
public class LoginView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);
    private final LoginForm login = new LoginForm();
    private final IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement;
    private final VortexCrudDataStore<FieldType, Object> dataStore;
    private final ReflectionService<FieldType> reflectionService;
    private final PasswordEncoder passwordEncoder;

    public LoginView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            ReflectionService<FieldType> reflectionService,
            PasswordEncoder passwordEncoder
    ) {
        this.reflectionService = reflectionService;
        this.passwordEncoder = passwordEncoder;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Application<ModelClass, FieldType, RepositoryType> configuration = configService.configuration();
        userManagement = configuration.identityAndAccessManagement();
        dataStore = userManagement != null ? (VortexCrudDataStore<FieldType, Object>) userManagement.dataStoreInstance() : null;
        if (userManagement == null) {
            Notification.show("User management not configured").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Handle login event using vortex-crud pattern
        login.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            try {
                // Get user DataStore (vortex-crud pattern)
                // Query for user by username field
                UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(username, password);
                if (authToken == null) return;
                setSecurityContextFor(authToken);
            } catch (Exception e) {
                log.error("Login failed", e);
                login.setError(true);
                Notification.show("Login failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        String applicationName = configuration.applicationName();
        add(new H1(applicationName != null ? getTranslation(applicationName) : ""), login);

        if (userManagement.isSignUpEnabled()) {
            Button signUpButton = new Button("Sign Up", event -> UI.getCurrent().navigate(SignUpView.class));
            add(signUpButton);
        }
    }

    private @Nullable UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String username, String password) {
        FieldType usernameField = userManagement.username().field();
        List<Object> users = dataStore.getRecordsFromTableWhereColumnEquals(usernameField, username, 0, 1);

        if (users.isEmpty()) {
            login.setError(true);
            return null;
        }

        Object userEntity = users.getFirst();

        // Get password hash using ReflectionService
        FieldType passwordField = userManagement.password().field();
        Object passwordValue = reflectionService.getValue(userEntity, passwordField);

        if (passwordValue == null || !passwordEncoder.matches(password, passwordValue.toString())) {
            login.setError(true);
            return null;
        }

        // Authentication successful - create Spring Security session
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userManagement.resolveRolesForEntity(reflectionService, userEntity);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, authorities);
        return authToken;
    }

    private void setSecurityContextFor(Authentication userEntity) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(userEntity);

        // Save security context to session
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request != null) {
            request.getHttpServletRequest().getSession()
                    .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        }

        Notification.show("Login successful!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Use getUI() instead of getCurrent() for better reliability in async contexts
        getUI().ifPresent(ui -> {
            // Navigate after a small delay to ensure security context is fully propagated
            ui.access(() -> {
                try {
                    ui.navigate("/");
                } catch (Exception e) {
                    log.error("Navigation to root failed after login, staying on current page", e);
                    // If navigation fails, at least the user is logged in and can manually navigate
                }
            });
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        if (Boolean.getBoolean("vortex.crud.disable.autologin")) {
            return;
        }

        //TODO Remove before release
        setSecurityContextFor(getUsernamePasswordAuthenticationToken("max@mustermann.de", "password"));
    }
}