package com.github.appreciated.vortex_crud.jooq.service.reflection;

import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JooqReflectionServiceTest {

    private final JooqReflectionService service = new JooqReflectionService();

    // Mocking UpdatableRecord is hard because it's complex.
    // We can try to use a real generated record if available in test scope,
    // or use Mockito to mock the behavior.

    @Test
    void testGetValue() {
        UpdatableRecordImpl record = mock(UpdatableRecordImpl.class);
        TableField field = mock(TableField.class);

        when(record.get(field)).thenReturn("Value");

        assertEquals("Value", service.getValue(record, field));
    }

    @Test
    void testSetValue() {
        UpdatableRecordImpl record = mock(UpdatableRecordImpl.class);
        TableField field = mock(TableField.class);

        service.setValue(record, field, "NewValue");

        verify(record).set(field, "NewValue");
    }

    // Testing getId is tricky with mocks because it relies on record.key() which returns a Key object.
    // Testing collection operations is also tricky as Standard JOOQ records don't have collection fields usually.
}
