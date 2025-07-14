package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqGridOrListRendererConfiguration extends GridOrListRendererConfiguration<TableRecord<?>, TableField<?, ?>> {

    public JooqGridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<TableField<?, ?>>> factory) {
        super(factory);
    }

    public static class Builder extends GridOrListRendererConfiguration.Builder<TableRecord<?>, TableField<?, ?>> {
        public Builder(JooqGridOrListRendererConfiguration product) {
            super(product);
        }
    }

    public static JooqGridOrListRendererConfiguration.Builder of(Class<? extends VortexCrudItemFactory> factory) {
        return new JooqGridOrListRendererConfiguration.Builder(new JooqGridOrListRendererConfiguration((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory));
    }
}
