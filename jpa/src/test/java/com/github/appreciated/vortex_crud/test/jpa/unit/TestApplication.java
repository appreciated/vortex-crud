package com.github.appreciated.vortex_crud.test.jpa.unit;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.jpa.service.JpaDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.jpa.service.JpaReflectionService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Push
public class TestApplication implements AppShellConfigurator {

    @Bean
    public ReflectionService<String> reflectionService(JpaDataStoreFieldNameResolver fieldNameResolver) {
        return new JpaReflectionService(fieldNameResolver);
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
