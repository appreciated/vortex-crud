package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.jooq.models.tables.records.TestTableRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JooqDataStoreTest.class)
@JooqTest
class JooqDataStoreTest {
    @Autowired
    private DSLContext dslContext;

    private JooqDataStore<TestTableRecord> service;

    @BeforeEach
    void setUp() {
        createTestTable();
        service = new JooqDataStore<>(TestTableRecord.class, dslContext);
    }

    @AfterEach
    void tearDown() {
        dslContext.execute("DROP TABLE IF EXISTS test_table");
    }

    private void createTestTable() {
        dslContext.execute("DROP TABLE IF EXISTS test_table");
        dslContext.execute("CREATE TABLE test_table (id INTEGER PRIMARY KEY, name VARCHAR(255), age INT)");
    }

    @Test
    void testInsertRecord() {
        TestTableRecord values = new TestTableRecord();
        values.setName("John Doe");
        values.setAge(30);

        service.insertRecord(values);

        List<TestTableRecord> records = service.getRecordsFromTable(0, 10);
        assertEquals(1, records.size());

        TestTableRecord record = records.get(0);
        assertEquals("John Doe", record.get("name"));
        assertEquals(30, record.get("age"));
    }

    @Test
    void testGetRecordsFromTable() {
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        List<TestTableRecord> records = service.getRecordsFromTable(0, 10);
        assertEquals(2, records.size());

        TestTableRecord firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.get("name"));
        assertEquals(25, firstRecord.get("age"));

        TestTableRecord secondRecord = records.get(1);
        assertEquals("Bob", secondRecord.get("name"));
        assertEquals(35, secondRecord.get("age"));
    }

    @Test
    void testGetRecordById() {
        insertTestRecord("Alice", 25);
        TestTableRecord record = service.getRecordById(1);
        assertNotNull(record);
        assertEquals("Alice", record.get("name"));
        assertEquals(25, record.get("age"));
    }

    @Test
    void testUpdateRecordById() {
        insertTestRecord("Alice", 25);
        TestTableRecord updatedValues = new TestTableRecord();
        updatedValues.setId(1);  // Set the ID to identify which record to update
        updatedValues.setName("Alice Updated");
        updatedValues.setAge(30);

        service.updateRecordById(updatedValues);

        TestTableRecord updatedRecord = service.getRecordById(1);
        assertNotNull(updatedRecord);
        assertEquals("Alice Updated", updatedRecord.get("name"));
        assertEquals(30, updatedRecord.get("age"));
    }

    @Test
    void testDeleteRecordById() {
        insertTestRecord("Alice", 25);
        service.deleteRecordById(1);

        TestTableRecord record = service.getRecordById(1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        service.deleteAllRecords();

        List<TestTableRecord> records = service.getRecordsFromTable(0, 10);
        assertTrue(records.isEmpty());
    }

    private void insertTestRecord(String name, int age) {
        TestTableRecord values = new TestTableRecord();
        values.setName( name);
        values.setAge(age);
        service.insertRecord(values);
    }

    @Test
    void testCountRecords() {
        int initialCount = service.count();
        assertEquals(0, initialCount);

        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        int countAfterInsert = service.count();
        assertEquals(2, countAfterInsert);

        service.deleteRecordById(1);

        int countAfterDelete = service.count();
        assertEquals(1, countAfterDelete);
    }
}
