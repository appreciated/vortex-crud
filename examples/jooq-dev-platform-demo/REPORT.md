# Report: jooq-dev-platform-demo missing features and unfinished implementations

## 1. i18n Key Mismatches

There is a systematic mismatch between the i18n keys used in `DevPlatformConfiguration.java` and those defined in `dev_i18n_en.properties` for Pull Requests.

-   **Code**: Uses underscore separator (e.g., `route.pull_requests.title`).
-   **Properties**: Uses hyphen separator (e.g., `route.pull-requests.title`).
-   **Impact**: All labels in the Pull Request views (list and form) will likely display as missing key placeholders (e.g., `!route.pull_requests.title!`).

## 2. Unfinished Implementations

### RepositoryDetailView - Star Button
The "Star" button in the `RepositoryDetailView` (lines 118-121) is purely visual.
-   **Current State**: `Button starButton = new Button("Star", new Icon(VaadinIcon.STAR));` with no click listener.
-   **Expected Behavior**: It should toggle the star status for the current user and update the star count, similar to how the `SingleEntityRouteAction` is implemented in the grid view configuration in `DevPlatformConfiguration`.

## 3. Missing Basic Features / UX Issues

### RepositoryDetailView - Navigation
The `RepositoryDetailView` acts as the landing page for a repository but fails to provide navigation to its sub-resources.
-   **Current State**: Displays README, metadata, and "Edit" button.
-   **Missing**: There are no tabs or links to view Issues, Pull Requests, Wiki, Commits, or Branches.
-   **Workaround**: Users must click "Edit" to see Wiki, Commits, and Branches (as they are configured as child collections in the form). Issues and PRs are top-level routes and not filtered by repository when accessed from the main menu, making context switching difficult.

### DashboardView - Read-Only Grids
The `DashboardView` displays "Assigned to me" issues and "My Pull Requests" but they are not interactive.
-   **Current State**: Uses basic `Grid` setup with text columns.
-   **Missing**: `addItemClickListener` or component renderers to link the rows to their respective detail/edit views (`issues/{id}` or `pull-requests/{id}`).

### RepositoryDetailView - Real-time Stats
The stats (Stars, Forks) in `RepositoryDetailView` are static text elements initialized from the record. They do not update if the data changes (e.g., if the user stars the repo via the (currently broken) button, the count won't update without a page reload or manual UI update logic).

## 4. Minor Inconsistencies

-   **Search Route**: A `SearchRoute` is configured but lacks specific configuration on *what* to search. Depending on the default behavior of `SearchRoute` (which is generic), this might result in a non-functional search experience.
