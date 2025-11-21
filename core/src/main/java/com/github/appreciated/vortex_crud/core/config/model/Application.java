package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Application<ModelClass, FieldType, RepositoryType> {

    private String applicationName;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores;

    private LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

    /**
     * Default menu actions that will be applied to all routes.
     * These can be overridden or supplemented by route-specific menu actions.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> defaultMenuActions;

    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    /**
     * Optional notification panel configuration.
     * When provided, a notification bell icon will be displayed in the application header.
     */
    private NotificationPanelConfiguration<FieldType, RepositoryType> notificationPanelConfiguration;
}