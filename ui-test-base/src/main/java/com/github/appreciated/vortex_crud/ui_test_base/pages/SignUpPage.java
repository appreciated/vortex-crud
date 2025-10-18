package com.github.appreciated.vortex_crud.ui_test_base.pages;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class SignUpPage extends VerticalLayoutElement {

    public LoginPage signUp(String username, String password) {
        $(TextFieldElement.class).id("username").setValue(username);
        $(PasswordFieldElement.class).id("password").setValue(password);
        $(PasswordFieldElement.class).id("confirmPassword").setValue(password);
        $(ButtonElement.class).first().click();
        return getDriver().waitFor(LoginPage.class);
    }
}