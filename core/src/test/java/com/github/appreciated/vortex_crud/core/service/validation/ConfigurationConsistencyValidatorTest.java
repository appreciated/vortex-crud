package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.GridRoute;
import com.github.appreciated.vortex_crud.core.config.model.Roles;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ConfigurationConsistencyValidatorTest {

    private final ConfigurationConsistencyValidator validator = new ConfigurationConsistencyValidator();

    @Test
    void unknownSelectKeyFailsStartupWithActionableMessage() {
        Application<Object, String, Object> application = applicationWith(
                Map.of("status", SelectField.<Object, String, Object>builder().values("missing-select").build()),
                selects("task-status"),
                null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> validator.validate(application));
        assertTrue(exception.getMessage().contains("missing-select"),
                "message should name the unknown key: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("task-status"),
                "message should list the known keys: " + exception.getMessage());
    }

    @Test
    void selectFieldWithoutKeyFailsStartup() {
        Application<Object, String, Object> application = applicationWith(
                Map.of("status", SelectField.<Object, String, Object>builder().build()),
                selects("task-status"),
                null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> validator.validate(application));
        assertTrue(exception.getMessage().contains("no select key configured"),
                "message should explain the missing key: " + exception.getMessage());
    }

    @Test
    void knownSelectKeyPasses() {
        Application<Object, String, Object> application = applicationWith(
                Map.of("status", SelectField.<Object, String, Object>builder().values("task-status").build()),
                selects("task-status"),
                null);

        assertDoesNotThrow(() -> validator.validate(application));
    }

    @Test
    void nonSelectFieldsAreIgnored() {
        Application<Object, String, Object> application = applicationWith(
                Map.of("name", TextField.<Object, String, Object>builder().build()),
                null,
                null);

        assertDoesNotThrow(() -> validator.validate(application));
    }

    @Test
    void undeclaredRoleIsCollectedAsWarningNotError() {
        ConsistencyValidationStrategy strategy = new ConsistencyValidationStrategy(
                selects(), Roles.builder().roles(List.of("admin", "viewer")).build());

        strategy.validate(List.of("admin", "manager"), "writeRoles", "GridRoute");

        assertEquals(0, strategy.errors().size());
        assertEquals(1, strategy.warnings().size());
        String warning = strategy.warnings().iterator().next();
        assertTrue(warning.contains("manager"), warning);
        assertTrue(warning.contains("GridRoute.writeRoles"), warning);
    }

    @Test
    void declaredRolesProduceNoWarnings() {
        ConsistencyValidationStrategy strategy = new ConsistencyValidationStrategy(
                selects(), Roles.builder().roles(List.of("admin", "viewer")).build());

        strategy.validate(List.of("admin", "viewer"), "writeRoles", "GridRoute");

        assertTrue(strategy.warnings().isEmpty());
    }

    @Test
    void withoutConfiguredRolesNoRoleWarningsAreEmitted() {
        ConsistencyValidationStrategy strategy = new ConsistencyValidationStrategy(selects(), null);

        strategy.validate(List.of("anything"), "writeRoles", "GridRoute");

        assertTrue(strategy.warnings().isEmpty());
    }

    @SuppressWarnings("unchecked")
    private Application<Object, String, Object> applicationWith(Map<String, Field<Object, String, Object>> fields,
                                                                Selects selects,
                                                                Roles roles) {
        DataStoreConfig<Object, String, Object> dataStoreConfig = DataStoreConfig.<Object, String, Object>builder()
                .dataStoreInstance(Mockito.mock(VortexCrudDataStore.class))
                .fields(fields)
                .build();
        RouteRenderer<Object, String, Object> route = GridRoute.<Object, String, Object>builder()
                .dataStoreConfig(dataStoreConfig)
                .title("route.title")
                .titleField("name")
                .build();
        return Application.<Object, String, Object>builder()
                .applicationName("application.name")
                .routes(Map.of("route", route))
                .selects(selects)
                .build();
    }

    private Selects selects(String... keys) {
        Map<String, LinkedHashMap<?, String>> configs = new LinkedHashMap<>();
        for (String key : keys) {
            LinkedHashMap<String, String> values = new LinkedHashMap<>();
            values.put("value", "i18n.key");
            configs.put(key, values);
        }
        return Selects.builder().configs(configs).build();
    }
}
