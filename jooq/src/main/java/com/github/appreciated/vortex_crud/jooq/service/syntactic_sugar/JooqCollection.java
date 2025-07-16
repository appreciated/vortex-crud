package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollection extends Collection<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public JooqCollection(Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> factory) {
        super(factory);
    }

    public static class Builder extends Collection.Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
        public Builder(Collection<TableRecord<?>, TableField<?, ?>, TableImpl<?>> product) {
            super(product);
        }
    }

    public static JooqCollection.Builder of(Class<? extends VortexCrudDialogFactory> factory) {
        return new Builder(new Collection<>((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) factory));
    }
}
