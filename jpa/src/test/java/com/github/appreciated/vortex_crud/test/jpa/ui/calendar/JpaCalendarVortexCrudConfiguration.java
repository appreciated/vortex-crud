package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaCalendarRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormElement;
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
        var dataStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        var config = JpaDataStoreConfig.builder(repository, dataStore)
                        .withServices(fieldService, Map.of(JpaEventEntity.class, dataStore))
                        .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> eventForm = FormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .titleField("title")
                .fields(List.of(
                        JpaFormElement.builder("title", "calendar-test.labels.title").build(),
                        JpaFormElement.builder("startDate", "calendar-test.labels.start").build(),
                        JpaFormElement.builder("endDate", "calendar-test.labels.end").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("calendar", JpaCalendarRoute.builder()
                .dataStoreConfig(config)
                .title("Calendar")
                .titleField("title")
                .startDateField("startDate")
                .endDateField("endDate")
                .filterField("title")
                .form(eventForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name.calendar-test")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
