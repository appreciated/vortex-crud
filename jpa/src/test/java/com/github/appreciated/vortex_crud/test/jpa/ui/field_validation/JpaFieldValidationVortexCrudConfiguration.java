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
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormElement;
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
    private final JpaEmailFieldValidationRepository emailRepository;
    private final JpaDateFieldValidationRepository dateRepository;
    private final JpaCheckboxFieldValidationRepository checkboxRepository;
    private final JpaDateTimeFieldValidationRepository dateTimeRepository;
    private final JpaImageFieldValidationRepository imageRepository;
    private final JpaNumberFieldValidationRepository numberRepository;
    private final JpaTextFieldValidationRepository textRepository;
    private final JpaSelectFieldValidationRepository selectRepository;
    private final JpaLifecycleFieldValidationRepository lifecycleRepository;

    public JpaFieldValidationVortexCrudConfiguration(
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService,
            JpaEmailFieldValidationRepository emailRepository,
            JpaDateFieldValidationRepository dateRepository,
            JpaCheckboxFieldValidationRepository checkboxRepository,
            JpaDateTimeFieldValidationRepository dateTimeRepository,
            JpaImageFieldValidationRepository imageRepository,
            JpaNumberFieldValidationRepository numberRepository,
            JpaTextFieldValidationRepository textRepository,
            JpaSelectFieldValidationRepository selectRepository,
            JpaLifecycleFieldValidationRepository lifecycleRepository
    ) {
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
        this.emailRepository = emailRepository;
        this.dateRepository = dateRepository;
        this.checkboxRepository = checkboxRepository;
        this.dateTimeRepository = dateTimeRepository;
        this.imageRepository = imageRepository;
        this.numberRepository = numberRepository;
        this.textRepository = textRepository;
        this.selectRepository = selectRepository;
        this.lifecycleRepository = lifecycleRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        addRoute(routes, "email-field-validation-test", emailRepository);
        addRoute(routes, "date-field-validation-test", dateRepository);
        addRoute(routes, "checkbox-field-validation-test", checkboxRepository);
        addRoute(routes, "datetime-field-validation-test", dateTimeRepository);
        addRoute(routes, "image-field-validation-test", imageRepository);
        addRoute(routes, "number-field-validation-test", numberRepository);
        addRoute(routes, "text-field-validation-test", textRepository);
        addRoute(routes, "select-field-validation-test", selectRepository);
        addRoute(routes, "lifecycle-field-validation-test", lifecycleRepository);

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

    private <T extends BaseJpaFieldValidationEntity> void addRoute(LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes, String path, JpaRepository<T, Long> repository) {
        var store = new JpaRepositoryDataStore<>(repository, annotationRegistryService);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(repository, store)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> validationForm = JpaFormRoute.builder()
                .titleField("requiredField")
                .fields(List.of(
                        JpaFormElement.builder("requiredField", "validation.fields.required").build(),
                        JpaFormElement.builder("emailField", "validation.fields.email").build(),
                        JpaFormElement.builder("numericField", "validation.fields.numeric").build(),
                        JpaFormElement.builder("dateField", "validation.fields.date").build(),
                        JpaFormElement.builder("dateTimeField", "validation.fields.datetime").build(),
                        JpaFormElement.builder("checkboxField", "validation.fields.checkbox").build(),
                        JpaFormElement.builder("enumField", "validation.fields.enum").build(),
                        JpaFormElement.builder("imageField", "validation.fields.image").build()
                ))
                .build();

        routes.put(path, JpaListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField("requiredField")
                .columns(List.of(
                        JpaFormElement.builder("requiredField", "route.projects.labels.name").build(),
                        JpaFormElement.builder("emailField", "route.projects.labels.description").build()
                ))
                .form(validationForm)
                .build());
    }
}
