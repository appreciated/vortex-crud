package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.password;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JpaPasswordVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    private final JpaPasswordTestRepository passwordTestRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> cachedApplication;

    public JpaPasswordVortexCrudConfiguration(
            JpaPasswordTestRepository passwordTestRepository,
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.passwordTestRepository = passwordTestRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        if (cachedApplication != null) {
            return cachedApplication;
        }

        LinkedHashMap<String, RouteRenderer<?,?,?>> routes = new LinkedHashMap<>();
        routes.put("password-test", createPasswordTestRoute());

        cachedApplication = JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();

        return cachedApplication;
    }

    private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createPasswordTestRoute() {
        var store = new JpaRepositoryDataStore<>(passwordTestRepository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudQueryDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(passwordTestRepository, store)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> passwordForm = JpaFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.password-test.title")
                .titleField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "password-test.labels.name").build(),
                        JpaFieldElement.builder("password", "password-test.labels.password").build()
                ))
                .build();

        return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.password-test.title-list")
                .filterField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "password-test.labels.name").build()
                ))
                .form(passwordForm)
                .build();
    }
}
