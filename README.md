# Music Control

A Fabric mod for **Minecraft 26.3** that lets you choose exactly which songs play and when.

- Turn any of the game's 70 background songs on or off, one by one.
- Give each song its own conditions: title screen or in a world, game mode, dimension, biome, underwater, weather, day/night, surface/underground.
- **Only C418** button: one click turns on every C418 song and turns off the rest.
- Title-screen songs (Mutation, Moog City 2, Beginning 2, Floating Trees, …) and creative songs (Biome Fest, Aria Math, Taswell, …) play in **survival** out of the box.
- Choose the gap between songs (2–5 seconds up to vanilla's 10–20 minutes).
- Works with the vanilla Music volume slider and the "Now Playing" toast.

The mod contains no music files. It plays the songs already in your copy of Minecraft (or whatever a resource pack replaced them with).

---

## 1. Turn this folder into a working `.jar` (free, about 10 minutes)

GitHub will compile the mod for you on its servers. You don't need to install Java or anything else.

1. Sign in at [github.com](https://github.com) (a free account is fine).
2. Click **+** (top right) → **New repository**. Name it `music-control`, leave everything else as is, click **Create repository**.
3. On the new page, click **uploading an existing file**. Drag **everything inside** this folder into the box (not the folder itself) and click **Commit changes**.
4. Make sure the build file came along: open the repository's main page and check that a folder named **`.github`** is listed. Some computers hide folders that start with a dot, so it may have been skipped. If it's missing:
   - Click **Add file → Create new file**.
   - Type `.github/workflows/build.yml` as the name (the slashes create the folders).
   - Open `.github/workflows/build.yml` from this folder in a text editor, copy all of it, paste it in, and click **Commit changes**.
5. Click the **Actions** tab. A run called **build** starts by itself (if it asks, click **I understand my workflows, go ahead and enable them**, then open **build** → **Run workflow**). Wait for the green check mark, about 3–5 minutes.
6. Click the finished run, scroll to **Artifacts**, and download **music-control-jar**. Unzip it to get `music-control-1.0.0.jar`.

If the run shows a red X, click it, open the **build** step, and copy the error text. That tells you (or whoever helps you) exactly what to fix.

## 2. Add it to your Modrinth instance

1. In the Modrinth app, open your **Minecraft 26.3 Fabric** instance.
2. Make sure **Fabric API** is installed (Content → search "Fabric API" → Install). It's required.
3. Optional but recommended: install **Mod Menu**, which adds a settings button for this mod.
4. Drag `music-control-1.0.0.jar` onto the instance's content list, or click the **⋮** menu → **Open folder** → put the jar in the `mods` folder.
5. Launch the game.

## 3. Use it

**Open the settings** with Mod Menu (Mods → Music Control → Configure), or type `/music` in chat while in a world.

- **Choose Songs and Conditions…** lists every song grouped by composer. The left button turns a song on/off. The right button shows when it plays; click it to change the conditions.
- A song plays only when **all** of its conditions match. Leave the biome box blank for "any biome". Biome names look like `cherry_grove`, `desert`, `deep_dark`; `*` works at either end (`*ocean`, `snowy_*`).
- **Play Now** (inside a song's conditions) plays that song immediately so you can hear it.
- **If Nothing Fits**: *Silence* (default) never plays a song you turned off; *Vanilla music* lets the normal game music fill the gaps.
- **Change Song to Fit**: fades out a song that stops matching (for example, you left the Nether) and picks one that fits.

Chat commands:

| Command | What it does |
|---|---|
| `/music` | Open the settings |
| `/music skip` | Fade out this song and play another |
| `/music now` | Show what's playing |
| `/music c418` | Only C418 songs |
| `/music off` / `/music on` | Hand music back to vanilla / take control again |

Settings are saved in `config/musiccontrol.json` inside the instance folder.

## Defaults

Out of the box each song keeps its vanilla home, with two changes you asked for:

| Songs | Default conditions |
|---|---|
| Title-screen songs (C418 menu tracks, Paula Ruiz's Chaos Cubed tracks) | Title screen **and** any Overworld game mode |
| Creative songs | Overworld, **any** game mode including survival |
| Regular Overworld songs | Overworld, any game mode |
| Biome-specific songs (Ancestry, Crescent Dunes, Stand Tall, …) | Only in the biomes where vanilla plays them |
| Nether songs | Nether (Lena Raine's three in their own Nether biomes) |
| The End, Boss, Alpha | The End |
| Axolotl, Dragon Fish, Shuniji | Underwater |

## For developers

- `./gradlew build` produces `build/libs/music-control-<version>.jar` (needs JDK 25).
- The song list (`TrackCatalog.java`) and `assets/musiccontrol/sounds.json` were generated from Minecraft 26.3's `sounds.json`. Each song gets its own sound event (`musiccontrol:track.<id>`) so one exact song can be played.
- Mixins: `MusicManager.tick` and `MusicManager.startPlaying` are intercepted; the vanilla music manager runs normally whenever the mod is off or (with the *Vanilla music* fallback) nothing fits.
