package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.items.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.items.defaults.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simplified builder for form routes.
 * Usage: FormRoute.of()
 *     .withDataStore(dataStore)
 *     .withTitleField(field)
 *     .withChildren(...)
 *     .build()
 */
public class FormRoute<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    public FormRoute() {
        super(FormRouteFactory.class);
    }

    public static <ModelClass, FieldType, RepositoryType> FormBuilder<ModelClass, FieldType, RepositoryType> of() {
        return new FormBuilder<>(new FormRoute<>());
    }

    public static class FormBuilder<ModelClass, FieldType, RepositoryType> extends RouteRenderer.Builder<ModelClass, FieldType, RepositoryType> {

        public FormBuilder(FormRoute<ModelClass, FieldType, RepositoryType> product) {
            super(product);
        }

        /**
         * Set the data store for this route
         */
        @Override
        public FormBuilder<ModelClass, FieldType, RepositoryType> withDataStore(RepositoryType dataStore) {
            super.withDataStore(dataStore);
            return this;
        }

        /**
         * Set the title for this route
         */
        @Override
        public FormBuilder<ModelClass, FieldType, RepositoryType> withTitle(String title) {
            super.withTitle(title);
            return this;
        }

        /**
         * Set the title field for items (uses default CardFactory)
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            return withTitleField(titleField, CardFactory.class);
        }

        /**
         * Set the title field and item factory
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField, Class<? extends VortexCrudItemFactory> factory) {
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(factory);
            config.setTitleField(titleField);
            return this;
        }

        /**
         * Set the description field for items
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setDescriptionField(descriptionField);
            return this;
        }

        /**
         * Set the image field for items
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setImageField(imageField);
            return this;
        }

        /**
         * Set form children (fields and collections)
         */
        @SafeVarargs
        public final FormBuilder<ModelClass, FieldType, RepositoryType> withChildren(InternalFormElement<ModelClass, FieldType, RepositoryType>... children) {
            return withChildren(Arrays.asList(children));
        }

        /**
         * Set form children (fields and collections)
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            config.setChildren(children);
            return this;
        }

        /**
         * Add a single child to the form
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> addChild(InternalFormElement<ModelClass, FieldType, RepositoryType> child) {
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config = getOrCreateConfig(CardFactory.class);
            if (config.getChildren() == null) {
                config.setChildren(new ArrayList<>());
            }
            config.getChildren().add(child);
            return this;
        }

        /**
         * Set the item factory for rendering
         */
        public FormBuilder<ModelClass, FieldType, RepositoryType> withItemFactory(Class<? extends VortexCrudItemFactory> factory) {
            getOrCreateConfig(factory);
            return this;
        }

        /**
         * Helper to get or create the configuration
         */
        private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getOrCreateConfig(Class<? extends VortexCrudItemFactory> factory) {
            if (product.getConfiguration() == null) {
                RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> config =
                    new RouteRendererConfiguration<>(factory);
                product.setConfiguration(config);
            }
            return (RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>) product.getConfiguration();
        }

        @Override
        public RouteRenderer<ModelClass, FieldType, RepositoryType> build() {
            return super.build();
        }
    }
}