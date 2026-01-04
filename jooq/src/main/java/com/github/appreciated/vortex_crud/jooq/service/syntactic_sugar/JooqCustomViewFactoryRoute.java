package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RecordViewProvider;
import com.github.appreciated.vortex_crud.core.config.model.ViewRoute;
import com.github.appreciated.vortex_crud.core.config.model.ViewRoute.ViewRouteBuilder;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig.TypeSafeBuilder;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

public class JooqCustomViewFactoryRoute {

    public static <R extends UpdatableRecord<R>> Builder<R> builder(TableImpl<R> table, DSLContext dsl) {
        return new Builder<>(table, dsl);
    }

    public static class Builder<R extends UpdatableRecord<R>> {
        private final ViewRouteBuilder<R, TableField<R, ?>, TableImpl<?>, ?, ?> builder;
        private final JooqDataStoreConfig.Builder<R> dataStoreBuilder;

        @SuppressWarnings("unchecked")
        public Builder(TableImpl<R> table, DSLContext dsl) {
            // Using raw cast to ViewRouteBuilder to bypass complex generic type inference issues with Lombok SuperBuilder
            this.builder = (ViewRouteBuilder) ViewRoute.builder();
            this.dataStoreBuilder = JooqDataStoreConfig.builder(table, dsl);
        }

        public Builder<R> withFactory(RecordViewProvider<R> viewFactory) {
            builder.viewFactory(viewFactory);
            return this;
        }

        public ViewRoute<R, TableField<R, ?>, TableImpl<?>> build() {
             DataStoreConfig<R, TableField<R, ?>, TableImpl<?>> config = dataStoreBuilder.build();
             builder.dataStoreConfig(config);
             return builder.build();
        }

        public JooqDataStoreConfig.Builder<R> dataStoreConfig() {
            return dataStoreBuilder;
        }
    }
}
