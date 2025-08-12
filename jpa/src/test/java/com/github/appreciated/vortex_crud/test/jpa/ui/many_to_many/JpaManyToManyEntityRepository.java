package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaManyToManyEntityRepository extends JpaRepository<JpaManyToManyEntity, Long> {}
