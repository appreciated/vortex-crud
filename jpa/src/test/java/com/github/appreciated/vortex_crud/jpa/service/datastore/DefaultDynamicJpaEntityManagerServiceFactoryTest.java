package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DefaultDynamicJpaEntityManagerServiceFactoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private JpaDataStore service;

    @BeforeEach
    void setUp() {
        createTestTable();
        service = new JpaDataStore("test_table", entityManager, transactionTemplate);
    }

    @AfterEach
    void tearDown() {
        dropTestTable();
    }

    // Dynamically create a test table
    private void createTestTable() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            entityManager.createNativeQuery("CREATE TABLE test_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), age INT)")
                    .executeUpdate();
        });
    }

    // Drop the test table after each test
    private void dropTestTable() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            entityManager.createNativeQuery("DROP TABLE IF EXISTS test_table").executeUpdate();
        });
    }

    @Test
    void testInsertRecord() {
        GenericEntity values = new GenericEntity();
        values.put("name", "John Doe");
        values.put("age", 30);

        service.insertRecord(values);

        List<GenericEntity> records = service.getRecordsFromTable(0, 10);
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

        List<GenericEntity> records = service.getRecordsFromTable(0, 10);
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
        GenericEntity record = service.getRecordById(1);
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

        service.updateRecordById(1, updatedValues);

        GenericEntity updatedRecord = service.getRecordById(1);
        assertNotNull(updatedRecord);
        assertEquals("Alice Updated", updatedRecord.get("name"));
        assertEquals(30, updatedRecord.get("age"));
    }

    @Test
    void testDeleteRecordById() {
        // Insert and delete a record
        insertTestRecord("Alice", 25);
        service.deleteRecordById(1);

        GenericEntity record = service.getRecordById(1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        // Insert multiple records and delete them all
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        service.deleteAllRecords();

        List<GenericEntity> records = service.getRecordsFromTable(0, 10);
        assertTrue(records.isEmpty());
    }

    // Utility method to insert a test record
    private void insertTestRecord(String name, int age) {
        GenericEntity values = new GenericEntity();
        values.put("name", name);
        values.put("age", age);
        service.insertRecord(values);
    }

    @Test
    void testCountRecords() {
        // Initial count should be 0
        int initialCount = service.count();
        assertEquals(0, initialCount);

        // Insert records
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        // Count after inserting records
        int countAfterInsert = service.count();
        assertEquals(2, countAfterInsert);

        // Delete a record
        service.deleteRecordById(1);

        // Count after deletion
        int countAfterDelete = service.count();
        assertEquals(1, countAfterDelete);
    }
}
