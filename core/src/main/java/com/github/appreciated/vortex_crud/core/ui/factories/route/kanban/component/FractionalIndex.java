package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

public class FractionalIndex {
    private static final int SCALE = 0;

    /**
     * Generate a position key between two positions.
     * If prev is null, generates a position before next.
     * If next is null, generates a position after prev.
     * If both are null, returns the base position.
     */
    public static Integer generateKeyBetween(Integer prev, Integer next) {
        return ((prev == null ? 0 : prev) + (next == null ? 0 : next)) / 2;
    }

}