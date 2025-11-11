# VortexCRUD FileSystem Module

This module provides file system-based storage implementations for vortex-crud, demonstrating how to create custom repository support.

## Overview

The filesystem module shows how to implement the `VortexCrudDataStore` interface for custom storage backends. Instead of using JPA or JOOQ, you can store data in:

- Local file system
- Cloud storage (S3, Azure Blob, etc.)
- MongoDB, Redis, or other NoSQL databases
- External REST APIs
- Any custom data source

## Components

### FileSystemDataStore

A generic file system data store that stores entities as JSON files.

**Features:**
- Stores entities as JSON files in a directory structure
- Auto-generates IDs
- Supports full CRUD operations
- Filtering and pagination
- Type-safe with generics

**Usage:**
```java
// Simple usage - no hooks needed
FileSystemDataStore<Product> productStore = new FileSystemDataStore<>(
    Product.class,
    Paths.get("data")
);

Product product = new Product("Laptop", "Gaming laptop", 1299.99);
Object id = productStore.insertRecord(product);

List<Product> products = productStore.getRecordsFromTable(0, 10);

// Advanced usage - with hooks (optional)
DataStoreHooks<Product> hooks = new DataStoreHooks<>();
hooks.addBeforeCreate(p -> System.out.println("Creating: " + p.getName()));

FileSystemDataStore<Product> productStoreWithHooks = new FileSystemDataStore<>(
    Product.class,
    Paths.get("data"),
    hooks
);
```

### FileDocumentDataStore

A specialized data store for managing actual text files in a directory.

**Features:**
- Maps files to Document entities
- Reads file metadata (size, timestamps)
- Full-text content access
- Create, read, update, delete files through the UI

**Usage:**
```java
// Simple usage - no hooks needed
FileDocumentDataStore documentStore = new FileDocumentDataStore(Paths.get("documents"));

// Access existing files
List<Document> docs = documentStore.getRecordsFromTable(0, 10);

// Create new document
Document doc = new Document("readme.txt", "README", "# Welcome!");
documentStore.insertRecord(doc);
```

### FileSystemDataStoreFactoryRegistry

Registry for managing multiple file system data stores.

**Features:**
- Register multiple entity types
- Single base directory for all entities
- Type-safe data store retrieval
- Easy Spring integration

**Usage:**
```java
@Bean
public FileSystemDataStoreFactoryRegistry fileSystemRegistry() {
    FileSystemDataStoreFactoryRegistry registry =
        new FileSystemDataStoreFactoryRegistry(Paths.get("data"));

    // Register entity types
    registry.registerEntityType(Product.class);
    registry.registerEntityType(Customer.class);
    registry.registerEntityType(Order.class);

    return registry;
}
```

## Integration with VortexCRUD

### Step 1: Add Dependency

```xml
<dependency>
    <groupId>com.github.appreciated.vortex-crud</groupId>
    <artifactId>filesystem</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Step 2: Create Configuration

```java
@Configuration
public class CustomStorageConfiguration {

    @Bean
    public FileDocumentDataStore documentStore() {
        // That's it! Hooks are optional.
        return new FileDocumentDataStore(Paths.get("documents"));
    }
}
```

### Step 3: Use in Routes

The FileSystemDataStore works seamlessly with vortex-crud routes, just like JPA repositories!

See the `jpa-sqlite-example` for a complete working example that combines JPA (for database entities) and FileSystem (for documents) in the same application.

## Example Files

The `example-documents/` directory contains sample text files demonstrating:
- Getting started guide
- Advanced usage patterns
- Real-world use cases
- Implementation examples

## Storage Structure

```
data/
├── Product/
│   ├── 1.json
│   ├── 2.json
│   └── 3.json
├── Customer/
│   ├── 1.json
│   └── 2.json
└── Order/
    └── 1.json

documents/
├── README.txt
├── getting-started.txt
└── user-guide.txt
```

## Custom Repository Pattern

### Implementing VortexCrudDataStore

To create your own custom repository:

1. **Implement the interface:**
   ```java
   public class MyCustomDataStore<T> implements VortexCrudDataStore<String, T> {
       // Implement all methods
   }
   ```

2. **Key methods to implement:**
   - `insertRecord()` - Create/update
   - `getRecordsFromTable()` - List with pagination
   - `getRecordById()` - Find by ID
   - `updateRecordById()` - Update
   - `deleteRecordById()` - Delete
   - `count()` - Count records
   - Filtering methods for search

3. **Register with the framework:**
   ```java
   @Bean
   public MyCustomDataStore<MyEntity> myDataStore() {
       return new MyCustomDataStore<>(myConfig);
   }
   ```

4. **Done!** The framework handles the rest automatically.

## Benefits

### Development
- **Fast prototyping**: No database setup required
- **Easy debugging**: JSON files are human-readable
- **Version control**: Track data changes in git

### Production
- **Flexibility**: Mix different storage backends
- **Migration**: Gradually move from one storage to another
- **Integration**: Connect to any data source
- **Performance**: Optimize for your specific use case

## Use Cases

1. **Document Management**: Store files while keeping metadata queryable
2. **Configuration Storage**: JSON files for app configuration
3. **Audit Logs**: Append-only file storage
4. **Prototyping**: No database needed for MVP
5. **Hybrid Storage**: Mix SQL, NoSQL, and filesystem
6. **Legacy Integration**: Read data from legacy systems
7. **API Proxies**: Cache external API responses locally

## Advanced Features

### Lifecycle Hooks

Add custom logic at any point in the entity lifecycle:

```java
DataStoreHooks<Product> hooks = new DataStoreHooks<>();
hooks.addBeforeCreate(product -> {
    // Validate, set defaults
    product.setCreatedAt(LocalDateTime.now());
});
hooks.addAfterCreate(product -> {
    // Send notifications, update indexes
    notificationService.send("Product created: " + product.getName());
});
```

### Custom Queries

Extend the base implementation to add custom query methods:

```java
public class ExtendedFileSystemDataStore<T> extends FileSystemDataStore<T> {
    public List<T> findByCustomCriteria(CustomCriteria criteria) {
        // Custom implementation
    }
}
```

## Testing

The module includes comprehensive tests demonstrating all features. Run with:

```bash
cd filesystem
mvn test
```

## Contributing

When adding new features:
1. Follow the existing `VortexCrudDataStore` contract
2. Add tests for all public methods
3. Update this README with examples
4. Consider backward compatibility

## License

Same as vortex-crud root project.
