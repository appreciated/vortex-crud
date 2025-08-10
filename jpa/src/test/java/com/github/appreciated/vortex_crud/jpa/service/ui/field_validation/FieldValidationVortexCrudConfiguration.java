package com.github.appreciated.vortex_crud.jpa.service.ui.field_validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaRouteRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaRouteRenderer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class FieldValidationVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final FieldValidationRepository validationEntityRepository;

    public FieldValidationVortexCrudConfiguration(FieldValidationRepository validationEntityRepository) {
        this.validationEntityRepository = validationEntityRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> validationForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(validationEntityRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("requiredField", "validation.fields.required"),
                                new JpaFieldElement("emailField", "validation.fields.email"),
                                new JpaFieldElement("numericField", "validation.fields.numeric"),
                                new JpaFieldElement("dateField", "validation.fields.date"),
                                new JpaFieldElement("enumField", "validation.fields.enum")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("field-validation-test", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(validationEntityRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-list")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description"),
                                new JpaFieldElement("startDate", "route.projects.labels.start_date"),
                                new JpaFieldElement("endDate", "route.projects.labels.end_date")
                        )
                        .build())
                .withChild(validationForm)
                .build());

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("OPTION1", "enums.option1");
        enumOptions.put("OPTION2", "enums.option2");
        enumOptions.put("OPTION3", "enums.option3");

        return JpaApplication.of()
                .withName("validation.application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withSelects(Selects.of().withConfigs(Map.of("enum-options", enumOptions)).build())
                .build();
    }

}