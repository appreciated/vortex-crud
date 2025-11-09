# DataStore Dropdown Menu Component Usage Guide

This guide demonstrates how to use the new DataStore dropdown component feature that allows adding dropdown components to menus, with data fed from a VortexCrudDataStore.

## Overview

The DataStore dropdown component system consists of:

1. **DataStoreDropdown** - A Vaadin ComboBox component that fetches data from a data store
2. **DataStoreDropdownConfig** - Configuration for the dropdown (filter field, value, label provider, etc.)
3. **MenuActionComponentFactory** - Interface for creating menu action components
4. **DataStoreDropdownMenuActionFactory** - Factory implementation for creating dropdown instances

## Components

### 1. DataStoreDropdownConfig

Configuration class for defining how the dropdown should behave:

```java
DataStoreDropdownConfig<User, UserField, UserRepository> config =
    DataStoreDropdownConfig.<User, UserField, UserRepository>builder()
        .dataStoreKey(UserRepository.USERS)
        .filterField(UserField.STATUS)
        .filterValue("ACTIVE")
        .limit(50)
        .labelProvider(user -> user.getFullName())
        .placeholder("Select a user...")
        .label("Active Users")
        .required(false)
        .readOnly(false)
        .build();
```

**Parameters:**
- `dataStoreKey` - The repository key to fetch data from
- `filterField` - The field to filter on (used with `getRecordsFromTableWhereColumnEquals`)
- `filterValue` - The value to filter by
- `limit` - Maximum number of items to fetch (default: 100)
- `labelProvider` - Function to convert entity to display label (optional, uses `toString()` if not provided)
- `placeholder` - Placeholder text when no item is selected
- `label` - Label for the dropdown field
- `required` - Whether the field is required
- `readOnly` - Whether the field is read-only

### 2. DataStoreDropdown

The actual dropdown component that extends Vaadin's ComboBox:

```java
// Direct usage (if you have the data store instance)
DataStoreDropdown<User, UserField, UserRepository> dropdown =
    new DataStoreDropdown<>(config, userDataStore);

// The component will automatically:
// - Call getRecordsFromTableWhereColumnEquals(filterField, filterValue, 0, limit)
// - Populate the dropdown with results
// - Use the labelProvider to display items
```

**Methods:**
- `refresh()` - Reload data from the data store
- `getConfig()` - Get the configuration
- `getDataStore()` - Get the data store instance

### 3. MenuActionComponentFactory

Interface for creating menu action components:

```java
@FunctionalInterface
public interface MenuActionComponentFactory<ModelClass, FieldType, RepositoryType>
        extends SerializableSupplier<Component> {
    Component get();
}
```

### 4. DataStoreDropdownMenuActionFactory

Factory for creating dropdown instances in menu contexts:

```java
// Create a factory that will produce dropdown instances
DataStoreDropdownMenuActionFactory<User, UserField, UserRepository> factory =
    new DataStoreDropdownMenuActionFactory<>(config, dataStoresMap);

// The factory will:
// - Look up the data store from the dataStoresMap using config.dataStoreKey()
// - Create a new DataStoreDropdown instance
```

## Configuration Levels

Menu actions can be configured at two levels:

1. **Application Level** - Default menu actions applied to all routes
2. **Route Level** - Route-specific menu actions for individual routes

Route-level menu actions supplement (not replace) application-level defaults. Both will be rendered together.

## Integration with Routes

### Adding to GridRoute

```java
// Create dropdown configuration
DataStoreDropdownConfig<Department, DepartmentField, DepartmentRepository> deptConfig =
    DataStoreDropdownConfig.<Department, DepartmentField, DepartmentRepository>builder()
        .dataStoreKey(DepartmentRepository.DEPARTMENTS)
        .filterField(DepartmentField.TYPE)
        .filterValue("SALES")
        .labelProvider(dept -> dept.getName())
        .placeholder("Filter by department...")
        .build();

// Create menu action factory
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> menuActionFactory =
    new DataStoreDropdownMenuActionFactory<>(deptConfig, dataStores);

// Configure grid item renderer
GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
    GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
        .titleField(EmployeeField.NAME)
        .descriptionField(EmployeeField.EMAIL)
        .build();

// Create grid route with menu actions at the route level
GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
    GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
        .dataStoreKey(EmployeeRepository.EMPLOYEES)
        .title("employees.title")
        .configuration(gridConfig)
        .menuActionFactories(List.of(menuActionFactory))
        .build();
```

