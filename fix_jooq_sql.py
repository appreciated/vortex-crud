import os

base_dir = "jooq/src/test/resources/com/github/appreciated/vortex_crud/test/jooq/ui/field_validation"

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

for sub, name in subpackages.items():
    table_name = f"{sub}_validation_test"
    sql_file = os.path.join(base_dir, sub, f"{table_name}.sql")

    if os.path.exists(sql_file):
        with open(sql_file, 'r') as f: content = f.read()

        drop_stmt = f"DROP TABLE IF EXISTS {table_name};\n"

        if "DROP TABLE" not in content:
            new_content = drop_stmt + content
            with open(sql_file, 'w') as f: f.write(new_content)
            print(f"Added DROP TABLE to {sql_file}")
