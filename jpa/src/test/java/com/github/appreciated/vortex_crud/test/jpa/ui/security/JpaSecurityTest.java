package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JpaSecurityTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "vaadin.productionMode=true")
public class JpaSecurityTest extends BaseUITest {

    @Autowired
    private JpaSecurityUserRepository userRepository;
    @Autowired
    private JpaSecurityRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value(value = "${local.server.port}")
    private int port;

    @BeforeEach
    public void setupData() {
        // Disable auto-login
        System.setProperty("vortex.crud.disable.autologin", "true");

        userRepository.deleteAll();
        roleRepository.deleteAll();

        JpaSecurityRole adminRole = roleRepository.save(new JpaSecurityRole("ADMIN"));
        JpaSecurityRole userRole = roleRepository.save(new JpaSecurityRole("USER"));
        JpaSecurityRole viewerRole = roleRepository.save(new JpaSecurityRole("VIEWER"));

        createUser("admin", "password", List.of(adminRole));
        createUser("user", "password", List.of(userRole));
        createUser("viewer", "password", List.of(viewerRole));
        createUser("guest", "password", List.of());
    }

    @AfterEach
    public void cleanup() {
        System.clearProperty("vortex.crud.disable.autologin");
    }

    private void createUser(String username, String password, List<JpaSecurityRole> roles) {
        JpaSecurityUser user = new JpaSecurityUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoles(roles);
        // Set test fields
        user.setPublicField("Public Value");
        user.setAdminField("Admin Value");
        user.setSecretField("Secret Value");
        userRepository.save(user);
    }

    private void login(String username, String password) {
        navigateTo("login");
        WebElement loginForm = waitForElement(By.tagName("vaadin-login-form"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new CustomEvent('login', { detail: { username: arguments[1], password: arguments[2] } }));",
                loginForm, username, password
        );
        waitForUrlToBe("");
    }

    @Test
    void testUnauthenticatedAccess() {
        driver.get("http://127.0.0.1:%s/users-grid".formatted(port));
        wait.until(ExpectedConditions.urlContains("login"));
        assertTrue(waitForElement(By.tagName("vaadin-login-form")).isDisplayed());
    }

    @Test
    void testAdminAccess() {
        login("admin", "password");
        navigateTo("users-grid");

        assertTrue(driver.getCurrentUrl().contains("users-grid"), "Admin should access users-grid");

        // Open the form (click on an item)
        clickElement(waitForAnyElementContainingText("admin"));

        // Wait for form to open
        waitForElement(By.tagName("vaadin-form-layout"));

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("publicField"), "Public field should be editable for ADMIN");

        assertTrue(isFieldVisible("adminField"), "Admin field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("adminField"), "Admin field should be editable for ADMIN");

        assertTrue(isFieldVisible("secretField"), "Secret field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("secretField"), "Secret field should be editable for ADMIN");
    }

    @Test
    void testUserAccess() {
        login("user", "password");
        navigateTo("users-grid");

        assertTrue(driver.getCurrentUrl().contains("users-grid"), "User should access users-grid");

        clickElement(waitForAnyElementContainingText("user"));
        waitForElement(By.tagName("vaadin-form-layout"));

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for USER");
        assertFalse(isFieldReadOnly("publicField"), "Public field should be editable for USER");

        assertTrue(isFieldVisible("adminField"), "Admin field should be visible for USER");
        assertTrue(isFieldReadOnly("adminField"), "Admin field should be read-only for USER");

        assertFalse(isFieldVisible("secretField"), "Secret field should be hidden for USER");
    }

    @Test
    void testViewerAccess() {
        login("viewer", "password");
        navigateTo("users-grid");

        assertTrue(driver.getCurrentUrl().contains("users-grid"), "Viewer should access users-grid");

        clickElement(waitForAnyElementContainingText("viewer"));
        waitForElement(By.tagName("vaadin-form-layout"));

        // Viewer has Read-Only access to route.

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for VIEWER");
        assertTrue(isFieldReadOnly("publicField"), "Public field should be read-only for VIEWER");

        // Admin field: Hidden because Viewer is not in readOnlyRoles (USER) nor writeRoles (ADMIN)
        assertFalse(isFieldVisible("adminField"), "Admin field should be hidden for VIEWER");

        assertFalse(isFieldVisible("secretField"), "Secret field should be hidden for VIEWER");
    }

    @Test
    void testGuestAccess() {
        login("guest", "password");
        navigateTo("users-grid");

        // Should be denied.
        String url = driver.getCurrentUrl();
        boolean denied = url.contains("access-denied") || url.contains("login") || driver.getPageSource().contains("Access Denied");
        assertTrue(denied, "Guest should be denied access to users-grid. Current URL: " + url);
    }

    private void clickElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private boolean isFieldVisible(String fieldName) {
        String label = getLabelForField(fieldName);
        // Check if any element contains this label
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + label + "')]"));
        return !elements.stream().filter(WebElement::isDisplayed).toList().isEmpty();
    }

    private boolean isFieldReadOnly(String fieldName) {
        String label = getLabelForField(fieldName);
        // Try to find the vaadin-text-field associated with this label.
        // Strategy: find label, find parent/sibling that is the field.
        // Or simpler: find all vaadin-text-field and check if they contain the label (if label is inside).

        List<WebElement> fields = driver.findElements(By.tagName("vaadin-text-field"));
        for (WebElement field : fields) {
            if (field.getText().contains(label) || field.getAttribute("label").equals(label)) {
                return field.getAttribute("readonly") != null;
            }
            // Check label property via JS if needed?
        }

        // Fallback: Check if there is a readonly input with that label nearby
        // This is tricky with Selenium + Vaadin.

        // Let's assume if it is visible and we want to check read-only:
        // We can try to send keys?
        // Or check 'readonly' attribute on the vaadin-text-field element.

        // Let's refine finding the element.
        for (WebElement element : driver.findElements(By.xpath("//vaadin-text-field"))) {
             // Vaadin 23+ stores label in 'label' property/attribute
             String elLabel = element.getAttribute("label");
             if (elLabel != null && elLabel.equals(label)) {
                 return element.getAttribute("readonly") != null;
             }
        }
        return false;
    }

    private String getLabelForField(String fieldName) {
        if (fieldName.equals("username")) return "Username";
        if (fieldName.equals("publicField")) return "Public Field";
        if (fieldName.equals("adminField")) return "Admin Field";
        if (fieldName.equals("secretField")) return "Secret Field";
        return fieldName;
    }
}
