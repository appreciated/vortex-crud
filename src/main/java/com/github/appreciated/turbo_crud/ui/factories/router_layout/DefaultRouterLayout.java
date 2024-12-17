package com.github.appreciated.turbo_crud.ui.factories.router_layout;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Map;
import java.util.Set;

/**
 * Default implementation of an application layout using Vaadin's AppLayout.
 * It configures the navigation drawer and the application title based on the {@link TurboCrudConfigService} configuration.
 */

public class DefaultRouterLayout extends AppLayout {

    private final TurboCrudConfigService configService;

    public DefaultRouterLayout(TurboCrudConfigService configService) {
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
        Set<Map.Entry<String, Route>> keys = configService.getConfiguration().getRoutes().entrySet();
        keys.forEach(configEntry -> {
            Route value = configEntry.getValue();
            if (!value.isHideInMenu()) {
                String translation = getTranslation(value.getTitle());
                String path = configEntry.getKey();
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