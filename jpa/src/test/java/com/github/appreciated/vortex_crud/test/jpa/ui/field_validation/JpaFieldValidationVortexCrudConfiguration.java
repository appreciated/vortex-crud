package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaListRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION1;
import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION2;
import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION3;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaFieldValidationVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    // Repositories
    private final JpaTextFieldRepository textFieldRepository;
    private final JpaCheckboxFieldRepository checkboxFieldRepository;
    private final JpaDateFieldRepository dateFieldRepository;
    private final JpaDateTimeFieldRepository dateTimeFieldRepository;
    private final JpaEmailFieldRepository emailFieldRepository;
    private final JpaImageFieldRepository imageFieldRepository;
    private final JpaNumberFieldRepository numberFieldRepository;
    private final JpaSelectFieldRepository selectFieldRepository;
    private final JpaLifecycleFieldRepository lifecycleFieldRepository;

    public JpaFieldValidationVortexCrudConfiguration(
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService,
            JpaTextFieldRepository textFieldRepository,
            JpaCheckboxFieldRepository checkboxFieldRepository,
            JpaDateFieldRepository dateFieldRepository,
            JpaDateTimeFieldRepository dateTimeFieldRepository,
            JpaEmailFieldRepository emailFieldRepository,
            JpaImageFieldRepository imageFieldRepository,
            JpaNumberFieldRepository numberFieldRepository,
            JpaSelectFieldRepository selectFieldRepository,
            JpaLifecycleFieldRepository lifecycleFieldRepository) {
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
        this.textFieldRepository = textFieldRepository;
        this.checkboxFieldRepository = checkboxFieldRepository;
        this.dateFieldRepository = dateFieldRepository;
        this.dateTimeFieldRepository = dateTimeFieldRepository;
        this.emailFieldRepository = emailFieldRepository;
        this.imageFieldRepository = imageFieldRepository;
        this.numberFieldRepository = numberFieldRepository;
        this.selectFieldRepository = selectFieldRepository;
        this.lifecycleFieldRepository = lifecycleFieldRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // Helper to register routes
        registerRoute(routes, "text-field-test", textFieldRepository);
        registerRoute(routes, "checkbox-field-test", checkboxFieldRepository);
        registerRoute(routes, "date-field-test", dateFieldRepository);
        registerRoute(routes, "datetime-field-test", dateTimeFieldRepository);
        registerRoute(routes, "email-field-test", emailFieldRepository);
        registerRoute(routes, "image-field-test", imageFieldRepository);
        registerRoute(routes, "number-field-test", numberFieldRepository);
        registerRoute(routes, "select-field-test", selectFieldRepository);
        registerRoute(routes, "lifecycle-field-test", lifecycleFieldRepository);

        LinkedHashMap<JpaFieldValidationEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

    private <T> void registerRoute(LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes, String routeName, JpaRepository<T, ?> repository) {
        var store = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(repository, store)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> validationForm = JpaFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField("requiredField")
                .fields(List.of(
                        JpaFieldElement.builder("requiredField", "validation.fields.required").build(),
                        JpaFieldElement.builder("emailField", "validation.fields.email").build(),
                        JpaFieldElement.builder("numericField", "validation.fields.numeric").build(),
                        JpaFieldElement.builder("dateField", "validation.fields.date").build(),
                        JpaFieldElement.builder("dateTimeField", "validation.fields.datetime").build(),
                        JpaFieldElement.builder("checkboxField", "validation.fields.checkbox").build(),
                        JpaFieldElement.builder("enumField", "validation.fields.enum").build(),
                        JpaFieldElement.builder("imageField", "validation.fields.image").build()
                ))
                .build();

        routes.put(routeName, JpaListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField("requiredField")
                .columns(List.of(
                        JpaFieldElement.builder("requiredField", "route.projects.labels.name").build(),
                        JpaFieldElement.builder("emailField", "route.projects.labels.description").build()
                ))
                .form(validationForm)
                .build());
    }
}
