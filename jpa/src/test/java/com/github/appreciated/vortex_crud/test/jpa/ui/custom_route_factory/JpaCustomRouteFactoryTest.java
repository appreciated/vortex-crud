package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCustomRouteFactoryTest {

    @Test
    void testCustomRouteFactoryErrorMessage() {
        CustomRouteFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> factory = new CustomRouteFactory<>();
        Component component = factory.renderRoute(null, null, null, null);

        Assertions.assertTrue(component instanceof Div);
        Div div = (Div) component;
        Assertions.assertEquals("CustomRoute misconfigured - check @Route annotation and path match", div.getText());
        Assertions.assertEquals("red", div.getStyle().get("color"));
        Assertions.assertEquals("2em", div.getStyle().get("padding"));
    }
}
