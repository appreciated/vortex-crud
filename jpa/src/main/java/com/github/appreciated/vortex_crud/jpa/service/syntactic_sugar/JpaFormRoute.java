package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@SuperBuilder
public class JpaFormRoute extends FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
}