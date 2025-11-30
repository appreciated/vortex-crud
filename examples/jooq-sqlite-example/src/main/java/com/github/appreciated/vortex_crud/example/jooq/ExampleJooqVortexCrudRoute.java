package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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
            VortexCrudContext<TableRecord<?>, TableField<?, ?>, TableImpl<?>> context
    ) {
        super(context);
    }

    @Override
    protected RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        return JooqGridRoute.builder()
                .dataStoreKey(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("manager", "admin"))
                .child(JooqFormRoute.builder()
                        .dataStoreKey(PROJECTS)
                        .title("route.projects.title-cards")
                        .formConfiguration(JooqFormRendererConfiguration.builder()
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
