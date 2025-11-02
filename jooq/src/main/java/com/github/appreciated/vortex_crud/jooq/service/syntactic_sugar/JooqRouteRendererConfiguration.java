package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JooqRouteRendererConfiguration extends RouteRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public static RouteRendererConfiguration.RouteRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return RouteRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory);
    }
}

