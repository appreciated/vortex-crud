package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("datetime-test")
public class JpaDatetimeFieldValidationConfiguration extends BaseJpaFieldValidationVortexCrudConfiguration {

    public JpaDatetimeFieldValidationConfiguration(JpaDatetimeFieldValidationRepository repository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        super(repository, fieldService, annotationRegistryService);
    }
}
