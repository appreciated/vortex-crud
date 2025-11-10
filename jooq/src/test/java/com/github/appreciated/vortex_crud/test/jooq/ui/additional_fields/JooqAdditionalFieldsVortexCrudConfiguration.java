package com.github.appreciated.vortex_crud.test.jooq.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.ADDITIONAL_FIELDS_TEST;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JooqAdditionalFieldsVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                ADDITIONAL_FIELDS_TEST, JooqDataStoreConfig.of(ADDITIONAL_FIELDS_TEST)
                        .fields(Map.of(
                                ADDITIONAL_FIELDS_TEST.ID, JooqIdField.builder().build(),
                                ADDITIONAL_FIELDS_TEST.NAME, JooqTextField.builder().required(true).validation(MaxLengthTextFieldValidation.builder().maxLength(255).build()).build(),
                                ADDITIONAL_FIELDS_TEST.DESCRIPTION, JooqTextAreaField.builder().build(),
                                ADDITIONAL_FIELDS_TEST.PASSWORD, JooqPasswordField.builder().build(),
                                ADDITIONAL_FIELDS_TEST.PRICE, JooqBigDecimalField.builder().build(),
                                ADDITIONAL_FIELDS_TEST.VIDEO_URL, JooqVideoField.builder().configuration(VideoFieldRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(LocalVideoResourceProvider.class)
                                        .build()).build()
                        )).build()
        );

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> additionalFieldsForm = JooqFormRoute.builder()
                .dataStoreKey(ADDITIONAL_FIELDS_TEST)
                .title("route.additional-fields.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(ADDITIONAL_FIELDS_TEST.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.NAME, "additional-fields.labels.name").build(),
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.DESCRIPTION, "additional-fields.labels.description").build(),
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.PASSWORD, "additional-fields.labels.password").build(),
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.PRICE, "additional-fields.labels.price").build(),
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.VIDEO_URL, "additional-fields.labels.video").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("additional-fields-test", JooqListRoute.builder()
                .dataStoreKey(ADDITIONAL_FIELDS_TEST)
                .iconFactory(COG::create)
                .title("route.additional-fields.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(ADDITIONAL_FIELDS_TEST.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.NAME, "additional-fields.labels.name").build(),
                                JooqFieldElement.of(ADDITIONAL_FIELDS_TEST.DESCRIPTION, "additional-fields.labels.description").build()
                        ))
                        .build())
                .child(additionalFieldsForm)
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }

}
