package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.testsupport.MapReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class KanbanWorkflowTest {

    @SuppressWarnings("unchecked")
    private final VortexCrudContext<Map<String, Object>, String, Object> context = Mockito.mock(VortexCrudContext.class);

    @Test
    void staticWorkflowAllowsConfiguredTransitions() {
        StaticKanbanWorkflow<Map<String, Object>, String, Object> workflow =
                StaticKanbanWorkflow.<Map<String, Object>, String, Object>builder()
                        .transition("todo", List.of("in_progress", "blocked"))
                        .transition("in_progress", List.of("done"))
                        .build();

        assertTrue(workflow.isTransitionAllowed("todo", "in_progress", context));
        assertTrue(workflow.isTransitionAllowed("todo", "blocked", context));
        assertTrue(workflow.isTransitionAllowed("in_progress", "done", context));
    }

    @Test
    void staticWorkflowRejectsUnconfiguredTransitions() {
        StaticKanbanWorkflow<Map<String, Object>, String, Object> workflow =
                StaticKanbanWorkflow.<Map<String, Object>, String, Object>builder()
                        .transition("todo", List.of("in_progress"))
                        .build();

        assertFalse(workflow.isTransitionAllowed("todo", "done", context));
        assertFalse(workflow.isTransitionAllowed("done", "todo", context));
    }

    @Test
    void stayingInTheSameColumnIsAlwaysAllowed() {
        StaticKanbanWorkflow<Map<String, Object>, String, Object> workflow =
                StaticKanbanWorkflow.<Map<String, Object>, String, Object>builder().build();

        assertTrue(workflow.isTransitionAllowed("todo", "todo", context));
    }

    @org.junit.jupiter.api.Nested
    class DynamicWorkflowTests {

        @SuppressWarnings("unchecked")
        private final VortexCrudContext<Map<String, Object>, String, Object> context = Mockito.mock(VortexCrudContext.class);
        @SuppressWarnings("unchecked")
        private final VortexCrudDataStore<String, Map<String, Object>> definitionsStore = Mockito.mock(VortexCrudDataStore.class);

        private DynamicKanbanWorkflow<Map<String, Object>, String, Object> workflow;

        @BeforeEach
        void setUp() {
            when(context.reflectionService()).thenReturn(new MapReflectionService());
            DataStoreConfig<Map<String, Object>, String, Object> definitionsConfig =
                    DataStoreConfig.<Map<String, Object>, String, Object>builder()
                            .dataStoreInstance(definitionsStore)
                            .fields(Map.of())
                            .build();
            workflow = DynamicKanbanWorkflow.<Map<String, Object>, String, Object>builder()
                    .definitionsDataStoreConfig(definitionsConfig)
                    .entityTypeField("entity_type")
                    .entityType("task")
                    .fromField("from_status")
                    .toField("to_status")
                    .build();
        }

        @Test
        void allowsTransitionDeclaredInTheDefinitionsTable() {
            when(definitionsStore.getRecordsFromTableWhereColumnEquals(eq("from_status"), eq("todo"), anyInt(), anyInt()))
                    .thenReturn(List.of(transition("task", "todo", "in_progress")));

            assertTrue(workflow.isTransitionAllowed("todo", "in_progress", context));
        }

        @Test
        void rejectsTransitionMissingFromTheDefinitionsTable() {
            when(definitionsStore.getRecordsFromTableWhereColumnEquals(eq("from_status"), eq("todo"), anyInt(), anyInt()))
                    .thenReturn(List.of(transition("task", "todo", "in_progress")));

            assertFalse(workflow.isTransitionAllowed("todo", "done", context));
        }

        @Test
        void ignoresTransitionsOfOtherEntityTypes() {
            when(definitionsStore.getRecordsFromTableWhereColumnEquals(eq("from_status"), eq("todo"), anyInt(), anyInt()))
                    .thenReturn(List.of(transition("issue", "todo", "in_progress")));

            assertFalse(workflow.isTransitionAllowed("todo", "in_progress", context));
        }

        @Test
        void appliesTheColumnValueMapper() {
            // Column values arrive e.g. as enums; the mapper converts them to the stored representation
            DynamicKanbanWorkflow<Map<String, Object>, String, Object> enumWorkflow =
                    DynamicKanbanWorkflow.<Map<String, Object>, String, Object>builder()
                            .definitionsDataStoreConfig(DataStoreConfig.<Map<String, Object>, String, Object>builder()
                                    .dataStoreInstance(definitionsStore)
                                    .fields(Map.of())
                                    .build())
                            .fromField("from_status")
                            .toField("to_status")
                            .columnValueMapper(value -> value.toString().toLowerCase())
                            .build();
            when(definitionsStore.getRecordsFromTableWhereColumnEquals(eq("from_status"), eq("todo"), anyInt(), anyInt()))
                    .thenReturn(List.of(transition(null, "todo", "done")));

            assertTrue(enumWorkflow.isTransitionAllowed("TODO", "DONE", context));
        }

        @Test
        void stayingInTheSameColumnNeedsNoDefinition() {
            assertTrue(workflow.isTransitionAllowed("todo", "todo", context));
        }

        private Map<String, Object> transition(String entityType, String from, String to) {
            return Map.of("entity_type", String.valueOf(entityType), "from_status", from, "to_status", to);
        }
    }

}
