package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollection extends Collection<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static Collection.CollectionBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder(VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> factory) {
        return Collection.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().factory(factory);
    }
}
