package com.github.appreciated.vortex_crud.ui_test_base.tests;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.github.appreciated.vortex_crud.ui_test_base.pages.LoginPage;
import com.github.appreciated.vortex_crud.ui_test_base.pages.MainViewPage;
import com.github.appreciated.vortex_crud.ui_test_base.pages.SignUpPage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractLoginTest extends BaseUITest {

    @Test
    public void testLogin() {
        SignUpPage signUpPage = getDriver().get(getSignUpUrl(), SignUpPage.class);
        LoginPage loginPage = signUpPage.signUp("testuser", "testpassword");
        MainViewPage mainViewPage = loginPage.login("testuser", "testpassword");
        assertTrue(mainViewPage.isMainViewVisible());
    }

    protected abstract String getSignUpUrl();
}