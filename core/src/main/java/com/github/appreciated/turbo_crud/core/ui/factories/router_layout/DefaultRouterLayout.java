package com.github.appreciated.turbo_crud.core.ui.factories.router_layout;

import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import java.util.Map;

/**
 * Default implementation of an application layout using Vaadin's AppLayout.
 * It configures the navigation drawer and the application title based on the {@link TurboCrudConfigService} configuration.
 */

public class DefaultRouterLayout<DataStoreId, FieldId> extends AppLayout {

    private final TurboCrudConfigService<DataStoreId, FieldId> configService;

    public DefaultRouterLayout(TurboCrudConfigService<DataStoreId, FieldId> configService) {
        this.configService = configService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1(getTranslation(configService.getApplicationName()));
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.getStyle().set("padding", "calc(var(--lumo-space-xs) * 1.5)");

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        Map<String, ? extends RouteRenderer<DataStoreId, FieldId>> routes = configService.getConfiguration().getRouteRenderers();
        routes.forEach((path, value) -> {
            if (!value.isHideInMenu()) {
                String translation = getTranslation(value.getTitle());
                Component icon = null;
                if (value.getIconFactory() != null) {
                    icon = value.getIconFactory().get();
                }
                nav.addItem(new SideNavItem(translation, path, icon));
            }
        });
        return nav;
    }

}