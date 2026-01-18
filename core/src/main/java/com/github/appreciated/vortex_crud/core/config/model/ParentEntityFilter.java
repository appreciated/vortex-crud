package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.function.SerializableFunction;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Configuration for filtering child routes by parent entity ID from the route context.
 *
 * Example: For URL "/projects/123/tasks", this extracts "123" from the context
 * where the context contains {projects: 123}, and filters tasks by PROJECT_ID = 123
 */
@Accessors(fluent = true)
@Getter
@Builder
public class ParentEntityFilter<FieldType> {

    /**
     * The field in the child entity to filter by (e.g., TASK.PROJECT_ID)
     */
    private final FieldType filterField;

    /**
     * The name of the parent route whose ID should be used for filtering.
     * Example: For "/projects/123/tasks", use "projects" to get ID 123
     */
    private final String parentRouteName;

    /**
     * Optional converter to transform the ID from string to the appropriate type.
     * Default: Integer conversion
     */
    @Builder.Default
    private final SerializableFunction<String, Object> idConverter = Integer::parseInt;
}
