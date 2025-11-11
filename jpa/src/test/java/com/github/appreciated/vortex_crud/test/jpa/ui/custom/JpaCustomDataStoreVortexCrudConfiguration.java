package com.github.appreciated.vortex_crud.test.jpa.ui.custom;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.list_route.ListRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class JpaCustomDataStoreVortexCrudConfiguration implements VortexCrudConfigurationProvider<FileDocumentDataStore, String, FileDocumentDataStore> {

    private final FileDocumentDataStore dataStore;

    public JpaCustomDataStoreVortexCrudConfiguration() throws IOException {
        Path documentsPath = Paths.get("target/test-documents");
        Files.createDirectories(documentsPath);

        // Create a test document
        Files.writeString(documentsPath.resolve("example.txt"), "This is an example document");

        this.dataStore = new FileDocumentDataStore(documentsPath);
    }

    @Bean
    public FileDocumentDataStore fileDocumentDataStore() {
        return dataStore;
    }

    @Override
    public Application<FileDocumentDataStore, String, FileDocumentDataStore> get() {
        return Application.<FileDocumentDataStore, String, FileDocumentDataStore>builder()
                .title("Custom DataStore Test")
                .routes(List.of(
                        ListRoute.<FileDocumentDataStore, String, FileDocumentDataStore>builder()
                                .dataStoreKey(dataStore)
                                .route("documents")
                                .label("Documents")
                                .children(List.of("title", "fileName"))
                                .build()
                ))
                .build();
    }
}
