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
                        .withFields(Map.of(
                                VALIDATION_TEST.ID, new IdField<>(),
                                VALIDATION_TEST.REQUIRED_FIELD, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                VALIDATION_TEST.EMAIL_FIELD, new EmailField<>(false, TextFieldValidation.builder().maxLength(500).build()),
                                VALIDATION_TEST.NUMERIC_FIELD, new DoubleField<>(false, NumberFieldValidation.builder().withMin(0.0).build()),
                                VALIDATION_TEST.DATE_FIELD, new DateField<>(),
                                VALIDATION_TEST.DATETIME_FIELD, new DateTimePickerField<>(),
                                VALIDATION_TEST.ENUM_FIELD, new SelectField<>("enum-options"),
                                VALIDATION_TEST.CHECKBOX_FIELD, new CheckboxField<>(),
                                VALIDATION_TEST.IMAGE_FIELD, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class)))
                        ).build()
        );

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
                                new JooqFieldElement(VALIDATION_TEST.DATETIME_FIELD, "validation.fields.datetime"),
                                new JooqFieldElement(VALIDATION_TEST.CHECKBOX_FIELD, "validation.fields.checkbox"),
                                new JooqFieldElement(VALIDATION_TEST.ENUM_FIELD, "validation.fields.enum"),
                                new JooqFieldElement(VALIDATION_TEST.IMAGE_FIELD, "validation.fields.image")
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

        return JooqApplication.builder()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .withSelects(Selects.builder().withConfigs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}