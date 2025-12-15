package com.github.appreciated.vortex_crud.security.core.strategy;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinTableRoleResolutionStrategyTest {

    @Mock
    private VortexCrudDataStore<String, Object> joinDataStore;
    @Mock
    private ReflectionService<String> reflectionService;

    private JoinTableRoleResolutionStrategy<String, Object> strategy;

    private final String USER_REF_FIELD = "user_id";
    private final String TARGET_REF_FIELD = "target_id";
    private final String ROLE_FIELD = "role";
    private final String USER_ID_FIELD = "id";
    private final String TARGET_ID_FIELD = "id";

    @BeforeEach
    void setUp() {
        strategy = new JoinTableRoleResolutionStrategy<>(
                joinDataStore,
                USER_REF_FIELD,
                TARGET_REF_FIELD,
                ROLE_FIELD,
                USER_ID_FIELD,
                TARGET_ID_FIELD
        );
    }

    @Test
    void resolveRoles_shouldReturnRoles_whenMatchFound() {
        Object user = new Object();
        Object target = new Object();
        Object membership1 = new Object();
        Object membership2 = new Object();

        when(reflectionService.getValue(user, USER_ID_FIELD)).thenReturn("user1");
        when(reflectionService.getValue(target, TARGET_ID_FIELD)).thenReturn("target1");

        when(joinDataStore.getRecordsFromTableWhereColumnEquals(USER_REF_FIELD, "user1", 0, 1000))
                .thenReturn(Arrays.asList(membership1, membership2));

        when(reflectionService.getValue(membership1, TARGET_REF_FIELD)).thenReturn("target1");
        when(reflectionService.getValue(membership1, ROLE_FIELD)).thenReturn("admin");

        when(reflectionService.getValue(membership2, TARGET_REF_FIELD)).thenReturn("target2"); // Different target

        Collection<? extends GrantedAuthority> roles = strategy.resolveRoles(reflectionService, user, target);

        assertEquals(1, roles.size());
        assertEquals("admin", roles.iterator().next().getAuthority());
    }

    @Test
    void resolveRoles_shouldReturnEmpty_whenNoMatch() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, USER_ID_FIELD)).thenReturn("user1");
        when(reflectionService.getValue(target, TARGET_ID_FIELD)).thenReturn("target1");

        when(joinDataStore.getRecordsFromTableWhereColumnEquals(USER_REF_FIELD, "user1", 0, 1000))
                .thenReturn(Collections.emptyList());

        Collection<? extends GrantedAuthority> roles = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(roles.isEmpty());
    }
}
