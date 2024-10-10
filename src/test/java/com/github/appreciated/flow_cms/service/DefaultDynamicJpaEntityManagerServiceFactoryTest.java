package com.github.appreciated.flow_cms.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(DefaultJpaEntityManagerService.class)
@Transactional
class DefaultDynamicJpaEntityManagerServiceFactoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DefaultJpaEntityManagerService service;

    @BeforeEach
    void setUp() {
        createTestTable();
    }

    @AfterEach
    void tearDown() {
        dropTestTable();
    }

    // Dynamically create a test table
    private void createTestTable() {
        entityManager.createNativeQuery("CREATE TABLE test_table (id SERIAL PRIMARY KEY, name VARCHAR(255), age INT)")
                .executeUpdate();
    }

    // Drop the test table after each test
    private void dropTestTable() {
        entityManager.createNativeQuery("DROP TABLE IF EXISTS test_table").executeUpdate();
    }

    @Test
    void testInsertRecord() {
        GenericEntity values = new GenericEntity();
        values.put("name", "John Doe");
        values.put("age", 30);

        service.insertRecord("test_table", values);
        entityManager.flush();  // Force the persistence context to synchronize with the database

        List<GenericEntity> records = service.getRecordsFromTable("test_table", 0, 10);
        assertEquals(1, records.size());

        GenericEntity record = records.get(0);
        assertEquals("John Doe", record.get("name"));
        assertEquals(30, record.get("age"));
    }

    @Test
    void testGetRecordsFromTable() {
        // Insert multiple records
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        List<GenericEntity> records = service.getRecordsFromTable("test_table", 0, 10);
        assertEquals(2, records.size());

        GenericEntity firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.get("name"));
        assertEquals(25, firstRecord.get("age"));

        GenericEntity secondRecord = records.get(1);
        assertEquals("Bob", secondRecord.get("name"));
        assertEquals(35, secondRecord.get("age"));
    }

    @Test
    void testGetRecordById() {
        // Insert a record and retrieve it by ID
        insertTestRecord("Alice", 25);
        GenericEntity record = service.getRecordById("test_table", 1);
        assertNotNull(record);
        assertEquals("Alice", record.get("name"));
        assertEquals(25, record.get("age"));
    }

    @Test
    void testUpdateRecordById() {
        // Insert and update a record
        insertTestRecord("Alice", 25);
        GenericEntity updatedValues = new GenericEntity();
        updatedValues.put("name", "Alice Updated");
        updatedValues.put("age", 30);

        service.updateRecordById("test_table", 1, updatedValues);

        GenericEntity updatedRecord = service.getRecordById("test_table", 1);
        assertNotNull(updatedRecord);
        assertEquals("Alice Updated", updatedRecord.get("name"));
        assertEquals(30, updatedRecord.get("age"));
    }

    @Test
    void testDeleteRecordById() {
        // Insert and delete a record
        insertTestRecord("Alice", 25);
        service.deleteRecordById("test_table", 1);

        GenericEntity record = service.getRecordById("test_table", 1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        // Insert multiple records and delete them all
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        service.deleteAllRecords("test_table");

        List<GenericEntity> records = service.getRecordsFromTable("test_table", 0, 10);
        assertTrue(records.isEmpty());
    }

    // Utility method to insert a test record
    private void insertTestRecord(String name, int age) {
        GenericEntity values = new GenericEntity();
        values.put("name", name);
        values.put("age", age);
        service.insertRecord("test_table", values);
    }

    @Test
    void testCountRecords() {
        // Initial count should be 0
        int initialCount = service.count("test_table");
        assertEquals(0, initialCount);

        // Insert records
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        // Count after inserting records
        int countAfterInsert = service.count("test_table");
        assertEquals(2, countAfterInsert);

        // Delete a record
        service.deleteRecordById("test_table", 1);

        // Count after deletion
        int countAfterDelete = service.count("test_table");
        assertEquals(1, countAfterDelete);
    }
}
