package com.github.appreciated.vortex_crud.test.jooq.ui.submenu;

import com.github.appreciated.vortex_crud.test.jooq.ui.subroute.JooqSubrouteTestApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.appreciated.vortex_crud.jooq",
        "com.github.appreciated.vortex_crud.test.jooq.ui.submenu" // Scan the test configuration
})
public class JooqSubmenuRouteTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(JooqSubmenuRouteTestApplication.class, args);
    }
}
