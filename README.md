# FlowCms

**FlowCms** is a flexible, customizable, and dynamic content management system (CMS) built using **Vaadin Flow** and **Spring Boot**. It allows users to configure and manage collections, fields, and views dynamically with ease, providing full control over data structures and UI components. FlowCms is ideal for developers and businesses looking for a customizable solution to manage content and data models without having to adjust the database schema manually.

## Features

- **Dynamic Collections**: Create and manage collections (data types) dynamically without schema changes.
- **Customizable Views**: Configure how collections are displayed using customizable views and layouts.
- **Role-based Permissions**: Assign user roles and permissions to control access to collections and views.
- **Vaadin Flow-based UI**: A modern and responsive UI built with Vaadin Flow for fast and intuitive use.
- **REST API**: Easily extend and integrate FlowCms with other applications using the provided REST API.
- **JSON Schema for Flexibility**: Use JSON-based configurations to define fields, relationships, and views dynamically.

## Getting Started

These instructions will guide you through setting up and running FlowCms locally on your machine.

### Prerequisites

- **Java 17** or higher
- **Maven** (for building the project)
- **PostgreSQL** or **MySQL** (for the database)
- Any modern browser (for accessing the Vaadin-based UI)

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/flowcms.git
   cd flowcms
   ```

2. **Configure the Database:**

   - Create a new PostgreSQL or MySQL database for FlowCms.
   - Update the database connection properties in `src/main/resources/application.properties` with your database credentials:

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/flowcms
     spring.datasource.username=your_db_username
     spring.datasource.password=your_db_password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build and Run the Application:**

   Run the following command to build the project with Maven and start the Spring Boot application:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application:**

   Once the application is running, you can access the CMS at:

   ```
   http://localhost:8080
   ```

   Log in using the default admin credentials:

   ```
   Username: admin
   Password: admin123
   ```

### Running with Docker (Optional)

If you prefer using Docker, you can run FlowCms with the following steps:

1. **Build the Docker Image:**

   ```bash
   docker build -t flowcms .
   ```

2. **Run the Docker Container:**

   ```bash
   docker run -p 8080:8080 flowcms
   ```

## Usage

### Creating a New Collection

1. Navigate to the **Collections** section in the admin panel.
2. Click on **Create New Collection**.
3. Define the fields and their types using the intuitive JSON-based configuration editor.
4. Save the collection and start adding records.

### Customizing Views

FlowCms allows you to customize the way collections are displayed in the UI. You can define new views for each collection, specifying the layout, field types, and actions available.

1. Go to the **View Configuration** section.
2. Select a collection and configure its view by specifying how each field should be rendered (text, textarea, dropdown, etc.).
3. Save the configuration, and the view will be rendered accordingly in the application.

### API Endpoints

FlowCms provides a REST API to manage collections, records, and views programmatically.

- **Get All Collections**: `GET /api/collections`
- **Get Collection Data**: `GET /api/collections/{collectionId}/data`
- **Get View Config**: `GET /api/collections/{collectionId}/view-config`

For more detailed API documentation, please refer to the `API.md` file in the repository.

## Built With

- [Vaadin Flow](https://vaadin.com/flow) - For building the modern UI.
- [Spring Boot](https://spring.io/projects/spring-boot) - For the backend and REST API.
- [PostgreSQL](https://www.postgresql.org/) / [MySQL](https://www.mysql.com/) - For the database layer.

## Contributing

We welcome contributions to FlowCms! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Open a Pull Request.

Please ensure your code adheres to the project's coding standards and includes appropriate tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any questions or support, please open an issue on GitHub or reach out to the maintainers.

---

Feel free to adjust the content based on specific features, customizations, or additional setup steps that your project may require!
