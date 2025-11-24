package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.TestTableRecord;
import com.github.appreciated.vortex_crud.jooq.models.tables.TestTable;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = JooqDataStoreTest.Config.class)
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
}
