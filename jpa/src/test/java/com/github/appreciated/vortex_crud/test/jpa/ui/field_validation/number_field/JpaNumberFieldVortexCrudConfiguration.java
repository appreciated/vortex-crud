package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.number_field;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
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
import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;

import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION1;
import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION2;
import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.OPTION3;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaNumberFieldVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaNumberFieldRepository validationEntityRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaNumberFieldVortexCrudConfiguration(JpaNumberFieldRepository validationEntityRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.validationEntityRepository = validationEntityRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var store = new JpaRepositoryDataStore<>(validationEntityRepository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(store.getModelClass(), store);

        var config = JpaDataStoreConfig.builder(validationEntityRepository, store)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> validationForm = JpaFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField("requiredField")
                .children(List.of(
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

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JpaListRoute.builder()
                .dataStoreConfig(config)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField("requiredField")
                .children(List.of(
                        JpaFieldElement.builder("requiredField", "route.projects.labels.name").build(),
                        JpaFieldElement.builder("emailField", "route.projects.labels.description").build()
                ))
                .form(validationForm)
                .build());

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
}
