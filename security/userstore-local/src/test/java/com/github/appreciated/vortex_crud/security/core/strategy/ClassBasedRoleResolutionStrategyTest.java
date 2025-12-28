package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassBasedRoleResolutionStrategyTest {

    @Mock
    private ReflectionService<String> reflectionService;
    @Mock
    private RoleResolutionStrategy<String> strategy1;
    @Mock
    private RoleResolutionStrategy<String> strategy2;

    private ClassBasedRoleResolutionStrategy<String> strategy;

    private static class EntityA {}
    private static class EntityB extends EntityA {}
    private static class EntityC {}

    @BeforeEach
    void setUp() {
        strategy = new ClassBasedRoleResolutionStrategy<>(Map.of(
                EntityA.class, strategy1,
                EntityC.class, strategy2
        ));
    }

    @Test
    void resolveRoles_ShouldDelegate_WhenExactMatchFound() {
        Object user = new Object();
        EntityA target = new EntityA();

        when(strategy1.resolveRoles(reflectionService, user, target)).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_TEST")));

        Collection<? extends GrantedAuthority> result = strategy.resolveRoles(reflectionService, user, target);

        assertEquals(1, result.size());
        assertEquals("ROLE_TEST", result.iterator().next().getAuthority());
        verify(strategy1).resolveRoles(reflectionService, user, target);
        verify(strategy2, never()).resolveRoles(any(), any(), any());
    }

    @Test
    void resolveRoles_ShouldDelegate_WhenSubclassMatchFound() {
        Object user = new Object();
        EntityB target = new EntityB(); // EntityB extends EntityA.

        when(strategy1.resolveRoles(reflectionService, user, target)).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_INHERITED")));

        Collection<? extends GrantedAuthority> result = strategy.resolveRoles(reflectionService, user, target);

        assertEquals(1, result.size());
        assertEquals("ROLE_INHERITED", result.iterator().next().getAuthority());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenNoMatchFound() {
        Object user = new Object();
        Object target = "SomeString"; // Not A or C

        Collection<? extends GrantedAuthority> result = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(result.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenTargetIsNull() {
         Object user = new Object();
         Collection<? extends GrantedAuthority> result = strategy.resolveRoles(reflectionService, user, null);
         assertTrue(result.isEmpty());
    }
}
