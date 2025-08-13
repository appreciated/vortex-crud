package com.github.appreciated.vortex_crud.uitest.config;

/**
 * Interface for implementation-specific test configurations.
 * Each implementation (JPA, JOOQ, etc.) should provide a configuration class
 * that implements this interface to set up the necessary beans and resources
 * for testing.
 */
public interface VortexCrudTestConfiguration {

    /**
     * Returns the name of the implementation (e.g., "jpa", "jooq").
     * This can be used for logging or to determine implementation-specific behavior.
     *
     * @return the implementation name
     */
    String getImplementationName();

    /**
     * Returns the path to the SQL script for field validation tests.
     *
     * @return the path to the field validation SQL script
     */
    String getFieldValidationScriptPath();

    /**
     * Returns the path to the SQL script for form elements tests.
     *
     * @return the path to the form elements SQL script
     */
    String getFormElementsScriptPath();

    /**
     * Returns the path to the SQL script for nested forms tests.
     *
     * @return the path to the nested forms SQL script
     */
    String getNestedFormsScriptPath();

    /**
     * Returns the path to the SQL script for one-to-many relationship tests.
     *
     * @return the path to the one-to-many SQL script
     */
    String getOneToManyScriptPath();

    /**
     * Returns the path to the SQL script for many-to-many relationship tests.
     *
     * @return the path to the many-to-many SQL script
     */
    String getManyToManyScriptPath();
}