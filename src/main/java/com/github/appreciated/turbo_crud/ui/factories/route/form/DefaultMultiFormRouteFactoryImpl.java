package com.github.appreciated.turbo_crud.ui.factories.route.form;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.FormConfiguration;
import com.github.appreciated.turbo_crud.config.model.MultiFormConfiguration;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class DefaultMultiFormRouteFactoryImpl implements TurboCrudRouteFactory {

    private final DefaultFormRouteFactoryImpl formRouteFactory;

    private String titleColumn;

    public DefaultMultiFormRouteFactoryImpl(
            TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
            TurboCrudConfigService configService,
            FormCreator formCreator,
            TurboCrudRouteFactoryRegistry factoryRegistry
    ) {
        this.formRouteFactory = new DefaultFormRouteFactoryImpl(entityManagerFactoryRegistry, configService, formCreator, factoryRegistry);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 boolean isWrapped,
                                 boolean hideHeader) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        MultiFormConfiguration formConfiguration = ConfigBeanFactory.create(route.getConfiguration(), MultiFormConfiguration.class);
        Div div = new Div();
        for (FormConfiguration child : formConfiguration.getChildren()) {
            div.add(formRouteFactory.getForm(routeResolver, isWrapped, hideHeader, route, child));
        }
        return div;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
