package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class JpaKanban extends Kanban<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static Kanban.KanbanBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return Kanban.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<String>>) factory);
    }
}