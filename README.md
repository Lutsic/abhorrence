# Abhorrence
A minecraft modpack.

## Current state
The modpack is still under active development and is not meant to be played in its current state

## Mods
7 external mods, 8 total. [Detailed modlist](modlist.md)

Game version: `1.16.5`, Modloader: `Forge 36.2.34`

## Build script
The build script is intended for development, not for regular players. The script was written for Arch Linux, but it can also be used on other linux distributions. Make sure your system have Python and zip utility. If you still want to try it, here are the instructions:

1. You need a launcher that supports multiple game instances. If you don't have one, we recommend using [Prism](https://prismlauncher.org/). Create a new instance with game version 1.16.5 and modloader Forge 36.2.34. Copy the path to its .minecraft folder.

2. Change the target .minecraft folder path in `build.config`. This is where the modpack will be installed:
```
# Target .minecraft folder
/path/to/your/.minecraft/folder/
```
3. Run the script:
```bash
python build.py
```
4. If the build finished without errors, you can launch the game.

## Credits
- [AE2 textures backport](resourcepacks/AE2Textures/credits.md)
- [Mods in game_files/mods](game_files/mods/credits.md)