package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.VALIDATION_TEST;
import static com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqFieldValidationVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    
    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> validationForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(VALIDATION_TEST)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(VALIDATION_TEST.REQUIRED_FIELD)
                        .withChildren(
                                new JooqFieldElement(VALIDATION_TEST.REQUIRED_FIELD, "validation.fields.required"),
                                new JooqFieldElement(VALIDATION_TEST.EMAIL_FIELD, "validation.fields.email"),
                                new JooqFieldElement(VALIDATION_TEST.NUMERIC_FIELD, "validation.fields.numeric"),
                                new JooqFieldElement(VALIDATION_TEST.DATE_FIELD, "validation.fields.date"),
                                new JooqFieldElement(VALIDATION_TEST.ENUM_FIELD, "validation.fields.enum")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(VALIDATION_TEST)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-list")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField(VALIDATION_TEST.REQUIRED_FIELD)
                        .withChildren(
                                new JooqFieldElement(VALIDATION_TEST.REQUIRED_FIELD, "route.projects.labels.name"),
                                new JooqFieldElement(VALIDATION_TEST.EMAIL_FIELD, "route.projects.labels.description")
                        )
                        .build())
                .withChild(validationForm)
                .build());

        LinkedHashMap<JooqFieldValidationTestEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withSelects(Selects.of().withConfigs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}