# AGENTS Instructions

## Core Principles

⚠️ **CRITICAL**: This is vortex-crud, NOT vanilla Spring Boot!

### DO NOT:
- Create custom repository methods
- Create `@Service` classes for data access
- Use `@Transactional` for CRUD operations
- Create custom `UserDetailsService` implementations

### DO:
- Use `VortexCrudDataStore` directly for ALL data access
- Follow existing patterns in `FormRouteFactory` and `FormDialogFactory`
- Inject `VortexCrudDataStoreFactoryRegistry` to get DataStores
- Use `ReflectionService` for field access

---

## Implementing Custom Views (e.g., Registration, Custom Forms)

When you need to create a custom view that saves data (like SignUpView), follow this pattern:

### Required Dependencies
```java
@Autowired VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService
@Autowired FormCreator<ModelClass, FieldType, RepositoryType> formCreator
@Autowired VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry
@Autowired ReflectionService<FieldType> reflectionService
```

### Implementation Pattern
```java
// 1. Get DataStore (NOT a repository!)
VortexCrudDataStore<FieldType, Object> dataStore =
    dataStoreFactoryRegistry.getDataStore(repositoryKey);

// 2. Create new instance
Object entity = dataStore.newInstance();

// 3. Create Binder for validation
Binder<Object> binder = new Binder<>(Object.class);
binder.setBean(entity);

// 4. Use FormCreator to build the form
formCreator.bindAndAddToLayout(
    repositoryKey,
    formRouteRenderer,
    fields,           // List of InternalFormElement
    entity,
    routeFactory,
    dataStoreConfig,
    binder,          // NOT null!
    formLayout
);

// 5. Save with validation
Button saveButton = new Button("Save", event -> {
    try {
        binder.writeBean(entity);
        // Modify entity if needed (e.g., hash password)
        dataStore.insertRecord(entity);
        // Show success, navigate, etc.
    } catch (ValidationException e) {
        // Handle validation errors
    }
});
```

### Example: Password Hashing
```java
// In SignUpView - hash password BEFORE saving
if (config instanceof LocalIdentityAndAccessManagement<...> localConfig) {
    FieldType passwordField = localConfig.getPassword().getField();
    Object passwordValue = reflectionService.getValue(entity, passwordField);
    if (passwordValue != null) {
        String hashedPassword = passwordEncoder.encode(passwordValue.toString());
        reflectionService.setValue(entity, passwordField, hashedPassword);
    }
}
dataStore.insertRecord(entity);
```

---

## Security Configuration

### Making Routes Public
When adding public views (login, registration), update SecurityConfig:

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**", "/sign-up", "/sign-up/**").permitAll())
            .with(VaadinSecurityConfigurer.vaadin(), ...)
            .build();
}
```

### Password Encoding
Always provide a PasswordEncoder bean in SecurityConfig:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## Common Patterns

### Getting Values from Entities
```java
// Use ReflectionService, NOT direct field access
Object value = reflectionService.getValue(entity, fieldName);
reflectionService.setValue(entity, fieldName, newValue);
```

### Checking if Entity is New
```java
// Inject VortexCrudDataStoreUtilStrategy
if (dataStoreUtil.isNew(entity)) {
    dataStore.insertRecord(entity);
} else {
    dataStore.updateRecordById(entity);
}
```

### Building Field Lists
```java
// For custom views, combine multiple field sources
List<InternalFormElement<...>> allFields = new ArrayList<>();
allFields.add(usernameField);
allFields.add(passwordField);
allFields.addAll(config.getSignUpFields());
```

---

## What Would Have Actually Helped

### The Real Issue:
I approached this like a typical Spring Boot project because there was nothing screaming "DON'T DO THAT" when I started looking at the security module.

### Why I Got It Wrong:
- Saw UserRepository exists → thought "I need to add methods to it"
- Saw Spring Security needed → thought "I need a UserDetailsService class"
- Didn't read FormRouteFactory until you told me to
- The existing code doesn't have examples of this pattern in the security context

### Bottom Line:
The framework works great, but the "pit of failure" for Spring Boot developers is wide open. The architecture is actually quite elegant once I understood it - but I had to learn by doing it wrong first!

---

## Task Completion Checklist

When completing a feature task:

1. ✅ Implement core functionality using VortexCrudDataStore (NOT custom repos/services)
2. ✅ Update SecurityConfig if adding public routes
3. ✅ Add/update configurations in examples/jpa-sqlite-example AND examples/jooq-sqlite-example
4. ✅ Test that the feature works in both examples
5. ✅ Update AGENTS.md if you learned something valuable