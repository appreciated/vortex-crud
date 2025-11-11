# Custom Repository Support Guide

This guide explains how to add custom repository support to vortex-crud applications.

## What is Custom Repository Support?

Custom repository support allows you to use **any storage backend** with vortex-crud, not just JPA or JOOQ databases. You can:

- Store data in the local filesystem
- Use MongoDB, Redis, or other NoSQL databases
- Connect to external REST APIs
- Access cloud storage (S3, Azure Blob Storage)
- Implement in-memory caching
- Mix multiple storage backends in one application

## How It Works

vortex-crud abstracts data access through the `VortexCrudDataStore` interface. By implementing this interface, you can plug in any storage backend.

```
┌─────────────────────────────────────┐
│     VortexCRUD Framework (UI)       │
└──────────────┬──────────────────────┘
               │ Uses VortexCrudDataStore interface
               │
      ┌────────┴────────┬───────────┬──────────┐
      │                 │           │          │
┌─────▼──────┐  ┌──────▼─────┐  ┌─▼────┐  ┌──▼─────┐
│ JPA (SQL)  │  │  FileSystem │  │ JOOQ │  │ Custom │
└────────────┘  └────────────┘  └──────┘  └────────┘
```

## Quick Start

### 1. Add the filesystem module dependency

```xml
<dependency>
    <groupId>com.github.appreciated.vortex-crud</groupId>
    <artifactId>filesystem</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Create a configuration bean

```java
@Configuration
public class CustomStorageConfiguration {

    @Bean
    public FileDocumentDataStore documentStore() {
        return new FileDocumentDataStore(
            Paths.get("documents"),
            new DataStoreHooks<>()
        );
    }
}
```

### 3. That's it!

The FileDocumentDataStore will automatically:
- Read existing files from the `documents/` directory
- Make them available in the vortex-crud UI
- Allow creating, editing, and deleting documents
- Work alongside your JPA repositories

## Example: JPA + FileSystem Together

The `jpa-sqlite-example` demonstrates mixing storage backends:

**JPA Entities (Database):**
- Tasks → SQLite database
- Users → SQLite database
- Projects → SQLite database

**FileSystem Entities (Local Files):**
- Documents → `documents/` directory as `.txt` files

Both appear in the same UI with the same operations available!

## File Structure

When you use FileSystemDataStore, files are organized like this:

```
your-app/
├── data/                    # FileSystemDataStore (JSON entities)
│   ├── Product/
│   │   ├── 1.json
│   │   ├── 2.json
│   │   └── 3.json
│   └── Customer/
│       ├── 1.json
│       └── 2.json
│
└── documents/               # FileDocumentDataStore (actual files)
    ├── README.txt
    ├── quick-start.txt
    └── user-guide.txt
```

## Implementing Your Own Custom DataStore

### Step 1: Implement VortexCrudDataStore

```java
public class MongoDataStore<T> implements VortexCrudDataStore<String, T> {

    private final MongoCollection<T> collection;
    private final Class<T> entityClass;

    public MongoDataStore(MongoDatabase database, Class<T> entityClass) {
        this.collection = database.getCollection(
            entityClass.getSimpleName(),
            entityClass
        );
        this.entityClass = entityClass;
    }

    @Override
    public Object insertRecord(T entity) {
        // Insert into MongoDB
        collection.insertOne(entity);
        return getId(entity);
    }

    @Override
    public List<T> getRecordsFromTable(int offset, int limit) {
        // Query MongoDB with pagination
        return collection.find()
            .skip(offset)
            .limit(limit)
            .into(new ArrayList<>());
    }

    @Override
    public T getRecordById(Object id) {
        // Find by ID in MongoDB
        return collection.find(eq("_id", id)).first();
    }

    // ... implement other methods
}
```

### Step 2: Create a configuration bean

```java
@Configuration
public class MongoConfiguration {

    @Bean
    public MongoDataStore<Product> productDataStore(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("myapp");
        return new MongoDataStore<>(database, Product.class);
    }
}
```

### Step 3: Use it in your application

It works exactly like JPA repositories - no additional configuration needed!

## Key Methods to Implement

The `VortexCrudDataStore` interface requires these methods:

| Method | Purpose |
|--------|---------|
| `insertRecord()` | Create new record or update existing |
| `getRecordsFromTable()` | List records with pagination |
| `getRecordsFromTableWhereColumnEquals()` | Filter by exact match |
| `getRecordsFromTableWhereColumnLike()` | Filter by pattern/contains |
| `getRecordById()` | Find single record by ID |
| `updateRecordById()` | Update existing record |
| `deleteRecordById()` | Delete record by ID |
| `count()` | Total number of records |
| `countWhereColumnLike()` | Count matching filter |
| `getModelClass()` | Return entity class |
| `newInstance()` | Create new entity instance |

## Lifecycle Hooks (Optional)

Hooks are completely optional. Add them only when you need custom logic at lifecycle events:

```java
// Simple usage - no hooks
FileSystemDataStore<Product> simpleStore =
    new FileSystemDataStore<>(Product.class, Paths.get("data"));

// Advanced usage - with hooks
DataStoreHooks<Product> hooks = new DataStoreHooks<>();

hooks.addBeforeCreate(product -> {
    // Validate, set defaults
    product.setCreatedAt(LocalDateTime.now());
    product.setId(UUID.randomUUID());
});

