package com.musiccontrol.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.fabricmc.loader.api.FabricLoader;

import com.musiccontrol.MusicControl;
import com.musiccontrol.track.Track;
import com.musiccontrol.track.TrackCatalog;

/** Everything the player can change, saved to config/musiccontrol.json. */
public class ModConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static ModConfig instance;

	/** Master switch. When off, Minecraft's normal music system runs untouched. */
	public boolean enabled = true;
	public Fallback fallback = Fallback.SILENCE;
	public GapPreset gap = GapPreset.RELAXED;
	/** Fade out the current song when it no longer fits (e.g. you entered the Nether). */
	public boolean switchWhenSituationChanges = true;
	public Map<String, TrackRule> tracks = new LinkedHashMap<>();

	public static ModConfig get() {
		if (instance == null) {
			load();
		}

		return instance;
	}

	private static Path path() {
		return FabricLoader.getInstance().getConfigDir().resolve(MusicControl.MOD_ID + ".json");
	}

	public static void load() {
		ModConfig loaded = null;
		Path path = path();

		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				loaded = GSON.fromJson(reader, ModConfig.class);
			} catch (IOException | JsonParseException e) {
				MusicControl.LOGGER.error("Could not read {}, using defaults", path, e);
			}
		}

		instance = loaded != null ? loaded : new ModConfig();
		instance.fillMissing();
		save();
	}

	public static void save() {
		if (instance == null) {
			return;
		}

		try {
			Files.createDirectories(path().getParent());
			try (Writer writer = Files.newBufferedWriter(path())) {
				GSON.toJson(instance, writer);
			}
		} catch (IOException e) {
			MusicControl.LOGGER.error("Could not save {}", path(), e);
		}
	}

	private void fillMissing() {
		if (fallback == null) fallback = Fallback.SILENCE;
		if (gap == null) gap = GapPreset.RELAXED;
		if (tracks == null) tracks = new LinkedHashMap<>();

		for (Track track : TrackCatalog.TRACKS) {
			TrackRule rule = tracks.get(track.id());
			if (rule == null) {
				tracks.put(track.id(), TrackRule.defaultsFor(track));
			} else {
				rule.sanitize();
			}
		}
	}

	public TrackRule rule(Track track) {
		return tracks.computeIfAbsent(track.id(), id -> TrackRule.defaultsFor(track));
	}

	/** Turns every C418 song on and every other song off, keeping each song's conditions. */
	public void onlyC418() {
		for (Track track : TrackCatalog.TRACKS) {
			rule(track).enabled = track.isC418();
		}
	}

	public void setAllEnabled(boolean enabled) {
		for (Track track : TrackCatalog.TRACKS) {
			rule(track).enabled = enabled;
		}
	}

	/** Makes every song able to play anywhere in any world (and on the title screen if it already could). */
	public void playEverywhere() {
		for (Track track : TrackCatalog.TRACKS) {
			TrackRule rule = rule(track);
			if (rule.where == Where.MENU) rule.where = Where.BOTH;
			rule.survival = rule.creative = rule.adventure = rule.spectator = true;
			rule.overworld = rule.nether = rule.end = rule.otherDimensions = true;
			rule.biomes = "";
			rule.underwater = Underwater.ANY;
			rule.weather = Weather.ANY;
			rule.time = TimeOfDay.ANY;
			rule.height = Height.ANY;
		}
	}

	public void resetTrack(Track track) {
		boolean wasEnabled = rule(track).enabled;
		TrackRule fresh = TrackRule.defaultsFor(track);
		fresh.enabled = wasEnabled;
		tracks.put(track.id(), fresh);
	}

	public void resetAll() {
		ModConfig fresh = new ModConfig();
		fresh.fillMissing();
		this.enabled = fresh.enabled;
		this.fallback = fresh.fallback;
		this.gap = fresh.gap;
		this.switchWhenSituationChanges = fresh.switchWhenSituationChanges;
		this.tracks = fresh.tracks;
	}
}
