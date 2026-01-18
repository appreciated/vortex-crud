package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.LinkedHashMap;
import java.util.Map;


public class JooqApplication extends Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static Application.ApplicationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return Application.builder();
    }

    public static JooqApplicationBuilder jooqBuilder() {
        return new JooqApplicationBuilder();
    }

    public static class JooqApplicationBuilder {
        private final Application.ApplicationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder = Application.builder();
        private final Map<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        public JooqApplicationBuilder route(String path, RouteRenderer<?, ?, ?> route) {
            routes.put(path, route);
            return this;
        }

        public JooqApplicationBuilder routes(Map<String, RouteRenderer<?, ?, ?>> routes) {
            this.routes.putAll(routes);
            return this;
        }

        public JooqApplicationBuilder applicationName(String applicationName) {
            builder.applicationName(applicationName);
            return this;
        }

        public JooqApplicationBuilder i18nBundlePrefix(String i18nBundlePrefix) {
            builder.i18nBundlePrefix(i18nBundlePrefix);
            return this;
        }

        public JooqApplicationBuilder identityAndAccessManagement(IdentityAndAccessManagement<TableRecord<?>, TableField<?, ?>, TableImpl<?>> identityAndAccessManagement) {
            builder.identityAndAccessManagement(identityAndAccessManagement);
            return this;
        }

        public JooqApplicationBuilder selects(Selects selects) {
            builder.selects(selects);
            return this;
        }

        public JooqApplicationBuilder versioning(Versioning<TableImpl<?>> versioning) {
            builder.versioning(versioning);
            return this;
        }

        public JooqApplicationBuilder auditing(Auditing auditing) {
            builder.auditing(auditing);
            return this;
        }

        public JooqApplicationBuilder notificationPanelConfiguration(NotificationPanelConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> notificationPanelConfiguration) {
            builder.notificationPanelConfiguration(notificationPanelConfiguration);
            return this;
        }

        public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> build() {
            builder.routes(routes);
            return builder.build();
        }
    }
}
