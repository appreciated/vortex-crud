package com.github.appreciated.vortex_crud.core.ui.factories.login;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class LoginFactory<DataStoreId, FieldId> implements VortexCrudLoginFactory<DataStoreId, FieldId> {

    @Override
    public Component getLoginView(Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return new LoginView();
    }
}