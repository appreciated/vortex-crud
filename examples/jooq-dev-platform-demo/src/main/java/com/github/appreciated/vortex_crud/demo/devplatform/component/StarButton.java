package com.github.appreciated.vortex_crud.demo.devplatform.component;

import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

public class StarButton extends CustomField<Integer> {
    private final JooqDataStore starStore;
    private final JooqDataStore userStore;
    private final Button button = new Button();
    private Integer repoId;

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

    private void updateState() {
        if (repoId == null) {
            button.setVisible(false);
            return;
        }
        button.setVisible(true);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || "anonymousUser".equals(username)) {
            button.setEnabled(false);
            return;
        }

        // Find user ID
        List<TableRecord<?>> users = userStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
        if (users.isEmpty()) {
            button.setEnabled(false);
            return;
        }
        Integer userId = (Integer) users.get(0).get(USERS.ID);

        List<TableRecord<?>> stars = starStore.getRecordsFromTableWhereColumnEquals(REPOSITORY_STAR.USER_ID, userId, 0, 1000);
        boolean isStarred = stars.stream().anyMatch(r -> repoId.equals(r.get(REPOSITORY_STAR.REPOSITORY_ID)));

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

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TableRecord<?>> users = userStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
        if (users.isEmpty()) return;
        Integer userId = (Integer) users.get(0).get(USERS.ID);

        List<TableRecord<?>> stars = starStore.getRecordsFromTableWhereColumnEquals(REPOSITORY_STAR.USER_ID, userId, 0, 1000);
        Optional<TableRecord<?>> star = stars.stream().filter(r -> repoId.equals(r.get(REPOSITORY_STAR.REPOSITORY_ID))).findFirst();

        if (star.isPresent()) {
            starStore.deleteRecord((UpdatableRecord) star.get());
        } else {
            TableRecord<?> newStar = starStore.newInstance();
            newStar.set((TableField) REPOSITORY_STAR.USER_ID, userId);
            newStar.set((TableField) REPOSITORY_STAR.REPOSITORY_ID, repoId);
            starStore.insertRecord((UpdatableRecord) newStar);
        }
        updateState();
    }
}
