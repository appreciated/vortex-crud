package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Route("sign-up")
public class SignUpView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final Binder<ModelClass> binder = new Binder<>();
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
    private final ReflectionService<FieldType> reflectionService;
    private final PasswordEncoder passwordEncoder;

    public SignUpView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            ReflectionService<FieldType> reflectionService,
            PasswordEncoder passwordEncoder
    ) {
        this.configService = configService;
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.reflectionService = reflectionService;
        this.passwordEncoder = passwordEncoder;

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config =
                configService.getConfiguration().getUserManagement();

        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create title
        H1 title = new H1("Sign Up");

        // Create form layout
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        // Get data store for users
        RepositoryType repositoryKey = config.getRepositoryKey();
        VortexCrudDataStore<FieldType, ModelClass> userDataStore = dataStoreFactoryRegistry.getDataStore(repositoryKey);
        DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig =
                configService.getConfiguration().getDataStores().get(repositoryKey);

        // Create new user instance using data store
        ModelClass userInstance = userDataStore.newInstance();
        binder.setBean(userInstance);

        // Add username field
        TextField usernameField = new TextField("Username");
        usernameField.setRequired(true);
        usernameField.setWidthFull();
        FieldType usernameFieldType = config.getUsername().getField();
        binder.forField(usernameField)
                .asRequired("Username is required")
                .bind(
                        user -> reflectionService.getString(user, usernameFieldType),
                        (user, value) -> reflectionService.setString(user, usernameFieldType, value)
                );
        formLayout.add(usernameField);

        // Add password field
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        passwordField.setWidthFull();
        FieldType passwordFieldType = config.getPassword().getField();
        binder.forField(passwordField)
                .asRequired("Password is required")
                .bind(
                        user -> reflectionService.getString(user, passwordFieldType),
                        (user, value) -> reflectionService.setString(user, passwordFieldType, value)
                );
        formLayout.add(passwordField);

        // Add password confirmation field
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setWidthFull();
        formLayout.add(confirmPasswordField);

        // Add additional signup fields if configured
        List<InternalFormElement<ModelClass, FieldType, RepositoryType>> signUpFields = config.getSignUpFields();
        if (signUpFields != null && !signUpFields.isEmpty()) {
            formCreator.bindAndAddToLayout(
                    repositoryKey,
                    formRouteRenderer,
                    signUpFields,
                    userInstance,
                    routeFactory,
                    dataStoreConfig,
                    binder,
                    formLayout
            );
        }

        // Create buttons
        Button signUpButton = new Button("Sign Up");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUpButton.setWidthFull();
        signUpButton.addClickListener(event -> handleSignUp(
                userDataStore,
                config,
                passwordField,
                confirmPasswordField
        ));

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setWidthFull();
        backToLoginButton.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));

        // Create button layout
        HorizontalLayout buttonLayout = new HorizontalLayout(signUpButton, backToLoginButton);
        buttonLayout.setWidthFull();

        // Create container for form
        VerticalLayout formContainer = new VerticalLayout(title, formLayout, buttonLayout);
        formContainer.setMaxWidth("500px");
        formContainer.setPadding(true);
        formContainer.setSpacing(true);

        add(formContainer);
    }

    private void handleSignUp(
            VortexCrudDataStore<FieldType, ModelClass> userDataStore,
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config,
            PasswordField passwordField,
            PasswordField confirmPasswordField
    ) {
        // Validate passwords match
        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            Notification notification = Notification.show("Passwords do not match");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
            return;
        }

        // Validate form
        if (!binder.validate().isOk()) {
            Notification notification = Notification.show("Please fill in all required fields");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
            return;
        }

        try {
            ModelClass user = binder.getBean();

            // Check if username already exists using VortexCrudDataStore
            FieldType usernameFieldType = config.getUsername().getField();
            String username = reflectionService.getString(user, usernameFieldType);

            List<ModelClass> existingUsers = userDataStore.getRecordsFromTableWhereColumnEquals(
                    usernameFieldType,
                    username,
                    0,
                    1
            );

            if (!existingUsers.isEmpty()) {
                Notification notification = Notification.show("Username already exists");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                return;
            }

            // Encode password and set it to passwordHash field
            FieldType passwordFieldType = config.getPassword().getField();
            String rawPassword = reflectionService.getString(user, passwordFieldType);
            String encodedPassword = passwordEncoder.encode(rawPassword);

            // Set the encoded password to passwordHash field (assuming it exists)
            reflectionService.setString(user, "passwordHash", encodedPassword);

            // Clear the password field
            reflectionService.setString(user, passwordFieldType, null);

            // Write bean to ensure all fields are set
            binder.writeBean(user);

            // Insert user using VortexCrudDataStore (like FormRouteFactory does)
            userDataStore.insertRecord(user);

            Notification notification = Notification.show("Registration successful! Please login.");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(3000);

            // Navigate to login page
            notification.addDetachListener(detachEvent ->
                    UI.getCurrent().navigate(LoginView.class)
            );

        } catch (ValidationException e) {
            Notification notification = Notification.show("Validation failed: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification notification = Notification.show("Registration failed: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
        }
    }
}
