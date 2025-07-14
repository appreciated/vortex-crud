package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ReflectionServiceTest {

    // Custom class to avoid ambiguous method calls
    private static class TestReflectionService extends ReflectionService<String> {
        public TestReflectionService(VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
            super(fieldNameResolver);
        }

        // Helper method to call the private getValue method directly
        public <T> Object getValueByName(T entity, String fieldName) {
            return super.getValueInternal(entity, fieldName);
        }

        // Helper method to call the private setValue method directly
        public <T> void setValueByName(T entity, String fieldName, Object value) {
            super.setValueInternal(entity, fieldName, value);
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

    // Test class with snake case fields
    static class SnakeCaseEntity {
        private String first_name;
        private String last_name;
        private int user_age;
        private boolean is_active;

        public SnakeCaseEntity() {
        }

        public SnakeCaseEntity(String first_name, String last_name, int user_age, boolean is_active) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.user_age = user_age;
            this.is_active = is_active;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public int getUser_age() {
            return user_age;
        }

        public void setUser_age(int user_age) {
            this.user_age = user_age;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }
    }

    @Test
    void testGetValueInternalWithCamelCase() {
        CamelCaseEntity entity = new CamelCaseEntity("John", "Doe", 30, true);

        assertEquals("John", reflectionService.getValueByName(entity, "firstName"));
        assertEquals("Doe", reflectionService.getValueByName(entity, "lastName"));
        assertEquals(30, reflectionService.getValueByName(entity, "userAge"));
        assertEquals(true, reflectionService.getValueByName(entity, "active"));
    }

    @Test
    void testSetValueInternalWithCamelCase() {
        CamelCaseEntity entity = new CamelCaseEntity();

        reflectionService.setValueByName(entity, "firstName", "Jane");
        reflectionService.setValueByName(entity, "lastName", "Smith");
        reflectionService.setValueByName(entity, "userAge", 25);
        reflectionService.setValueByName(entity, "active", false);

        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertEquals(25, entity.getUserAge());
        assertEquals(false, entity.isActive());
    }

    @Test
    void testGetValueInternalWithSnakeCase() {
        SnakeCaseEntity entity = new SnakeCaseEntity("John", "Doe", 30, true);

        assertEquals("John", reflectionService.getValueByName(entity, "first_name"));
        assertEquals("Doe", reflectionService.getValueByName(entity, "last_name"));
        assertEquals(30, reflectionService.getValueByName(entity, "user_age"));
        assertEquals(true, reflectionService.getValueByName(entity, "is_active"));
    }

    @Test
    void testSetValueInternalWithSnakeCase() {
        SnakeCaseEntity entity = new SnakeCaseEntity();

        reflectionService.setValueByName(entity, "first_name", "Jane");
        reflectionService.setValueByName(entity, "last_name", "Smith");
        reflectionService.setValueByName(entity, "user_age", 25);
        reflectionService.setValueByName(entity, "is_active", false);

        assertEquals("Jane", entity.getFirst_name());
        assertEquals("Smith", entity.getLast_name());
        assertEquals(25, entity.getUser_age());
        assertEquals(false, entity.isIs_active());
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

    @Test
    void testGetStringWithSnakeCase() {
        SnakeCaseEntity entity = new SnakeCaseEntity("John", "Doe", 30, true);

        when(mockResolver.getKeyForFieldId("first_name")).thenReturn("first_name");
        when(mockResolver.getKeyForFieldId("last_name")).thenReturn("last_name");
        when(mockResolver.getKeyForFieldId("user_age")).thenReturn("user_age");
        when(mockResolver.getKeyForFieldId("is_active")).thenReturn("is_active");

        assertEquals("John", reflectionService.getString(entity, "first_name"));
        assertEquals("Doe", reflectionService.getString(entity, "last_name"));
        assertEquals(30, reflectionService.getValue(entity, "user_age"));
        assertEquals(true, reflectionService.getValue(entity, "is_active"));
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

        assertEquals("John", reflectionService.getValueByName(entity, "firstName"));
        assertEquals("Doe", reflectionService.getValueByName(entity, "lastName"));
        assertEquals(30, reflectionService.getValueByName(entity, "userAge"));
        assertEquals(true, reflectionService.getValueByName(entity, "active"));
    }

    @Test
    void testSetValueInternalWithGetterSetterOnly() {
        GetterSetterOnlyEntity entity = new GetterSetterOnlyEntity();

        when(mockResolver.getKeyForFieldId("firstName")).thenReturn("firstName");
        when(mockResolver.getKeyForFieldId("lastName")).thenReturn("lastName");
        when(mockResolver.getKeyForFieldId("userAge")).thenReturn("userAge");
        when(mockResolver.getKeyForFieldId("active")).thenReturn("active");

        reflectionService.setValueByName(entity, "firstName", "Jane");
        reflectionService.setValueByName(entity, "lastName", "Smith");
        reflectionService.setValueByName(entity, "userAge", 25);
        reflectionService.setValueByName(entity, "active", false);

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

}
