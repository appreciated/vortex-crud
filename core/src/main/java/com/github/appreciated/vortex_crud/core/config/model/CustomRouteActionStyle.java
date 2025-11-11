package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Defines the visual style of a custom route action button.
 */
public enum CustomRouteActionStyle {
    /**
     * Primary button with prominent styling (e.g., filled background).
     * Use for the most important action.
     */
    PRIMARY,

    /**
     * Secondary button with less prominent styling (e.g., outlined).
     * Use for common but not primary actions.
     */
    SECONDARY,

    /**
     * Tertiary button with minimal styling (e.g., text-only).
     * Use for less important actions.
     */
    TERTIARY,

    /**
     * Error/danger button with red/warning styling.
     * Use for destructive actions like delete, remove, etc.
     */
    ERROR,

    /**
     * Success button with green/success styling.
     * Use for positive actions like approve, confirm, etc.
     */
    SUCCESS
}
