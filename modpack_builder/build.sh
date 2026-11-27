#!/bin/bash

# $1 - Target path
# $2 - no_resourcepacks
# $3 - no_chemistry
# $4 - no_external

set -e

rm -rf build
mkdir build
cp -a game_files/. build
echo "[General] Game files copied into build directory."

if [ $2 == "0" ]; then
    mkdir build/resourcepacks
    cd resourcepacks/AE2Textures
    zip -r -9 "../../build/resourcepacks/AE2 textures backport.zip" . > /dev/null
    cd ../../
    echo "[Resourcepacks] Added AE2Textures to build."
fi

if [ $3 == "0" ]; then
    echo "[Chemistry] Compiling mod..."
    cd chemistry
    ./gradlew build > /dev/null
    cd ..
    mkdir build/mods/
    cp chemistry/build/libs/chemistry-*.jar build/mods/
    echo "[Chemistry] Mod compiled and added to build"
fi

if [ $4 == "0" ]; then
    python modpack_builder/external.py
fi

cp -a build/. "$1"
echo "[General] Build finished"