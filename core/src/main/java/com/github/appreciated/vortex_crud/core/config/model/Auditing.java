package com.github.appreciated.vortex_crud.core.config.model;

import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record Auditing(
    boolean enabled,
    List<AuditingAction> actions
) {
}