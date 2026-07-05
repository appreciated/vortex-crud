package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@link KanbanWorkflow} whose transitions are defined statically in the application
 * configuration. Keys and values are the column values of the board's column field
 * (the keys of the backing select configuration). A column value without an entry
 * has no outgoing transitions.
 *
 * <pre>{@code
 * StaticKanbanWorkflow.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
 *         .transition(TaskStatus.TODO, List.of(TaskStatus.IN_PROGRESS, TaskStatus.BLOCKED))
 *         .transition(TaskStatus.IN_PROGRESS, List.of(TaskStatus.IN_REVIEW, TaskStatus.BLOCKED, TaskStatus.TODO))
 *         .transition(TaskStatus.IN_REVIEW, List.of(TaskStatus.DONE, TaskStatus.IN_PROGRESS))
 *         .build()
 * }</pre>
 */
@Accessors(fluent = true)
@Builder
@Getter
public class StaticKanbanWorkflow<ModelClass, FieldType, RepositoryType>
        implements KanbanWorkflow<ModelClass, FieldType, RepositoryType> {

    /**
     * Allowed transitions: source column value → target column values.
     */
    @Singular
    private Map<Object, List<Object>> transitions;

    @Override
    public boolean isTransitionAllowed(Object from, Object to, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        if (Objects.equals(from, to)) {
            return true;
        }
        List<Object> allowedTargets = transitions.get(from);
        return allowedTargets != null && allowedTargets.contains(to);
    }
}
