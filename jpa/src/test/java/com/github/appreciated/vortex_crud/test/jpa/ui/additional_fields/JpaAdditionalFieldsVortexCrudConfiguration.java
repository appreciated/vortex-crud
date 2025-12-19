package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
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

import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JpaAdditionalFieldsVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaAdditionalFieldsRepository additionalFieldsRepository;
    private final JpaLifecycleTestRepository lifecycleTestRepository;
    private final JpaPasswordTestRepository passwordTestRepository;
    private final JpaTextAreaTestRepository textAreaTestRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaAdditionalFieldsVortexCrudConfiguration(
            JpaAdditionalFieldsRepository additionalFieldsRepository,
            JpaLifecycleTestRepository lifecycleTestRepository,
            JpaPasswordTestRepository passwordTestRepository,
            JpaTextAreaTestRepository textAreaTestRepository,
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.additionalFieldsRepository = additionalFieldsRepository;
        this.lifecycleTestRepository = lifecycleTestRepository;
        this.passwordTestRepository = passwordTestRepository;
        this.textAreaTestRepository = textAreaTestRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        JpaRepository<?, ?> activeRepository = findActiveRepository();
        return createApplicationForRepository(activeRepository);
    }

    private <T> Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createApplicationForRepository(JpaRepository<T, ?> repository) {
        var store = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(repository, store)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> additionalFieldsForm = JpaFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.additional-fields.title")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "additional-fields.labels.name").build(),
                                JpaFieldElement.builder("description", "additional-fields.labels.description").build(),
                                JpaFieldElement.builder("password", "additional-fields.labels.password").build(),
                                JpaFieldElement.builder("price", "additional-fields.labels.price").build(),
                                JpaFieldElement.builder("videoUrl", "additional-fields.labels.video").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig = JpaListItemRendererConfiguration.builder()
                .filterField("name")
                .children(List.of(
                        JpaFieldElement.builder("name", "additional-fields.labels.name").build(),
                        JpaFieldElement.builder("description", "additional-fields.labels.description").build()
                ))
                .build();

        routes.put("additional-fields-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreConfig(config)
                .iconFactory(COG::create)
                .title("route.additional-fields.title-list")
                .configuration(listConfig)
                .child(additionalFieldsForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }

    private JpaRepository<?, ?> findActiveRepository() {
        JpaRepository<?, ?>[] repositories = {lifecycleTestRepository, passwordTestRepository, textAreaTestRepository, additionalFieldsRepository};

        for (JpaRepository<?, ?> repository : repositories) {
            try {
                if (repository.count() > 0) {
                    return repository;
                }
            } catch (Exception e) {
                // Table might not exist yet or other issue, continue
            }
        }

        return additionalFieldsRepository;
    }

}
