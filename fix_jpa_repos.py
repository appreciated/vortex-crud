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
    repo_name = f"{prefix}Repository"

    file_path = os.path.join(base_dir, sub, f"{repo_name}.java")
    if os.path.exists(file_path):
        with open(file_path, 'r') as f: content = f.read()

        # The import is inside the interface definition or misplaced due to replace logic in previous script.
        # Current content:
        # @Repository
        # import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.text_field.JpaTextFieldEntity;
        # public interface JpaTextFieldRepository ...

        # I need to move the import to the top, after package declaration.

        if "import " in content and "public interface" in content:
            lines = content.split('\n')
            package_line_index = -1
            import_line_index = -1
            interface_line_index = -1

            for i, line in enumerate(lines):
                if line.startswith("package "):
                    package_line_index = i
                if line.strip().startswith("import ") and "Entity" in line:
                    import_line_index = i
                if "public interface" in line:
                    interface_line_index = i

            if import_line_index > interface_line_index or (import_line_index > 0 and lines[import_line_index-1].strip() == "@Repository"):
                # It is misplaced.
                import_line = lines[import_line_index]
                lines.pop(import_line_index)

                # Insert after package line
                lines.insert(package_line_index + 1, "")
                lines.insert(package_line_index + 2, import_line)

                with open(file_path, 'w') as f: f.write('\n'.join(lines))
                print(f"Fixed {repo_name}")
