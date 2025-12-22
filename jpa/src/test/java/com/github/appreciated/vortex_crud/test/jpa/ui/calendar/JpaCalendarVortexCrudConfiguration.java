package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaCalendarVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaEventRepository repository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaCalendarVortexCrudConfiguration(JpaEventRepository repository,
                                              JpaFieldAnnotationRegistryService annotationRegistryService,
                                              JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var dataStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        var config = JpaDataStoreConfig.builder(repository, dataStore)
                        .withServices(fieldService, Map.of(JpaEventEntity.class, dataStore))
                        .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> eventForm = FormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(config)
                .title("Event")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "Title").build(),
                                JpaFieldElement.builder("startDate", "Start").build(),
                                JpaFieldElement.builder("endDate", "End").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("calendar", JpaCalendarRoute.builder()
                .dataStoreConfig(config)
                .title("Calendar")
                .configuration(JpaCalendarConfiguration.builder()
                        .titleField("title")
                        .startDateField("startDate")
                        .endDateField("endDate")
                        .filterField("title")
                        .build())
                .child(eventForm)
                .build());

        return JpaApplication.builder()
                .applicationName("Calendar Test")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
