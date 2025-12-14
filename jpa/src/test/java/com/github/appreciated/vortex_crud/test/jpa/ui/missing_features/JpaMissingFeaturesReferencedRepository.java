package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMissingFeaturesReferencedRepository extends JpaRepository<JpaMissingFeaturesReferencedEntity, Long> {
}
