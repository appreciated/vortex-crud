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
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
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

    private final JpaFieldValidationRepository validationEntityRepository;
    private final JpaCheckboxValidationRepository checkboxValidationRepository;
    private final JpaDateValidationRepository dateValidationRepository;
    private final JpaDateTimeValidationRepository dateTimeValidationRepository;
    private final JpaEmailValidationRepository emailValidationRepository;
    private final JpaImageValidationRepository imageValidationRepository;
    private final JpaNumberValidationRepository numberValidationRepository;
    private final JpaSelectValidationRepository selectValidationRepository;
    private final JpaTextValidationRepository textValidationRepository;
    private final JpaLifecycleValidationRepository lifecycleValidationRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaFieldValidationVortexCrudConfiguration(
            JpaFieldValidationRepository validationEntityRepository,
            JpaCheckboxValidationRepository checkboxValidationRepository,
            JpaDateValidationRepository dateValidationRepository,
            JpaDateTimeValidationRepository dateTimeValidationRepository,
            JpaEmailValidationRepository emailValidationRepository,
            JpaImageValidationRepository imageValidationRepository,
            JpaNumberValidationRepository numberValidationRepository,
            JpaSelectValidationRepository selectValidationRepository,
            JpaTextValidationRepository textValidationRepository,
            JpaLifecycleValidationRepository lifecycleValidationRepository,
            JpaFieldService fieldService,
            JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.validationEntityRepository = validationEntityRepository;
        this.checkboxValidationRepository = checkboxValidationRepository;
        this.dateValidationRepository = dateValidationRepository;
        this.dateTimeValidationRepository = dateTimeValidationRepository;
        this.emailValidationRepository = emailValidationRepository;
        this.imageValidationRepository = imageValidationRepository;
        this.numberValidationRepository = numberValidationRepository;
        this.selectValidationRepository = selectValidationRepository;
        this.textValidationRepository = textValidationRepository;
        this.lifecycleValidationRepository = lifecycleValidationRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        addRoute(routes, "field-validation-test", validationEntityRepository);
        addRoute(routes, "checkbox-validation-test", checkboxValidationRepository);
        addRoute(routes, "date-validation-test", dateValidationRepository);
        addRoute(routes, "datetime-validation-test", dateTimeValidationRepository);
        addRoute(routes, "email-validation-test", emailValidationRepository);
        addRoute(routes, "image-validation-test", imageValidationRepository);
        addRoute(routes, "number-validation-test", numberValidationRepository);
        addRoute(routes, "select-validation-test", selectValidationRepository);
        addRoute(routes, "text-validation-test", textValidationRepository);
        addRoute(routes, "lifecycle-validation-test", lifecycleValidationRepository);

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

    private <T> void addRoute(LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes, String routeName, JpaRepository<T, ?> repository) {
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
