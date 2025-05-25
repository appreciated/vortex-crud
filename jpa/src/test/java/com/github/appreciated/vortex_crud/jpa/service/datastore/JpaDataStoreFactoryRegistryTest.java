package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.NumberFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
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
        Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String>> fields =
                fieldService.getFieldsForDataStore((JpaRepositoryDataStore<?>) registry.getFactory(testRepository));

        // Verify the results
        assertNotNull(fields);
        assertEquals(2, fields.size());

        // Verify field names
        assertTrue(fields.containsKey("name"));
        assertTrue(fields.containsKey("age"));

        // Verify field properties
        assertFalse(fields.get("name").isPrimary());
        assertFalse(fields.get("age").isPrimary());

        assertFalse(fields.get("name").isRequired());
        assertFalse(fields.get("age").isRequired());

        assertEquals(TextFieldFactory.class, fields.get("name").getFactory());
        assertEquals(NumberFieldFactory.class, fields.get("age").getFactory());
    }
}
