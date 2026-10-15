package com.musiccontrol.play;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;


import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;

import com.musiccontrol.MusicControl;
import com.musiccontrol.config.Fallback;
import com.musiccontrol.config.ModConfig;
import com.musiccontrol.mixin.MusicManagerAccessor;
import com.musiccontrol.track.Track;
import com.musiccontrol.track.TrackCatalog;

/**
 * Picks and plays songs. Runs inside Minecraft's own music tick, so it gets called on the title
 * screen and in worlds exactly when vanilla music would.
 */
public final class MusicController {
	private static final int CONTEXT_REFRESH_TICKS = 20;
	/** How long a song must keep not fitting before it fades out, so a step across a biome border doesn't cut it. */
	private static final int MISMATCH_GRACE_TICKS = 100;
	private static final int FADE_TICKS = 60;
	private static final int FIRST_SONG_IN_WORLD_TICKS = 100;

	private static final Random RANDOM = new Random();
	private static final Deque<String> RECENT = new ArrayDeque<>();

	private static TrackSoundInstance current;
	private static boolean forced;
	private static int delay = 40;
	private static int mismatchTicks;
	private static boolean shortGapNext;

	private static GameContext context = GameContext.MENU;
	private static int contextAge = CONTEXT_REFRESH_TICKS;
	private static Boolean lastWasMenu;
	private static boolean lastHadCandidates = true;

	private static boolean crashed;

	private MusicController() {
	}

	/** @return true to skip vanilla's music logic for this tick. */
	public static boolean tick(MusicManager manager) {
		if (crashed) {
			return false;
		}

		try {
			return doTick(Minecraft.getInstance(), manager, (MusicManagerAccessor) manager);
		} catch (Throwable t) {
			crashed = true;
			MusicControl.LOGGER.error("Music Control hit an error and handed music back to vanilla until restart", t);
			return false;
		}
	}

	private static boolean doTick(Minecraft mc, MusicManager manager, MusicManagerAccessor acc) {
		ModConfig config = ModConfig.get();
		SoundManager sounds = mc.getSoundManager();

		if (!config.enabled) {
			if (current != null) {
				stopNow(sounds, acc);
			}

			return false;
		}

		// 1. Clean up our song if it ended (or was stopped by the game, e.g. leaving a world).
		if (current != null && !sounds.isActive(current)) {
			if (acc.musiccontrol$getCurrentMusic() == current) {
				acc.musiccontrol$setCurrentMusic(null);
			}

			current = null;
			forced = false;
			delay = shortGapNext ? shortGap() : gapFor(config);
			shortGapNext = false;
		}

		// 2. Refresh what situation we're in.
		boolean menuNow = mc.level == null || mc.player == null;
		if (++contextAge >= CONTEXT_REFRESH_TICKS || context.menu() != menuNow) {
			context = GameContext.capture(mc);
			contextAge = 0;
		}

		if (lastWasMenu == null || lastWasMenu != context.menu()) {
			lastWasMenu = context.menu();
			delay = Math.min(delay, context.menu() ? 20 : FIRST_SONG_IN_WORLD_TICKS);
		}

		// 3. If our song is playing, keep it unless it stopped fitting.
		if (current != null) {
			if (!forced && config.switchWhenSituationChanges && !current.isFadingOut()) {
				if (config.rule(current.track()).matches(context)) {
					mismatchTicks = 0;
				} else if (++mismatchTicks >= (context.menu() ? 1 : MISMATCH_GRACE_TICKS)) {
					current.fadeOut(FADE_TICKS);
					shortGapNext = true;
				}
			}

			return true;
		}

		List<Track> candidates = candidates(config);
		lastHadCandidates = !candidates.isEmpty();
		SoundInstance vanillaMusic = acc.musiccontrol$getCurrentMusic();

		if (candidates.isEmpty()) {
			if (config.fallback == Fallback.VANILLA) {
				return false;
			}

			// Silence: let a vanilla song that was already playing finish, but start nothing new.
			if (vanillaMusic != null && !sounds.isActive(vanillaMusic)) {
				acc.musiccontrol$setCurrentMusic(null);
			}

			return true;
		}

		// 4. A vanilla song is still playing from before: replace it or let it finish.
		if (vanillaMusic != null) {
			if (sounds.isActive(vanillaMusic)) {
				if (!config.switchWhenSituationChanges) {
					return true;
				}

				sounds.stop(vanillaMusic);
				delay = Math.min(delay, shortGap());
			}

			acc.musiccontrol$setCurrentMusic(null);
		}

		// 5. Wait out the gap, then start a song.
		if (delay > 0) {
			delay--;
			return true;
		}

		start(mc, manager, acc, pick(candidates), false);
		return true;
	}

