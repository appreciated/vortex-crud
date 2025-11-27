package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenericFilterableDataProviderTest {

    @Mock
    private VortexCrudDataStore<String, Object> dataStore;

    private GenericFilterableDataProvider<String> dataProvider;
    private final String filterField = "name";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataProvider = new GenericFilterableDataProvider<>(dataStore, filterField);
    }

    @Test
    void fetch_shouldReturnAllRecords_whenFilterIsEmpty() {
        List<Object> records = Arrays.asList(new Object(), new Object());
        when(dataStore.getRecordsFromTable(0, 10)).thenReturn(records);

        Query<Object, String> query = new Query<>(0, 10, Collections.emptyList(), null, null);
        Stream<Object> result = dataProvider.fetch(query);

        assertEquals(records.size(), result.count());
        verify(dataStore).getRecordsFromTable(0, 10);
    }

    @Test
    void fetch_shouldReturnFilteredRecords_whenFilterIsPresent() {
        String filterText = "test";
        List<Object> records = Arrays.asList(new Object());
        when(dataStore.getRecordsFromTableWhereColumnLike(filterField, filterText, 0, 10)).thenReturn(records);

        Query<Object, String> query = new Query<>(0, 10, Collections.emptyList(), null, filterText);
        Stream<Object> result = dataProvider.fetch(query);

        assertEquals(records.size(), result.count());
        verify(dataStore).getRecordsFromTableWhereColumnLike(filterField, filterText, 0, 10);
    }

    @Test
    void size_shouldReturnTotalCount_whenFilterIsEmpty() {
        when(dataStore.count()).thenReturn(5);

        Query<Object, String> query = new Query<>(0, 10, Collections.emptyList(), null, null);
        int size = dataProvider.size(query);

        assertEquals(5, size);
        verify(dataStore).count();
    }

    @Test
    void size_shouldReturnFilteredCount_whenFilterIsPresent() {
        String filterText = "test";
        when(dataStore.countWhereColumnLike(filterField, filterText)).thenReturn(3);

        Query<Object, String> query = new Query<>(0, 10, Collections.emptyList(), null, filterText);
        int size = dataProvider.size(query);

        assertEquals(3, size);
        verify(dataStore).countWhereColumnLike(filterField, filterText);
    }
}
