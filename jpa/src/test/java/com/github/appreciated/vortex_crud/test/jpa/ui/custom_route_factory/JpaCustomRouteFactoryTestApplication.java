package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@Push
@ComponentScan(
        basePackages = {"com.github.appreciated.vortex_crud.jpa"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        VortexCrudConfigService.class // Exclude global config service to allow local override
                })
        }
)
public class JpaCustomRouteFactoryTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JpaCustomRouteFactoryTestApplication.class, args);
    }
}
