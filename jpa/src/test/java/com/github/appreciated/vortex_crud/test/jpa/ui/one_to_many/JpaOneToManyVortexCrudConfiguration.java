package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaRouteRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaOneToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaOneToManyParentRepository parentRepository;
    private final JpaOneToManyChildRepository childRepository;

    public JpaOneToManyVortexCrudConfiguration(JpaOneToManyParentRepository parentRepository, JpaOneToManyChildRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> childForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(childRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("name", "relations.labels.name")
                        )
                        .build())
                .build();

        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> parentForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(parentRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("name", "relations.labels.name"),
                                JpaCollectionElement.of("relations.labels.children")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JpaCollection.of(FormDialogFactory.class)
                                                .withData(JpaCollectionConfiguration.of(childRepository)
                                                        .withOneToMany(new com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany("parent"))
                                                        .withChildren("name")
                                                        .build())
                                                .withEmptyMessage("relations.children.empty")
                                                .withChild(childForm)
                                                .build())
                                        .build()
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(parentRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("relations.tests.one-to-many.title")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "relations.labels.name")
                        )
                        .build())
                .withChild(parentForm)
                .build());

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .build();
    }
}
