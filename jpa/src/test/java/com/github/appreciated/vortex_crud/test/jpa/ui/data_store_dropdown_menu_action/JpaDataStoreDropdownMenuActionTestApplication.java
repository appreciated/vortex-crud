package com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action;

import com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action.JpaDataStoreDropdownMenuActionVortexCrudConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Push
@EnableJpaRepositories(basePackageClasses = {JpaDropdownRepository.class})
@Import({JpaDataStoreDropdownMenuActionVortexCrudConfiguration.class})
public class JpaDataStoreDropdownMenuActionTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JpaDataStoreDropdownMenuActionTestApplication.class, args);
    }
}
