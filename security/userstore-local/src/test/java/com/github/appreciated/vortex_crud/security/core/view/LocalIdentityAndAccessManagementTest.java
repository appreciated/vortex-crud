package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalIdentityAndAccessManagementTest {

    @Mock
    private ReflectionService<String> reflectionService;

    private LocalIdentityAndAccessManagement<String, String, String> identityManagement;
    private String rolesField = "roles";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        identityManagement = LocalIdentityAndAccessManagement.<String, String, String>builder()
                .rolesField(rolesField)
                .build();
    }

    @Test
    void testResolveRolesForEntity_WithValidRoles() {
        Object userEntity = new Object();
        VortexCrudRoleProvider roleProvider = mock(VortexCrudRoleProvider.class);
        when(roleProvider.getRole()).thenReturn("admin");

        when(reflectionService.getValue(userEntity, rolesField)).thenReturn(List.of(roleProvider));

        List<SimpleGrantedAuthority> authorities = identityManagement.resolveRolesForEntity(reflectionService, userEntity);

        assertEquals(1, authorities.size());
        assertEquals("admin", authorities.get(0).getAuthority());
    }

    @Test
    void testResolveRolesForEntity_WithNullEntity() {
        List<SimpleGrantedAuthority> authorities = identityManagement.resolveRolesForEntity(reflectionService, null);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testResolveRolesForEntity_WithNullRolesFieldInConfig() {
        LocalIdentityAndAccessManagement<String, String, String> configWithoutRoles = LocalIdentityAndAccessManagement.<String, String, String>builder()
                .rolesField(null)
                .build();

        Object userEntity = new Object();
        List<SimpleGrantedAuthority> authorities = configWithoutRoles.resolveRolesForEntity(reflectionService, userEntity);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testResolveRolesForEntity_WithNullRolesValue() {
        Object userEntity = new Object();
        when(reflectionService.getValue(userEntity, rolesField)).thenReturn(null);

        List<SimpleGrantedAuthority> authorities = identityManagement.resolveRolesForEntity(reflectionService, userEntity);
        assertTrue(authorities.isEmpty());
    }
}
