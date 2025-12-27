import os

base_dir = "jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui/field_validation"

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
    prefix = f"Jpa{name}"
    config_name = f"{prefix}VortexCrudConfiguration"

    file_path = os.path.join(base_dir, sub, f"{config_name}.java")
    if os.path.exists(file_path):
        with open(file_path, 'r') as f: content = f.read()

        # Check if JpaFieldValidationEnum is imported.
        # It is used in `LinkedHashMap<JpaFieldValidationEnum, String> enumOptions`.
        # The static imports import constants, but not the Class itself for generic type usage.

        if "import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;" not in content:
            # Add it
            content = content.replace("import java.util.Map;",
                                      "import java.util.Map;\nimport com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.JpaFieldValidationEnum;")

            with open(file_path, 'w') as f: f.write(content)
            print(f"Fixed {config_name}")
