package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@AnonymousAllowed
@Route("sign-up")
public class SignUpView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    public SignUpView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            ReflectionService<FieldType> reflectionService,
            PasswordEncoder passwordEncoder
    ) {
        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config = configService.configuration().identityAndAccessManagement();

        if (config == null) {
            Notification.show("User management not configured").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VortexCrudDataStore<FieldType, Object> dataStore = (VortexCrudDataStore<FieldType, Object>) configService.configuration().dataStores().get(config.repositoryKey()).dataStoreInstance();
        Object entity = dataStore.newInstance();

        DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = configService.configuration().dataStores().get(config.repositoryKey());

        Binder<Object> binder = new Binder<>(Object.class);
        binder.setBean(entity);

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("500px");

        // Add all sign-up fields
        List<InternalFormElement<ModelClass, FieldType, RepositoryType>> allFields = new java.util.ArrayList<>();

        // Add username and password fields from config
        if (config.username() != null) {
            allFields.add(config.username());
        }
        if (config.password() != null) {
            allFields.add(config.password());
        }

        // Add additional sign-up fields
        if (config.signUpFields() != null) {
            allFields.addAll(config.signUpFields());
        }

        formCreator.bindAndAddToLayout(
                config.repositoryKey(),
                null,
                allFields,
                entity,
                dataStoreConfig,
                binder,
                formLayout
        );

        Button signUpButton = new Button("Sign Up", event -> {
            try {
                binder.writeBean(entity);

                // Hash the password before saving
                FieldType passwordField = config.password().field();
                Object passwordValue = reflectionService.getValue(entity, passwordField);
                if (passwordValue != null) {
                    String hashedPassword = passwordEncoder.encode(passwordValue.toString());
                    reflectionService.setValue(entity, passwordField, hashedPassword);
                }

                dataStore.insertRecord(entity);
                Notification.show("Registration successful! Please login.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate(LoginView.class);
            } catch (ValidationException e) {
                Notification.show("Validation failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception e) {
                Notification.show("Registration failed: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button loginButton = new Button("Already have an account? Login", event ->
            UI.getCurrent().navigate(LoginView.class)
        );
        loginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        VerticalLayout wrapper = new VerticalLayout(
                new H1("Sign Up"),
                formLayout,
                signUpButton,
                loginButton
        );
        wrapper.setMaxWidth("500px");
        wrapper.setAlignItems(Alignment.STRETCH);
        wrapper.setPadding(true);

        add(wrapper);
    }
}
