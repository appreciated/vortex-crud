package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import lombok.NonNull;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
        private Map<TableField<R, ?>, Field<R, TableField<R, ?>, TableImpl<?>>> fields = new HashMap<>();
        private JooqDataStore<R> dataStore;

        public Builder(TableImpl<R> table, DSLContext dsl) {
            this.table = table;
            this.dsl = dsl;
        }

        public Builder<R> dataStore(JooqDataStore<R> dataStore) {
            this.dataStore = dataStore;
            return this;
        }

        public Builder<R> fields(Map<TableField<R, ?>, Field<R, TableField<R, ?>, TableImpl<?>>> fields) {
            this.fields = fields;
            return this;
        }

        public Builder<R> field(TableField<R, ?> key, Field<R, TableField<R, ?>, TableImpl<?>> field) {
            this.fields.put(key, field);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder<R> autoFields() {
            for (org.jooq.Field<?> f : table.fields()) {
                TableField<R, ?> field = (TableField<R, ?>) f;
                if (fields.containsKey(field)) {
                    continue; // Skip if already defined
                }

                Field<R, TableField<R, ?>, TableImpl<?>> mappedField = null;
                Class<?> type = field.getDataType().getType();
                boolean required = !field.getDataType().nullable();
                boolean isPk = table.getPrimaryKey() != null && table.getPrimaryKey().getFields().contains(field);

                if (isPk && Number.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqNumericIdField.builder().build();
                } else if (String.class.isAssignableFrom(type)) {
                    int length = field.getDataType().length();
                    if (length > 255) {
                        var builder = JooqTextAreaField.builder().required(required);
                        if (length > 0) {
                             builder.validators(List.of(new StringLengthValidator("Maximum " + length + " characters", 0, length)));
                        }
                        mappedField = (Field) builder.build();
                    } else {
                        var builder = JooqTextField.builder().required(required);
                         if (length > 0) {
                             builder.validators(List.of(new StringLengthValidator("Maximum " + length + " characters", 0, length)));
                        }
                        mappedField = (Field) builder.build();
                    }
                } else if (Integer.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqIntegerField.builder().required(required).build();
                } else if (Double.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqDoubleField.builder().required(required).build();
                } else if (BigDecimal.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqBigDecimalField.builder().required(required).build();
                } else if (Boolean.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqCheckboxField.builder().required(required).build();
                } else if (LocalDateTime.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqDateTimePickerField.builder().required(required).build();
                } else if (LocalDate.class.isAssignableFrom(type)) {
                    mappedField = (Field) JooqDateField.builder().required(required).build();
                }

                if (mappedField != null) {
                    fields.put(field, mappedField);
                }
            }
            return this;
        }

        public Builder<R> withHooks(DataStoreHooks<R> hooks) {
            this.hooks = hooks;
            return this;
        }

        @SuppressWarnings("unchecked")
        public DataStoreConfig<R, TableField<R, ?>, TableImpl<?>> build() {
            JooqDataStore<R> store = this.dataStore != null ? this.dataStore : new JooqDataStore<>((Class<R>) table.getRecordType(), dsl, hooks);
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
