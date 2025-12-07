import os

def process_file(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()

    new_lines = []
    skip_next = False

    # Imports to remove
    imports_to_remove = [
        "import com.vaadin.flow.theme.Theme;",
        "import com.vaadin.flow.theme.lumo.Lumo;"
    ]

    # Iterate and filter
    i = 0
    while i < len(lines):
        line = lines[i]
        stripped = line.strip()

        # Check for imports to remove
        if any(imp in stripped for imp in imports_to_remove):
            i += 1
            continue

        # Check for the annotation block
        # We added @SuppressWarnings("deprecation") just before @Theme in previous steps
        # But we need to handle cases where it might be separate or part of the file originally (though unlikely for TestApp)

        if stripped == '@SuppressWarnings("deprecation")':
            # Check if next line is @Theme
            if i + 1 < len(lines) and lines[i+1].strip().startswith("@Theme"):
                i += 2 # Skip both
                continue

        if stripped.startswith("@Theme"):
             i += 1
             continue

        new_lines.append(line)
        i += 1

    with open(filepath, 'w') as f:
        f.writelines(new_lines)

# Find files
import glob
import subprocess

# Using find command to get list of files
cmd = ['find', '.', '-name', '*TestApplication.java']
result = subprocess.run(cmd, stdout=subprocess.PIPE, text=True)
files = result.stdout.splitlines()

count = 0
for fpath in files:
    # Double check it had @Theme before processing to avoid unnecessary IO on wrong files
    with open(fpath, 'r') as f:
        content = f.read()
    if "@Theme" in content:
        print(f"Processing {fpath}")
        process_file(fpath)
        count += 1

print(f"Processed {count} files.")
