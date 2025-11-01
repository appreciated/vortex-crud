package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.items.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.items.defaults.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified builder for grid routes.
 * Usage: GridRoute.of()
 *     .withDataStore(dataStore)
 *     .withTitleField(field)
 *     .withFilterField(field)
 *     .withChildren(...)
 *     .build()
 */
public class GridRoute<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    public GridRoute() {
        super(GridRouteFactory.class);
    }

    public static <ModelClass, FieldType, RepositoryType> GridBuilder<ModelClass, FieldType, RepositoryType> of() {
        return new GridBuilder<>(new GridRoute<>());
    }

    public static class GridBuilder<ModelClass, FieldType, RepositoryType> extends RouteRenderer.Builder<ModelClass, FieldType, RepositoryType> {

        public GridBuilder(GridRoute<ModelClass, FieldType, RepositoryType> product) {
            super(product);
        }

        /**
         * Set the data store for this route
         */
        @Override
        public GridBuilder<ModelClass, FieldType, RepositoryType> withDataStore(RepositoryType dataStore) {
            super.withDataStore(dataStore);
            return this;
        }

        /**
         * Set the title for this route
         */
        @Override
        public GridBuilder<ModelClass, FieldType, RepositoryType> withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        /**
         * Set the title field for items (uses default CardFactory)
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            return withTitleField(titleField, CardFactory.class);
        }

        /**
         * Set the title field and item factory
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField, Class<? extends VortexCrudItemFactory> factory) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(factory);
            config.setTitleField(titleField);
            return this;
        }

        /**
         * Set the description field for items
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setDescriptionField(descriptionField);
            return this;
        }

        /**
         * Set the image field for items
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setImageField(imageField);
            return this;
        }

        /**
         * Set the filter field for search/filtering
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setFilterField(filterField);
            return this;
        }

        /**
         * Enable inline editing
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withInlineEdit(boolean inlineEdit) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setInlineEdit(inlineEdit);
            return this;
        }

        /**
         * Set grid columns (fields and collections)
         */
        @SafeVarargs
        public final GridBuilder<ModelClass, FieldType, RepositoryType> withChildren(InternalFormElement<ModelClass, FieldType, RepositoryType>... children) {
            return withChildren(Arrays.asList(children));
        }

        /**
         * Set grid columns (fields and collections)
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setChildren(children);
            return this;
        }

        /**
         * Add a single column to the grid
         */
        public GridBuilder<ModelClass, FieldType, RepositoryType> addChild(InternalFormElement<ModelClass, FieldType, RepositoryType> child) {
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
        public GridBuilder<ModelClass, FieldType, RepositoryType> withItemFactory(Class<? extends VortexCrudItemFactory> factory) {
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