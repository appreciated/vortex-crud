Custom Repository Support - Document Management Example

This directory demonstrates vortex-crud's ability to work with custom data stores.

While the rest of this application uses JPA for database storage, this document
section uses the FileSystemDataStore to read and manage actual text files from
this local directory.

Key points:
- JPA entities (Task, User, Project) are stored in SQLite database
- Documents are stored as plain text files in this directory
- Both use the same VortexCrudDataStore interface
- Both appear seamlessly in the same UI

This shows how you can mix different storage backends in one application!