package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.springframework.data.repository.CrudRepository;

public class JpaApplication extends Application<CrudRepository<?,?>, String> {

    public static Application.Builder<CrudRepository<?,?>, String> of() {
        return new Application.Builder<>(new JpaApplication());
    }
}
