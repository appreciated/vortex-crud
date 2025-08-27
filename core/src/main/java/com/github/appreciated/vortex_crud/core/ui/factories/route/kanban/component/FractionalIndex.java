package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FractionalIndex {
    private static final BigDecimal BASE = new BigDecimal("65536"); // 2^16
    private static final int SCALE = 16;

    /**
     * Generate a position key between two positions.
     * If prev is null, generates a position before next.
     * If next is null, generates a position after prev.
     * If both are null, returns the base position.
     */
    public static String generateKeyBetween(String prev, String next) {
        if (prev == null && next == null) {
            return BASE.toString();
        }

        BigDecimal prevNum = prev == null ? BigDecimal.ZERO : new BigDecimal(prev);
        BigDecimal nextNum = next == null ? prevNum.add(BASE) : new BigDecimal(next);
        
        BigDecimal mid = prevNum.add(nextNum)
                .divide(BigDecimal.valueOf(2), SCALE, RoundingMode.HALF_UP);
        
        return mid.toString();
    }

    /**
     * Generate multiple position keys between two positions
     */
    public static String[] generateKeysBetween(String prev, String next, int count) {
        String[] keys = new String[count];
        BigDecimal prevNum = prev == null ? BigDecimal.ZERO : new BigDecimal(prev);
        BigDecimal nextNum = next == null ? prevNum.add(BASE) : new BigDecimal(next);
        
        BigDecimal step = nextNum.subtract(prevNum)
                .divide(BigDecimal.valueOf(count + 1), SCALE, RoundingMode.HALF_UP);
        
        for (int i = 0; i < count; i++) {
            prevNum = prevNum.add(step);
            keys[i] = prevNum.toString();
        }
        
        return keys;
    }

    /**
     * Generate an initial set of position keys
     */
    public static String[] generateInitialKeys(int count) {
        String[] keys = new String[count];
        BigDecimal step = BASE.divide(BigDecimal.valueOf(count + 1), SCALE, RoundingMode.HALF_UP);
        BigDecimal current = step;
        
        for (int i = 0; i < count; i++) {
            keys[i] = current.toString();
            current = current.add(step);
        }
        
        return keys;
    }
}