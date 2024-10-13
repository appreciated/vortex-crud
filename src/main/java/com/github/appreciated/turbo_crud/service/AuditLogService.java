package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.entity.AuditLog;
import com.github.appreciated.turbo_crud.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing audit logs.
 * Provides methods for finding, saving, and deleting audit log entries using the AuditLogRepository.
 */

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    public Optional<AuditLog> findById(Long id) {
        return auditLogRepository.findById(id);
    }

    public AuditLog save(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    public void deleteById(Long id) {
        auditLogRepository.deleteById(id);
    }
}
