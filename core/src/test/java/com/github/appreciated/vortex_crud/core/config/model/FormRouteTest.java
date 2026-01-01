package com.github.appreciated.vortex_crud.core.config.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormRouteTest {

    @Test
    void testDataStoreConfigAlwaysNull() {
        FormRoute<Object, Object, Object> route = FormRoute.builder().build();
        Assertions.assertNull(route.dataStoreConfig());
    }
}
