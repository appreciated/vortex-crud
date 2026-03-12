package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import com.github.appreciated.vortex_crud.demo.devplatform.enums.RepositoryVisibility;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.Repository;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.RepositoryCollaborator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
public class GitAuthorizationService {

    private final DSLContext dsl;

    public GitAuthorizationService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public boolean canWrite(int userId, String repoSlug) {
        // Check if owner
        boolean isOwner = dsl.fetchExists(
                dsl.selectOne()
                        .from(Repository.REPOSITORY)
                        .where(Repository.REPOSITORY.SLUG.eq(repoSlug))
                        .and(Repository.REPOSITORY.OWNER_ID.eq(userId))
        );

        if (isOwner) {
            return true;
        }

        // Check if collaborator with write permission
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(RepositoryCollaborator.REPOSITORY_COLLABORATOR)
                        .join(Repository.REPOSITORY).on(RepositoryCollaborator.REPOSITORY_COLLABORATOR.REPOSITORY_ID.eq(Repository.REPOSITORY.ID))
                        .where(Repository.REPOSITORY.SLUG.eq(repoSlug))
                        .and(RepositoryCollaborator.REPOSITORY_COLLABORATOR.USER_ID.eq(userId))
                        .and(RepositoryCollaborator.REPOSITORY_COLLABORATOR.PERMISSION.in("write", "admin"))
        );
    }

    public boolean canRead(int userId, String repoSlug) {
        // 1. Fetch Repository Visibility and Owner
        var record = dsl.select(Repository.REPOSITORY.VISIBILITY, Repository.REPOSITORY.OWNER_ID, Repository.REPOSITORY.ID)
                .from(Repository.REPOSITORY)
                .where(Repository.REPOSITORY.SLUG.eq(repoSlug))
                .fetchOne();

        if (record == null) {
            return false; // Repo does not exist
        }

        RepositoryVisibility visibility = record.get(Repository.REPOSITORY.VISIBILITY);
        Integer ownerId = record.get(Repository.REPOSITORY.OWNER_ID);
        Integer repoId = record.get(Repository.REPOSITORY.ID);

        // Public or Internal (assuming authenticated user implies internal access)
        if (visibility == RepositoryVisibility.PUBLIC || visibility == RepositoryVisibility.INTERNAL) {
            return true;
        }

        // Private: Check if owner
        if (ownerId != null && ownerId.equals(userId)) {
            return true;
        }

        // Private: Check if collaborator
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(RepositoryCollaborator.REPOSITORY_COLLABORATOR)
                        .where(RepositoryCollaborator.REPOSITORY_COLLABORATOR.REPOSITORY_ID.eq(repoId))
                        .and(RepositoryCollaborator.REPOSITORY_COLLABORATOR.USER_ID.eq(userId))
        );
    }
}
