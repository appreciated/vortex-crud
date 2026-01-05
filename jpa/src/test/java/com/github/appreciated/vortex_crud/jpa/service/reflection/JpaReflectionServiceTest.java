package com.github.appreciated.vortex_crud.jpa.service.reflection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JpaReflectionServiceTest {

    private final JpaReflectionService service = new JpaReflectionService();

    @Test
    void testGetValue() {
        TestEntity entity = new TestEntity();
        entity.setName("Test");
        assertEquals("Test", service.getValue(entity, "name"));
    }

    @Test
    void testSetValue() {
        TestEntity entity = new TestEntity();
        service.setValue(entity, "name", "Updated");
        assertEquals("Updated", entity.getName());
    }

    @Test
    void testGetId() {
        TestEntity entity = new TestEntity();
        entity.setId("123");
        assertEquals("123", service.getId(entity));
    }

    @Test
    void testCollectionOperations() {
        TestEntity entity = new TestEntity();

        // AddAll with null list (should initialize)
        assertTrue(service.addAll(entity, "items", Arrays.asList("A", "B")));
        assertEquals(2, entity.getItems().size());
        assertTrue(entity.getItems().contains("A"));

        // RemoveAll
        assertTrue(service.removeAll(entity, "items", List.of("A")));
        assertEquals(1, entity.getItems().size());
        assertFalse(entity.getItems().contains("A"));
    }

    @Test
    void testGetCollectionType() {
        TestEntity entity = new TestEntity();
        assertEquals(String.class, service.getCollectionType(entity, "items"));
    }
}