## Example: Complete Implementation

Here's a complete example showing how to add a department filter dropdown to an employee grid:

```java
public class EmployeeApplicationConfig {

    public Application<Object, EmployeeField, EmployeeRepository> createApplication(
            Map<EmployeeRepository, VortexCrudDataStore<EmployeeField, ?>> dataStores) {

        // 1. Configure department dropdown
        DataStoreDropdownConfig<Department, DepartmentField, DepartmentRepository> deptDropdownConfig =
            DataStoreDropdownConfig.<Department, DepartmentField, DepartmentRepository>builder()
                .dataStoreKey(DepartmentRepository.DEPARTMENTS)
                .filterField(DepartmentField.IS_ACTIVE)
                .filterValue(true)
                .labelProvider(dept -> dept.getName() + " (" + dept.getCode() + ")")
                .placeholder("Select department...")
                .label("Department Filter")
                .limit(100)
                .build();

        // 2. Create menu action factory
        MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> deptFilterFactory =
            new DataStoreDropdownMenuActionFactory<>(deptDropdownConfig, dataStores);

        // 3. Configure grid item renderer
        GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
            GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
                .titleField(EmployeeField.FULL_NAME)
                .descriptionField(EmployeeField.EMAIL)
                .imageField(EmployeeField.PHOTO)
                .build();

        // 4. Create the grid route with menu actions at the route level
        GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
            GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
                .dataStoreKey(EmployeeRepository.EMPLOYEES)
                .title("employees.grid.title")
                .configuration(gridConfig)
                .iconFactory(() -> VaadinIcon.USERS.create())
                .menuActionFactories(List.of(deptFilterFactory))
                .build();

        // 5. Build application with routes
        return Application.<Object, EmployeeField, EmployeeRepository>builder()
            .name("Employee Management")
            .dataStores(dataStoreConfigs)
            .routes(new LinkedHashMap<>() {{
                put("employees", employeeRoute);
            }})
            .build();
    }
}
```

## Application-Level Default Menu Actions

You can configure default menu actions that apply to all routes at the Application level:

```java
public Application<Object, EmployeeField, EmployeeRepository> createApplication(
        Map<EmployeeRepository, VortexCrudDataStore<EmployeeField, ?>> dataStores) {

    // 1. Create a default filter that will appear on all routes
    DataStoreDropdownConfig<Status, StatusField, StatusRepository> statusConfig =
        DataStoreDropdownConfig.<Status, StatusField, StatusRepository>builder()
            .dataStoreKey(StatusRepository.STATUSES)
            .labelProvider(status -> status.getName())
            .placeholder("Filter by status...")
            .build();

    MenuActionComponentFactory<Object, EmployeeField, EmployeeRepository> defaultStatusFilter =
        new DataStoreDropdownMenuActionFactory<>(statusConfig, dataStores);

    // 2. Create a default export button for all routes
    MenuActionComponentFactory<Object, EmployeeField, EmployeeRepository> defaultExportButton =
        () -> {
            Button exportBtn = new Button("Export", VaadinIcon.DOWNLOAD.create());
            exportBtn.addClickListener(e -> {
                // Global export logic
            });
            return exportBtn;
        };

    // 3. Build application with default menu actions
    return Application.<Object, EmployeeField, EmployeeRepository>builder()
        .name("Employee Management")
        .dataStores(dataStoreConfigs)
        .defaultMenuActionFactories(List.of(defaultStatusFilter, defaultExportButton))
        .routes(routesMap)
        .build();
}
```

### Combining Application and Route-Level Menu Actions

