package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqFieldElement;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqFormRendererConfiguration;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqGridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqRouteRenderer;
import com.vaadin.flow.router.Route;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Route("test/:path?")
public class ExampleJooqVortexCrudRoute extends VortexCrudRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public ExampleJooqVortexCrudRoute(
            VortexCrudRouteFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> routeFactoryRegistry,
            VortexCrudDataStoreUtilStrategy storeUtilStrategy
    ) {
        super(routeFactoryRegistry, storeUtilStrategy);
    }

    @Override
    protected RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        return JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStoreKey(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JooqGridItemRendererConfiguration.of(CardFactory.class)
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("manager", "admin"))
                .child(JooqRouteRenderer.of(FormRouteFactory.class)
                        .dataStoreKey(PROJECTS)
                        .title("route.projects.title-cards")
                        .configuration(JooqFormRendererConfiguration.builder()
                                .titleField(PROJECTS.NAME)
                                .children(List.of(
                                        JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                                        JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                                        JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                                        JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                                ))
                                .build())
                        .build())
                .build();
    }
}

