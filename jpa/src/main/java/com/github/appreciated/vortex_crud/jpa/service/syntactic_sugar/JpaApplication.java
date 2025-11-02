package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JpaApplication extends Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static JpaApplication.JpaApplicationBuilder<?, ?> of() {
        return JpaApplication.builder();
    }
}
