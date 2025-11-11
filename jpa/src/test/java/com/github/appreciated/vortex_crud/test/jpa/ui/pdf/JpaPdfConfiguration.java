package com.github.appreciated.vortex_crud.test.jpa.ui.pdf;

import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.config.VortexCrudConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JpaPdfConfiguration implements VortexCrudConfigurationProvider {

    @Autowired
    private JpaPdfRepository pdfRepository;

    @Override
    public Map<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> get() {
        return Map.of(
                pdfRepository, new JpaRepositoryDataStore<>(JpaPdfEntity.class)
        );
    }
}
