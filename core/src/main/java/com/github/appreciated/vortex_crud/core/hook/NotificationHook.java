package com.github.appreciated.vortex_crud.core.hook;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHook;
import com.github.appreciated.vortex_crud.core.service.NotificationService;
import lombok.Builder;

import java.util.function.Function;

@Builder
public class NotificationHook<ModelClass> implements DataStoreHook<ModelClass> {

    private final NotificationService<?, ?, ?> notificationService;
    private final Function<ModelClass, String> messageProvider;
    private final Function<ModelClass, Object> targetUserProvider;
    private final Function<ModelClass, String> actorNameProvider;
    private final Function<ModelClass, String> actorAvatarUrlProvider;

    @Override
    public void execute(ModelClass context) {
        if (notificationService == null) {
             return;
        }

        String message = messageProvider != null ? messageProvider.apply(context) : null;

        if (message == null) {
            return;
        }

        Object targetUser = targetUserProvider != null ? targetUserProvider.apply(context) : null;
        String actorName = actorNameProvider != null ? actorNameProvider.apply(context) : null;
        String actorAvatarUrl = actorAvatarUrlProvider != null ? actorAvatarUrlProvider.apply(context) : null;

        notificationService.createNotification(message, targetUser, actorName, actorAvatarUrl);
    }
}
