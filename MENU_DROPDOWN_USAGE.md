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

## Integration with Routes

### Adding to GridItemRendererConfiguration

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

// Configure grid with menu action
GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
    GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
        .titleField(EmployeeField.NAME)
        .descriptionField(EmployeeField.EMAIL)
        .menuActionFactories(List.of(menuActionFactory))
        .build();

// Create grid route with the configuration
GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
    GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
        .dataStoreKey(EmployeeRepository.EMPLOYEES)
        .title("employees.title")
        .configuration(gridConfig)
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

        // 3. Configure grid with menu actions
        GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
            GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
                .titleField(EmployeeField.FULL_NAME)
                .descriptionField(EmployeeField.EMAIL)
                .imageField(EmployeeField.PHOTO)
                .menuActionFactories(List.of(deptFilterFactory))
                .build();

        // 4. Create the grid route
        GridRoute<Employee, EmployeeField, EmployeeRepository> employeeRoute =
            GridRoute.<Employee, EmployeeField, EmployeeRepository>builder()
                .dataStoreKey(EmployeeRepository.EMPLOYEES)
                .title("employees.grid.title")
                .configuration(gridConfig)
                .iconFactory(() -> VaadinIcon.USERS.create())
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

## Multiple Menu Actions

You can add multiple menu action components to a single route:

```java
// Department filter dropdown
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> deptFilter =
    new DataStoreDropdownMenuActionFactory<>(deptDropdownConfig, dataStores);

// Status filter dropdown
MenuActionComponentFactory<Employee, EmployeeField, EmployeeRepository> statusFilter =
    new DataStoreDropdownMenuActionFactory<>(statusDropdownConfig, dataStores);

// Add both to configuration
GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
    GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
        .titleField(EmployeeField.FULL_NAME)
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

// Add custom factory alongside dropdown
GridItemRendererConfiguration<Employee, EmployeeField, EmployeeRepository> gridConfig =
    GridItemRendererConfiguration.<Employee, EmployeeField, EmployeeRepository>builder()
        .titleField(EmployeeField.FULL_NAME)
        .menuActionFactories(List.of(deptFilterFactory, customButtonFactory))
        .build();
```

## Notes

1. **Filter Behavior**: When both `filterField` and `filterValue` are set, the component uses `getRecordsFromTableWhereColumnEquals()`. If they're null, it uses `getRecordsFromTable()`.

2. **Type Safety**: The dropdown uses the same generic types (`ModelClass`, `FieldType`, `RepositoryType`) as the rest of the VortexCRUD framework for type safety.

3. **Data Store Lookup**: The `DataStoreDropdownMenuActionFactory` requires access to the data stores map to look up the appropriate data store at runtime.

4. **Refresh**: Call `dropdown.refresh()` to reload data if the underlying data changes.

5. **Configuration Field**: The `menuActionFactories` field is added to specific configuration classes like `GridItemRendererConfiguration`, allowing each route type to optionally support menu actions without affecting the base interface.

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
  - `GridItemRendererConfiguration.java` - Extended with `menuActionFactories` field for menu action support
