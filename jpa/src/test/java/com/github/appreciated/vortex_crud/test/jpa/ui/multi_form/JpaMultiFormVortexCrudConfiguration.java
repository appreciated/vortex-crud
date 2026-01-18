package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.MultiFormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.USER;

@Service
public class JpaMultiFormVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMultiFormRepository multiFormRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaMultiFormVortexCrudConfiguration(JpaMultiFormRepository multiFormRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.multiFormRepository = multiFormRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var multiFormStore = new JpaRepositoryDataStore<>(multiFormRepository, annotationRegistryService);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(multiFormStore.getModelClass(), multiFormStore);

        var multiFormConfig = JpaDataStoreConfig.builder(multiFormRepository, multiFormStore)
                .withServices(fieldService, storeMap)
                .build();

        // Individual form configurations for multi-form rendering
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> basicInfoForm =
                JpaFormRoute.builder()
                        .titleField("profileName")
                        .fields(List.of(
                                JpaFormElement.builder("profileName", "multi_form.fields.profile_name").build(),
                                JpaFormElement.builder("email", "multi_form.fields.email").build()
                        ))
                        .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> additionalDetailsForm =
                JpaFormRoute.builder()
                        .titleField("description")
                        .fields(List.of(
                                JpaFormElement.builder("description", "multi_form.fields.description").build(),
                                JpaFormElement.builder("age", "multi_form.fields.age").build()
                        ))
                        .build();

        // Create MultiFormRoute with both forms
        MultiFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> multiFormRoute =
                MultiFormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(multiFormConfig)
                        .title("route.multi_form.title")
                        .forms(List.of(basicInfoForm, additionalDetailsForm))
                        .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // Use multi-form route for create/edit
        routes.put("multi-form-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(multiFormConfig)
                .iconFactory(USER::create)
                .title("route.multi_form.title")
                .searchField("profileName")
                .columns(List.of(
                        JpaFormElement.builder("profileName", "relations.labels.name").build()
                ))
                .form(multiFormRoute)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
