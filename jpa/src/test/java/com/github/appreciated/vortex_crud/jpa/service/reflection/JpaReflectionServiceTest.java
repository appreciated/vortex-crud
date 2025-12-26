package com.github.appreciated.vortex_crud.jpa.service.reflection;

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
        entity.setName("Test Name");
        assertEquals("Test Name", service.getValue(entity, "name"));
    }

    @Test
    void testSetValue() {
        TestEntity entity = new TestEntity();
        service.setValue(entity, "name", "New Name");
        assertEquals("New Name", entity.getName());
    }

    @Test
    void testGetId() {
        TestEntity entity = new TestEntity();
        entity.setId(123L);
        assertEquals(123L, service.getId(entity));
    }

    @Test
    void testAddAll() {
        TestEntity entity = new TestEntity();
        service.addAll(entity, "items", Arrays.asList("A", "B"));
        assertEquals(2, entity.getItems().size());
        assertTrue(entity.getItems().contains("A"));
    }

    @Test
    void testRemoveAll() {
        TestEntity entity = new TestEntity();
        entity.setItems(new ArrayList<>(Arrays.asList("A", "B", "C")));
        service.removeAll(entity, "items", Arrays.asList("A", "C"));
        assertEquals(1, entity.getItems().size());
        assertTrue(entity.getItems().contains("B"));
    }

    @Test
    void testGetCollectionType() {
        TestEntity entity = new TestEntity();
        Class<?> type = service.getCollectionType(entity, "items");
        assertEquals(String.class, type);
    }

    // Simple POJO for testing
    public static class TestEntity {
        private Long id;
        private String name;
        private List<String> items = new ArrayList<>();

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<String> getItems() { return items; }
        public void setItems(List<String> items) { this.items = items; }
    }
}
