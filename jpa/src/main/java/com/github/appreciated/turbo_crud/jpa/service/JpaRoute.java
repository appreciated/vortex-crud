package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;

public class JpaRoute extends Route<String, String> {
        public JpaRoute(Class<? extends TurboCrudRouteFactory<String, String>> factory) {
            super(factory);
        }

        public static Route.Builder<String, String> of(Class<? extends TurboCrudRouteFactory> factory) {
            return new Route.Builder<>(new Route<>((Class<? extends TurboCrudRouteFactory<String, String>>)factory));
        }
    }