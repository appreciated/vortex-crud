package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaCollectionElement extends InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    public static JpaCollectionElementBuilder builder(String label) {
        return new JpaCollectionElementBuilder().label(label);
    }

    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor
    public static class JpaCollectionElementBuilder {
        private String label;
        private VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> factory;
        private boolean readOnly;
        private List<String> readOnlyForRoles;
        private int span;

        // Collection properties
        private VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dialogFactory;
        private String emptyMessage;
        private RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> form;
        private String titleField;
        private DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dataStoreConfig;
        private OneToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> oneToMany;
        private ManyToMany<JpaRepository<?, ?>, String, JpaRepository<?, ?>> manyToMany;
        private List<String> children;

        public InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build() {
            Collection<JpaRepository<?, ?>, String, JpaRepository<?, ?>> collectionConfig = Collection.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .label(label)
                    .factory(dialogFactory)
                    .emptyMessage(emptyMessage)
                    .form(form)
                    .titleField(titleField)
                    .dataStoreConfig(dataStoreConfig)
                    .oneToMany(oneToMany)
                    .manyToMany(manyToMany)
                    .children(children)
                    .build();

            return InternalFormElement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .label(label)
                    .type(ViewFieldType.COLLECTION)
                    .factory(factory)
                    .readOnly(readOnly)
                    .readOnlyForRoles(readOnlyForRoles)
                    .span(span)
                    .configuration(collectionConfig)
                    .build();
        }
    }
}
