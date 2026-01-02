package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldValueStrategy;
import com.github.appreciated.vortex_crud.ui_test_base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

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
    private ReferenceFieldValueStrategy referenceFieldValueStrategy;

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

        // 1. Reset State
        userStore.clear();
        roleStore.clear();
        userIdCounter.set(1);
        roleIdCounter.set(1);

        // 2. Mock TranslationService
        when(translationService.getTranslation(anyString(), any())).thenAnswer(inv -> inv.getArgument(0));

        // 3. Mock ReflectionService (CRITICAL)
        when(reflectionService.getValue(any(), anyString())).thenAnswer(inv -> {
            Object entity = inv.getArgument(0);
            String fieldName = inv.getArgument(1);
            if (entity == null || fieldName == null) return null;
            try {
                java.lang.reflect.Field field = entity.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(entity);
            } catch (Exception e) {
                return null;
            }
        });

        when(reflectionService.getString(any(), anyString())).thenAnswer(inv -> {
            Object val = reflectionService.getValue(inv.getArgument(0), inv.getArgument(1));
            return val == null ? null : val.toString();
        });

        // 5. Mock User DataStore - METADATA
        when(userDataStore.getModelClass()).thenReturn((Class) TestUser.class);
        when(userDataStore.newInstance()).thenAnswer(inv -> new TestUser());

        // 6. Mock User DataStore - COUNT
        when(userDataStore.count()).thenAnswer(inv -> userStore.size());
        when(userDataStore.countWhereColumnLike(any(), anyString())).thenAnswer(inv -> userStore.size());

        // 7. Mock User DataStore - READ OPERATIONS
        when(userDataStore.getRecordsFromTable(anyInt(), anyInt()))
                .thenAnswer(inv -> new ArrayList<>(userStore.values()));
        when(userDataStore.getRecordsFromTableWhereColumnEquals(anyString(), any(), anyInt(), anyInt()))
                .thenAnswer(inv -> userStore.values().stream()
                        .filter(u -> matchesField(u, inv.getArgument(0), inv.getArgument(1)))
                        .toList());
        when(userDataStore.getRecordsFromTableWhereColumnEqualsOrdered(any(), any(), any(), anyInt(), anyInt()))
                .thenAnswer(inv -> new ArrayList<>(userStore.values()));
        when(userDataStore.getRecordsFromTableWhereColumnLike(any(), any(), anyInt(), anyInt()))
                .thenAnswer(inv -> new ArrayList<>(userStore.values()));
        when(userDataStore.getRecordsFromTableWhereColumnIn(any(), any(), anyInt(), anyInt()))
                .thenAnswer(inv -> new ArrayList<>(userStore.values()));
        when(userDataStore.getRecordById(any())).thenAnswer(inv -> {
            Object id = inv.getArgument(0);
            if (id instanceof String) {
                return userStore.get(Integer.parseInt((String) id));
            }
            return userStore.get(id);
        });

        // 8. Mock User DataStore - WRITE OPERATIONS
        when(userDataStore.insertRecord(any(TestUser.class))).thenAnswer(inv -> {
            TestUser user = inv.getArgument(0);
            if (user.getId() == null) {
                user.setId(userIdCounter.getAndIncrement());
            }
            userStore.put(user.getId(), user);
            return user.getId();
        });

        // 9. Mock Role DataStore
        when(roleDataStore.insertRecord(any(TestRole.class))).thenAnswer(inv -> {
            TestRole role = inv.getArgument(0);
            if (role.getId() == null) {
                role.setId(roleIdCounter.getAndIncrement());
            }
            roleStore.put(role.getId(), role);
            return role.getId();
        });

        // 10. Create Test Data
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

    private void login(String username, String password, String targetUrl) {
        navigateTo("login");
        Locator loginForm = waitForElement("vaadin-login-form");
        loginForm.evaluate("el => el.dispatchEvent(new CustomEvent('login', { detail: { username: '" + username + "', password: '" + password + "' } }))");
        waitForUrlToBe(targetUrl);
    }

    @Test
    void testUnauthenticatedAccess() {
        page.navigate("http://127.0.0.1:%s/users-grid".formatted(port));
        page.waitForURL(Pattern.compile(".*login.*"));
        assertTrue(waitForElement("vaadin-login-form").isVisible());
    }

    @Test
    void testAdminAccess() {
        login("admin", "password","users-grid");

        waitForAnyElementContainingText("admin").click();
        waitForElement("vaadin-form-layout");

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("publicField"), "Public field should be editable for ADMIN");

        assertTrue(isFieldVisible("adminField"), "Admin field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("adminField"), "Admin field should be editable for ADMIN");

        assertTrue(isFieldVisible("secretField"), "Secret field should be visible for ADMIN");
        assertFalse(isFieldReadOnly("secretField"), "Secret field should be editable for ADMIN");
    }

    @Test
    void testUserAccess() {
        login("user", "password", "users-grid");

        waitForElementContainingText("h4","user").click();
        waitForElement("vaadin-form-layout");

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for USER");
        assertFalse(isFieldReadOnly("publicField"), "Public field should be editable for USER");

        assertTrue(isFieldVisible("adminField"), "Admin field should be visible for USER");
        assertTrue(isFieldReadOnly("adminField"), "Admin field should be read-only for USER");

        assertFalse(isFieldVisible("secretField"), "Secret field should be hidden for USER");
    }

    @Test
    void testViewerAccess() {
        login("viewer", "password","users-grid");
        navigateTo("users-grid");

        assertTrue(page.url().contains("users-grid"), "Viewer should access users-grid");

        waitForAnyElementContainingText("viewer").click();
        waitForElement("vaadin-form-layout");

        assertTrue(isFieldVisible("publicField"), "Public field should be visible for VIEWER");
        assertTrue(isFieldReadOnly("publicField"), "Public field should be read-only for VIEWER");

        assertFalse(isFieldVisible("adminField"), "Admin field should be hidden for VIEWER");

        assertFalse(isFieldVisible("secretField"), "Secret field should be hidden for VIEWER");
    }

    @Test
    void testGuestAccess() {
        navigateTo("login");
        Locator loginForm = waitForElement("vaadin-login-form");
        loginForm.evaluate("el => el.dispatchEvent(new CustomEvent('login', { detail: { username: 'guest', password: 'password' } }))");

        // Wait briefly for redirection logic to trigger
        page.waitForTimeout(1000);

        navigateTo("users-grid");

        String url = page.url();
        String pageSource = page.content();
        boolean denied = url.contains("access-denied")
                || url.contains("login")
                || pageSource.contains("Access Denied")
                || pageSource.contains("AccessDeniedException") // Vaadin Dev Mode error
                || pageSource.contains("Internal Server Error") // Vaadin Prod Mode default error
                || pageSource.contains("NotFoundException") // If route hidden
                || page.locator("vaadin-grid").count() == 0; // Content check: Grid should not be visible
        assertTrue(denied, "Guest should be denied access to users-grid. Current URL: " + url);
    }

    private boolean isFieldVisible(String fieldName) {
        String label = getLabelForField(fieldName);
        return page.locator("//*[contains(text(), '" + label + "')]").count() > 0;
    }

    private boolean isFieldReadOnly(String fieldName) {
        String labelText = getLabelForField(fieldName);
        Locator label = page.locator("//label[contains(text(), '" + labelText + "')]");
        if (label.count() == 0) {
            return false;
        }
        String targetId = label.first().getAttribute("for");
        if (targetId == null || targetId.isEmpty()) {
            return false;
        }
        Locator element = page.locator("#" + targetId);
        if (element.count() == 0) {
            return false;
        }
        return element.first().getAttribute("readonly") != null;
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
