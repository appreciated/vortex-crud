package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Versioning;
import org.jooq.TableRecord;

public class JooqVersioning extends Versioning<Class<? extends TableRecord<?>>> {
    
    public static class Builder extends Versioning.Builder<Class<? extends TableRecord<?>>> {
        public Builder(Versioning<Class<? extends TableRecord<?>>> product) {
            super(product);
        }

        public static JooqVersioning.Builder of() {
            return new JooqVersioning.Builder(new Versioning<>());
        }
    }
}