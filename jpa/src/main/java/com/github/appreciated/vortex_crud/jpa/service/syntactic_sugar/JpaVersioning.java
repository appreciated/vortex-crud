package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaVersioning extends Versioning {

    public static class Builder extends Versioning.Builder<JpaRepository<?, ?>> {
        public Builder(Versioning product) {
            super(product);
        }
    }

    public static JpaVersioning.Builder of() {
        return new JpaVersioning.Builder(new Versioning<>());
    }
}