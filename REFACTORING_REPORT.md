# Refactoring Report

This report identifies areas in the codebase that would benefit from refactoring to improve maintainability, reduce technical debt, and ensure robustness.

## 1. Unused Code
**File:** `core/src/main/java/com/github/appreciated/vortex_crud/core/ui/factories/form/elements/fields/VortexCrudFieldFactory.java` and implementations.
- **Issue:** The method `getValidDatabaseTypesForExpectedType()` is defined in the interface and implemented in over 20 classes (e.g., `TextFieldFactory`, `IntegerFieldFactory`) but appears to be completely unused in the codebase.
- **Recommendation:** Remove this method from the interface and all implementing classes to reduce noise and maintenance burden.

## 2. Code Duplication
### Test Utilities
**Files:** `JpaImageEntity.java` and `JpaImageRepository.java`
- **Issue:** These classes are duplicated across multiple test packages in the `jpa` module:
  - `jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/card/`
  - `jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/form_slide/`
  - `jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/grid/`
  - `jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/i18n/`
- **Recommendation:** Move these common test entities to a shared package (e.g., `com.github.appreciated.vortex_crud.test.jpa.model`) and import them in the tests.

### Confusing Test Naming
**Files:** `JpaSelectFieldTest.java` and `JooqSelectFieldTest.java`
- **Issue:** In both `jpa` and `jooq` modules, there are two classes named `JpaSelectFieldTest` (and `JooqSelectFieldTest`) in different packages (`field_validation` vs `select_field`). They extend different base classes (`AbstractSelectFieldTest` vs `AbstractSelectFieldRouteTest`).
- **Recommendation:** Rename them to reflect their purpose, e.g., `JpaSelectFieldValidationTest` and `JpaSelectFieldRouteTest`.

## 3. Fragility & Robustness
### JooqDataStore
**File:** `jooq/src/main/java/com/github/appreciated/vortex_crud/jooq/service/JooqDataStore.java`
- **Issue:** The `getRecordById` and `deleteRecordById` methods use `DSL.field("id")` to query by ID. This assumes the primary key column is named exactly "id". If a table uses a different PK name, this will fail.
- **Issue:** Unchecked casts: `((Field<Object>) filterField).eq(filterValue)`.
- **Recommendation:**
  - Use the jOOQ `Table` metadata to resolve the primary key field dynamically instead of hardcoding "id".
  - Refactor to avoid unchecked casts, perhaps by enforcing stricter types on `VortexCrudDataStore` or using jOOQ's type-safe methods more effectively.

### ReflectionService
**File:** `core/src/main/java/com/github/appreciated/vortex_crud/core/entity/reflection/ReflectionService.java`
- **Issue:** Contains manual implementation of snake_case to CamelCase conversion (`toCamelCase`, `determineMethodName`). This is wheel-reinventing and potentially fragile.
- **Recommendation:** Evaluate if standard libraries (like Spring's `BeanUtils` or `PropertyAccessor`) can be used to handle property access more robustly.

## 4. Complexity
### JpaFieldService
**File:** `jpa/src/main/java/com/github/appreciated/vortex_crud/jpa/service/datastore/JpaFieldService.java`
- **Issue:** The `getFieldsForDataStore` method is a massive chain of `.or(() -> ...)` calls, making it hard to read and maintain.
- **Recommendation:** Refactor the field mapping logic into a strategy pattern or a map-based lookup to decompose the monolithic method.

### JpaRepositoryDataStore
**File:** `jpa/src/main/java/com/github/appreciated/vortex_crud/jpa/service/config/JpaRepositoryDataStore.java`
- **Issue:** The `insertRecord` method contains complex reflection logic to handle updates vs inserts and relationship management. `convertToFieldType` is also very long.
- **Recommendation:** Break down these methods into smaller helper methods or delegate conversion logic to a dedicated converter service.

## 5. Technical Debt
**File:** `security/core/src/main/java/com/github/appreciated/vortex_crud/security/core/view/LoginView.java`
- **Issue:** Contains `//TODO Remove before release`.
- **Recommendation:** Verify if this TODO is actionable and address it.

## 6. Cleanup
**File:** `VortexCrudFieldFactory`
- **Issue:** As mentioned in #1, `getValidDatabaseTypesForExpectedType` is unused.
