package com.github.appreciated.flow_cms.repository;

import com.github.appreciated.flow_cms.entity.UserCollectionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollectionPermissionRepository extends
        JpaRepository<UserCollectionPermission, Long> {
}
