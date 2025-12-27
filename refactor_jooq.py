import os

base_dir = "jooq/src/test/java/com/github/appreciated/vortex_crud/test/jooq/ui/field_validation"
# Note: jooq tests use shared SQL or jooq/src/test/resources/field_validation_test.sql?
# Actually jooq uses @Sql("field_validation_test.sql").
# I should put the SQLs in jooq/src/test/resources/com/github/appreciated/...
# Wait, jooq usually has resources in `jooq/src/test/resources` root or relative.
# I'll check where the existing one is.
# Assuming standard maven layout for relative loading: `jooq/src/test/resources/com/github/.../field_validation/text_field/`
# But jooq test files often just use classpath root or relative.
# Let's put them in `jooq/src/test/resources/` root if that's easier, OR
# put them in package structure.
# JPA worked with package structure because I used `scripts="file.sql"` which is relative.
# So I will replicate package structure in resources for jooq too.

resource_base = "jooq/src/test/resources/com/github/appreciated/vortex_crud/test/jooq/ui/field_validation"

subpackages = {
    "text_field": "TextField",
    "email_field": "EmailField",
    "number_field": "NumberField",
    "date_field": "DateField",
    "datetime_field": "DateTimeField",
    "select_field": "SelectField",
    "checkbox_field": "CheckboxField",
    "image_field": "ImageField",
    "lifecycle": "Lifecycle"
}

template_config_path = os.path.join(base_dir, "JooqFieldValidationVortexCrudConfiguration.java")
template_app_path = os.path.join(base_dir, "JooqFieldValidationTestApplication.java")

with open(template_config_path, 'r') as f: template_config = f.read()
with open(template_app_path, 'r') as f: template_app = f.read()

# SQL Template from JPA side (since they share schema) OR jooq side if existing?
# I can use the same logic as JPA: create `text_field_validation_test.sql`.
# But schema must match what jooq expects.
# I already added tables to changelog.sql.
# Does @Sql need to CREATE tables? Or just INSERT?
# If using SQLite in memory and Liquibase runs on startup (via Spring Boot or plugin), tables exist.
# But often integration tests disable liquibase or use @Sql to init schema.
# The user said: "Running JOOQ integration tests against an in-memory SQLite database requires explicit CREATE TABLE statements in the @Sql script...".
# So YES, I need to create tables in the SQL script.
# I can use the SQL content I put in changelog.sql (CREATE + INSERT).
# I'll construct a template SQL.

sql_create_template = """
CREATE TABLE validation_test
(
    id             INTEGER      NOT NULL,
    required_field VARCHAR(255) NOT NULL,
    email_field    VARCHAR(255),
    numeric_field  DOUBLE PRECISION CHECK (numeric_field > 0),
    date_field     DATE,
    datetime_field TIMESTAMP,
    enum_field     VARCHAR(20),
    checkbox_field BOOLEAN,
    image_field    VARCHAR(255),
    PRIMARY KEY (id)
);
INSERT INTO validation_test (id, required_field, email_field, numeric_field, date_field, datetime_field, enum_field, checkbox_field, image_field)
VALUES (1, 'Test Value', 'test@example.com', 42, '2023-01-01 00:00:00.000', '2023-01-01 10:15:00.000', 'OPTION1', 1, './red.png'),
       (2, 'Another Value', 'user@domain.org', 100, '2023-02-15 00:00:00.000', '2023-02-15 11:20:00.000', 'OPTION2', 0, './green.png');
"""

for sub, name in subpackages.items():
    prefix = f"Jooq{name}"
    sub_dir = os.path.join(base_dir, sub)
    res_sub_dir = os.path.join(resource_base, sub)
    os.makedirs(res_sub_dir, exist_ok=True)

    table_name = f"{sub}_validation_test"
    # Convert table name to Upper Case constant format for JOOQ Tables reference if needed,
    # but in Config we use `Tables.TEXT_FIELD_VALIDATION_TEST`.
    table_constant = f"{sub}_validation_test".upper()

    # 1. Config
    config_name = f"{prefix}VortexCrudConfiguration"
    new_config = template_config.replace("package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;",
                                         f"package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.{sub};")
    new_config = new_config.replace("JooqFieldValidationVortexCrudConfiguration", config_name)

    # Replace Table constant
    # Original: import static com.github.appreciated.vortex_crud.jooq.models.Tables.VALIDATION_TEST;
    # Replace VALIDATION_TEST with table_constant
    new_config = new_config.replace("VALIDATION_TEST", table_constant)

    # Import Enum if needed (it is in parent package)
    if "import com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum;" not in new_config:
        new_config = new_config.replace("import org.springframework.context.annotation.Configuration;",
                                        "import org.springframework.context.annotation.Configuration;\nimport com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.JooqFieldValidationTestEnum;")

    with open(os.path.join(sub_dir, f"{config_name}.java"), 'w') as f: f.write(new_config)

    # 2. App
    app_name = f"{prefix}TestApplication"
    new_app = template_app.replace("package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;",
                                   f"package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.{sub};")
    new_app = new_app.replace("JooqFieldValidationTestApplication", app_name)

    with open(os.path.join(sub_dir, f"{app_name}.java"), 'w') as f: f.write(new_app)

    # 3. SQL
    sql_file_name = f"{sub}_validation_test.sql"
    new_sql = sql_create_template.replace("validation_test", table_name)
    with open(os.path.join(res_sub_dir, sql_file_name), 'w') as f: f.write(new_sql)

    # 4. Update Test Class
    if sub == "lifecycle":
         test_class_name = "JooqFieldValidationLifecycleTest"
    elif sub == "select_field":
         test_class_name = "JooqSelectFieldValidationTest"
    else:
         test_class_name = f"{prefix}Test"

    test_path = os.path.join(sub_dir, f"{test_class_name}.java")
    if os.path.exists(test_path):
        with open(test_path, 'r') as f: test_content = f.read()

        # Package
        test_content = test_content.replace("package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation;",
                                            f"package com.github.appreciated.vortex_crud.test.jooq.ui.field_validation.{sub};")

        # Update @Sql
        # Original: @Sql("field_validation_test.sql") or @Sql(scripts = ...)
        # Regex replacement might be safer but string replace usually works if consistent style.
        test_content = test_content.replace('"field_validation_test.sql"', f'"{sql_file_name}"')

        with open(test_path, 'w') as f: f.write(test_content)
