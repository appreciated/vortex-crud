package com.github.appreciated.vortex_crud.security.core.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LoginView extends VerticalLayout {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");
        add(new H1("Vortex CRUD"), login);
        Button signUpButton = new Button("Sign Up", event -> UI.getCurrent().navigate(SignUpView.class));
        add(signUpButton);
    }
}