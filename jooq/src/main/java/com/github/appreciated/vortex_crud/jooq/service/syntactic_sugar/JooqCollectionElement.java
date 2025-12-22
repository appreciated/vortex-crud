package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;

public class JooqCollectionElement extends InternalFormElement<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public static JooqCollectionElementBuilder of(String label) {
        return new JooqCollectionElementBuilder().label(label);
    }

    // Alias for builder style if preferred
    public static JooqCollectionElementBuilder builder(String label) {
        return of(label);
    }

    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor
    public static class JooqCollectionElementBuilder {
        private String label;
        private VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> factory;
        private boolean readOnly;
        private List<String> readOnlyForRoles;
        private int span;

        // Collection properties
        private VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dialogFactory;
        private String emptyMessage;
        private RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> form;
        private TableField<?, ?> titleField;
        private DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dataStoreConfig;
        private OneToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> oneToMany;
        private ManyToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToMany;
        private List<TableField<?, ?>> children;

        public InternalFormElement<TableRecord<?>, TableField<?, ?>, TableImpl<?>> build() {
             Collection<TableRecord<?>, TableField<?, ?>, TableImpl<?>> collectionConfig =
                Collection.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
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

            return InternalFormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
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
