package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
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

    private JooqDataStore service;

    @BeforeEach
    void setUp() {
        createTestTable();
        service = new JooqDataStore(TestTableRecord.class, dslContext);
    }

    @AfterEach
    void tearDown() {
        dslContext.execute("DROP TABLE IF EXISTS test_table");
    }

    private void createTestTable() {
        dslContext.execute("CREATE TABLE test_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), age INT)");
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
    void testGetModelClassById() {
        insertTestRecord("Alice", 25);
        GenericEntity record = service.getRecordById(1);
        assertNotNull(record);
        assertEquals("Alice", record.get("name"));
        assertEquals(25, record.get("age"));
    }

    @Test
    void testUpdateRecordById() {
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
        insertTestRecord("Alice", 25);
        service.deleteRecordById(1);

        GenericEntity record = service.getRecordById(1);
        assertNull(record);
    }

    @Test
    void testDeleteAllRecords() {
        insertTestRecord("Alice", 25);
        insertTestRecord("Bob", 35);

        service.deleteAllRecords();

        List<GenericEntity> records = service.getRecordsFromTable(0, 10);
        assertTrue(records.isEmpty());
    }

    private void insertTestRecord(String name, int age) {
        GenericEntity values = new GenericEntity();
        values.put("name", name);
        values.put("age", age);
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