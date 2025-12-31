package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

import java.util.Map;

public class JooqDataStoreConfig {

    /**
     * Helper class to add type-safe dataStoreInstance method
     */
    public static class TypeSafeBuilder {
        private final DataStoreConfig.DataStoreConfigBuilder<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder;

        public TypeSafeBuilder(DataStoreConfig.DataStoreConfigBuilder<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder) {
            this.builder = builder;
        }

        @SuppressWarnings("unchecked")
        public TypeSafeBuilder dataStoreInstance(VortexCrudDataStore<?, ?> dataStore) {
            builder.dataStoreInstance((VortexCrudDataStore<TableField<?, ?>, org.jooq.TableRecord<?>>) (Object) dataStore);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <R extends UpdatableRecord<R>> TypeSafeBuilder fields(Map<TableField<R, ?>, Field<?, TableField<?, ?>, TableImpl<?>>> fields) {
            builder.fields((Map<TableField<?, ?>, Field<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Object) fields);
            return this;
        }

        public DataStoreConfig<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>> build() {
            return builder.build();
        }
    }

    public static <R extends UpdatableRecord<R>> Builder<R> builder(TableImpl<R> table, DSLContext dsl) {
        return new Builder<>(table, dsl);
    }

    public static class Builder<R extends UpdatableRecord<R>> {
        private final TableImpl<R> table;
        private final DSLContext dsl;
        private DataStoreHooks<R> hooks = new DataStoreHooks<>();
        private Map<TableField<R, ?>, Field<R, TableField<R, ?>, TableImpl<?>>> fields;

        public Builder(TableImpl<R> table, DSLContext dsl) {
            this.table = table;
            this.dsl = dsl;
        }

        public Builder<R> fields(Map<TableField<R, ?>, Field<R, TableField<R, ?>, TableImpl<?>>> fields) {
            this.fields = fields;
            return this;
        }

        public Builder<R> withHooks(DataStoreHooks<R> hooks) {
            this.hooks = hooks;
            return this;
        }

        @SuppressWarnings("unchecked")
        public DataStoreConfig<R, TableField<R, ?>, TableImpl<?>> build() {
            JooqDataStore<R> store = new JooqDataStore<>((Class<R>) table.getRecordType(), dsl, hooks);
            return DataStoreConfig.<R, TableField<R, ?>, TableImpl<?>>builder()
                    .factory(table)
                    .dataStoreInstance(store)
                    .fields(fields)
                    .build();
        }
    }

    // Legacy support restored for compatibility with examples
    public static <R extends UpdatableRecord<R>> TypeSafeBuilder of(TableImpl<R> factory) {
         DataStoreConfig.DataStoreConfigBuilder<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder =
                 DataStoreConfig.<org.jooq.TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                         .factory(factory);
         return new TypeSafeBuilder(builder);
    }
}
