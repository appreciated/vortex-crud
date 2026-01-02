package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqCollection {
    public static Collection.CollectionBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>, ?, ?> builder() {
        return Collection.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
