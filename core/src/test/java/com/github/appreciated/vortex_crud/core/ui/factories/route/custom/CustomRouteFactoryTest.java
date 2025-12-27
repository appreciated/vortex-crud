package com.github.appreciated.vortex_crud.core.ui.factories.route.custom;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomRouteFactoryTest {

    @Test
    void testCustomRouteFactoryErrorMessage() {
        CustomRouteFactory<Object, Object, Object> factory = new CustomRouteFactory<>();
        Component component = factory.renderRoute(null, null, null, null);

        Assertions.assertTrue(component instanceof Div);
        Div div = (Div) component;
        Assertions.assertEquals("CustomRoute misconfigured - check @Route annotation and path match", div.getText());
        Assertions.assertEquals("red", div.getStyle().get("color"));
        Assertions.assertEquals("2em", div.getStyle().get("padding"));
    }

    @Test
    void testIsContainerRoute() {
        CustomRouteFactory<Object, Object, Object> factory = new CustomRouteFactory<>();
        Assertions.assertFalse(factory.isContainerRoute());
    }
}
