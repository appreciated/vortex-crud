package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

public interface FlowCmsComponentFactory {

    <Comp extends Component & HasValue> Comp createComponent(FieldConfig type);

}