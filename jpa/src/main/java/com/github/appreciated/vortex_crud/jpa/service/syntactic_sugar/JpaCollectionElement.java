package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaCollectionElement extends InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static JpaCollectionElementBuilder builder(String label) {
        return new JpaCollectionElementBuilder().label(label);
    }

    public static class JpaCollectionElementBuilder {
        private String label;
        private VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> factory;
        private VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dialogFactory;
        private DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dataStoreConfig;
        private OneToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> oneToMany;
        private ManyToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> manyToMany;
        private List<String> children;
        private String emptyMessage;
        private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form;
        private int span;
        private boolean readOnly;
        private List<String> readOnlyForRoles;
        private String titleField;

        public JpaCollectionElementBuilder label(String label) {
            this.label = label;
            return this;
        }

        public JpaCollectionElementBuilder factory(VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> factory) {
            this.factory = factory;
            return this;
        }

        public JpaCollectionElementBuilder dialogFactory(VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dialogFactory) {
            this.dialogFactory = dialogFactory;
            return this;
        }

        public JpaCollectionElementBuilder dataStoreConfig(DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dataStoreConfig) {
            this.dataStoreConfig = dataStoreConfig;
            return this;
        }

        public JpaCollectionElementBuilder oneToMany(OneToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> oneToMany) {
            this.oneToMany = oneToMany;
            return this;
        }

        public JpaCollectionElementBuilder manyToMany(ManyToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> manyToMany) {
            this.manyToMany = manyToMany;
            return this;
        }

        public JpaCollectionElementBuilder children(List<String> children) {
            this.children = children;
            return this;
        }

        public JpaCollectionElementBuilder emptyMessage(String emptyMessage) {
            this.emptyMessage = emptyMessage;
            return this;
        }

        public JpaCollectionElementBuilder form(RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form) {
            this.form = form;
            return this;
        }

        public JpaCollectionElementBuilder span(int span) {
            this.span = span;
            return this;
        }

        public JpaCollectionElementBuilder readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public JpaCollectionElementBuilder readOnlyForRoles(List<String> readOnlyForRoles) {
            this.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public JpaCollectionElementBuilder titleField(String titleField) {
            this.titleField = titleField;
            return this;
        }

        public InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build() {
            Collection<JpaRepository<?, ?>, String, JpaRepository<?, ?>> collectionConfig = Collection.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .label(label)
                    .factory(dialogFactory)
                    .dataStoreConfig(dataStoreConfig)
                    .oneToMany(oneToMany)
                    .manyToMany(manyToMany)
                    .children(children)
                    .emptyMessage(emptyMessage)
                    .form(form)
                    .titleField(titleField)
                    .build();

            return InternalFormElement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .label(label)
                    .type(ViewFieldType.COLLECTION)
                    .factory(factory)
                    .span(span)
                    .readOnly(readOnly)
                    .readOnlyForRoles(readOnlyForRoles)
                    .configuration(collectionConfig)
                    .build();
        }
    }
}
