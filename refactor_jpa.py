import os

base_dir = "jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/field_validation"
resource_base = "jpa/src/test/resources/com/github/appreciated/vortex_crud/test/jpa/ui/field_validation"

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

template_entity_path = os.path.join(base_dir, "JpaFieldValidationEntity.java")
template_repo_path = os.path.join(base_dir, "JpaFieldValidationRepository.java")
template_config_path = os.path.join(base_dir, "JpaFieldValidationVortexCrudConfiguration.java")
template_app_path = os.path.join(base_dir, "JpaFieldValidationTestApplication.java")

with open(template_entity_path, 'r') as f: template_entity = f.read()
with open(template_repo_path, 'r') as f: template_repo = f.read()
with open(template_config_path, 'r') as f: template_config = f.read()
with open(template_app_path, 'r') as f: template_app = f.read()

# SQL Template
sql_template_path = os.path.join(resource_base, "field_validation_test.sql")
with open(sql_template_path, 'r') as f: template_sql = f.read()

for sub, name in subpackages.items():
    prefix = f"Jpa{name}" # e.g. JpaTextField
    sub_dir = os.path.join(base_dir, sub)
    res_sub_dir = os.path.join(resource_base, sub)
    os.makedirs(res_sub_dir, exist_ok=True)

    table_name = f"{sub}_validation_test"

    # 1. Entity
    # Special handling for Lifecycle: usually it's "FieldValidation" entity but here we give it its own.
    # But JpaFieldValidationLifecycleTest likely relies on the structure.
    # We will generate JpaLifecycleEntity for it.

    entity_name = f"{prefix}Entity"
    if sub == "lifecycle":
         # Maybe keep name simpler? JpaLifecycleEntity.
         pass

    new_entity = template_entity.replace("package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;",
                                         f"package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub};")
    new_entity = new_entity.replace("public class JpaFieldValidationEntity", f"public class {entity_name}")
    new_entity = new_entity.replace("JpaFieldValidationEntity", entity_name)
    # Replace table name. Template has "text_field_validation_test" because I modified it.
    new_entity = new_entity.replace('name = "text_field_validation_test"', f'name = "{table_name}"')

    # Import Enum
    if "import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;" not in new_entity:
        new_entity = new_entity.replace("import jakarta.persistence.*;",
                                        "import jakarta.persistence.*;\nimport com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;")

    with open(os.path.join(sub_dir, f"{entity_name}.java"), 'w') as f: f.write(new_entity)

    # 2. Repository
    repo_name = f"{prefix}Repository"
    new_repo = template_repo.replace("package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;",
                                     f"package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub};")
    new_repo = new_repo.replace("JpaFieldValidationRepository", repo_name)
    new_repo = new_repo.replace("JpaFieldValidationEntity", entity_name)
    new_repo = new_repo.replace("public interface", f"import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub}.{entity_name};\npublic interface")

    with open(os.path.join(sub_dir, f"{repo_name}.java"), 'w') as f: f.write(new_repo)

    # 3. Config
    config_name = f"{prefix}VortexCrudConfiguration"
    new_config = template_config.replace("package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;",
                                         f"package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub};")
    new_config = new_config.replace("JpaFieldValidationVortexCrudConfiguration", config_name)
    new_config = new_config.replace("JpaFieldValidationRepository", repo_name)
    new_config = new_config.replace("JpaFieldValidationEntity", entity_name)
    # The config variable names: validationEntityRepository -> textFieldRepository?
    # Not strictly necessary but clean.
    # The template has "private final JpaFieldValidationRepository validationEntityRepository;"
    # I replaced class name.

    with open(os.path.join(sub_dir, f"{config_name}.java"), 'w') as f: f.write(new_config)

    # 4. App
    app_name = f"{prefix}TestApplication"
    new_app = template_app.replace("package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;",
                                   f"package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub};")
    new_app = new_app.replace("JpaFieldValidationTestApplication", app_name)

    with open(os.path.join(sub_dir, f"{app_name}.java"), 'w') as f: f.write(new_app)

    # 5. SQL
    sql_file_name = f"{sub}_validation_test.sql"
    new_sql = template_sql.replace("validation_test", table_name)
    with open(os.path.join(res_sub_dir, sql_file_name), 'w') as f: f.write(new_sql)

    # 6. Update Test Class
    if sub == "lifecycle":
         test_class_name = "JpaFieldValidationLifecycleTest"
    elif sub == "select_field":
         test_class_name = "JpaSelectFieldValidationTest"
    else:
         test_class_name = f"{prefix}Test"

    test_path = os.path.join(sub_dir, f"{test_class_name}.java")
    if os.path.exists(test_path):
        with open(test_path, 'r') as f: test_content = f.read()

        # Package
        test_content = test_content.replace("package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;",
                                            f"package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.{sub};")

        # Update @Sql
        test_content = test_content.replace('scripts = "field_validation_test.sql"', f'scripts = "{sql_file_name}"')

        with open(test_path, 'w') as f: f.write(test_content)