Both application-level and route-level menu actions will be rendered together:

```java
// Application-level: Export button (appears on all routes)
MenuActionComponentFactory<Object, EmployeeField, EmployeeRepository> defaultExportButton = ...;

Application<Object, EmployeeField, EmployeeRepository> app =
    Application.<Object, EmployeeField, EmployeeRepository>builder()
        .defaultMenuActionFactories(List.of(defaultExportButton))
        .routes(...)
        .build();

// Route-level: Department filter (appears only on employee route)
GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
    GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
        .menuActionFactories(List.of(deptFilterFactory))
        .build();

// Result: Employee route will show BOTH the export button AND the department filter
```

## Multiple Menu Actions

You can add multiple menu action components to a single route:

```java
// Department filter dropdown
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> deptFilter =
    new DataStoreDropdownMenuActionFactory<>(deptDropdownConfig, dataStores);

// Status filter dropdown
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> statusFilter =
    new DataStoreDropdownMenuActionFactory<>(statusDropdownConfig, dataStores);

// Add both to the GridRoute
GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
    GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
        .dataStoreKey(EmployeeRepository.EMPLOYEES)
        .title("employees.title")
        .configuration(gridConfig)
        .menuActionFactories(List.of(deptFilter, statusFilter))
        .build();
```

## Custom Menu Action Components

You can also create custom menu action factories for other types of components:

```java
// Custom button factory
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> customButtonFactory =
    () -> {
        Button exportButton = new Button("Export", VaadinIcon.DOWNLOAD.create());
        exportButton.addClickListener(e -> {
            // Handle export logic
        });
        return exportButton;
    };

// Add custom factory alongside dropdown to the GridRoute
GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
    GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
        .dataStoreKey(EmployeeRepository.EMPLOYEES)
        .title("employees.title")
        .configuration(gridConfig)
        .menuActionFactories(List.of(deptFilterFactory, customButtonFactory))
        .build();
```

## Notes

1. **Filter Behavior**: When both `filterField` and `filterValue` are set, the component uses `getRecordsFromTableWhereColumnEquals()`. If they're null, it uses `getRecordsFromTable()`.

2. **Type Safety**: The dropdown uses the same generic types (`ModelClass`, `FieldType`, `RepositoryType`) as the rest of the VortexCRUD framework for type safety.

3. **Data Store Lookup**: The `DataStoreDropdownMenuActionFactory` requires access to the data stores map to look up the appropriate data store at runtime.

4. **Refresh**: Call `dropdown.refresh()` to reload data if the underlying data changes.

5. **Route-Level Configuration**: Menu actions are configured at the Route level (GridRoute, ListRoute) rather than at the Configuration level, keeping the core configuration interfaces stable and allowing flexibility in menu customization per route.

## API Reference

### Core Classes

- **Location**: `com.github.appreciated.vortex_crud.core.ui.components`
  - `DataStoreDropdown.java` - Main dropdown component
  - `DataStoreDropdownConfig.java` - Configuration class

- **Location**: `com.github.appreciated.vortex_crud.core.ui.factories.menu`
  - `MenuActionComponentFactory.java` - Factory interface
  - `DataStoreDropdownMenuActionFactory.java` - Dropdown factory implementation

### Extended Configuration Classes

- **Location**: `com.github.appreciated.vortex_crud.core.config.model`
  - `Application.java` - Extended with `defaultMenuActionFactories` field for application-wide menu actions

### Extended Route Classes

- **Location**: `com.github.appreciated.vortex_crud.core.config.model`
  - `GridRoute.java` - Extended with `menuActionFactories` field for route-specific menu actions
  - `ListRoute.java` - Extended with `menuActionFactories` field for route-specific menu actions
  - `FormRoute.java` - Extended with `menuActionFactories` field for route-specific menu actions
  - `KanbanRoute.java` - Extended with `menuActionFactories` field for route-specific menu actions
  - `SubmenuRoute.java` - Extended with `menuActionFactories` field for route-specific menu actions
