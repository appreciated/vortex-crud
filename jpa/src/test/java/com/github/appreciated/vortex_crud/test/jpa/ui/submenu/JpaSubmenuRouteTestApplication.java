package com.github.appreciated.vortex_crud.test.jpa.ui.submenu;

import com.github.appreciated.vortex_crud.test.jpa.ui.subroute.JpaSubrouteTestRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.appreciated.vortex_crud.jpa",
        "com.github.appreciated.vortex_crud.test.jpa.ui.submenu" // Scan the test configuration
})
@EnableJpaRepositories(basePackageClasses = {JpaSubrouteTestRepository.class}) // Reuse repository
@EntityScan(basePackages = {"com.github.appreciated.vortex_crud.test.jpa.ui.subroute"}) // Reuse entity
public class JpaSubmenuRouteTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaSubmenuRouteTestApplication.class, args);
    }
}
