package com.github.appreciated.flow_cms.ui.app_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Set;

public class AppLayoutNavbarPlacement extends AppLayout {

    private final FlowCmsConfigService flowCmsConfigService;

    public AppLayoutNavbarPlacement(FlowCmsConfigService flowCmsConfigService) {
        this.flowCmsConfigService = flowCmsConfigService;
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("MyApp");
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
        Set<String> keys = flowCmsConfigService.getViews().keySet();
        keys.forEach(key -> nav.addItem(new SideNavItem(key, "/"+key, VaadinIcon.DASHBOARD.create())));
        return nav;
    }

}