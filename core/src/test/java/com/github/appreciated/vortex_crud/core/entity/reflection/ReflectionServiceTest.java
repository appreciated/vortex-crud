package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        when(mockResolver.getKeyForFieldId(Mockito.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
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

        when(mockResolver.getKeyForFieldId("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldId("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldId("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldId("isActive")).thenReturn("active");

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

        when(mockResolver.getKeyForFieldId("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldId("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldId("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldId("active")).thenReturn("active");

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

        when(mockResolver.getKeyForFieldId("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldId("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldId("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldId("active")).thenReturn("active");

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

        when(mockResolver.getKeyForFieldId("start_date")).thenReturn("start_date");
        when(mockResolver.getKeyForFieldId("end_date")).thenReturn("end_date");
        when(mockResolver.getKeyForFieldId("user_count")).thenReturn("user_count");
        when(mockResolver.getKeyForFieldId("is_completed")).thenReturn("is_completed");

        assertEquals("2023-01-01", reflectionService.getString(entity, "start_date"));
        assertEquals("2023-12-31", reflectionService.getString(entity, "end_date"));
        assertEquals("100", reflectionService.getString(entity, "user_count"));
        assertEquals("true", reflectionService.getString(entity, "is_completed"));
    }
}
