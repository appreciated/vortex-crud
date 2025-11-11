package com.github.appreciated.vortex_crud.test.jpa.ui.multi_select_field;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.vaadin.flow.component.icon.VaadinIcon.PACKAGE;

@Service
public class JpaMultiSelectFieldVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMultiSelectFieldRepository multiSelectFieldRepository;

    public JpaMultiSelectFieldVortexCrudConfiguration(JpaMultiSelectFieldRepository multiSelectFieldRepository) {
        this.multiSelectFieldRepository = multiSelectFieldRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> multiSelectFieldForm = JpaFormRoute.builder()
                .dataStoreKey(multiSelectFieldRepository)
                .title("route.multi-select.title")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "multi-select.labels.name").build(),
                                JpaFieldElement.builder("categories", "multi-select.labels.categories").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig = JpaListItemRendererConfiguration.builder()
                .filterField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "multi-select.labels.name").build()
                ))
                .build();

        routes.put("multi-select-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(multiSelectFieldRepository)
                .iconFactory(PACKAGE::create)
                .title("route.multi-select.title-list")
                .configuration(listConfig)
                .child(multiSelectFieldForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }

}
