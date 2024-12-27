package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.Application;

public class JpaApplication extends Application<String> {

    public static Application.Builder<String> of() {
        return new Application.Builder<>(new Application<String>());
    }
}
