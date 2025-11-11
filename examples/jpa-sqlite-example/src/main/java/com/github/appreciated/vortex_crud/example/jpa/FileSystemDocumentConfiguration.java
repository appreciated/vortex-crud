package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.example.jpa.custom.FileDocumentDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
 *
 * Notice how simple this is - just one line to create the data store!
 * No hooks or complex configuration required unless you need it.
 */
@Configuration
public class FileSystemDocumentConfiguration {

    /**
     * Create a FileDocumentDataStore bean that manages documents in the local filesystem.
     * This is an example of a custom repository implementation with minimal boilerplate.
     */
    @Bean
    public FileDocumentDataStore fileDocumentDataStore() {
        // That's it! Just point to your documents directory.
        // Hooks are optional - add them only if you need custom logic.
        return new FileDocumentDataStore(Paths.get("documents"));
    }
}
