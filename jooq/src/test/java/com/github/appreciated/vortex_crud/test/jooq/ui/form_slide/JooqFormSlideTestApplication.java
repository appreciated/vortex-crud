package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class JooqFormSlideTestApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JooqFormSlideTestApplication.class, args);
    }
}
