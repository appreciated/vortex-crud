package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import com.github.appreciated.vortex_crud.demo.devplatform.enums.RepositoryVisibility;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.RepositoryCollaboratorRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.RepositoryRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.REPOSITORY;
import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.REPOSITORY_COLLABORATOR;

@Service
public class GitAuthorizationService {

    private final DSLContext dsl;

    public GitAuthorizationService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public boolean canRead(Integer userId, String repoSlug) {
        RepositoryRecord repo = getRepository(repoSlug);
        if (repo == null) {
            return false;
        }

        if (RepositoryVisibility.PUBLIC.equals(repo.getVisibility())) {
            return true;
        }

        if (userId == null) {
            return false;
        }

        if (repo.getOwnerId().equals(userId)) {
            return true;
        }

        return isCollaborator(repo.getId(), userId);
    }

    public boolean canWrite(Integer userId, String repoSlug) {
        if (userId == null) {
            return false;
        }

        RepositoryRecord repo = getRepository(repoSlug);
        if (repo == null) {
            return false;
        }

        if (repo.getOwnerId().equals(userId)) {
            return true;
        }

        return hasWriteAccess(repo.getId(), userId);
    }

    private RepositoryRecord getRepository(String slug) {
        return dsl.selectFrom(REPOSITORY)
                .where(REPOSITORY.SLUG.eq(slug))
                .limit(1)
                .fetchOne();
    }

    private boolean isCollaborator(Integer repoId, Integer userId) {
        return dsl.fetchExists(
            dsl.selectFrom(REPOSITORY_COLLABORATOR)
               .where(REPOSITORY_COLLABORATOR.REPOSITORY_ID.eq(repoId))
               .and(REPOSITORY_COLLABORATOR.USER_ID.eq(userId))
        );
    }

    private boolean hasWriteAccess(Integer repoId, Integer userId) {
         RepositoryCollaboratorRecord collaborator = dsl.selectFrom(REPOSITORY_COLLABORATOR)
                .where(REPOSITORY_COLLABORATOR.REPOSITORY_ID.eq(repoId))
                .and(REPOSITORY_COLLABORATOR.USER_ID.eq(userId))
                .fetchOne();

         if (collaborator == null) return false;

         String perm = collaborator.getPermission();
         return "write".equalsIgnoreCase(perm) || "admin".equalsIgnoreCase(perm);
    }
}
