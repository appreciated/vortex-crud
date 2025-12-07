package com.github.appreciated.vortex_crud.test.jooq.ui.many_to_many;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqManyToManyTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JooqManyToManyTestApplication.class, args);
    }

}
