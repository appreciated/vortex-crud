package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaListItemRendererConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.vaadin.flow.component.icon.VaadinIcon.USER;

@Service
public class JpaMultiFormVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMultiFormRepository multiFormRepository;

    public JpaMultiFormVortexCrudConfiguration(JpaMultiFormRepository multiFormRepository) {
        this.multiFormRepository = multiFormRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // First form: Basic Information
        FormRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> basicInfoForm =
                JpaFormRendererConfiguration.builder()
                        .titleField("profileName")
                        .children(List.of(
                                JpaFieldElement.builder("profileName", "multi_form.fields.profile_name").build(),
                                JpaFieldElement.builder("email", "multi_form.fields.email").build()
                        ))
                        .build();

        // Second form: Additional Details
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
                        .dataStoreKey(multiFormRepository)
                        .title("route.multi_form.title")
                        .configuration(MultiFormRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .forms(List.of(basicInfoForm, additionalDetailsForm))
                                .build())
                        .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listConfig =
                JpaListItemRendererConfiguration.builder()
                        .filterField("profileName")
                        .children(List.of(
                                JpaFieldElement.builder("profileName", "multi_form.list.profile_name").build(),
                                JpaFieldElement.builder("email", "multi_form.list.email").build(),
                                JpaFieldElement.builder("description", "multi_form.fields.description").build(),
                                JpaFieldElement.builder("age", "multi_form.fields.age").build()
                        ))
                        .build();

        routes.put("multi-form-test", ListRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(multiFormRepository)
                .iconFactory(USER::create)
                .title("route.multi_form.title")
                .configuration(listConfig)
                .child(multiFormRoute)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
