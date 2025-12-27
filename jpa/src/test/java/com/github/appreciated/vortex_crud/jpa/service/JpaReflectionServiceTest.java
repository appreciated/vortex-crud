package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JpaReflectionServiceTest {

    private JpaReflectionService reflectionService;
    private JpaDataStoreFieldNameResolver mockResolver;

    @BeforeEach
    void setUp() {
        mockResolver = Mockito.mock(JpaDataStoreFieldNameResolver.class);
        reflectionService = new JpaReflectionService(mockResolver);

        // Setup mock resolver to return the field name as is
        when(mockResolver.getKeyForFieldType(Mockito.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // Test class with standard getters/setters
    static class StandardEntity {
        private String firstName;
        private int age;
        private boolean active;
        private Long id;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Test
    void testGetValue() {
        StandardEntity entity = new StandardEntity();
        entity.setFirstName("John");
        entity.setAge(30);
        entity.setActive(true);
        entity.setId(1L);

        assertEquals("John", reflectionService.getValue(entity, "firstName"));
        assertEquals(30, reflectionService.getValue(entity, "age"));
        assertEquals(true, reflectionService.getValue(entity, "active"));
        assertEquals(1L, reflectionService.getValue(entity, "id"));
    }

    @Test
    void testGetString() {
        StandardEntity entity = new StandardEntity();
        entity.setFirstName("John");
        entity.setAge(30);

        assertEquals("John", reflectionService.getString(entity, "firstName"));
        assertEquals("30", reflectionService.getString(entity, "age"));
    }

    @Test
    void testSetValue() {
        StandardEntity entity = new StandardEntity();

        reflectionService.setValue(entity, "firstName", "Jane");
        reflectionService.setValue(entity, "age", 25);
        reflectionService.setValue(entity, "active", false);

        assertEquals("Jane", entity.getFirstName());
        assertEquals(25, entity.getAge());
        assertEquals(false, entity.isActive());
    }

    @Test
    void testGetId() {
        StandardEntity entity = new StandardEntity();
        entity.setId(123L);
        assertEquals(123L, reflectionService.getId(entity));
    }

    static class CollectionEntity {
        private List<String> items;
        private Set<Integer> numbers;

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public Set<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(Set<Integer> numbers) {
            this.numbers = numbers;
        }
    }

    @Test
    void testAddAll() {
        CollectionEntity entity = new CollectionEntity();
        entity.setItems(new ArrayList<>());

        List<String> toAdd = Arrays.asList("A", "B");
        reflectionService.addAll(entity, "items", toAdd);

        assertEquals(2, entity.getItems().size());
        assertTrue(entity.getItems().contains("A"));
    }

    @Test
    void testAddAllToNullCollection() {
        CollectionEntity entity = new CollectionEntity();
        // items is null

        List<String> toAdd = Arrays.asList("A", "B");
        reflectionService.addAll(entity, "items", toAdd);

        assertNotNull(entity.getItems());
        assertEquals(2, entity.getItems().size());
        assertTrue(entity.getItems().contains("A"));
    }

    @Test
    void testRemoveAll() {
        CollectionEntity entity = new CollectionEntity();
        entity.setItems(new ArrayList<>(Arrays.asList("A", "B", "C")));

        List<String> toRemove = Arrays.asList("A", "C");
        reflectionService.removeAll(entity, "items", toRemove);

        assertEquals(1, entity.getItems().size());
        assertTrue(entity.getItems().contains("B"));
    }

    @Test
    void testGetCollectionType() {
        CollectionEntity entity = new CollectionEntity();
        assertEquals(String.class, reflectionService.getCollectionType(entity, "items"));
        assertEquals(Integer.class, reflectionService.getCollectionType(entity, "numbers"));
    }
}
