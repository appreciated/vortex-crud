package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.jooq.TableRecord;

public class JooqVersioning extends Versioning<TableRecord<?>> {

    public static class Builder extends Versioning.Builder<TableRecord<?>> {
        public Builder(Versioning<TableRecord<?>> product) {
            super(product);
        }
    }

    public static JooqVersioning.Builder of() {
        return new JooqVersioning.Builder(new Versioning<>());
    }
}