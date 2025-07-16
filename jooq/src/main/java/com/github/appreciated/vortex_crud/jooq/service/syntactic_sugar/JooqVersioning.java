package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.jooq.impl.TableImpl;

public class JooqVersioning extends Versioning<TableImpl<?>> {

    public static class Builder extends Versioning.Builder<TableImpl<?>> {
        public Builder(Versioning<TableImpl<?>> product) {
            super(product);
        }
    }

    public static JooqVersioning.Builder of() {
        return new JooqVersioning.Builder(new Versioning<>());
    }
}