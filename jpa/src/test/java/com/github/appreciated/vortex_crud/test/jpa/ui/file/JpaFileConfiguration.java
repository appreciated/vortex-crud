package com.github.appreciated.vortex_crud.test.jpa.ui.file;

import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.config.VortexCrudConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JpaFileConfiguration implements VortexCrudConfigurationProvider {

    @Autowired
    private JpaFileRepository fileRepository;

    @Override
    public Map<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> get() {
        return Map.of(
                fileRepository, new JpaRepositoryDataStore<>(JpaFileEntity.class)
        );
    }
}
