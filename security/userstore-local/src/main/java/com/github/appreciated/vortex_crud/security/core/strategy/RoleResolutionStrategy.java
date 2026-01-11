package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface RoleResolutionStrategy<FieldType> {
    Collection<? extends GrantedAuthority> resolveRoles(ReflectionService<? super FieldType> reflectionService, Object userEntity, Object targetEntity);
}
