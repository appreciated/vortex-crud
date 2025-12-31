package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class VortexCrudQueryDataStoreAdapterTest {

    @Mock
    private VortexCrudDataStore<String, TestModel> delegate;
    @Mock
    private ReflectionService<String> reflectionService;

    private VortexCrudQueryDataStoreAdapter<String, TestModel> adapter;

    static class TestModel {
        String id;
        String name;
        int value;

        TestModel(String id, String name, int value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new VortexCrudQueryDataStoreAdapter<>(delegate, reflectionService);
    }

    @Test
    void getRecordsFromTableWhereColumnLike_shouldFilterInMemory() {
        TestModel m1 = new TestModel("1", "Apple", 10);
        TestModel m2 = new TestModel("2", "Banana", 20);
        TestModel m3 = new TestModel("3", "Pineapple", 30);
        List<TestModel> allRecords = Arrays.asList(m1, m2, m3);

        when(delegate.getRecordsFromTable(0, Integer.MAX_VALUE)).thenReturn(allRecords);
        when(reflectionService.getValue(any(), eq("name"))).thenAnswer(invocation -> {
            TestModel model = invocation.getArgument(0);
            return model.name;
        });

        List<TestModel> result = adapter.getRecordsFromTableWhereColumnLike("name", "apple", 0, 10);

        assertEquals(2, result.size());
        assertTrue(result.contains(m1));
        assertTrue(result.contains(m3));
    }

    @Test
    void countWhereFiltersEqual_shouldCountInMemory() {
        TestModel m1 = new TestModel("1", "A", 10);
        TestModel m2 = new TestModel("2", "B", 20);
        TestModel m3 = new TestModel("3", "A", 30);
        List<TestModel> allRecords = Arrays.asList(m1, m2, m3);

        when(delegate.getRecordsFromTable(0, Integer.MAX_VALUE)).thenReturn(allRecords);
        when(reflectionService.getValue(any(), eq("name"))).thenAnswer(invocation -> {
            TestModel model = invocation.getArgument(0);
            return model.name;
        });

        List<RouteFilter<String>> filters = List.of(new RouteFilter<>("name", "A"));
        int count = adapter.countWhereFiltersEqual(filters);

        assertEquals(2, count);
    }

    @Test
    void getRecordsFromTableWhereColumnEqualsOrdered_shouldSortInMemory() {
        TestModel m1 = new TestModel("1", "C", 30);
        TestModel m2 = new TestModel("2", "A", 10);
        TestModel m3 = new TestModel("3", "B", 20);
        List<TestModel> allRecords = Arrays.asList(m1, m2, m3);

        when(delegate.getRecordsFromTable(0, Integer.MAX_VALUE)).thenReturn(allRecords);
        // Filter by id != null (dummy filter logic for test if needed, but method filters by column equals)
        when(reflectionService.getValue(any(), eq("filterCol"))).thenReturn("match");

        when(reflectionService.getValue(any(), eq("value"))).thenAnswer(invocation -> {
            TestModel model = invocation.getArgument(0);
            return model.value;
        });

        List<TestModel> result = adapter.getRecordsFromTableWhereColumnEqualsOrdered("filterCol", "match", "value", 0, 10);

        assertEquals(3, result.size());
        assertEquals(m2, result.get(0)); // 10
        assertEquals(m3, result.get(1)); // 20
        assertEquals(m1, result.get(2)); // 30
    }
}
