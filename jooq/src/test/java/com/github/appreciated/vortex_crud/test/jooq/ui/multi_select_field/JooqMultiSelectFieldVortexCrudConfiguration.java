package com.github.appreciated.vortex_crud.test.jooq.ui.multi_select_field;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.PRODUCT;
import static com.github.appreciated.vortex_crud.jooq.models.Tables.CATEGORY;
import static com.vaadin.flow.component.icon.VaadinIcon.PACKAGE;

@Service
public class JooqMultiSelectFieldVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                PRODUCT, JooqDataStoreConfig.of(PRODUCT)
                        .fields(Map.of(
                                PRODUCT.ID, JooqIdField.builder().build(),
                                PRODUCT.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Invalid length", 0, 255))).build(),
                                PRODUCT.CATEGORIES, JooqMultiSelectField.builder()
                                        .dataStore(CATEGORY)
                                        .field(PRODUCT.CATEGORIES)
                                        .filterField(CATEGORY.NAME)
                                        .children(List.of(CATEGORY.NAME))
                                        .build()
                        )).build(),
                CATEGORY, JooqDataStoreConfig.of(CATEGORY)
                        .fields(Map.of(
                                CATEGORY.ID, JooqIdField.builder().build(),
                                CATEGORY.NAME, JooqTextField.builder().required(true).build()
                        )).build()
        );

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> multiSelectFieldForm = JooqFormRoute.builder()
                .dataStoreKey(PRODUCT)
                .title("route.multi-select.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(PRODUCT.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PRODUCT.NAME, "multi-select.labels.name").build(),
                                JooqFieldElement.of(PRODUCT.CATEGORIES, "multi-select.labels.categories").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("multi-select-test", JooqListRoute.builder()
                .dataStoreKey(PRODUCT)
                .iconFactory(PACKAGE::create)
                .title("route.multi-select.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(PRODUCT.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PRODUCT.NAME, "multi-select.labels.name").build()
                        ))
                        .build())
                .child(multiSelectFieldForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }

}
