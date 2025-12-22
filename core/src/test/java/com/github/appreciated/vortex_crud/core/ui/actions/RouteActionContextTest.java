package com.github.appreciated.vortex_crud.core.ui.actions;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.component.Component;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RouteActionContextTest {

    @Test
    void testBuilderAndGetters() {
        VortexCrudDataStore<String, Object> dataStore = Mockito.mock(VortexCrudDataStore.class);
        Component viewComponent = Mockito.mock(Component.class);
        Runnable refreshCallback = () -> {};
        Object entity = new Object();

        RouteActionContext<String, Object> context = RouteActionContext.<String, Object>builder()
                .dataStore(dataStore)
                .selectedEntities(Collections.singletonList(entity))
                .refreshCallback(refreshCallback)
                .viewComponent(viewComponent)
                .build();

        assertEquals(dataStore, context.dataStore());
        assertEquals(1, context.selectedEntities().size());
        assertEquals(entity, context.getFirstSelectedEntity());
        assertEquals(refreshCallback, context.refreshCallback());
        assertEquals(viewComponent, context.viewComponent());
        assertEquals(1, context.getSelectionCount());
    }

    @Test
    void testAttributesMap() {
        RouteActionContext<String, Object> context = RouteActionContext.<String, Object>builder()
                .build();

        assertNotNull(context.attributes());
        assertTrue(context.attributes().isEmpty());

        context.attributes().put("key1", "value1");
        assertEquals("value1", context.getAttribute("key1"));

        context.attributes().put("key2", 123);
        assertEquals(123, context.getAttribute("key2"));
    }

    @Test
    void testFirstSelectedEntityNullWhenEmpty() {
         RouteActionContext<String, Object> context = RouteActionContext.<String, Object>builder()
                .selectedEntities(Collections.emptyList())
                .build();
         assertNull(context.getFirstSelectedEntity());
         assertEquals(0, context.getSelectionCount());
    }

    @Test
    void testFirstSelectedEntityNullWhenNullList() {
         RouteActionContext<String, Object> context = RouteActionContext.<String, Object>builder()
                .selectedEntities(null)
                .build();
         assertNull(context.getFirstSelectedEntity());
         assertEquals(0, context.getSelectionCount());
    }
}
