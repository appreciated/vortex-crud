package com.github.appreciated.turbo_crud.core.ui.factories.login;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class LoginFactory<DataStoreId, FieldId> implements TurboCrudLoginFactory<DataStoreId, FieldId> {

    @Override
    public Component getLoginView(Integer currentPathIndex, TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return new LoginView();
    }
}