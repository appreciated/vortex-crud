package com.github.appreciated.vortex_crud.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as containing an internationalization (i18n) key.
 * Fields annotated with this should contain a String value (or be a container of Strings)
 * that corresponds to a key in the resource bundles.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface I18nKey {
}
