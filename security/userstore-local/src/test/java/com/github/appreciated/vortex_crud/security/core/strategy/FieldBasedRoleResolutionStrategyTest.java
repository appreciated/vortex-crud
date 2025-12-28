package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldBasedRoleResolutionStrategyTest {

    @Mock
    private ReflectionService<String> reflectionService;

    private FieldBasedRoleResolutionStrategy<String> strategy;
    private final String ROLES_FIELD = "roles";

    @BeforeEach
    void setUp() {
        strategy = new FieldBasedRoleResolutionStrategy<>(ROLES_FIELD);
    }

    @Test
    void resolveRoles_ShouldReturnRoles_WhenUserHasRoles() {
        Object user = new Object();
        Object target = new Object();

        VortexCrudRoleProvider role1 = mock(VortexCrudRoleProvider.class);
        when(role1.getRole()).thenReturn("ROLE_ADMIN");

        VortexCrudRoleProvider role2 = mock(VortexCrudRoleProvider.class);
        when(role2.getRole()).thenReturn("ROLE_USER");

        when(reflectionService.getValue(user, ROLES_FIELD)).thenReturn(Arrays.asList(role1, role2));

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenUserHasNoRoles() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, ROLES_FIELD)).thenReturn(Collections.emptyList());

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenRolesFieldIsNull() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, ROLES_FIELD)).thenReturn(null);

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenValueIsNotList() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, ROLES_FIELD)).thenReturn("NotAList");

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenUserIsNull() {
        Object target = new Object();
        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, null, target);
        assertTrue(authorities.isEmpty());
    }
}
