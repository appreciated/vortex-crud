package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum.*;
import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaFieldValidationVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaFieldValidationRepository validationEntityRepository;

    public JpaFieldValidationVortexCrudConfiguration(JpaFieldValidationRepository validationEntityRepository) {
        this.validationEntityRepository = validationEntityRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> validationForm = JpaFormRoute.builder()
                .dataStoreKey(validationEntityRepository)
                .title("route.projects.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
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
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build = JpaListItemRendererConfiguration.builder()
                .filterField("requiredField")
                .children(List.of(
                        JpaFieldElement.builder("requiredField", "route.projects.labels.name").build(),
                        JpaFieldElement.builder("emailField", "route.projects.labels.description").build()
                ))
                .build();
        routes.put("field-validation-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(validationEntityRepository)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .configuration(build)
                .child(validationForm)
                .build());

        LinkedHashMap<JpaFieldValidationEnum, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put(OPTION1, "enums.option1");
        enumOptions.put(OPTION2, "enums.option2");
        enumOptions.put(OPTION3, "enums.option3");

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}