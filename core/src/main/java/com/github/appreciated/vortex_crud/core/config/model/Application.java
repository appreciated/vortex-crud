package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Application<ModelClass, FieldType, RepositoryType> implements ValidatableConfiguration {

    @I18nKey
    private String applicationName;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<String, RouteRenderer<?, ?, ?>> routes;

    /**
     * Optional notification panel configuration.
     * When provided, a notification bell icon will be displayed in the application header.
     */
    private NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> notificationPanelConfiguration;
}
