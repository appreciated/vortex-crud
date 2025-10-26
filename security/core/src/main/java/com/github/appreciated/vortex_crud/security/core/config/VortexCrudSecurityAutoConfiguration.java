package com.github.appreciated.vortex_crud.security.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.github.appreciated.vortex_crud.security.core.config"})
public class VortexCrudSecurityAutoConfiguration {

    public VortexCrudSecurityAutoConfiguration() {
        System.out.println();
    }
}
