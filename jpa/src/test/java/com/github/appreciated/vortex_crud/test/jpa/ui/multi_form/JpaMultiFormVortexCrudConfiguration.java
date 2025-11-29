package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRendererConfiguration;
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
        var multiFormStore = new JpaRepositoryDataStore<>(multiFormRepository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(multiFormStore.getModelClass(), multiFormStore);

        var multiFormConfig = JpaDataStoreConfig.builder(multiFormRepository, multiFormStore)
                .withServices(fieldService, storeMap)
                .build();

        // Individual form configurations for multi-form rendering
        FormRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> basicInfoForm =
                JpaFormRendererConfiguration.builder()
                        .titleField("profileName")
                        .children(List.of(
                                JpaFieldElement.builder("profileName", "multi_form.fields.profile_name").build(),
                                JpaFieldElement.builder("email", "multi_form.fields.email").build()
                        ))
                        .build();

        FormRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> additionalDetailsForm =
                JpaFormRendererConfiguration.builder()
                        .titleField("description")
                        .children(List.of(
                                JpaFieldElement.builder("description", "multi_form.fields.description").build(),
                                JpaFieldElement.builder("age", "multi_form.fields.age").build()
                        ))
                        .build();

        // Create MultiFormRoute with both forms
        MultiFormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> multiFormRoute =
                MultiFormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(multiFormConfig)
                        .title("route.multi_form.title")
                        .configuration(MultiFormRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .forms(List.of(basicInfoForm, additionalDetailsForm))
                                .build())
                        .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        // Use multi-form route for create/edit
        routes.put("multi-form-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(multiFormConfig)
                .iconFactory(USER::create)
                .title("route.multi_form.title")
                .configuration(ListItemRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .filterField("profileName")
                        .children(List.of(
                                JpaFieldElement.builder("profileName", "relations.labels.name").build()
                        ))
                        .build())
                .child(multiFormRoute)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
