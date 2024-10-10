package com.github.appreciated.flow_cms.ui.factories.router_layout;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIcon;
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
 * It configures the navigation drawer and the application title based on the FlowCmsConfigService configuration.
 */

public class DefaultRouterLayout extends AppLayout {

    private final FlowCmsConfigService flowCmsConfigService;
    private final FlowCmsIcon flowCmsIcon;

    public DefaultRouterLayout(FlowCmsConfigService flowCmsConfigService, FlowCmsIcon flowCmsIcon) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.flowCmsIcon = flowCmsIcon;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1(getTranslation(flowCmsConfigService.getApplicationName()));
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        Set<Map.Entry<String, RouteConfig>> keys = flowCmsConfigService.getConfiguration().getRoutesConfig().entrySet();
        keys.forEach(configEntry -> {
            String translation = getTranslation(configEntry.getValue().getTitle());
            String path = "/view/" + configEntry.getKey();
            Component icon = null;
            if (configEntry.getValue().getIcon() != null) {
                icon = flowCmsIcon.renderIcon(configEntry.getValue().getIcon());
            }
            nav.addItem(new SideNavItem(translation, path, icon));
        });
        return nav;
    }

}