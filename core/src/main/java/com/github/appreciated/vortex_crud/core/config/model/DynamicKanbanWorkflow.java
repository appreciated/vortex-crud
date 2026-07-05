package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * {@link KanbanWorkflow} whose allowed transitions live as rows in a database table
 * (e.g. {@code workflow_transition}) instead of the application configuration, so the
 * flow can be changed at runtime — for example through a regular CRUD route on the
 * definitions table — without a redeploy.
 * <p>
 * Each definition row declares one allowed transition via {@link #fromField} and
 * {@link #toField}. If several boards share one table, {@link #entityTypeField} and
 * {@link #entityType} scope the rows to one board. Column values are compared against
 * the stored values through {@link #columnValueMapper} (defaults to
 * {@link String#valueOf}); pass a custom mapper when the board's column values are
 * enums whose database literal differs from their {@code toString()}.
 */
@Accessors(fluent = true)
@Builder
@Getter
public class DynamicKanbanWorkflow<ModelClass, FieldType, RepositoryType>
        implements KanbanWorkflow<ModelClass, FieldType, RepositoryType> {

    /**
     * Data store holding the transition definition rows.
     */
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> definitionsDataStoreConfig;

    /**
     * Optional definition column: board discriminator, filtered by {@link #entityType}.
     */
    private FieldType entityTypeField;

    /**
     * Value used to filter the definitions table by {@link #entityTypeField}.
     */
    private String entityType;

    /**
     * Definition column: source column value of the allowed transition.
     */
    private FieldType fromField;

    /**
     * Definition column: target column value of the allowed transition.
     */
    private FieldType toField;

    /**
     * Maps the board's column values to the representation stored in the definitions
     * table, e.g. {@code status -> ((TaskStatus) status).getValue()}.
     */
    @Builder.Default
    private Function<Object, String> columnValueMapper = String::valueOf;

    @Override
    public boolean isTransitionAllowed(Object from, Object to, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        if (Objects.equals(from, to)) {
            return true;
        }
        ReflectionService<FieldType> reflectionService = context.reflectionService();
        VortexCrudDataStore<FieldType, ModelClass> definitionsStore = definitionsDataStoreConfig.dataStoreInstance();

        String fromValue = columnValueMapper.apply(from);
        String toValue = columnValueMapper.apply(to);

        List<ModelClass> transitions = definitionsStore.getRecordsFromTableWhereColumnEquals(
                fromField, fromValue, 0, Integer.MAX_VALUE);
        return transitions.stream()
                .filter(transition -> entityTypeField == null
                        || Objects.equals(reflectionService.getString(transition, entityTypeField), entityType))
                .anyMatch(transition -> Objects.equals(reflectionService.getString(transition, toField), toValue));
    }
}
