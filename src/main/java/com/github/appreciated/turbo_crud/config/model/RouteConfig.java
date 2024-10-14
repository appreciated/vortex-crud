package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class RouteConfig {

    private String table;
    private String title;
    @Optional
    private String icon;
    @Optional
    private boolean defaultRoute;
    private String factory;
    @Optional
    private boolean hideInMenu;
    @Optional
    private ItemFactoryConfig items;
    @Optional
    private DetailFactory detail;


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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public ItemFactoryConfig getItems() {
        return items;
    }

    public void setItems(ItemFactoryConfig items) {
        this.items = items;
    }

    public DetailFactory getDetail() {
        return detail;
    }

    public void setDetail(DetailFactory detail) {
        this.detail = detail;
    }
}

