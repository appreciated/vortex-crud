package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.entity.User;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

public class UserService<T extends User> {

    private static final String USER_PROVIDER_SESSION_KEY = "userProvider";
    private final UserProvider<T> provider;

    public UserService(UserProvider<T> provider) {
        this.provider = provider;
    }

    public Optional<T> findByUsername(String username) {
        return provider.findByUsername(username);
    }

    public T registerUser(String username, String password) {
        return provider.registerUser(username, password);
    }

    public static void setUserProvider(UserProvider provider) {
        VaadinSession.getCurrent().setAttribute(USER_PROVIDER_SESSION_KEY, provider);
    }

    public static <T extends User> UserProvider<T> getUserProvider() {
        return (UserProvider<T>) VaadinSession.getCurrent().getAttribute(USER_PROVIDER_SESSION_KEY);
    }
}