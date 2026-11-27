import subprocess
import os
import argparse

parser = argparse.ArgumentParser(
    description="Build tool for Abhorrence"
)

parser.add_argument(
    "-target",
    help="Target .minecraft directory"
)

parser.add_argument(
    "--no_resourcepacks",
    action="store_true"
)

parser.add_argument(
    "--no_chemistry",
    action="store_true"
)

parser.add_argument(
    "--no_external",
    action="store_true"
)

args = parser.parse_args()

found = False
target_folder = ""
no_resourcepacks = "0"
no_chemistry = "0"
no_external = "0"

if args.no_resourcepacks:
    no_resourcepacks = "1"

if args.no_chemistry:
    no_chemistry = "1"

if args.no_external:
    no_external = "1"

if args.target != None:
    found = True
    target_folder = args.target
    if not os.path.isdir(target_folder):
        raise ValueError(f"{target_folder} is not a valid directory.")

with open("build.config", "r") as f:
    for line in f:
        if line[0] == "#" or line == "\n":
            continue
        if not found:
            if os.path.isdir(line.strip()):
                target_folder = line.strip()
                found = True
                break
    if not found:
        raise ValueError("Valid target .minecraft folder path not found in build.config file.")
    print("[General] Target .minecraft folder:", target_folder)

subprocess.run(
    ["bash", "modpack_builder/build.sh", target_folder, no_resourcepacks, no_chemistry, no_external],
    check=True
)