	/** Vanilla may try to start music outside its tick; block that while the mod is in charge. */
	public static boolean blocksVanillaStart() {
		if (crashed) {
			return false;
		}

		ModConfig config = ModConfig.get();
		if (!config.enabled) {
			return false;
		}

		return current != null || lastHadCandidates || config.fallback == Fallback.SILENCE;
	}

	private static List<Track> candidates(ModConfig config) {
		List<Track> list = new ArrayList<>();
		for (Track track : TrackCatalog.TRACKS) {
			if (config.rule(track).matches(context)) {
				list.add(track);
			}
		}

		return list;
	}

	private static Track pick(List<Track> candidates) {
		List<Track> fresh = new ArrayList<>();
		for (Track track : candidates) {
			if (!RECENT.contains(track.id())) {
				fresh.add(track);
			}
		}

		if (fresh.isEmpty()) {
			fresh.addAll(candidates);
			if (fresh.size() > 1 && !RECENT.isEmpty()) {
				fresh.removeIf(track -> track.id().equals(RECENT.peekLast()));
			}
		}

		Track chosen = fresh.get(RANDOM.nextInt(fresh.size()));
		RECENT.addLast(chosen.id());
		int memory = Math.max(1, Math.min(8, candidates.size() / 2));
		while (RECENT.size() > memory) {
			RECENT.removeFirst();
		}

		return chosen;
	}

	private static void start(Minecraft mc, MusicManager manager, MusicManagerAccessor acc, Track track, boolean force) {
		TrackSoundInstance instance = new TrackSoundInstance(track);
		mc.getSoundManager().play(instance);
		current = instance;
		forced = force;
		mismatchTicks = 0;
		shortGapNext = false;

		// Register it as the game's current music so vanilla's "Now Playing" toast, pause menu and
		// stop-on-disconnect all treat it like normal music.
		acc.musiccontrol$setCurrentMusic(instance);
		acc.musiccontrol$setToastShown(false);
		try {
			manager.showNowPlayingToastIfNeeded();
		} catch (RuntimeException e) {
			MusicControl.LOGGER.debug("Now Playing toast failed", e);
		}
	}

	private static void stopNow(SoundManager sounds, MusicManagerAccessor acc) {
		if (current != null) {
			sounds.stop(current);
			if (acc.musiccontrol$getCurrentMusic() == current) {
				acc.musiccontrol$setCurrentMusic(null);
			}
		}

		current = null;
		forced = false;
	}

	private static int gapFor(ModConfig config) {
		if (context.menu()) {
			return 20 + RANDOM.nextInt(181); // 1–10 seconds on the title screen
		}

		return config.gap.randomTicks(RANDOM);
	}

	private static int shortGap() {
		return 40 + RANDOM.nextInt(61); // 2–5 seconds
	}

	// --- Called from the config screens and the /music command ---

	public static Track currentTrack() {
		return current != null ? current.track() : null;
	}

	/** Fades out the current song and starts another shortly after. */
	public static void skip() {
		Minecraft mc = Minecraft.getInstance();
		if (current != null) {
			current.fadeOut(20);
			shortGapNext = true;
			forced = false;
		} else {
			MusicManagerAccessor acc = (MusicManagerAccessor) mc.getMusicManager();
			SoundInstance vanillaMusic = acc.musiccontrol$getCurrentMusic();
			if (vanillaMusic != null) {
				mc.getSoundManager().stop(vanillaMusic);
				acc.musiccontrol$setCurrentMusic(null);
			}

			delay = Math.min(delay, shortGap());
		}
	}

	/** Plays a song right now, ignoring its conditions. It won't be faded out for not fitting. */
	public static void playNow(Track track) {
		Minecraft mc = Minecraft.getInstance();
		MusicManager manager = mc.getMusicManager();
		MusicManagerAccessor acc = (MusicManagerAccessor) manager;

		if (current != null) {
			stopNow(mc.getSoundManager(), acc);
		} else {
			SoundInstance vanillaMusic = acc.musiccontrol$getCurrentMusic();
			if (vanillaMusic != null) {
				mc.getSoundManager().stop(vanillaMusic);
				acc.musiccontrol$setCurrentMusic(null);
			}
		}

		start(mc, manager, acc, track, true);
	}

	/** Called after settings change so the new rules apply right away. */
	public static void settingsChanged() {
		crashed = false;
		contextAge = CONTEXT_REFRESH_TICKS;
		mismatchTicks = 0;
		RECENT.clear();
	}
}
