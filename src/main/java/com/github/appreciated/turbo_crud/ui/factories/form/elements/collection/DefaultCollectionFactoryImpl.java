package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.item.DefaultOneToManyCollectionItemImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.*;

public class DefaultCollectionFactoryImpl implements TurboCrudCollectionFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudDialogFactoryRegistry dialogFactory;

    public DefaultCollectionFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                        TurboCrudDialogFactoryRegistry dialogFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      Route route,
                                      FormElement factoryConfig,
                                      TurboCrudRouteFactoryRegistry routeFactory,
                                      FormCreator formCreator) {
        VerticalLayout list = new VerticalLayout();
        list.setPadding(false);
        list.setSpacing(false);
        list.getElement().setAttribute("theme", "spacing-s");
        list.getStyle().setMarginTop("calc(var(--lumo-font-size-s) * 1.5)");
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.add(new H4(list.getTranslation(factoryConfig.getLabel())));
        Button button = new Button(VaadinIcon.PLUS.create());
        button.addThemeVariants(LUMO_PRIMARY);
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, entityManagerFactoryRegistry, routeFactory, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, routeFactory, entityManagerFactoryRegistry, formCreator, list, header);
        return list;
    }

    private void loadCollection(String foreignKeyValue,
                                FormElement formElement,
                                TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                                TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                FormCreator formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionData data = formElement.getConfiguration().getData();

        TurboCrudEntityManager entityManager = entityManagerFactoryRegistry.getFactory(data.getRepository());
        List<GenericEntity> records = getDataByConfig(foreignKeyValue, entityManager, data, entityManagerFactoryRegistry);
        for (GenericEntity record : records) {
            DefaultOneToManyCollectionItemImpl item = new DefaultOneToManyCollectionItemImpl();
            item.getContent().addClickListener(event -> openDialog(EntityUtil.getId(record), foreignKeyValue, formElement, entityManagerFactoryRegistry, routeFactoryRegistry, formCreator, list, header));
            Config configuration = formElement.getConfiguration().getChild().getConfiguration();
            Form form = ConfigBeanFactory.create(configuration, Form.class);
            for (FormElement child : form.getChildren()) {
                Object o = record.get(child.getField());
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    entityManager.deleteRecordById(EntityUtil.getId(record));
                    loadCollection(foreignKeyValue, formElement, routeFactoryRegistry, entityManagerFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(formElement.getConfiguration().getEmptyMessage())));
        }
    }

    private static List<GenericEntity> getDataByConfig(String foreignKeyValue, TurboCrudEntityManager targetEntityManager, CollectionData collectionData, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        if (collectionData.getOneToMany() != null) {
            return foreignKeyValue == null ? List.of() :
                    targetEntityManager.getRecordsFromTableWhereColumnEquals(collectionData.getOneToMany().getReferenceField(), foreignKeyValue, 0, Integer.MAX_VALUE);
        } else if (collectionData.getManyToMany() != null) {
            // If we need to resolve a many-to-many relation it is necessary to do two selects one over the associative
            // repository and one over the target repository and one with the actual entries.
            // This could be improved upon, if it was allowed to provide a custom repository / interface for the sake
            // of resolving the following data.
            ManyToManyConfiguration manyToMany = collectionData.getManyToMany();
            TurboCrudEntityManager associativeEntityManager = entityManagerFactoryRegistry.getFactory(manyToMany.getAssociativeRepository());
            List<GenericEntity> associativeRecords = associativeEntityManager.getRecordsFromTableWhereColumnEquals(manyToMany.getAssociativeSourceIdField(), foreignKeyValue, 0, Integer.MAX_VALUE);
            List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.getString(manyToMany.getAssociativeTargetIdField())).toList();
            return foreignKeyValue == null ? List.of() :
                            targetEntityManager.getRecordsFromTableWhereColumnIn(manyToMany.getRepositoryField(), associativeRecordIds, 0, Integer.MAX_VALUE);
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            FormElement formElement,
                            TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                            TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                            FormCreator formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        CollectionConfiguration collectionData = formElement.getConfiguration();
        com.vaadin.flow.component.dialog.Dialog dialog = dialogFactory.getFactory(formElement.getConfiguration().getFactory()).create(
                entityId,
                foreignKey,
                getReferenceField(collectionData.getData()),
                collectionData.getChild(),
                collectionData.getData().getRepository(),
                routeFactoryRegistry,
                () -> loadCollection(foreignKey, formElement, routeFactoryRegistry, entityManagerFactoryRegistry, formCreator, list, header),
                formCreator);
        dialog.open();
    }

    private static String getReferenceField(CollectionData collectionData) {
        if (collectionData.getOneToMany() != null) {
            OneToManyConfiguration oneToMany = collectionData.getOneToMany();
            return oneToMany.getReferenceField();
        } else if (collectionData.getManyToMany() != null) {
            ManyToManyConfiguration oneToMany = collectionData.getManyToMany();
            return oneToMany.getRepositoryField();
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }
}