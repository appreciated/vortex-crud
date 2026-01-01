package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EntityComboBoxWrapperTest {

    @Test
    public void testNullChildrenWithSetValue() {
        VortexCrudDataStoreFieldNameResolver resolver = mock(VortexCrudDataStoreFieldNameResolver.class);
        ReferenceField field = mock(ReferenceField.class);
        ReflectionService reflectionService = mock(ReflectionService.class);
        VortexCrudDataStoreUtilStrategy dataStoreUtil = mock(VortexCrudDataStoreUtilStrategy.class);
        VortexCrudDataStore dataStore = mock(VortexCrudDataStore.class);

        doReturn(dataStore).when(field).dataStore();
        when(field.children()).thenReturn(null);
        when(field.filterField()).thenReturn("filterField");

        EntityComboBoxWrapper wrapper = new EntityComboBoxWrapper(resolver, field, reflectionService, dataStoreUtil);

        when(dataStore.getRecordById(1)).thenReturn(new Object());
        when(dataStoreUtil.getId(org.mockito.ArgumentMatchers.any())).thenReturn("1");

        assertDoesNotThrow(() -> wrapper.setValue(1));
    }
}
