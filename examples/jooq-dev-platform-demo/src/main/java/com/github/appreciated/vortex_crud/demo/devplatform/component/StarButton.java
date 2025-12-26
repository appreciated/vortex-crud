package com.github.appreciated.vortex_crud.demo.devplatform.component;

import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import java.util.List;
import java.util.Optional;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

public class StarButton extends CustomField<Integer> {
    private final JooqDataStore starStore;
    private final JooqDataStore userStore;
    private final Button button = new Button();
    private Integer repoId;
    private Integer userId;

    public StarButton(JooqDataStore starStore, JooqDataStore userStore) {
        this.starStore = starStore;
        this.userStore = userStore;
        add(button);
        button.addClickListener(e -> toggleStar());
    }

    @Override
    protected Integer generateModelValue() {
        return repoId;
    }

    @Override
    protected void setPresentationValue(Integer repoId) {
        this.repoId = repoId;
        updateState();
    }

    private Integer getUserId() {
        if (userId != null) {
            return userId;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || "anonymousUser".equals(username)) {
            return null;
        }
        // Find user ID
        List<TableRecord<?>> users = userStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
        if (users.isEmpty()) {
            return null;
        }
        userId = (Integer) users.get(0).get(USERS.ID);
        return userId;
    }

    private void updateState() {
        if (repoId == null) {
            button.setVisible(false);
            return;
        }
        button.setVisible(true);

        Integer currentUserId = getUserId();
        if (currentUserId == null) {
            button.setEnabled(false);
            return;
        }
        button.setEnabled(true);

        boolean isStarred = starStore.countWhereFiltersEqual(List.of(
                new RouteFilter<>((TableField<?, ?>) REPOSITORY_STAR.USER_ID, currentUserId),
                new RouteFilter<>((TableField<?, ?>) REPOSITORY_STAR.REPOSITORY_ID, repoId)
        )) > 0;

        if (isStarred) {
            button.setIcon(VaadinIcon.STAR.create());
            button.setText("Unstar");
        } else {
            button.setIcon(VaadinIcon.STAR_O.create());
            button.setText("Star");
        }
    }

    @SuppressWarnings("unchecked")
    private void toggleStar() {
        if (repoId == null) return;

        Integer currentUserId = getUserId();
        if (currentUserId == null) return;

        List<TableRecord<?>> stars = starStore.getRecordsFromTableWhereFiltersEqual(List.of(
                new RouteFilter<>((TableField<?, ?>) REPOSITORY_STAR.USER_ID, currentUserId),
                new RouteFilter<>((TableField<?, ?>) REPOSITORY_STAR.REPOSITORY_ID, repoId)
        ), 0, 1);

        if (!stars.isEmpty()) {
            starStore.deleteRecord((UpdatableRecord) stars.get(0));
        } else {
            TableRecord<?> newStar = starStore.newInstance();
            newStar.set((TableField) REPOSITORY_STAR.USER_ID, currentUserId);
            newStar.set((TableField) REPOSITORY_STAR.REPOSITORY_ID, repoId);
            starStore.insertRecord((UpdatableRecord) newStar);
        }
        updateState();
    }
}
