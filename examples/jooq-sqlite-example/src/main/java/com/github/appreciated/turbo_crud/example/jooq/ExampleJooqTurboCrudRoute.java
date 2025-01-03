package com.github.appreciated.turbo_crud.example.jooq;

import com.github.appreciated.turbo_crud.core.config.model.GridOrListConfiguration;
import com.github.appreciated.turbo_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.routes.TurboCrudRoute;
import com.github.appreciated.turbo_crud.jooq.service.JooqFormElement;
import com.github.appreciated.turbo_crud.jooq.service.JooqRoute;
import com.github.appreciated.turbo_crud.jooq.service.JooqRouteConfiguration;
import com.vaadin.flow.router.Route;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.List;

import static com.github.appreciated.turbo_crud.jooq.models.tables.Projects.PROJECTS;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Route("test/:path?")
public class ExampleJooqTurboCrudRoute extends TurboCrudRoute<Table<?>, TableField<?, ?>> {

    public ExampleJooqTurboCrudRoute(TurboCrudRouteFactoryRegistry<Table<?>, TableField<?, ?>> routeFactoryRegistry) {
        super(routeFactoryRegistry);
    }

    @Override
    protected com.github.appreciated.turbo_crud.core.config.model.Route<Table<?>, TableField<?, ?>> getConfiguration() {
        return JooqRoute.of(GridRouteFactory.class)
                .withDataStore(PROJECTS)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
                        .withTitleField(PROJECTS.NAME)
                        .withDescriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(JooqRoute.of(FormRouteFactory.class)
                        .withDataStore(PROJECTS)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                                .withTitleField(PROJECTS.NAME)
                                .withChildren(
                                        new JooqFormElement(PROJECTS.NAME, "field", "route.projects.labels.name"),
                                        new JooqFormElement(PROJECTS.DESCRIPTION, "field", "route.projects.labels.description"),
                                        new JooqFormElement(PROJECTS.START_DATE, "field", "route.projects.labels.start_date"),
                                        new JooqFormElement(PROJECTS.END_DATE, "field", "route.projects.labels.end_date")
                                )
                                .build())
                        .build())
                .build();
    }
}


