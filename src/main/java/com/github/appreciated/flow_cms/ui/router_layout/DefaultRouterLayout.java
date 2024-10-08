package com.github.appreciated.flow_cms.ui.router_layout;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Map;
import java.util.Set;

/**
 * Default implementation of an application layout using Vaadin's AppLayout.
 * It configures the navigation drawer and the application title based on the FlowCmsConfigService configuration.
 */

public class DefaultRouterLayout extends AppLayout {

    private final FlowCmsConfigService flowCmsConfigService;

    public DefaultRouterLayout(FlowCmsConfigService flowCmsConfigService) {
        this.flowCmsConfigService = flowCmsConfigService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1(flowCmsConfigService.getApplicationName());
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav(attachEvent.getUI());

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private SideNav getSideNav(UI ui) {
        SideNav nav = new SideNav();
        Set<Map.Entry<String, RouteConfig>> keys = flowCmsConfigService.getConfiguration().getRoutesConfig().entrySet();
        keys.forEach(configEntry -> nav.addItem(new SideNavItem(getTranslation(configEntry.getValue().getTitle()), "/view/" + configEntry.getKey(), VaadinIcon.DASHBOARD.create())));
        return nav;
    }

}