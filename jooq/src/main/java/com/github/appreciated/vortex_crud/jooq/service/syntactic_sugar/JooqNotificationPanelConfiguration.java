package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.jooq.service.JooqNotificationService;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqNotificationPanelConfiguration extends NotificationPanelConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static NotificationPanelConfiguration.NotificationPanelConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return NotificationPanelConfiguration.builder();
    }

    public static NotificationPanelConfiguration.NotificationPanelConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> from(JooqNotificationService<?, ?> service) {
        return service.configurePanel();
    }
}
