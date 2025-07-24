# Vortex CRUD UI Tests

This module contains UI tests for the Vortex CRUD application. The tests use Selenium WebDriver with WebDriverManager to interact with the browser and verify that the application's views are accessible and functioning correctly.

## Test Structure

The tests are organized using the Page Object Model (POM) pattern, which separates the page structure from the test logic. This makes the tests more maintainable and easier to understand.

### Page Objects

Page objects represent the different views in the application:

- `BasePage`: Abstract base class for all page objects
- Projects views:
  - `ProjectsCardsPage`: Represents the projects-cards view
  - `ProjectsListPage`: Represents the projects-list view
  - `ProjectDetailPage`: Represents the project detail view
- Tasks views:
  - `TasksPage`: Represents the main tasks view
  - `TasksOpenPage`: Represents the open tasks view
  - `TasksDonePage`: Represents the done tasks view
  - `TaskDetailPage`: Represents the task detail view
- Images views:
  - `ImagesGridPage`: Represents the images-grid view
  - `ImagesListPage`: Represents the images-list view
  - `ImageDetailPage`: Represents the image detail view

### Test Classes

The test classes are organized by functionality:

- `ProjectsCardsTest`: Tests for the projects-cards view
- `ProjectDetailTest`: Tests for the project detail view
- `ProjectsListTest`: Tests for the projects-list view
- `TasksTest`: Tests for the main tasks view
- `TasksOpenTest`: Tests for the open tasks view
- `TasksDoneTest`: Tests for the done tasks view
- `ImagesGridTest`: Tests for the images-grid view
- `ImagesListTest`: Tests for the images-list view
- `NavigationTest`: Tests for navigation between different views
- `InteractionTest`: Tests for interactions within views and verifying state changes

## Test Coverage

The tests cover the following functionality:

1. **View Accessibility**: Verifying that all views are accessible and load correctly
2. **Data Display**: Checking that data is displayed correctly in different views
3. **Navigation**: Testing navigation between different views
4. **CRUD Operations**: Testing create, read, update, and delete operations
5. **Form Validation**: Verifying that form validation works correctly
6. **State Changes**: Ensuring that state changes are reflected correctly across different views

## URLs Covered

The tests cover the following URLs:

- `/projects-cards`
- `/projects-cards/{id}`
- `/projects-list`
- `/projects-list/{id}`
- `/tasks`
- `/tasks/open`
- `/tasks/done`
- `/images-grid`
- `/images-grid/{id}`
- `/images-list`
- `/images-list/{id}`

## Running the Tests

To run the tests, you need to have the application running locally on port 8080. The tests assume that the application is accessible at `http://127.0.0.1:8080`.

You can run the tests using Maven:

```bash
mvn test
```

Or you can run individual test classes using your IDE.

## Test Environment

The tests use Chrome in headless mode by default. This can be changed in the `BaseUITest` class if needed.

## Dependencies

The tests use the following dependencies:

- Selenium WebDriver: For browser automation
- WebDriverManager: For managing WebDriver binaries
- JUnit Jupiter: For test execution and assertions
- AssertJ: For fluent assertions

## Best Practices

The tests follow these best practices:

1. **Page Object Model**: Separating page structure from test logic
2. **Fluent API**: Using method chaining for better readability
3. **Explicit Waits**: Waiting for elements to be visible or clickable before interacting with them
4. **Robust Selectors**: Using CSS selectors that are less likely to break with UI changes
5. **Error Handling**: Gracefully handling cases where elements are not found or conditions are not met
6. **Test Independence**: Each test is independent and can run on its own
7. **Clear Assertions**: Using descriptive assertion messages
8. **Documentation**: Each class and method is documented with Javadoc comments

## Extending the Tests

To add new tests:

1. Create a new page object class if needed
2. Create a new test class or add methods to an existing test class
3. Follow the existing patterns for navigation, interaction, and assertions

## Troubleshooting

If the tests fail, check the following:

1. Is the application running and accessible at `http://127.0.0.1:8080`?
2. Have there been changes to the UI that might break the selectors?
3. Is the test environment properly set up with all dependencies?
4. Are there any network issues that might affect the tests?

## Contributing

When contributing to the UI tests, please follow these guidelines:

1. Follow the existing code style and patterns
2. Add appropriate documentation for new classes and methods
3. Ensure that new tests are independent and can run on their own
4. Add appropriate assertions with descriptive messages
5. Handle edge cases and error conditions gracefully