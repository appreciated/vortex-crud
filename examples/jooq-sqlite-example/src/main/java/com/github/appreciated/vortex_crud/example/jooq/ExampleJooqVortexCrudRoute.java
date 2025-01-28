package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute;
import com.github.appreciated.vortex_crud.jooq.service.JooqFieldElement;
import com.github.appreciated.vortex_crud.jooq.service.JooqRouteRenderer;
import com.github.appreciated.vortex_crud.jooq.service.JooqRouteRendererConfiguration;
import com.vaadin.flow.router.Route;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.List;

import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Route("test/:path?")
public class ExampleJooqVortexCrudRoute extends VortexCrudRoute<Table<?>, TableField<?, ?>> {

    public ExampleJooqVortexCrudRoute(VortexCrudRouteFactoryRegistry<Table<?>, TableField<?, ?>> routeFactoryRegistry) {
        super(routeFactoryRegistry);
    }

    @Override
    protected RouteRenderer<Table<?>, TableField<?, ?>> getConfiguration() {
        return JooqRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(PROJECTS)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
                        .withTitleField(PROJECTS.NAME)
                        .withDescriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(JooqRouteRenderer.of(FormRouteFactory.class)
                        .withDataStore(PROJECTS)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                                .withTitleField(PROJECTS.NAME)
                                .withChildren(
                                        new JooqFieldElement(PROJECTS.NAME, "route.projects.labels.name"),
                                        new JooqFieldElement(PROJECTS.DESCRIPTION, "route.projects.labels.description"),
                                        new JooqFieldElement(PROJECTS.START_DATE, "route.projects.labels.start_date"),
                                        new JooqFieldElement(PROJECTS.END_DATE, "route.projects.labels.end_date")
                                )
                                .build())
                        .build())
                .build();
    }
}


