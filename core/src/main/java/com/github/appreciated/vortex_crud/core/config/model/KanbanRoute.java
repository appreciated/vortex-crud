package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.items.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.items.defaults.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified builder for kanban routes.
 * Usage: KanbanRoute.of()
 *     .withDataStore(dataStore)
 *     .withColumnField(field)  // Required
 *     .withTitleField(field)
 *     .withChildren(...)
 *     .build()
 */
public class KanbanRoute<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    public KanbanRoute() {
        super(KanbanDetailFactory.class);
    }

    public static <ModelClass, FieldType, RepositoryType> KanbanBuilder<ModelClass, FieldType, RepositoryType> of() {
        return new KanbanBuilder<>(new KanbanRoute<>());
    }

    public static class KanbanBuilder<ModelClass, FieldType, RepositoryType> extends RouteRenderer.Builder<ModelClass, FieldType, RepositoryType> {

        public KanbanBuilder(KanbanRoute<ModelClass, FieldType, RepositoryType> product) {
            super(product);
        }

        /**
         * Set the data store for this route
         */
        @Override
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withDataStore(RepositoryType dataStore) {
            super.withDataStore(dataStore);
            return this;
        }

        /**
         * Set the title for this route
         */
        @Override
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        /**
         * Set the column field for kanban (REQUIRED)
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withColumnField(FieldType columnField) {
            return withColumnField(columnField, CardFactory.class);
        }

        /**
         * Set the column field and item factory
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withColumnField(FieldType columnField, Class<? extends VortexCrudItemFactory> factory) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(factory);
            config.setColumnField(columnField);
            return this;
        }

        /**
         * Set the row index field for ordering within columns
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withRowIndexField(FieldType rowIndexField) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setRowIndexField(rowIndexField);
            return this;
        }

        /**
         * Set the title field for items (uses default CardFactory)
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            return withTitleField(titleField, CardFactory.class);
        }

        /**
         * Set the title field and item factory
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField, Class<? extends VortexCrudItemFactory> factory) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(factory);
            config.setTitleField(titleField);
            return this;
        }

        /**
         * Set the description field for items
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setDescriptionField(descriptionField);
            return this;
        }

        /**
         * Set the image field for items
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setImageField(imageField);
            return this;
        }

        /**
         * Set the filter field for search/filtering
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setFilterField(filterField);
            return this;
        }

        /**
         * Set kanban card fields (fields and collections)
         */
        @SafeVarargs
        public final KanbanBuilder<ModelClass, FieldType, RepositoryType> withChildren(InternalFormElement<ModelClass, FieldType, RepositoryType>... children) {
            return withChildren(Arrays.asList(children));
        }

        /**
         * Set kanban card fields (fields and collections)
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setChildren(children);
            return this;
        }

        /**
         * Add a single field to the kanban card
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> addChild(InternalFormElement<ModelClass, FieldType, RepositoryType> child) {
            Kanban<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            if (config.getChildren() == null) {
                config.setChildren(new ArrayList<>());
            }
            config.getChildren().add(child);
            return this;
        }

        /**
         * Set the item factory for rendering
         */
        public KanbanBuilder<ModelClass, FieldType, RepositoryType> withItemFactory(Class<? extends VortexCrudItemFactory> factory) {
            getOrCreateConfig(factory);
            return this;
        }

        /**
         * Helper to get or create the configuration
         */
        private Kanban<ModelClass, FieldType, RepositoryType> getOrCreateConfig(Class<? extends VortexCrudItemFactory> factory) {
            if (product.getConfiguration() == null) {
                Kanban<ModelClass, FieldType, RepositoryType> config =
                    new Kanban<>(factory);
                product.setConfiguration(config);
            }
            return (Kanban<ModelClass, FieldType, RepositoryType>) product.getConfiguration();
        }

        @Override
        public RouteRenderer<ModelClass, FieldType, RepositoryType> build() {
            return super.build();
        }
    }
}