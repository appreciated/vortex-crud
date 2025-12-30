package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Application<ModelClass, FieldType, RepositoryType> implements I18nKeyProvider {

    private String applicationName;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<String, RouteRenderer<?, ?, ?>> routes;

    /**
     * Default menu actions that will be applied to all routes.
     * These can be overridden or supplemented by route-specific menu actions.
     */
    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> defaultMenuActions;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    /**
     * Optional notification panel configuration.
     * When provided, a notification bell icon will be displayed in the application header.
     */
    private NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> notificationPanelConfiguration;

    @Override
    public Collection<String> getI18nKeys() {
        List<String> keys = new ArrayList<>();
        if (applicationName != null) keys.add(applicationName);
        if (selects != null) keys.addAll(selects.getI18nKeys());
        if (notificationPanelConfiguration != null) keys.addAll(notificationPanelConfiguration.getI18nKeys());
        if (defaultMenuActions != null) defaultMenuActions.forEach(a -> keys.addAll(a.getI18nKeys()));
        if (menuActions != null) menuActions.forEach(a -> keys.addAll(a.getI18nKeys()));
        if (routes != null) routes.values().forEach(r -> keys.addAll(r.getI18nKeys()));
        return keys;
    }
}
