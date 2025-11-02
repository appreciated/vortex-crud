package com.github.appreciated.vortex_crud.test.jooq.ui.i18n;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.I18N_IMAGES;

@Service
public class JooqI18NTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                I18N_IMAGES, JooqDataStoreConfig.of(I18N_IMAGES)
                        .fields(Map.of(
                                I18N_IMAGES.ID, new IdField<>(),
                                I18N_IMAGES.TITLE, new TextField<>(),
                                I18N_IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(I18N_IMAGES)
                .title("route.projects.title-cards")
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(I18N_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(I18N_IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFieldElement.of(I18N_IMAGES.URL, "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .dataStoreKey(I18N_IMAGES)
                .title("route.images-list")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .inlineEdit(true)
                        .filterField(I18N_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(I18N_IMAGES.URL, "route.projects.labels.description").build(),
                                JooqFieldElement.of(I18N_IMAGES.TITLE, "route.projects.labels.name").build()
                        ))
                        .build())
                .childrenMap(Map.of("form", imageForm))
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }

}