package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudLogoutService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.NotificationPanel;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinServletRequest;

import java.util.Map;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public class DefaultRouterLayout<ModelClass, FieldType, RepositoryType> extends AppLayout {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudLogoutService logoutService;
    private final ReflectionService<FieldType> reflectionService;

    public DefaultRouterLayout(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            VortexCrudLogoutService logoutService,
            ReflectionService<FieldType> reflectionService) {
        this.configService = configService;
        this.logoutService = logoutService;
        this.reflectionService = reflectionService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1(getTranslation(configService.applicationName()));
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.getStyle().set("padding", "calc(var(--lumo-space-xs) * 1.5)");

        addToDrawer(scroller);

        // Create action buttons layout
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setAlignItems(CENTER);

        // Add notification panel if configured
        NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> notificationConfig =
                configService.configuration().notificationPanelConfiguration();
        if (notificationConfig != null) {
            NotificationPanel<ModelClass, FieldType, RepositoryType> notificationPanel =
                    new NotificationPanel<>(notificationConfig, reflectionService);
            actionButtons.add(notificationPanel);
        }

        // Add logout button
        Button logoutButton = new Button("Logout", click -> handleLogout());
        actionButtons.add(logoutButton);

        HorizontalLayout appToggleTitle = new HorizontalLayout(toggle, title);
        appToggleTitle.setAlignItems(CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout(appToggleTitle, actionButtons);
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(CENTER);
        horizontalLayout.setJustifyContentMode(BETWEEN);
        horizontalLayout.setPadding(false);
        addToNavbar(horizontalLayout);

    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        Map<String, ? extends RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = configService.configuration().routes();
        routes.forEach((path, value) -> {
            if (!value.isHiddenInMenu()) {
                String translation = getTranslation(value.title());
                Component icon = null;
                if (value.iconFactory() != null) {
                    icon = value.iconFactory().get();
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
        logoutService.logout();
        // Invalidate the HTTP session
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request != null && request.getHttpServletRequest().getSession(false) != null) {
            request.getHttpServletRequest().getSession().invalidate();
        }

        // Navigate to login page
        UI.getCurrent().getPage().setLocation("/login");
    }

}