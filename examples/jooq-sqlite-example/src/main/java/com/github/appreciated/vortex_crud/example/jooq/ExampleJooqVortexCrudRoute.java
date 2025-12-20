package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContextProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Route("test/:path?")
public class ExampleJooqVortexCrudRoute extends VortexCrudRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public ExampleJooqVortexCrudRoute(
            VortexCrudContextProvider contextProvider,
            DSLContext dsl
    ) {
        super(contextProvider);
        this.dsl = dsl;
    }

    @Override
    protected RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        JooqDataStore projectsStore = new JooqDataStore(PROJECTS.getRecordType(), dsl, new DataStoreHooks<>());
        var projectsConfig = JooqDataStoreConfig.of(PROJECTS)
                .dataStoreInstance((VortexCrudDataStore) projectsStore)
                .fields(Map.of(
                        PROJECTS.ID, JooqNumericIdField.builder().build(),
                        PROJECTS.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        PROJECTS.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        PROJECTS.START_DATE, JooqDateField.builder().build(),
                        PROJECTS.END_DATE, JooqDateField.builder().build(),
                        PROJECTS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        PROJECTS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        return JooqGridRoute.builder()
                .dataStoreConfig(projectsConfig)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("manager", "admin"))
                .child(JooqFormRoute.builder()
                        .dataStoreConfig(projectsConfig)
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
