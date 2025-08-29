package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FractionalIndexTest {

    @Test
    void generatesBasePositionWhenBothNull() {
        assertEquals(0, FractionalIndex.generateKeyBetween(null, null));
    }

    @Test
    void generatesBeforeNextWhenPrevNull() {
        int next = 100;
        int result = FractionalIndex.generateKeyBetween(null, next);
        assertTrue(result < next, "Result should be before next when prev is null");
    }

    @Test
    void generatesAfterPrevWhenNextNull() {
        int prev = 100;
        int result = FractionalIndex.generateKeyBetween(prev, null);
        assertTrue(result > prev, "Result should be after prev when next is null");
    }

    @Test
    void generatesBetweenPrevAndNext() {
        int prev = 100;
        int next = 200;
        int result = FractionalIndex.generateKeyBetween(prev, next);
        assertTrue(result > prev && result < next, "Result should be between prev and next");
    }
}
