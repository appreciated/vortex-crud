package com.github.appreciated.vortex_crud.core.ui.view;

import com.github.appreciated.vortex_crud.core.entity.User;
import com.github.appreciated.vortex_crud.core.service.UserService;
import com.github.appreciated.vortex_crud.core.ui.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("login")
@PageTitle("Login")
public class LoginView<T extends User> extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final UserService<T> userService;

    @Autowired
    public LoginView(UserService<T> userService) {
        this.userService = userService;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.addLoginListener(event -> {
            Optional<T> user = userService.findByUsername(event.getUsername());
            if (user.isPresent() && user.get().getPassword().equals(event.getPassword())) {
                SecurityUtils.setLoggedInUser(user.get());
                UI.getCurrent().navigate("");
            } else {
                login.setError(true);
            }
        });

        add(new H1("Vortex CRUD"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate("");
        }
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}