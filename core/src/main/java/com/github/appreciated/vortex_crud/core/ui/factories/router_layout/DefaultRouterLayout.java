package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class DefaultRouterLayout<ModelClass, FieldType, RepositoryType> extends AppLayout {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;

    public DefaultRouterLayout(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        this.configService = configService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1(getTranslation(configService.getApplicationName()));
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.getStyle().set("padding", "calc(var(--lumo-space-xs) * 1.5)");

        addToDrawer(scroller);
        Button logoutButton = new Button("Logout", click -> {
            handleLogout();
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout(toggle, title, logoutButton);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setPadding(true);
        addToNavbar(horizontalLayout);

    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        Map<String, ? extends RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = configService.getConfiguration().getRouteRenderers();
        routes.forEach((path, value) -> {
            if (!value.isHiddenInMenu()) {
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

    private void handleLogout() {
        // Clear the Spring Security context
        SecurityContextHolder.clearContext();

        // Invalidate the HTTP session
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request != null && request.getHttpServletRequest().getSession(false) != null) {
            request.getHttpServletRequest().getSession().invalidate();
        }

        // Navigate to login page
        UI.getCurrent().getPage().setLocation("/login");
    }

}