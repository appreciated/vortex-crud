package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqCollection extends Collection<Class<? extends TableRecord<?>>, TableField<?, ?>> {

    public JooqCollection(Class<? extends VortexCrudDialogFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>> factory) {
        super(factory);
    }

    public static class Builder extends Collection.Builder<Class<? extends TableRecord<?>>, TableField<?, ?>> {
        public Builder(Collection<Class<? extends TableRecord<?>>, TableField<?, ?>> product) {
            super(product);
        }
    }

    public static JooqCollection.Builder of(Class<? extends VortexCrudDialogFactory> factory) {
        return new Builder(new Collection<>((Class<? extends VortexCrudDialogFactory<Class<? extends TableRecord<?>>, TableField<?, ?>>>) factory));
    }
}
