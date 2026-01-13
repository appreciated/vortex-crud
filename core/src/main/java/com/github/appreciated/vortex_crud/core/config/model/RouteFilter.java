package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Interface for filtering routes based on field values.
 * Implementations can provide static or dynamic filter values.
 *
 * @param <FieldType> the type of field to filter on
 */
public interface RouteFilter<FieldType> {
    /**
     * Gets the field to filter on.
     * @return the field
     */
    FieldType field();

    /**
     * Gets the filter value to use for comparison.
     * @return the filter value
     */
    Object value();
}
