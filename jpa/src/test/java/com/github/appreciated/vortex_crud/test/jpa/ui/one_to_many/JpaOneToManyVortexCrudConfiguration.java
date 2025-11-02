package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

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
        RouteRendererSingleChild<JpaRepository<?, ?>, String, JpaRepository<?, ?>> childForm = FormRoute.builder()
                .dataStoreKey(childRepository)
                .configuration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .titleField("name")
                        .children(List.of(JpaFieldElement.of("name", "relations.labels.name").build())
                        )
                        .build())
                .build();

        RouteRendererSingleChild<JpaRepository<?, ?>, String, JpaRepository<?, ?>> parentForm = FormRoute.builder()
                .dataStoreKey(parentRepository)
                .configuration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "relations.labels.name").build(),
                                JpaCollectionElement.of("relations.labels.children")
                                        .factory((Class) ListCollectionFactory.class)
                                        .configuration(JpaCollection.of(FormDialogFactory.class)
                                                .data(JpaCollectionConfiguration.of(childRepository)
                                                        .oneToMany(new JpaOneToMany("parent"))
                                                        .children(List.of("name"))
                                                        .build())
                                                .emptyMessage("relations.children.empty")
                                                .child(childForm)
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", ListRoute.builder()
                .dataStoreKey(parentRepository)
                .iconFactory(FACTORY::create)
                .title("relations.tests.one-to-many.title")
                .configuration(ListItemRendererConfiguration.builder()
                        .filterField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "relations.labels.name").build()
                        ))
                        .build())
                .child(parentForm)
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
