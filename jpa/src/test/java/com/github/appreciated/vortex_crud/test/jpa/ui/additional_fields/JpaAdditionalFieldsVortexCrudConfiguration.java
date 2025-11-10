package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.vaadin.flow.component.icon.VaadinIcon.COG;

@Service
public class JpaAdditionalFieldsVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaAdditionalFieldsRepository additionalFieldsRepository;

    public JpaAdditionalFieldsVortexCrudConfiguration(JpaAdditionalFieldsRepository additionalFieldsRepository) {
        this.additionalFieldsRepository = additionalFieldsRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> additionalFieldsForm = JpaFormRoute.builder()
                .dataStoreKey(additionalFieldsRepository)
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
                .dataStoreKey(additionalFieldsRepository)
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

}
