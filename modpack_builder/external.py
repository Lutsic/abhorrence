import json
import requests
from pathlib import Path

def download_file(url, path):
     with requests.get(url, stream=True, timeout=(10, 60)) as response:
        response.raise_for_status()

        with open(path, "wb") as file:
            for chunk in response.iter_content(chunk_size=1024 * 1024):
                if not chunk:
                    continue

                file.write(chunk)

with open("modpack_builder/modrinth.index.json", "r", encoding="utf-8") as file:
    modrith_index = json.load(file)

with open("modpack_builder/other.index.json", "r", encoding="utf-8") as file:
    other_index = json.load(file)

build_dir = Path("build")
current_file = 0
total_files = 0
for mod in modrith_index["files"]:
    total_files += 1
for mod in other_index["files"]:
    total_files += 1

for mod in modrith_index["files"]:
    print(f"\r[External] Downloaded {current_file}/{total_files} files.", end="")
    url = mod["downloads"][0]
    path = build_dir / mod["path"]

    path.parent.mkdir(parents=True, exist_ok=True)

    download_file(url, str(path))
    current_file += 1

for mod in other_index["files"]:
    print(f"\r[External] Downloaded {current_file}/{total_files} files.", end="")
    url = mod["downloads"][0]
    path = build_dir / mod["path"]

    path.parent.mkdir(parents=True, exist_ok=True)

    download_file(url, str(path))
    current_file += 1

print(f"\r[External] Downloaded {current_file}/{total_files} files.", end="")
print("")
    