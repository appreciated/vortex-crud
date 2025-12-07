package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JpaManyToManyTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JpaManyToManyTestApplication.class, args);
    }

}
