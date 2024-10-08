package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigObject;
import com.typesafe.config.Optional;

import static com.github.appreciated.flow_cms.config.model.ConfigModelUtil.toStringMapWithValueType;

public class RouteConfig {

    private String table;
    private String title;
    private String renderer;
    @Optional
    private boolean defaultRoute;
    private RenderConfig renderConfiguration;
    @Optional
    private AccessControlConfig accessControl;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public RenderConfig getRenderConfiguration() {
        return renderConfiguration;
    }

    public void setRenderConfiguration(RenderConfig renderConfiguration) {
        this.renderConfiguration = renderConfiguration;
    }

    public AccessControlConfig getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControlConfig accessControl) {
        this.accessControl = accessControl;
    }

    public boolean isDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(boolean defaultRoute) {
        this.defaultRoute = defaultRoute;
    }
}

