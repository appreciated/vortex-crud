package com.github.appreciated.vortex_crud.test.jpa.ui.custom;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomDataStoreTest;

/**
 * Tests custom FileSystemDataStore integration with JPA example.
 * The JPA example includes FileDocumentDataStore which manages documents from ./documents directory.
 */
public class JpaCustomDataStoreTest extends AbstractCustomDataStoreTest {

    @Override
    protected String getPath() {
        return "documents";
    }

    @Override
    protected String getExpectedVisibleValue() {
        return "example";
    }
}
