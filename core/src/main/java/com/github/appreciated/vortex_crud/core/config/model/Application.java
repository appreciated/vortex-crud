package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.config.visitor.ConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.config.visitor.Visitable;
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
public class Application<ModelClass, FieldType, RepositoryType> implements Visitable {

    @I18nKey
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
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);

        if (routes != null) {
            routes.values().forEach(route -> {
                if (route instanceof Visitable) {
                    ((Visitable) route).accept(visitor);
                } else {
                    visitor.visit(route);
                }
            });
        }

        if (identityAndAccessManagement != null) {
            visitor.visit(identityAndAccessManagement);
        }
        if (selects != null) {
            visitor.visit(selects);
        }
        if (versioning != null) {
            visitor.visit(versioning);
        }
        if (auditing != null) {
            visitor.visit(auditing);
        }
        if (defaultMenuActions != null) {
            defaultMenuActions.forEach(visitor::visit);
        }
        if (menuActions != null) {
            menuActions.forEach(visitor::visit);
        }
        if (notificationPanelConfiguration != null) {
            visitor.visit(notificationPanelConfiguration);
        }
    }
}
