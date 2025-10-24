package com.github.appreciated.vortex_crud.core.ui.factories.login;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SignUpView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final VortexCrudDataStore<FieldType, ModelClass> dataStore;
    private final IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config;

    public SignUpView(
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory
    ) {
        config = configService.getConfiguration().getUserManagement();
        dataStore = dataStoreFactoryRegistry.getDataStore(config.getRepositoryKey());
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        FormLayout formLayout = new FormLayout();
        formCreator.bindAndAddToLayout(dataStore, formRouteRenderer, config, null, routeFactory, tables, null, formLayout);
    }
}
