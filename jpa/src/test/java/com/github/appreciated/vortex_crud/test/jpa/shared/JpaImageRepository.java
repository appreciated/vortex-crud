package com.github.appreciated.vortex_crud.test.jpa.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaImageRepository extends JpaRepository<JpaImageEntity, Integer> {
}
