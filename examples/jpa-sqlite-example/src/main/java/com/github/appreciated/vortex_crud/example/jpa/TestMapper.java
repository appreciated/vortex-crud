package com.github.appreciated.vortex_crud.example.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class TestMapper {

    @Bean
    public Jackson2ObjectMapperBuilder build(){
        return new Jackson2ObjectMapperBuilder();
    }

}
