package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class SecurityIntegrationTest extends BaseUITest {

    @MockitoBean
    @Qualifier("userDataStore")
    private VortexCrudDataStore<String, Object> userDataStore;

    @MockitoBean
    @Qualifier("roleDataStore")
    private VortexCrudDataStore<String, Object> roleDataStore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private ReflectionService reflectionService;

    @MockitoBean
    private VortexCrudForeignKeyResolutionStrategy vortexCrudForeignKeyResolutionStrategy;

    @MockitoBean
    private TranslationService translationService;

    @Autowired
    private com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService<Object, String, String> configService;

    @Value(value = "${local.server.port}")
    private int port;

    private final Map<Integer, TestUser> userStore = new HashMap<>();
    private final Map<Integer, TestRole> roleStore = new HashMap<>();
    private final AtomicInteger userIdCounter = new AtomicInteger(1);
    private final AtomicInteger roleIdCounter = new AtomicInteger(1);

    @BeforeEach
    public void setupData() {
        System.setProperty("vortex.crud.disable.autologin", "true");

        // Clear stores
        userStore.clear();
        roleStore.clear();
        userIdCounter.set(1);
        roleIdCounter.set(1);

        // Setup minimal mocks for userRepository
        when(userDataStore.insertRecord(any(TestUser.class))).thenAnswer(inv -> {
            TestUser user = inv.getArgument(0);
            if (user.getId() == null) {
                user.setId(userIdCounter.getAndIncrement());
            }
            userStore.put(user.getId(), user);
            return user.getId();
        });
        when(userDataStore.getRecordsFromTableWhereColumnEquals(anyString(), any(), anyInt(), anyInt()))
                .thenAnswer(inv -> userStore.values().stream()
                        .filter(u -> matchesField(u, inv.getArgument(0), inv.getArgument(1)))
                        .toList());

        // Setup minimal mocks for roleRepository
        when(roleDataStore.insertRecord(any(TestRole.class))).thenAnswer(inv -> {
            TestRole role = inv.getArgument(0);
            if (role.getId() == null) {
                role.setId(roleIdCounter.getAndIncrement());
            }
            roleStore.put(role.getId(), role);
            return role.getId();
        });

        // Create test data
        TestRole adminRole = new TestRole(null, "ADMIN");
        roleDataStore.insertRecord(adminRole);
        TestRole userRole = new TestRole(null, "USER");
        roleDataStore.insertRecord(userRole);
        TestRole viewerRole = new TestRole(null, "VIEWER");
        roleDataStore.insertRecord(viewerRole);

        createUser("admin", "password", List.of(adminRole));
        createUser("user", "password", List.of(userRole));
        createUser("viewer", "password", List.of(viewerRole));
        createUser("guest", "password", List.of());
    }

    @AfterEach
    public void cleanup() {
        System.clearProperty("vortex.crud.disable.autologin");
    }

    private void createUser(String username, String password, List<TestRole> roles) {
        TestUser user = new TestUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoles(roles);
        user.setPublicField("Public Value");
        user.setAdminField("Admin Value");
        user.setSecretField("Secret Value");
        userDataStore.insertRecord(user);
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

        clickElement(waitForAnyElementContainingText("admin"));
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

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for VIEWER");
        assertTrue(isFieldReadOnly("publicField"), "Public field should be read-only for VIEWER");

        assertFalse(isFieldVisible("adminField"), "Admin field should be hidden for VIEWER");

        assertFalse(isFieldVisible("secretField"), "Secret field should be hidden for VIEWER");
    }

    @Test
    void testGuestAccess() {
        login("guest", "password");
        navigateTo("users-grid");

        String url = driver.getCurrentUrl();
        boolean denied = url.contains("access-denied") || url.contains("login") || driver.getPageSource().contains("Access Denied");
        assertTrue(denied, "Guest should be denied access to users-grid. Current URL: " + url);
    }

    private void clickElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private boolean isFieldVisible(String fieldName) {
        String label = getLabelForField(fieldName);
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + label + "')]"));
        return !elements.stream().filter(WebElement::isDisplayed).toList().isEmpty();
    }

    private boolean isFieldReadOnly(String fieldName) {
        String label = getLabelForField(fieldName);
        for (WebElement element : driver.findElements(By.xpath("//vaadin-text-field"))) {
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

    private boolean matchesField(TestUser user, String field, Object value) {
        try {
            java.lang.reflect.Field f = user.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return Objects.equals(f.get(user), value);
        } catch (Exception e) {
            return false;
        }
    }
}
