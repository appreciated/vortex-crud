package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.fields.MarkDownField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkDownFieldFactoryTest {

    @Test
    void testMarkDownFieldDefaultFactory() {
        MarkDownField<?, ?, ?> field = MarkDownField.builder().build();
        assertEquals(MarkDownFieldFactory.class, field.factory());
    }

    @Test
    void testValidDatabaseTypes() {
        MarkDownFieldFactory<?, ?, ?> factory = new MarkDownFieldFactory<>();
        assertTrue(factory.getValidDatabaseTypesForExpectedType().contains("TEXT"));
        assertTrue(factory.getValidDatabaseTypesForExpectedType().contains("CLOB"));
    }
}
