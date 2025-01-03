package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;

public class JpaRouteRenderer extends RouteRenderer<String, String> {
        public JpaRouteRenderer(Class<? extends TurboCrudRouteFactory<String, String>> factory) {
            super(factory);
        }

        public static RouteRenderer.Builder<String, String> of(Class<? extends TurboCrudRouteFactory> factory) {
            return new RouteRenderer.Builder<>(new RouteRenderer<>((Class<? extends TurboCrudRouteFactory<String, String>>)factory));
        }
    }