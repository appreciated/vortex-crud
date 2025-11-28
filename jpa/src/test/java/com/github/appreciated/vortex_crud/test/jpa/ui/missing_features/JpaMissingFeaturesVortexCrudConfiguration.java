package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.vaadin.flow.component.button.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.vaadin.flow.component.icon.VaadinIcon.COG;
import static com.vaadin.flow.component.icon.VaadinIcon.PRINT;

@Service
public class JpaMissingFeaturesVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMissingFeaturesRepository repository;

    public JpaMissingFeaturesVortexCrudConfiguration(JpaMissingFeaturesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // Selects config with explicit typing
        LinkedHashMap<String, String> tagOptions = new LinkedHashMap<>();
        tagOptions.put("tag1", "Tag 1");
        tagOptions.put("tag2", "Tag 2");

        Map<String, LinkedHashMap<?, String>> selectsConfig = new HashMap<>();
        selectsConfig.put("tags", (LinkedHashMap) tagOptions);

        Selects selects = Selects.builder()
            .configs(selectsConfig)
            .build();

        // Form Route
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form = JpaFormRoute.builder()
            .dataStoreKey(repository)
            .title("route.missing.title")
            .formConfiguration(JpaFormRendererConfiguration.builder()
                .titleField("name")
                .children(List.of(
                    JpaFieldElement.builder("name", "Name").build(),
                    JpaFieldElement.builder("tags", "Tags").build(),
                    JpaFieldElement.builder("pdfDoc", "PDF").build(),
                    JpaFieldElement.builder("notes", "Notes").build()
                ))
                .build())
            .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // List Route with Global Action
        routes.put("missing-features-test", JpaListRoute.builder()
            .dataStoreKey(repository)
            .iconFactory(COG::create)
            .title("route.missing.list")
            .configuration(JpaListItemRendererConfiguration.builder()
                 .filterField("name")
                 .children(List.of(
                      JpaFieldElement.builder("name", "Name").build()
                 ))
                 .build())
            .routeActions(List.of(
                 GlobalRouteAction.<String, JpaRepository<?, ?>>builder()
                    .componentFactory(() -> new Button("Print", PRINT.create()))
                    .handler(ctx -> {})
                    .build()
            ))
            .child(form)
            .build());

        // Single Form Route
        routes.put("single-form-test", JpaSingleFormRoute.builder()
             .dataStoreKey(repository)
             .title("Single Form")
             .entityFilterField("id")
             .entityFilterValueProvider(() -> 1L)
             .formConfiguration(JpaFormRendererConfiguration.builder()
                 .titleField("name")
                 .children(List.of(
                     JpaFieldElement.builder("name", "Name").build(),
                     JpaFieldElement.builder("tags", "Tags").build(),
                     JpaFieldElement.builder("pdfDoc", "PDF").build()
                 ))
                 .build())
             .build());

        return JpaApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
