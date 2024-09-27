package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.UserCollectionPermission;
import com.github.appreciated.flow_cms.repository.UserCollectionPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCollectionPermissionService {
    @Autowired
    private UserCollectionPermissionRepository userCollectionPermissionRepository;

    public List<UserCollectionPermission> findAll() {
        return userCollectionPermissionRepository.findAll();
    }

    public Optional<UserCollectionPermission> findById(Long id) {
        return userCollectionPermissionRepository.findById(id);
    }

    public UserCollectionPermission save(UserCollectionPermission userCollectionPermission) {
        return userCollectionPermissionRepository.save(userCollectionPermission);
    }

    public void deleteById(Long id) {
        userCollectionPermissionRepository.deleteById(id);
    }
}
