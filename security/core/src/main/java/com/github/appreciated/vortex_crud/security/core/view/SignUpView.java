package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.security.core.service.UserRegistrationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("sign-up")
public class SignUpView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final Binder<Object> binder = new Binder<>();
    private final UserRegistrationService<ModelClass, FieldType, RepositoryType> registrationService;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;

    public SignUpView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
            UserRegistrationService<ModelClass, FieldType, RepositoryType> registrationService
    ) {
        this.configService = configService;
        this.registrationService = registrationService;

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

        // Create user instance for binding
        DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig =
                configService.getConfiguration().getDataStores().get(config.getRepositoryKey());

        Object userInstance;
        try {
            userInstance = dataStoreConfig.getModelClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user instance", e);
        }

        binder.setBean(userInstance);

        // Add username field
        TextField usernameField = new TextField("Username");
        usernameField.setRequired(true);
        usernameField.setWidthFull();
        String usernameFieldName = config.getUsername().getFieldName();
        binder.forField(usernameField)
                .asRequired("Username is required")
                .bind(
                        user -> getFieldValue(user, usernameFieldName),
                        (user, value) -> setFieldValue(user, usernameFieldName, value)
                );
        formLayout.add(usernameField);

        // Add password field
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        passwordField.setWidthFull();
        String passwordFieldName = config.getPassword().getFieldName();
        binder.forField(passwordField)
                .asRequired("Password is required")
                .bind(
                        user -> getFieldValue(user, passwordFieldName),
                        (user, value) -> setFieldValue(user, passwordFieldName, value)
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
                    config.getRepositoryKey(),
                    formRouteRenderer,
                    signUpFields,
                    null,
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
        signUpButton.addClickListener(event -> handleSignUp(passwordField, confirmPasswordField));

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

    private void handleSignUp(PasswordField passwordField, PasswordField confirmPasswordField) {
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
            Object user = binder.getBean();

            // Check if username already exists
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config =
                    configService.getConfiguration().getUserManagement();
            String usernameFieldName = config.getUsername().getFieldName();
            String username = getFieldValue(user, usernameFieldName);

            if (registrationService.usernameExists(username)) {
                Notification notification = Notification.show("Username already exists");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                return;
            }

            // Register the user
            registrationService.registerUser(user);

            Notification notification = Notification.show("Registration successful! Please login.");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(3000);

            // Navigate to login page
            notification.addDetachListener(detachEvent ->
                    UI.getCurrent().navigate(LoginView.class)
            );

        } catch (Exception e) {
            Notification notification = Notification.show("Registration failed: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
        }
    }

    @SuppressWarnings("unchecked")
    private String getFieldValue(Object obj, String fieldName) {
        try {
            String methodName = "get" + capitalizeFirstLetter(fieldName);
            return (String) obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private void setFieldValue(Object obj, String fieldName, String value) {
        try {
            String methodName = "set" + capitalizeFirstLetter(fieldName);
            obj.getClass().getMethod(methodName, String.class).invoke(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
