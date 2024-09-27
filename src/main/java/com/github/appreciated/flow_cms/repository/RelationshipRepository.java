package com.github.appreciated.flow_cms.repository;

import com.github.appreciated.flow_cms.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends
        JpaRepository<Relationship, Long> {
}
