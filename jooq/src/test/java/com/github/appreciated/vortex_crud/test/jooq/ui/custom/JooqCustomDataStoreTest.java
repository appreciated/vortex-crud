package com.github.appreciated.vortex_crud.test.jooq.ui.custom;

import com.github.appreciated.vortex_crud.ui_test_base.tests.AbstractCustomDataStoreTest;

/**
 * Tests custom FileSystemDataStore integration with JOOQ example.
 * The JOOQ example can include FileDocumentDataStore for document management.
 */
public class JooqCustomDataStoreTest extends AbstractCustomDataStoreTest {

    @Override
    protected String getPath() {
        return "documents";
    }

    @Override
    protected String getExpectedVisibleValue() {
        // This should match content from one of the document files
        return "README";
    }
}
