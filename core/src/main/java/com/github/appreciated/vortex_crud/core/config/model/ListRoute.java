package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.items.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.items.defaults.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified builder for list routes.
 * Usage: ListRoute.of()
 *     .withDataStore(dataStore)
 *     .withTitleField(field)
 *     .withFilterField(field)
 *     .withChildren(...)
 *     .build()
 */
public class ListRoute<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    public ListRoute() {
        super(ListRouteFactory.class);
    }

    public static <ModelClass, FieldType, RepositoryType> ListBuilder<ModelClass, FieldType, RepositoryType> of() {
        return new ListBuilder<>(new ListRoute<>());
    }

    public static class ListBuilder<ModelClass, FieldType, RepositoryType> extends RouteRenderer.Builder<ModelClass, FieldType, RepositoryType> {

        public ListBuilder(ListRoute<ModelClass, FieldType, RepositoryType> product) {
            super(product);
        }

        /**
         * Set the data store for this route
         */
        @Override
        public ListBuilder<ModelClass, FieldType, RepositoryType> withDataStore(RepositoryType dataStore) {
            super.withDataStore(dataStore);
            return this;
        }

        /**
         * Set the title for this route
         */
        @Override
        public ListBuilder<ModelClass, FieldType, RepositoryType> withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        /**
         * Set the title field for items (uses default CardFactory)
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            return withTitleField(titleField, CardFactory.class);
        }

        /**
         * Set the title field and item factory
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField, Class<? extends VortexCrudItemFactory> factory) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(factory);
            config.setTitleField(titleField);
            return this;
        }

        /**
         * Set the description field for items
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setDescriptionField(descriptionField);
            return this;
        }

        /**
         * Set the image field for items
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setImageField(imageField);
            return this;
        }

        /**
         * Set the filter field for search/filtering
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setFilterField(filterField);
            return this;
        }

        /**
         * Enable inline editing
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withInlineEdit(boolean inlineEdit) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setInlineEdit(inlineEdit);
            return this;
        }

        /**
         * Set list fields (fields and collections)
         */
        @SafeVarargs
        public final ListBuilder<ModelClass, FieldType, RepositoryType> withChildren(InternalFormElement<ModelClass, FieldType, RepositoryType>... children) {
            return withChildren(Arrays.asList(children));
        }

        /**
         * Set list fields (fields and collections)
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setChildren(children);
            return this;
        }

        /**
         * Add a single field to the list
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> addChild(InternalFormElement<ModelClass, FieldType, RepositoryType> child) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            if (config.getChildren() == null) {
                config.setChildren(new ArrayList<>());
            }
            config.getChildren().add(child);
            return this;
        }

        /**
         * Set the item factory for rendering
         */
        public ListBuilder<ModelClass, FieldType, RepositoryType> withItemFactory(Class<? extends VortexCrudItemFactory> factory) {
            getOrCreateConfig(factory);
            return this;
        }

        /**
         * Helper to get or create the configuration
         */
        private GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> getOrCreateConfig(Class<? extends VortexCrudItemFactory> factory) {
            if (product.getConfiguration() == null) {
                GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config =
                    new GridOrListRendererConfiguration<>(factory);
                product.setConfiguration(config);
            }
            return (GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType>) product.getConfiguration();
        }

        @Override
        public RouteRenderer<ModelClass, FieldType, RepositoryType> build() {
            return super.build();
        }
    }
}