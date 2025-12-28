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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JoinTableRoleResolutionStrategyTest {

    @Mock
    private VortexCrudDataStore<String, Object> joinDataStore;
    @Mock
    private ReflectionService<String> reflectionService;

    private JoinTableRoleResolutionStrategy<String, Object> strategy;

    private final String USER_REF_FIELD = "userRef";
    private final String TARGET_REF_FIELD = "targetRef";
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
    void resolveRoles_ShouldReturnRoles_WhenMembershipExists() {
        Object user = new Object();
        Object target = new Object();
        Object joinRecord1 = new Object();
        Object joinRecord2 = new Object();

        String userId = "user1";
        String targetId = "target1";

        // IDs retrieval
        when(reflectionService.getValue(user, USER_ID_FIELD)).thenReturn(userId);
        when(reflectionService.getValue(target, TARGET_ID_FIELD)).thenReturn(targetId);

        // Fetch memberships for user
        when(joinDataStore.getRecordsFromTableWhereColumnEquals(USER_REF_FIELD, userId, 0, 1000))
                .thenReturn(Arrays.asList(joinRecord1, joinRecord2));

        // Membership 1 matches target
        when(reflectionService.getValue(joinRecord1, TARGET_REF_FIELD)).thenReturn(targetId);
        when(reflectionService.getValue(joinRecord1, ROLE_FIELD)).thenReturn("ADMIN");

        // Membership 2 does not match target
        when(reflectionService.getValue(joinRecord2, TARGET_REF_FIELD)).thenReturn("otherTarget");

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenNoMembershipMatchesTarget() {
        Object user = new Object();
        Object target = new Object();
        Object joinRecord = new Object();

        String userId = "user1";
        String targetId = "target1";

        when(reflectionService.getValue(user, USER_ID_FIELD)).thenReturn(userId);
        when(reflectionService.getValue(target, TARGET_ID_FIELD)).thenReturn(targetId);

        when(joinDataStore.getRecordsFromTableWhereColumnEquals(USER_REF_FIELD, userId, 0, 1000))
                .thenReturn(Collections.singletonList(joinRecord));

        when(reflectionService.getValue(joinRecord, TARGET_REF_FIELD)).thenReturn("otherTarget");

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenIdsAreNull() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, USER_ID_FIELD)).thenReturn(null);
        when(reflectionService.getValue(target, TARGET_ID_FIELD)).thenReturn("target1");

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenUserOrTargetIsNull() {
        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, null, null);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void resolveRoles_ShouldReturnEmpty_WhenExceptionOccurs() {
        Object user = new Object();
        Object target = new Object();

        when(reflectionService.getValue(user, USER_ID_FIELD)).thenThrow(new RuntimeException("Error"));

        Collection<? extends GrantedAuthority> authorities = strategy.resolveRoles(reflectionService, user, target);

        assertTrue(authorities.isEmpty());
    }
}
