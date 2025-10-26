package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("sign-up")
public class SignUpView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    public SignUpView(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory
    ) {
        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> config = configService.getConfiguration().getUserManagement();
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        FormLayout formLayout = new FormLayout();

        DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = configService.getConfiguration().getDataStores().get(config.getRepositoryKey());

        formCreator.bindAndAddToLayout(config.getRepositoryKey(),
                formRouteRenderer,
                config.getSignUpFields(),
                null,
                routeFactory,
                dataStoreConfig,
                null,
                formLayout);
    }
}
