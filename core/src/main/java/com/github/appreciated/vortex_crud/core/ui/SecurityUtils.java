package com.github.appreciated.vortex_crud.core.ui;

import com.github.appreciated.vortex_crud.core.entity.User;
import com.vaadin.flow.server.VaadinSession;

public class SecurityUtils {

    private static final String LOGGED_IN_USER_SESSION_KEY = "loggedInUser";

    public static User getLoggedInUser() {
        return (User) VaadinSession.getCurrent().getAttribute(LOGGED_IN_USER_SESSION_KEY);
    }

    public static void setLoggedInUser(User user) {
        VaadinSession.getCurrent().setAttribute(LOGGED_IN_USER_SESSION_KEY, user);
    }

    public static boolean isUserLoggedIn() {
        return getLoggedInUser() != null;
    }
}