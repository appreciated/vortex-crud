package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.filesystem.entity.Document;
import com.github.appreciated.vortex_crud.filesystem.service.config.FileDocumentDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration for custom filesystem-based document storage.
 *
 * This demonstrates how to add custom repository support to vortex-crud
 * by using a FileSystem-based data store alongside JPA repositories.
 *
 * Key features:
 * - JPA entities (Task, User, Project) stored in SQLite database
 * - Documents stored as plain text files in ./documents directory
 * - Both use the same VortexCrudDataStore abstraction
 * - Both appear seamlessly in the same UI
 */
@Configuration
public class FileSystemDocumentConfiguration {

    /**
     * Create a FileDocumentDataStore bean that manages documents in the local filesystem.
     * This is an example of a custom repository implementation.
     */
    @Bean
    public FileDocumentDataStore fileDocumentDataStore() {
        // Point to the documents directory in the project root
        Path documentsPath = Paths.get("documents");

        // Create hooks for logging (optional)
        DataStoreHooks<Document> hooks = new DataStoreHooks<>();
        hooks.addBeforeCreate(doc ->
            System.out.println("Creating document: " + doc.getFileName())
        );
        hooks.addAfterCreate(doc ->
            System.out.println("Document created with ID: " + doc.getId())
        );

        // Create and return the data store
        FileDocumentDataStore dataStore = new FileDocumentDataStore(documentsPath, hooks);

        System.out.println("========================================");
        System.out.println("FileSystem Document Store initialized!");
        System.out.println("Directory: " + documentsPath.toAbsolutePath());
        System.out.println("Documents count: " + dataStore.count());
        System.out.println("========================================");

        return dataStore;
    }
}
