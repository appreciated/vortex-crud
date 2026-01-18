package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A route filter with a static value.
 * Unlike DynamicRouteFilter which computes the value dynamically,
 * this filter holds a constant value set at construction time.
 *
 * @param <FieldType> the type of field to filter on
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StaticRouteFilter<FieldType> implements RouteFilter<FieldType> {
    private FieldType field;
    private Object filterValue;

    @Override
    public Object value() {
        return filterValue;
    }
}
