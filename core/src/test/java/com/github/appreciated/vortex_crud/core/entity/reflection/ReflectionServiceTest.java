package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReflectionServiceTest {

    // Custom class to avoid ambiguous method calls
    private static class TestReflectionService extends ReflectionService<String> {
        public TestReflectionService(VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
            super(fieldNameResolver);
        }
    }

    private TestReflectionService reflectionService;
    private VortexCrudDataStoreFieldNameResolver<String> mockResolver;

    @BeforeEach
    void setUp() {
        mockResolver = Mockito.mock(VortexCrudDataStoreFieldNameResolver.class);
        reflectionService = new TestReflectionService(mockResolver);

        // Setup mock resolver to return the field name as is
        when(mockResolver.getKeyForFieldType(Mockito.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // Test class with camel case fields
    static class CamelCaseEntity {
        private String firstName;
        private String lastName;
        private int userAge;
        private boolean isActive;

        public CamelCaseEntity() {
        }

        public CamelCaseEntity(String firstName, String lastName, int userAge, boolean isActive) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userAge = userAge;
            this.isActive = isActive;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getUserAge() {
            return userAge;
        }

        public void setUserAge(int userAge) {
            this.userAge = userAge;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }

    // Test class with snake case fields but camel case getters/setters
    static class SnakeCaseToCamelCaseEntity {
        private String start_date;
        private String end_date;
        private int user_count;
        private boolean is_completed;

        public SnakeCaseToCamelCaseEntity() {
        }

        public SnakeCaseToCamelCaseEntity(String start_date, String end_date, int user_count, boolean is_completed) {
            this.start_date = start_date;
            this.end_date = end_date;
            this.user_count = user_count;
            this.is_completed = is_completed;
        }

        public String getStartDate() {
            return start_date;
        }

        public void setStartDate(String start_date) {
            this.start_date = start_date;
        }

        public String getEndDate() {
            return end_date;
        }

        public void setEndDate(String end_date) {
            this.end_date = end_date;
        }

        public int getUserCount() {
            return user_count;
        }

        public void setUserCount(int user_count) {
            this.user_count = user_count;
        }

        public boolean isCompleted() {
            return is_completed;
        }

        public void setCompleted(boolean is_completed) {
            this.is_completed = is_completed;
        }
    }

    @Test
    void testGetValueInternalWithCamelCase() {
        CamelCaseEntity entity = new CamelCaseEntity("John", "Doe", 30, true);

        assertEquals("John", reflectionService.getValue(entity, "firstName"));
        assertEquals("Doe", reflectionService.getValue(entity, "lastName"));
        assertEquals(30, reflectionService.getValue(entity, "userAge"));
        assertEquals(true, reflectionService.getValue(entity, "active"));
    }

    @Test
    void testSetValueInternalWithCamelCase() {
        CamelCaseEntity entity = new CamelCaseEntity();

        reflectionService.setValue(entity, "firstName", "Jane");
        reflectionService.setValue(entity, "lastName", "Smith");
        reflectionService.setValue(entity, "userAge", 25);
        reflectionService.setValue(entity, "active", false);

        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertEquals(25, entity.getUserAge());
        assertEquals(false, entity.isActive());
    }

    @Test
    void testGetStringWithCamelCase() {
        CamelCaseEntity entity = new CamelCaseEntity("John", "Doe", 30, true);

        when(mockResolver.getKeyForFieldType("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldType("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldType("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldType("isActive")).thenReturn("active");

        assertEquals("John", reflectionService.getString(entity, "firstName"));
        assertEquals("Doe", reflectionService.getString(entity, "lastName"));
        assertEquals(30, reflectionService.getValue(entity, "userAge"));
        assertEquals(true, reflectionService.getValue(entity, "isActive"));
    }

    // Test class with getters/setters but different field names
    static class GetterSetterOnlyEntity {
        private String differentFirstName;
        private String differentLastName;
        private int differentAge;
        private boolean differentActive;

        public GetterSetterOnlyEntity() {
        }

        public String getFirstName() {
            return differentFirstName;
        }

        public void setFirstName(String firstName) {
            this.differentFirstName = firstName;
        }

        public String getLastName() {
            return differentLastName;
        }

        public void setLastName(String lastName) {
            this.differentLastName = lastName;
        }

        public int getUserAge() {
            return differentAge;
        }

        public void setUserAge(int age) {
            this.differentAge = age;
        }

        public boolean isActive() {
            return differentActive;
        }

        public void setActive(boolean active) {
            this.differentActive = active;
        }
    }

    @Test
    void testGetValueInternalWithGetterSetterOnly() {
        GetterSetterOnlyEntity entity = new GetterSetterOnlyEntity();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setUserAge(30);
        entity.setActive(true);

        assertEquals("John", reflectionService.getValue(entity, "firstName"));
        assertEquals("Doe", reflectionService.getValue(entity, "lastName"));
        assertEquals(30, reflectionService.getValue(entity, "userAge"));
        assertEquals(true, reflectionService.getValue(entity, "active"));
    }

    @Test
    void testSetValueInternalWithGetterSetterOnly() {
        GetterSetterOnlyEntity entity = new GetterSetterOnlyEntity();

        when(mockResolver.getKeyForFieldType("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldType("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldType("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldType("active")).thenReturn("active");

        reflectionService.setValue(entity, "firstName", "Jane");
        reflectionService.setValue(entity, "lastName", "Smith");
        reflectionService.setValue(entity, "userAge", 25);
        reflectionService.setValue(entity, "active", false);

        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertEquals(25, entity.getUserAge());
        assertEquals(false, entity.isActive());
    }

    @Test
    void testGetStringWithGetterSetterOnly() {
        GetterSetterOnlyEntity entity = new GetterSetterOnlyEntity();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setUserAge(30);
        entity.setActive(true);

        when(mockResolver.getKeyForFieldType("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldType("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldType("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldType("active")).thenReturn("active");

        assertEquals("John", reflectionService.getString(entity, "firstName"));
        assertEquals("Doe", reflectionService.getString(entity, "lastName"));
        assertEquals("30", reflectionService.getString(entity, "userAge"));
        assertEquals("true", reflectionService.getString(entity, "active"));
    }

    @Test
    void testGetValueWithSnakeCaseToCamelCase() {
        SnakeCaseToCamelCaseEntity entity = new SnakeCaseToCamelCaseEntity("2023-01-01", "2023-12-31", 100, true);

        assertEquals("2023-01-01", reflectionService.getValue(entity, "start_date"));
        assertEquals("2023-12-31", reflectionService.getValue(entity, "end_date"));
        assertEquals(100, reflectionService.getValue(entity, "user_count"));
        assertEquals(true, reflectionService.getValue(entity, "is_completed"));
    }

    @Test
    void testSetValueWithSnakeCaseToCamelCase() {
        SnakeCaseToCamelCaseEntity entity = new SnakeCaseToCamelCaseEntity();

        reflectionService.setValue(entity, "start_date", "2024-01-01");
        reflectionService.setValue(entity, "end_date", "2024-12-31");
        reflectionService.setValue(entity, "user_count", 200);
        reflectionService.setValue(entity, "is_completed", false);

        assertEquals("2024-01-01", entity.getStartDate());
        assertEquals("2024-12-31", entity.getEndDate());
        assertEquals(200, entity.getUserCount());
        assertFalse(entity.isCompleted());
    }

    @Test
    void testGetStringWithSnakeCaseToCamelCase() {
        SnakeCaseToCamelCaseEntity entity = new SnakeCaseToCamelCaseEntity("2023-01-01", "2023-12-31", 100, true);

        when(mockResolver.getKeyForFieldType("start_date")).thenReturn("start_date");
        when(mockResolver.getKeyForFieldType("end_date")).thenReturn("end_date");
        when(mockResolver.getKeyForFieldType("user_count")).thenReturn("user_count");
        when(mockResolver.getKeyForFieldType("is_completed")).thenReturn("is_completed");

        assertEquals("2023-01-01", reflectionService.getString(entity, "start_date"));
        assertEquals("2023-12-31", reflectionService.getString(entity, "end_date"));
        assertEquals("100", reflectionService.getString(entity, "user_count"));
        assertEquals("true", reflectionService.getString(entity, "is_completed"));
    }

    // Test class with collection fields
    static class CollectionEntity {
        private List<String> stringList;
        private Set<Integer> integerSet;
        private Collection<Double> doubleCollection;

        public CollectionEntity() {
            this.stringList = new ArrayList<>();
            this.integerSet = new HashSet<>();
            this.doubleCollection = new ArrayList<>();
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public Set<Integer> getIntegerSet() {
            return integerSet;
        }

        public void setIntegerSet(Set<Integer> integerSet) {
            this.integerSet = integerSet;
        }

        public Collection<Double> getDoubleCollection() {
            return doubleCollection;
        }

        public void setDoubleCollection(Collection<Double> doubleCollection) {
            this.doubleCollection = doubleCollection;
        }
    }

    @Test
    void testAddAll() {
        CollectionEntity entity = new CollectionEntity();

        when(mockResolver.getKeyForFieldType("stringList")).thenReturn("stringList");
        when(mockResolver.getKeyForFieldType("integerSet")).thenReturn("integerSet");
        when(mockResolver.getKeyForFieldType("doubleCollection")).thenReturn("doubleCollection");

        // Test adding to List
        List<String> stringsToAdd = Arrays.asList("one", "two", "three");
        boolean listResult = reflectionService.addAll(entity, "stringList", stringsToAdd);

        assertTrue(listResult);
        assertEquals(3, entity.getStringList().size());
        assertTrue(entity.getStringList().contains("one"));
        assertTrue(entity.getStringList().contains("two"));
        assertTrue(entity.getStringList().contains("three"));

        // Test adding to Set
        Set<Integer> integersToAdd = new HashSet<>(Arrays.asList(1, 2, 3));
        boolean setResult = reflectionService.addAll(entity, "integerSet", integersToAdd);

        assertTrue(setResult);
        assertEquals(3, entity.getIntegerSet().size());
        assertTrue(entity.getIntegerSet().contains(1));
        assertTrue(entity.getIntegerSet().contains(2));
        assertTrue(entity.getIntegerSet().contains(3));

        // Test adding to Collection
        List<Double> doublesToAdd = Arrays.asList(1.1, 2.2, 3.3);
        boolean collectionResult = reflectionService.addAll(entity, "doubleCollection", doublesToAdd);

        assertTrue(collectionResult);
        assertEquals(3, entity.getDoubleCollection().size());
        assertTrue(entity.getDoubleCollection().contains(1.1));
        assertTrue(entity.getDoubleCollection().contains(2.2));
        assertTrue(entity.getDoubleCollection().contains(3.3));

        // Test adding empty collection
        boolean emptyResult = reflectionService.addAll(entity, "stringList", new ArrayList<>());
        assertFalse(emptyResult);

        // Test adding to null entity
        boolean nullEntityResult = reflectionService.addAll(null, "stringList", stringsToAdd);
        assertFalse(nullEntityResult);

        // Test adding null collection
        boolean nullCollectionResult = reflectionService.addAll(entity, "stringList", null);
        assertFalse(nullCollectionResult);
    }

    @Test
    void testRemoveAll() {
        CollectionEntity entity = new CollectionEntity();

        when(mockResolver.getKeyForFieldType("stringList")).thenReturn("stringList");
        when(mockResolver.getKeyForFieldType("integerSet")).thenReturn("integerSet");
        when(mockResolver.getKeyForFieldType("doubleCollection")).thenReturn("doubleCollection");

        // Setup initial collections
        entity.getStringList().addAll(Arrays.asList("one", "two", "three", "four"));
        entity.getIntegerSet().addAll(Arrays.asList(1, 2, 3, 4));
        entity.getDoubleCollection().addAll(Arrays.asList(1.1, 2.2, 3.3, 4.4));

        // Test removing from List
        List<String> stringsToRemove = Arrays.asList("one", "three");
        boolean listResult = reflectionService.removeAll(entity, "stringList", stringsToRemove);

        assertTrue(listResult);
        assertEquals(2, entity.getStringList().size());
        assertFalse(entity.getStringList().contains("one"));
        assertTrue(entity.getStringList().contains("two"));
        assertFalse(entity.getStringList().contains("three"));
        assertTrue(entity.getStringList().contains("four"));

        // Test removing from Set
        Set<Integer> integersToRemove = new HashSet<>(Arrays.asList(1, 3));
        boolean setResult = reflectionService.removeAll(entity, "integerSet", integersToRemove);

        assertTrue(setResult);
        assertEquals(2, entity.getIntegerSet().size());
        assertFalse(entity.getIntegerSet().contains(1));
        assertTrue(entity.getIntegerSet().contains(2));
        assertFalse(entity.getIntegerSet().contains(3));
        assertTrue(entity.getIntegerSet().contains(4));

        // Test removing from Collection
        List<Double> doublesToRemove = Arrays.asList(1.1, 3.3);
        boolean collectionResult = reflectionService.removeAll(entity, "doubleCollection", doublesToRemove);

        assertTrue(collectionResult);
        assertEquals(2, entity.getDoubleCollection().size());
        assertFalse(entity.getDoubleCollection().contains(1.1));
        assertTrue(entity.getDoubleCollection().contains(2.2));
        assertFalse(entity.getDoubleCollection().contains(3.3));
        assertTrue(entity.getDoubleCollection().contains(4.4));

        // Test removing empty collection
        boolean emptyResult = reflectionService.removeAll(entity, "stringList", new ArrayList<>());
        assertFalse(emptyResult);

        // Test removing from null entity
        boolean nullEntityResult = reflectionService.removeAll(null, "stringList", stringsToRemove);
        assertFalse(nullEntityResult);

        // Test removing null collection
        boolean nullCollectionResult = reflectionService.removeAll(entity, "stringList", null);
        assertFalse(nullCollectionResult);
    }

    @Test
    void testGetCollectionType() {
        CollectionEntity entity = new CollectionEntity();

        when(mockResolver.getKeyForFieldType("stringList")).thenReturn("stringList");
        when(mockResolver.getKeyForFieldType("integerSet")).thenReturn("integerSet");
        when(mockResolver.getKeyForFieldType("doubleCollection")).thenReturn("doubleCollection");

        // Test getting collection types
        Class<?> stringListType = reflectionService.getCollectionType(entity, "stringList");
        Class<?> integerSetType = reflectionService.getCollectionType(entity, "integerSet");
        Class<?> doubleCollectionType = reflectionService.getCollectionType(entity, "doubleCollection");

        assertEquals(String.class, stringListType);
        assertEquals(Integer.class, integerSetType);
        assertEquals(Double.class, doubleCollectionType);

        // Test with null entity
        Class<?> nullEntityType = reflectionService.getCollectionType(null, "stringList");
        assertNull(nullEntityType);
    }
}
