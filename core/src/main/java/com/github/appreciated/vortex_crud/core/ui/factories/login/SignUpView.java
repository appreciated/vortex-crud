package com.github.appreciated.vortex_crud.core.ui.factories.login;

import com.github.appreciated.vortex_crud.auth.UserManagementService;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route("signup")
public class SignUpView extends VerticalLayout {

    private final UserManagementService userManagementService;
    private final Class<? extends VortexCrudUser> userClass;

    @Autowired
    public SignUpView(UserManagementService userManagementService, Class<? extends VortexCrudUser> userClass) {
        this.userManagementService = userManagementService;
        this.userClass = userClass;
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Sign Up");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        Button signUpButton = new Button("Sign Up", event -> signUp(username.getValue(), password.getValue(), confirmPassword.getValue()));
        Button loginButton = new Button("Back to Login", event -> UI.getCurrent().navigate(LoginView.class));

        FormLayout formLayout = new FormLayout(username, password, confirmPassword, signUpButton, loginButton);
        add(title, formLayout);
    }

    private void signUp(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty()) {
            Notification.show("Username and password cannot be empty");
            return;
        }
        if (!password.equals(confirmPassword)) {
            Notification.show("Passwords do not match");
            return;
        }
        if (userManagementService.isUsernameTaken(username)) {
            Notification.show("Username is already taken");
            return;
        }
        try {
            VortexCrudUser user = userClass.getDeclaredConstructor().newInstance();
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(Collections.singleton("USER"));
            userManagementService.register(user);
            Notification.show("Sign up successful");
            UI.getCurrent().navigate(LoginView.class);
        } catch (Exception e) {
            Notification.show("An error occurred during sign up");
        }
    }
}
