package com.github.appreciated.vortex_crud.test.jooq.unit;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.tables.TestTable;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.TestTableRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = JooqDataStoreTest.Config.class)
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite::memory:")
@Transactional
class JooqDataStoreTest {

    @SpringBootApplication
    static class Config {
        @Bean
        public VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configurationProvider() {
            return () -> mock(Application.class);
        }
    }

    @Autowired
    private DSLContext dslContext;

    private JooqDataStore<TestTableRecord> dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new JooqDataStore<>(TestTableRecord.class, dslContext, new DataStoreHooks<>());
        // Create table for in-memory DB
        dslContext.execute("CREATE TABLE IF NOT EXISTS test_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, age INTEGER NOT NULL)");
        // Clear table
        dslContext.deleteFrom(TestTable.TEST_TABLE).execute();
    }

    @Test
    void testInsertAndGetById() {
        TestTableRecord record = dataStore.newInstance();
        record.setName("John");
        record.setAge(30);

        Object id = dataStore.insertRecord(record);
        assertNotNull(id);

        TestTableRecord fetched = dataStore.getRecordById(id);
        assertNotNull(fetched);
        assertEquals("John", fetched.getName());
        assertEquals(30, fetched.getAge());
    }

    @Test
    void testGetRecordsFromTable() {
        TestTableRecord record1 = dataStore.newInstance();
        record1.setName("Alice");
        record1.setAge(25);
        dataStore.insertRecord(record1);

        TestTableRecord record2 = dataStore.newInstance();
        record2.setName("Bob");
        record2.setAge(35);
        dataStore.insertRecord(record2);

        List<TestTableRecord> records = dataStore.getRecordsFromTable(0, 10);
        assertEquals(2, records.size());
    }

    @Test
    void testUpdateRecordById() {
        TestTableRecord record = dataStore.newInstance();
        record.setName("Charlie");
        record.setAge(40);
        Object id = dataStore.insertRecord(record);

        TestTableRecord update = dataStore.getRecordById(id);
        update.setAge(41);
        dataStore.updateRecordById(update);

        TestTableRecord fetched = dataStore.getRecordById(id);
        assertEquals(41, fetched.getAge());
    }

    @Test
    void testDeleteRecordById() {
        TestTableRecord record = dataStore.newInstance();
        record.setName("Dave");
        record.setAge(50);
        Object id = dataStore.insertRecord(record);

        dataStore.deleteRecordById(id);

        TestTableRecord fetched = dataStore.getRecordById(id);
        assertNull(fetched);
    }

    @Test
    void testHooks() {
        AtomicBoolean beforeCreate = new AtomicBoolean(false);
        AtomicBoolean afterCreate = new AtomicBoolean(false);
        AtomicBoolean beforeUpdate = new AtomicBoolean(false);
        AtomicBoolean afterUpdate = new AtomicBoolean(false);
        AtomicBoolean beforeDelete = new AtomicBoolean(false);
        AtomicBoolean afterDelete = new AtomicBoolean(false);

        DataStoreHooks<TestTableRecord> hooks = DataStoreHooks.<TestTableRecord>builder()
                .beforeCreate(r -> beforeCreate.set(true))
                .afterCreate(r -> afterCreate.set(true))
                .beforeUpdate(r -> beforeUpdate.set(true))
                .afterUpdate(r -> afterUpdate.set(true))
                .beforeDelete(r -> beforeDelete.set(true))
                .afterDelete(r -> afterDelete.set(true))
                .build();

        JooqDataStore<TestTableRecord> hookedStore = new JooqDataStore<>(TestTableRecord.class, dslContext, hooks);

        // Test Create
        TestTableRecord record = hookedStore.newInstance();
        record.setName("HookTest");
        record.setAge(10);
        Object id = hookedStore.insertRecord(record);

        assertTrue(beforeCreate.get(), "beforeCreate hook not called");
        assertTrue(afterCreate.get(), "afterCreate hook not called");

        // Test Update
        TestTableRecord update = hookedStore.getRecordById(id);
        update.setAge(11);
        hookedStore.updateRecordById(update);

        assertTrue(beforeUpdate.get(), "beforeUpdate hook not called");
        assertTrue(afterUpdate.get(), "afterUpdate hook not called");

        // Test Delete
        hookedStore.deleteRecordById(id);

        assertTrue(beforeDelete.get(), "beforeDelete hook not called");
        assertTrue(afterDelete.get(), "afterDelete hook not called");
    }
}
