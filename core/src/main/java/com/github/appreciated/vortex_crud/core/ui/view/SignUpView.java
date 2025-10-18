package com.github.appreciated.vortex_crud.core.ui.view;

import com.github.appreciated.vortex_crud.core.entity.User;
import com.github.appreciated.vortex_crud.core.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("signup")
@PageTitle("Sign Up")
public class SignUpView<T extends User> extends VerticalLayout {

    private final UserService<T> userService;

    @Autowired
    public SignUpView(UserService<T> userService) {
        this.userService = userService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");

        Button signUpButton = new Button("Sign Up", event -> {
            if (password.getValue().equals(confirmPassword.getValue())) {
                userService.registerUser(username.getValue(), password.getValue());
                UI.getCurrent().navigate("login");
            } else {
                // Show an error message
            }
        });

        FormLayout formLayout = new FormLayout(username, password, confirmPassword, signUpButton);
        add(new H1("Sign Up"), formLayout);
    }
}