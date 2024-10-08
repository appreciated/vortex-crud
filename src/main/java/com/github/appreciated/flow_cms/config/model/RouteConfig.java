package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigObject;
import com.typesafe.config.Optional;

import static com.github.appreciated.flow_cms.config.model.ConfigModelUtil.toStringMapWithValueType;

public class RouteConfig {

    private String table;
    private ConfigObject title;
    private String renderer;
    @Optional
    private boolean is_default;
    private RenderConfig render_configuration;
    @Optional
    private AccessControlConfig access_control;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ConfigObject getTitle() {
        return title;
    }

    public void setTitle(ConfigObject title) {
        this.title = title;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public RenderConfig getRender_configuration() {
        return render_configuration;
    }

    public void setRender_configuration(RenderConfig render_configuration) {
        this.render_configuration = render_configuration;
    }

    public AccessControlConfig getAccess_control() {
        return access_control;
    }

    public void setAccess_control(AccessControlConfig access_control) {
        this.access_control = access_control;
    }

    public Translatable getTitleConfig() {
        return new Translatable(toStringMapWithValueType(title, String.class));
    }

    public boolean isDefault() {
        return is_default;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}

