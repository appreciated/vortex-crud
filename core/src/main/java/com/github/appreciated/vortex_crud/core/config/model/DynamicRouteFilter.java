package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A route filter with a dynamic value provided by a SerializableSupplier.
 * The value is computed each time getValue() is called, allowing for
 * context-dependent filtering (e.g., based on the current user).
 *
 * @param <FieldType> the type of field to filter on
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DynamicRouteFilter<FieldType> implements RouteFilter<FieldType> {
    private FieldType field;
    private SerializableSupplier<Object> valueProvider;

    @Override
    public FieldType field() {
        return field;
    }

    @Override
    public Object value() {
        return valueProvider != null ? valueProvider.get() : null;
    }
}
