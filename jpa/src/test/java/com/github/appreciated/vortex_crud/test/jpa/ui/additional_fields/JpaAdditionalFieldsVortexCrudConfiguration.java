package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JpaAdditionalFieldsVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
        private final JpaAdditionalFieldsRepository additionalFieldsRepository;
        private final JpaLifecycleTestRepository lifecycleTestRepository;
        private final JpaPasswordTestRepository passwordTestRepository;
        private final JpaTextAreaTestRepository textAreaTestRepository;
        private final JpaFieldService fieldService;
        private final JpaFieldAnnotationRegistryService annotationRegistryService;
        private Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> cachedApplication;

        public JpaAdditionalFieldsVortexCrudConfiguration(
                JpaAdditionalFieldsRepository additionalFieldsRepository,
                JpaLifecycleTestRepository lifecycleTestRepository,
                JpaPasswordTestRepository passwordTestRepository,
                JpaTextAreaTestRepository textAreaTestRepository,
                JpaFieldService fieldService,
                JpaFieldAnnotationRegistryService annotationRegistryService,
                JdbcTemplate jdbcTemplate) {
            this.additionalFieldsRepository = additionalFieldsRepository;
            this.lifecycleTestRepository = lifecycleTestRepository;
            this.passwordTestRepository = passwordTestRepository;
            this.textAreaTestRepository = textAreaTestRepository;
            this.fieldService = fieldService;
            this.annotationRegistryService = annotationRegistryService;
        }

        @Override
        public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
            if (cachedApplication != null) {
                return cachedApplication;
            }

            LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
            routes.put("lifecycle-test", createLifecycleTestRoute());
            routes.put("password-test", createPasswordTestRoute());
            routes.put("textarea-test", createTextAreaTestRoute());

            cachedApplication = JpaApplication.builder()
                    .applicationName("application.name")
                    .i18nBundlePrefix("ui_test_i18n")
                    .routes(routes)
                    .build();

            return cachedApplication;
        }

        private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createLifecycleTestRoute() {
            var store = new JpaRepositoryDataStore<>(lifecycleTestRepository, annotationRegistryService, new DataStoreHooks<>());
            Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

            var config = JpaDataStoreConfig.builder(lifecycleTestRepository, store)
                    .withServices(fieldService, storeMap)
                    .build();

            FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> lifecycleForm = JpaFormRoute.builder()
                    .dataStoreConfig(config)
                    .title("route.lifecycle-test.title")
                    .formConfiguration(JpaFormRendererConfiguration.builder()
                            .titleField("name")
                            .children(List.of(
                                    JpaFieldElement.builder("name", "lifecycle-test.labels.name").build(),
                                    JpaFieldElement.builder("description", "lifecycle-test.labels.description").build()
                            ))
                            .build())
                    .build();

            RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig = JpaListItemRendererConfiguration.builder()
                    .filterField("name")
                    .children(List.of(
                            JpaFieldElement.builder("name", "lifecycle-test.labels.name").build(),
                            JpaFieldElement.builder("description", "lifecycle-test.labels.description").build()
                    ))
                    .build();

            return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .dataStoreConfig(config)
                    .iconFactory(COG::create)
                    .title("route.lifecycle-test.title-list")
                    .configuration(listConfig)
                    .child(lifecycleForm)
                    .build();
        }

        private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createPasswordTestRoute() {
            var store = new JpaRepositoryDataStore<>(passwordTestRepository, annotationRegistryService, new DataStoreHooks<>());
            Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

            var config = JpaDataStoreConfig.builder(passwordTestRepository, store)
                    .withServices(fieldService, storeMap)
                    .build();

            FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> passwordForm = JpaFormRoute.builder()
                    .dataStoreConfig(config)
                    .title("route.password-test.title")
                    .formConfiguration(JpaFormRendererConfiguration.builder()
                            .titleField("name")
                            .children(List.of(
                                    JpaFieldElement.builder("name", "password-test.labels.name").build(),
                                    JpaFieldElement.builder("password", "password-test.labels.password").build()
                            ))
                            .build())
                    .build();

            RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig = JpaListItemRendererConfiguration.builder()
                    .filterField("name")
                    .children(List.of(
                            JpaFieldElement.builder("name", "password-test.labels.name").build()
                    ))
                    .build();

            return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .dataStoreConfig(config)
                    .iconFactory(COG::create)
                    .title("route.password-test.title-list")
                    .configuration(listConfig)
                    .child(passwordForm)
                    .build();
        }

        private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createTextAreaTestRoute() {
            var store = new JpaRepositoryDataStore<>(textAreaTestRepository, annotationRegistryService, new DataStoreHooks<>());
            Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

            var config = JpaDataStoreConfig.builder(textAreaTestRepository, store)
                    .withServices(fieldService, storeMap)
                    .build();

            FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> textAreaForm = JpaFormRoute.builder()
                    .dataStoreConfig(config)
                    .title("route.textarea-test.title")
                    .formConfiguration(JpaFormRendererConfiguration.builder()
                            .titleField("name")
                            .children(List.of(
                                    JpaFieldElement.builder("name", "textarea-test.labels.name").build(),
                                    JpaFieldElement.builder("description", "textarea-test.labels.content").build()
                            ))
                            .build())
                    .build();

            RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig = JpaListItemRendererConfiguration.builder()
                    .filterField("name")
                    .children(List.of(
                            JpaFieldElement.builder("name", "textarea-test.labels.name").build(),
                            JpaFieldElement.builder("description", "textarea-test.labels.content").build()
                    ))
                    .build();

            return ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .dataStoreConfig(config)
                    .iconFactory(COG::create)
                    .title("route.textarea-test.title-list")
                    .configuration(listConfig)
                    .child(textAreaForm)
                    .build();
        }
    }