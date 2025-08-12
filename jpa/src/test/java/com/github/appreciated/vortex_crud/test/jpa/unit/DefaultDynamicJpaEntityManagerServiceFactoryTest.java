package com.github.appreciated.vortex_crud.test.jpa.unit;

import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldTypeResolverService;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaRepositoryDataStore;
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
    private JpaFieldTypeResolverService fieldTypeResolver;

    @BeforeEach
    void setUp() {
        createTestTable();
        dataStore = new JpaRepositoryDataStore<>(testRepository, fieldTypeResolver);
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
        TestEntity values = new TestEntity();
        values.setName("John Doe");
        values.setAge(30);

        dataStore.insertRecord(values);

        List<TestEntity> records = dataStore.getRecordsFromTable(0, 10);
        assertEquals(1, records.size());

        TestEntity record = records.get(0);
        assertEquals("John Doe", record.getName());
        assertEquals(30, record.getAge());
    }

    @Test
    void testGetRecordsFromTable() {
        // Insert multiple records
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        List<TestEntity> records = dataStore.getRecordsFromTable(0, 10);
        assertEquals(2, records.size());

        TestEntity firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.getName());
        assertEquals(25, firstRecord.getAge());

        TestEntity secondRecord = records.get(1);
        assertEquals("Bob", secondRecord.getName());
        assertEquals(35, secondRecord.getAge());
    }

    @Test
    void testGetRecordById() {
        // Insert a record and retrieve it by ID
        insertTestRecord("Alice", 25);
        TestEntity record = dataStore.getRecordById(1);
        assertNotNull(record);
        assertEquals("Alice", record.getName());
        assertEquals(25, record.getAge());
    }

    @Test
    void testUpdateRecordById() {
        // Insert and update a record
        insertTestRecord("Alice", 25);
        TestEntity updatedValues = new TestEntity();
        updatedValues.setName("Alice Updated");
        updatedValues.setAge(30);

        dataStore.updateRecordById(1, updatedValues);

        TestEntity updatedRecord = dataStore.getRecordById(1);
        assertNotNull(updatedRecord);
        assertEquals("Alice Updated", updatedRecord.getName());
        assertEquals(30, updatedRecord.getAge());
    }

    @Test
    void testDeleteRecordById() {
        // Insert and delete a record
        insertTestRecord("Alice", 25);
        dataStore.deleteRecordById(1);

        TestEntity record = dataStore.getRecordById(1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        // Insert multiple records and delete them all
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        dataStore.deleteAllRecords();

        List<TestEntity> records = dataStore.getRecordsFromTable(0, 10);
        assertTrue(records.isEmpty());
    }

    // Utility method to insert a test record
    private void insertTestRecord(String name, int age) {
        TestEntity values = new TestEntity();
        values.setName(name);
        values.setAge(age);
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
