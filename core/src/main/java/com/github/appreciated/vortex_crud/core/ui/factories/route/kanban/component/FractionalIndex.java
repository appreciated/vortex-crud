package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

public class FractionalIndex {
    private static final int BASE_POSITION = 0;

    /**
     * Generate a position key between two positions.
     * <p>
     * The behaviour follows the documentation of fractional indexes used in the
     * Kanban component:
     * <ul>
     *     <li>If both {@code prev} and {@code next} are {@code null}, the base position is returned.</li>
     *     <li>If {@code prev} is {@code null}, a position before {@code next} is generated.</li>
     *     <li>If {@code next} is {@code null}, a position after {@code prev} is generated.</li>
     *     <li>If both are present, a position strictly between {@code prev} and {@code next} is returned.</li>
     * </ul>
     */
    public static Integer generateKeyBetween(Integer prev, Integer next) {
        if (prev == null && next == null) {
            return BASE_POSITION;
        }
        if (prev == null) {
            return next - 1;
        }
        if (next == null) {
            return prev + 1;
        }
        return prev + (next - prev) / 2;
    }

}