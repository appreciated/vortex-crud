package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqCollection extends Collection<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {

    public JooqCollection(Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>> factory) {
        super(factory);
    }

    public static class Builder extends Collection.Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {
        public Builder(Collection<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> product) {
            super(product);
        }
    }

    public static JooqCollection.Builder of(Class<? extends VortexCrudDialogFactory> factory) {
        return new Builder(new Collection<>((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory));
    }
}
