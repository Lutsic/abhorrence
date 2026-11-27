#!/bin/bash

set -e

rm -rf build
mkdir build
cp -a game_files/. build
echo "[General] Game files copied into build directory."

mkdir build/resourcepacks

cd resourcepacks/AE2Textures
zip -r -9 "../../build/resourcepacks/AE2 textures backport.zip" . > /dev/null
cd ../../
echo "[Resourcepacks] Added AE2Textures to build."

echo "[Chemistry] Compiling mod..."
cd chemistry
./gradlew build > /dev/null
cd ..
mkdir build/mods/
cp chemistry/build/libs/chemistry-*.jar build/mods/
echo "[Chemistry] Mod compiled and added to build"

python modpack_builder/external.py

cp -a build/. "$1"
echo "[General] Build finished"