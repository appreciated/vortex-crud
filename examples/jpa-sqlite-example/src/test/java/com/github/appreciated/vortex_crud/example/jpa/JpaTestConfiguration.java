package com.github.appreciated.vortex_crud.example.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan("com.github.appreciated.vortex_crud.example.jpa.entity")
public class JpaTestConfiguration {
}
