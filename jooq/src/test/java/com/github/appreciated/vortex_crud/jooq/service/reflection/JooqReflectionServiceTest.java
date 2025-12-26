package com.github.appreciated.vortex_crud.jooq.service.reflection;

import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JooqReflectionServiceTest {

    private final JooqReflectionService service = new JooqReflectionService();

    @Test
    void testGetValue() {
        Record record = mock(Record.class);
        TableField field = mock(TableField.class);

        when(record.get(field)).thenReturn("Value");

        assertEquals("Value", service.getValue(record, field));
    }

    @Test
    void testSetValue() {
        Record record = mock(Record.class);
        TableField field = mock(TableField.class);

        service.setValue(record, field, "New Value");

        verify(record).set(field, "New Value");
    }

    @Test
    void testGetId() {
        // Mocking UpdatableRecord and its key() method
        UpdatableRecord record = mock(UpdatableRecord.class);
        Record keyRecord = mock(Record.class);

        // Setup the key to return a Record
        when(record.key()).thenReturn(keyRecord);
        when(keyRecord.size()).thenReturn(1);
        when(keyRecord.get(0)).thenReturn(123);

        Object id = service.getId(record);
        assertEquals(123, id);
    }
}
