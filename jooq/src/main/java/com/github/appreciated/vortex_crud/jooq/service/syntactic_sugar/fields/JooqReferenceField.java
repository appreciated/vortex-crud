package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqReferenceField {

    /**
     * Type-safe builder wrapper that handles data store casts
     */
    public static class TypeSafeBuilder {
        private final ReferenceField.ReferenceFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder;

        public TypeSafeBuilder(ReferenceField.ReferenceFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder) {
            this.builder = builder;
        }

        @SuppressWarnings("unchecked")
        public TypeSafeBuilder dataStore(VortexCrudDataStore<?, ?> dataStore) {
            builder.dataStore((VortexCrudDataStore<TableField<?, ?>, TableRecord<?>>) (Object) dataStore);
            return this;
        }

        public <R extends org.jooq.Record> TypeSafeBuilder field(TableField<R, ?> field) {
            builder.field(field);
            return this;
        }

        public <R extends org.jooq.Record> TypeSafeBuilder searchField(TableField<R, ?> filterField) {
            builder.searchField(filterField);
            return this;
        }

        @SuppressWarnings("unchecked")
        public TypeSafeBuilder children(java.util.List<?> children) {
            builder.children((java.util.List<TableField<?, ?>>) (Object) children);
            return this;
        }

        public TypeSafeBuilder required(boolean required) {
            builder.required(required);
            return this;
        }

        public ReferenceField<TableRecord<?>, TableField<?, ?>, TableImpl<?>> build() {
            return builder.build();
        }
    }

    public static TypeSafeBuilder builder() {
        return new TypeSafeBuilder(ReferenceField.builder());
    }
}
