package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
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
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.VALIDATION_TEST;
import static com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JooqFieldValidationVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                VALIDATION_TEST, JooqDataStoreConfig.of(VALIDATION_TEST)
                        .fields(Map.of(
                                VALIDATION_TEST.ID, new IdField<>(),
                                VALIDATION_TEST.REQUIRED_FIELD, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                VALIDATION_TEST.EMAIL_FIELD, new EmailField<>(false, TextFieldValidation.builder().maxLength(500).build()),
                                VALIDATION_TEST.NUMERIC_FIELD, new DoubleField<>(false, NumberFieldValidation.builder().min(0.0).build()),
                                VALIDATION_TEST.DATE_FIELD, new DateField<>(),
                                VALIDATION_TEST.DATETIME_FIELD, new DateTimePickerField<>(),
                                VALIDATION_TEST.ENUM_FIELD, new SelectField<>("enum-options"),
                                VALIDATION_TEST.CHECKBOX_FIELD, new CheckboxField<>(),
                                VALIDATION_TEST.IMAGE_FIELD, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class)))
                        ).build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> validationForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(VALIDATION_TEST)
                .title("route.projects.title-cards")
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(VALIDATION_TEST.REQUIRED_FIELD)
                        .children(List.of(
                                JooqFieldElement.of(VALIDATION_TEST.REQUIRED_FIELD, "validation.fields.required").build(),
                                JooqFieldElement.of(VALIDATION_TEST.EMAIL_FIELD, "validation.fields.email").build(),
                                JooqFieldElement.of(VALIDATION_TEST.NUMERIC_FIELD, "validation.fields.numeric").build(),
                                JooqFieldElement.of(VALIDATION_TEST.DATE_FIELD, "validation.fields.date").build(),
                                JooqFieldElement.of(VALIDATION_TEST.DATETIME_FIELD, "validation.fields.datetime").build(),
                                JooqFieldElement.of(VALIDATION_TEST.CHECKBOX_FIELD, "validation.fields.checkbox").build(),
                                JooqFieldElement.of(VALIDATION_TEST.ENUM_FIELD, "validation.fields.enum").build(),
                                JooqFieldElement.of(VALIDATION_TEST.IMAGE_FIELD, "validation.fields.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JooqRouteRenderer.of(ListRouteFactory.class)
                .dataStoreKey(VALIDATION_TEST)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .filterField(VALIDATION_TEST.REQUIRED_FIELD)
                        .children(List.of(
                                JooqFieldElement.of(VALIDATION_TEST.REQUIRED_FIELD, "route.projects.labels.name").build(),
                                JooqFieldElement.of(VALIDATION_TEST.EMAIL_FIELD, "route.projects.labels.description").build()
                        ))
                        .build())
                .childrenMap(Map.of("form", validationForm))
                .build());

        LinkedHashMap<JooqFieldValidationTestEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}