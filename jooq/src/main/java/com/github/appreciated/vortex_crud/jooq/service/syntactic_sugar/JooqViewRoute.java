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

public class JooqViewRoute {

    public static <R extends UpdatableRecord<R>> Builder<R> builder(TableImpl<R> table, DSLContext dsl) {
        return new Builder<>(table, dsl);
    }

    public static class Builder<R extends UpdatableRecord<R>> {
        private final ViewRouteBuilder<R, TableField<R, ?>, TableImpl<?>, ?, ?> builder;
        private final JooqDataStoreConfig.Builder<R> dataStoreBuilder;

        public Builder(TableImpl<R> table, DSLContext dsl) {
            this.builder = ViewRoute.builder();
            this.dataStoreBuilder = JooqDataStoreConfig.builder(table, dsl);
        }

        public Builder<R> withView(RecordViewProvider<R, TableField<R, ?>, TableImpl<?>> viewProvider) {
            builder.viewProvider(viewProvider);
            return this;
        }

        public ViewRoute<R, TableField<R, ?>, TableImpl<?>> build() {
             DataStoreConfig<R, TableField<R, ?>, TableImpl<?>> config = dataStoreBuilder.build();
             builder.dataStoreConfig(config);
             // fields are likely empty or derived from config in DataStoreConfig
             // But FormRoute/ViewRoute might require fields list if it validates against them.
             // ViewRouteFactory creates StoreAccessor which accesses config.fields().
             // So config must be populated.
             return builder.build();
        }

        public JooqDataStoreConfig.Builder<R> dataStoreConfig() {
            return dataStoreBuilder;
        }
    }
}
