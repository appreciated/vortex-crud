package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class EntityComboBoxWrapperTest {

    @Test
    public void testNullChildrenDoesNotThrowNPE() {
        VortexCrudDataStoreFieldNameResolver<String> resolver = mock(VortexCrudDataStoreFieldNameResolver.class);
        ReferenceField<Object, String, Object> field = mock(ReferenceField.class);
        ReflectionService<String> reflectionService = mock(ReflectionService.class);
        VortexCrudDataStoreUtilStrategy dataStoreUtil = mock(VortexCrudDataStoreUtilStrategy.class);
        VortexCrudDataStore<String, Object> dataStore = mock(VortexCrudDataStore.class);

        Mockito.doReturn(dataStore).when(field).dataStore();
        when(field.children()).thenReturn(null);
        when(field.filterField()).thenReturn("filterField");

        EntityComboBoxWrapper<Object, String, Object> wrapper = new EntityComboBoxWrapper<>(resolver, field, reflectionService, dataStoreUtil);

        // Trigger label generation by setting a value
        wrapper.setValue("someId");

        // Verify that reflectionService was called with the fallback filterField at least once
        verify(reflectionService, atLeastOnce()).getString("someId", "filterField");
    }
}
