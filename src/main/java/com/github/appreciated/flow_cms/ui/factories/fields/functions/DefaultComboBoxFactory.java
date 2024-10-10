package com.github.appreciated.flow_cms.ui.factories.fields.functions;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.TableConfig;
import com.github.appreciated.flow_cms.ui.factories.fields.FlowCmsFieldFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class DefaultComboBoxFactory implements FlowCmsFieldFactory {

    private final ConfigObject selectsConfig;
    private final Map<String, TableConfig> tablesConfig;

    public DefaultComboBoxFactory(ConfigObject selectsConfig, Map<String, TableConfig> tablesConfig) {
        this.selectsConfig = selectsConfig;
        this.tablesConfig = tablesConfig;
    }

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        ComboBox<String> objectComboBox = new ComboBox<>();

        TableConfig tableConfig = tablesConfig.get(table);
        FieldConfig tableFieldConfig = tableConfig.getFieldsConfig().get(field);

        String selectName = tableFieldConfig.getValues();
        ConfigObject selectConfig = selectsConfig.toConfig().getObject(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();
        Config config = selectConfig.toConfig();
        objectComboBox.setItems(new ArrayList<>(strings));
        objectComboBox.setItemLabelGenerator(item -> objectComboBox.getTranslation(config.getString(item)));

        return objectComboBox;
    }
}