hooks.addAfterCreate(product -> {
    // Send notifications, update search indexes
    searchService.index(product);
    notificationService.send("New product: " + product.getName());
});

hooks.addBeforeUpdate(product -> {
    product.setUpdatedAt(LocalDateTime.now());
});

hooks.addBeforeDelete(product -> {
    // Check dependencies
    if (hasActiveOrders(product)) {
        throw new IllegalStateException("Cannot delete product with active orders");
    }
});

FileSystemDataStore<Product> storeWithHooks =
    new FileSystemDataStore<>(Product.class, Paths.get("data"), hooks);
```

## Real-World Examples

### Example 1: Elasticsearch for Search

```java
public class ElasticsearchDataStore<T> implements VortexCrudDataStore<String, T> {
    private final RestHighLevelClient client;
    private final String indexName;

    @Override
    public List<T> getRecordsFromTableWhereColumnLike(String field, Object value, int offset, int limit) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery(field, value));
        sourceBuilder.from(offset);
        sourceBuilder.size(limit);

        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        return parseResponse(response);
    }
}
```

### Example 2: Redis Cache

```java
public class RedisCacheDataStore<T> implements VortexCrudDataStore<String, T> {
    private final RedisTemplate<String, T> redisTemplate;

    @Override
    public Object insertRecord(T entity) {
        String key = getKey(entity);
        redisTemplate.opsForValue().set(key, entity, Duration.ofHours(24));
        return getId(entity);
    }
}
```

### Example 3: REST API Proxy

```java
public class RestApiDataStore<T> implements VortexCrudDataStore<String, T> {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    @Override
    public List<T> getRecordsFromTable(int offset, int limit) {
        String url = String.format("%s?offset=%d&limit=%d", apiUrl, offset, limit);
        ResponseEntity<List<T>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<T>>() {}
        );
        return response.getBody();
    }
}
```

## Benefits

### Development

- **Rapid Prototyping**: Start without setting up databases
- **Easy Testing**: Use in-memory or file-based storage for tests
- **Debugging**: Human-readable JSON files for inspection

### Production

- **Flexibility**: Choose the best storage for each entity type
- **Migration**: Gradually move between storage backends
- **Integration**: Connect to any external system
- **Optimization**: Custom implementations for specific needs

### Architecture

- **Clean Separation**: Business logic independent of storage
- **Testability**: Easy to mock data stores
- **Maintainability**: Swap storage without changing application code

## Testing Custom DataStores

```java
@Test
void testCustomDataStore() {
    // Create test data store
    FileSystemDataStore<Product> store = new FileSystemDataStore<>(
        Product.class,
        testDirectory,
        new DataStoreHooks<>()
    );

    // Test insert
    Product product = new Product("Test", "Description", 99.99);
    Object id = store.insertRecord(product);
    assertNotNull(id);

    // Test retrieve
    Product retrieved = store.getRecordById(id);
    assertEquals("Test", retrieved.getName());

    // Test list
    List<Product> products = store.getRecordsFromTable(0, 10);
    assertTrue(products.size() > 0);

    // Test filter
    List<Product> filtered = store.getRecordsFromTableWhereColumnLike("name", "Test", 0, 10);
    assertEquals(1, filtered.size());

    // Test delete
    store.deleteRecordById(id);
    assertNull(store.getRecordById(id));
}
```

## Best Practices

1. **Error Handling**: Wrap storage exceptions in RuntimeException
2. **Thread Safety**: Make data stores thread-safe
3. **Resource Management**: Close connections properly
4. **Caching**: Implement caching for frequently accessed data
5. **Logging**: Log important operations for debugging
6. **Validation**: Validate data before storage operations
7. **Transactions**: Handle transactions if your backend supports them

## Performance Tips

- **Pagination**: Always implement efficient pagination
- **Indexing**: Create indexes for frequently queried fields
- **Caching**: Cache frequently accessed records
- **Batch Operations**: Implement batch inserts/updates when possible
- **Connection Pooling**: Reuse connections to external systems
- **Lazy Loading**: Load related entities only when needed

## Migration Strategy

To migrate from one storage backend to another:

1. **Parallel Writing**: Write to both old and new storage
2. **Gradual Reading**: Read from new, fallback to old
3. **Background Migration**: Migrate data in batches
4. **Verification**: Compare results between storages
5. **Cutover**: Switch entirely to new storage
6. **Cleanup**: Remove old storage

## Troubleshooting

### Data not appearing in UI

- Verify `getModelClass()` returns correct class
- Check `count()` returns > 0
- Ensure `getRecordsFromTable()` returns data

### IDs not working

- Implement `getId()` helper correctly
- Field must be named "id" (case-insensitive)
- ID should be unique

### Filters not working

- Implement filter methods correctly
- Handle null values
- Test case-sensitivity

### Performance issues

- Add caching
- Optimize queries
- Implement pagination properly
- Profile your storage backend

## Support

For questions and issues:
- Check the `filesystem` module code for reference implementation
- Look at `jpa-sqlite-example` for integration example
- Review the example-documents for additional guidance

## Conclusion

Custom repository support makes vortex-crud incredibly flexible. You can integrate any data source by implementing a single interface, giving you:

- Complete control over data storage
- Freedom to choose the best tool for each job
- Easy migration paths
- Simple testing strategies
- Clean, maintainable code

Start with the FileSystemDataStore example and adapt it to your needs!
