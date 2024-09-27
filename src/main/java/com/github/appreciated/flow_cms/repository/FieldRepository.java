package com.github.appreciated.flow_cms.repository;

import com.github.appreciated.flow_cms.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
}
