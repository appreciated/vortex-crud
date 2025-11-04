package com.github.appreciated.vortex_crud.test.jpa.unit;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DoubleNumberFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableJpaRepositories
class JpaDataStoreFactoryRegistryTest {

    @Autowired
    private JpaDataStoreFactoryRegistry registry;
    @Autowired
    private JpaFieldService fieldService;
    @Autowired
    private TestRepository testRepository;

    @Test
    void testGetFieldsForDataStore() {
        // Call the method under test
        Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> fields =
                fieldService.getFieldsForDataStore((JpaRepositoryDataStore<?>) registry.getDataStore(testRepository), registry);

        // Verify the results
        assertNotNull(fields);
        assertEquals(2, fields.size());

        // Verify field names
        assertTrue(fields.containsKey("name"));
        assertTrue(fields.containsKey("age"));

        assertTrue(fields.get("name").required());
        assertTrue(fields.get("age").required());

        assertEquals(TextFieldFactory.class, (fields.get("name")).factory());
        assertEquals(DoubleNumberFieldFactory.class, (fields.get("age")).factory());
    }
}
