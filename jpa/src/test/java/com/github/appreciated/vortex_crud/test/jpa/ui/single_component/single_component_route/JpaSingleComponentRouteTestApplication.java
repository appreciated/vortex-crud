package com.github.appreciated.vortex_crud.test.jpa.ui.single_component.single_component_route;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackageClasses = {
        JpaSingleComponentRouteVortexCrudConfiguration.class
    }
)
@EnableJpaRepositories(basePackageClasses = {JpaSingleComponentRouteRepository.class})
@EntityScan(basePackageClasses = {JpaSingleComponentRouteEntity.class})
@Push
public class JpaSingleComponentRouteTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JpaSingleComponentRouteTestApplication.class, args);
    }
}
