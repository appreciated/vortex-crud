package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqKanban extends Kanban<TableRecord<?>, TableField<?, ?>> {

    public JooqKanban(Class<? extends VortexCrudItemFactory<TableField<?, ?>>> factory) {
        super(factory);
    }

    public static class Builder extends Kanban.Builder<TableRecord<?>, TableField<?, ?>> {
        public Builder(JooqKanban product) {
            super(product);
        }
    }

    public static JooqKanban.Builder of(Class<? extends VortexCrudItemFactory> factory) {
        return new JooqKanban.Builder(new JooqKanban((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory));
    }
}