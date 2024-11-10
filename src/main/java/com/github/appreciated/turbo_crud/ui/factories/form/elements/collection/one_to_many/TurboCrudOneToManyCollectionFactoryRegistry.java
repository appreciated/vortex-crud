package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.one_to_many;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudOneToManyCollectionFactoryRegistry {

    TurboCrudOneToManyCollectionFactory getFactory(String factory);

    void addFactory(String key, TurboCrudOneToManyCollectionFactory factory);

}