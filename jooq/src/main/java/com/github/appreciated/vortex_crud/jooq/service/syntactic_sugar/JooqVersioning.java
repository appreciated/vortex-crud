package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.jooq.impl.TableImpl;

public class JooqVersioning extends Versioning<TableImpl<?>> {
    public static Versioning.VersioningBuilder<TableImpl<?>> builder() {
        return Versioning.<TableImpl<?>>builder();
    }
}