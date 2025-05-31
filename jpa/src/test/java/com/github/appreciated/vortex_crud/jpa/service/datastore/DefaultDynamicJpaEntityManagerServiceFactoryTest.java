package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.jpa.service.JpaGenericEntityMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableJpaRepositories
class DefaultDynamicJpaEntityManagerServiceFactoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private JpaRepositoryDataStore<TestEntity> dataStore;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private JpaDataStoreFactoryRegistry jpaDataStoreFactoryRegistry;

    @Autowired
    private JpaGenericEntityMapper mapper;

    @BeforeEach
    void setUp() {
        createTestTable();
        dataStore = new JpaRepositoryDataStore<>(testRepository, mapper);
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

        dataStore.insertRecord(values);

        List<GenericEntity> records = dataStore.getRecordsFromTable(0, 10);
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

        List<GenericEntity> records = dataStore.getRecordsFromTable(0, 10);
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
        GenericEntity record = dataStore.getRecordById(1);
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

        dataStore.updateRecordById(1, updatedValues);

        GenericEntity updatedRecord = dataStore.getRecordById(1);
        assertNotNull(updatedRecord);
        assertEquals("Alice Updated", updatedRecord.get("name"));
        assertEquals(30, updatedRecord.get("age"));
    }

    @Test
    void testDeleteRecordById() {
        // Insert and delete a record
        insertTestRecord("Alice", 25);
        dataStore.deleteRecordById(1);

        GenericEntity record = dataStore.getRecordById(1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        // Insert multiple records and delete them all
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        dataStore.deleteAllRecords();

        List<GenericEntity> records = dataStore.getRecordsFromTable(0, 10);
        assertTrue(records.isEmpty());
    }

    // Utility method to insert a test record
    private void insertTestRecord(String name, int age) {
        GenericEntity values = new GenericEntity();
        values.put("name", name);
        values.put("age", age);
        dataStore.insertRecord(values);
    }

    @Test
    void testCountRecords() {
        // Initial count should be 0
        int initialCount = dataStore.count();
        assertEquals(0, initialCount);

        // Insert records
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        // Count after inserting records
        int countAfterInsert = dataStore.count();
        assertEquals(2, countAfterInsert);

        // Delete a record
        dataStore.deleteRecordById(1);

        // Count after deletion
        int countAfterDelete = dataStore.count();
        assertEquals(1, countAfterDelete);
    }
}
