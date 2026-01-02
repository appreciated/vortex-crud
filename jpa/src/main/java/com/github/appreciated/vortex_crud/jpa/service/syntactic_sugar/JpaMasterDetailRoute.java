package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@SuperBuilder
public class JpaMasterDetailRoute extends MasterDetailRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>>{
    public static MasterDetailRoute.MasterDetailRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> builder() {
        return MasterDetailRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder();
    }
}