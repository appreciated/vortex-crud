package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import java.util.Map;

/**
 * Default implementation of an application layout using Vaadin's AppLayout.
 * It configures the navigation drawer and the application title based on the {@link VortexCrudConfigService} configuration.
 */

public class DefaultRouterLayout<DataStoreId, FieldId, KeyType> extends AppLayout {

    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;

    public DefaultRouterLayout(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService) {
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
        HorizontalLayout horizontalLayout = new HorizontalLayout(toggle, title);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setPadding(true);
        addToNavbar(horizontalLayout);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        Map<String, ? extends RouteRenderer<DataStoreId, FieldId, KeyType>> routes = configService.getConfiguration().getRouteRenderers();
        routes.forEach((path, value) -> {
            if (!value.isHideInMenu()) {
                String translation = getTranslation(value.getTitle());
                Component icon = null;
                if (value.getIconFactory() != null) {
                    icon = value.getIconFactory().get();
                }
                if (icon != null) {
                    icon.getStyle()
                            .set("color", "var(--lumo-primary-text-color)")
                            .set("opacity", "0.5");
                }
                SideNavItem sideNavItem = new SideNavItem(translation, path, icon);
                sideNavItem.setMatchNested(true);
                nav.addItem(sideNavItem);
            }
        });
        return nav;
    }

}