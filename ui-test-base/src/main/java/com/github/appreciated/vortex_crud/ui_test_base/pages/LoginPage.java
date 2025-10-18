package com.github.appreciated.vortex_crud.ui_test_base.pages;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.testbench.annotations.Attribute;
import com.vaadin.testbench.elements.VerticalLayoutElement;

@Attribute(name = "class", value = "login-view")
public class LoginPage extends VerticalLayoutElement {

    public MainViewPage login(String username, String password) {
        LoginFormElement loginForm = $(LoginFormElement.class).first();
        loginForm.getUsernameField().setValue(username);
        loginForm.getPasswordField().setValue(password);
        loginForm.submit();
        return BaseUITest.getDriver().waitFor(MainViewPage.class);
    }
}