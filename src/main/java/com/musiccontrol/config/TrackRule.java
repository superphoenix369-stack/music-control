package com.musiccontrol.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.musiccontrol.play.GameContext;
import com.musiccontrol.track.Track;

/** The on/off switch and conditions for one song. Saved as part of {@link ModConfig}. */
public class TrackRule {
	public boolean enabled = true;
	public Where where = Where.WORLD;

	public boolean survival = true;
	public boolean creative = true;
	public boolean adventure = true;
	public boolean spectator = true;

	public boolean overworld = true;
	public boolean nether = false;
	public boolean end = false;
	public boolean otherDimensions = false;

	/** Comma-separated biome ids, e.g. "cherry_grove, minecraft:desert". Blank means any biome. * is a wildcard at either end ("*ocean", "snowy_*"). */
	public String biomes = "";

	public Underwater underwater = Underwater.ANY;
	public Weather weather = Weather.ANY;
	public TimeOfDay time = TimeOfDay.ANY;
	public Height height = Height.ANY;

	public static TrackRule defaultsFor(Track track) {
		TrackRule rule = new TrackRule();
		rule.where = track.defaultWhere();
		rule.overworld = track.defaultDim() == Dim.OVERWORLD;
		rule.nether = track.defaultDim() == Dim.NETHER;
		rule.end = track.defaultDim() == Dim.END;
		rule.otherDimensions = false;
		rule.underwater = track.defaultUnderwater();
		rule.biomes = track.defaultBiomes();
		return rule;
	}

	/** Fills in anything missing from an older or hand-edited config file. */
	public void sanitize() {
		if (where == null) where = Where.WORLD;
		if (biomes == null) biomes = "";
		if (underwater == null) underwater = Underwater.ANY;
		if (weather == null) weather = Weather.ANY;
		if (time == null) time = TimeOfDay.ANY;
		if (height == null) height = Height.ANY;
	}

	public boolean matches(GameContext ctx) {
		if (!enabled) {
			return false;
		}

		if (ctx.menu()) {
			return where.allowsMenu();
		}

		if (!where.allowsWorld()) {
			return false;
		}

		boolean modeOk = switch (ctx.mode()) {
			case SURVIVAL -> survival;
			case CREATIVE -> creative;
			case ADVENTURE -> adventure;
			case SPECTATOR -> spectator;
		};

		if (!modeOk) {
			return false;
		}

		boolean dimOk = switch (ctx.dim()) {
			case OVERWORLD -> overworld;
			case NETHER -> nether;
			case END -> end;
			case OTHER -> otherDimensions;
		};

		if (!dimOk) {
			return false;
		}

		if (!biomeMatches(ctx.biome())) {
			return false;
		}

		switch (underwater) {
			case ONLY -> {
				if (!ctx.underwater()) return false;
			}
			case NEVER -> {
				if (ctx.underwater()) return false;
			}
			default -> { }
		}

		switch (weather) {
			case CLEAR -> {
				if (ctx.raining()) return false;
			}
			case RAIN -> {
				if (!ctx.raining() || ctx.thundering()) return false;
			}
			case THUNDER -> {
				if (!ctx.thundering()) return false;
			}
			default -> { }
		}

		switch (time) {
			case DAY -> {
				if (!ctx.day()) return false;
			}
			case NIGHT -> {
				if (ctx.day()) return false;
			}
			default -> { }
		}

		switch (height) {
			case SURFACE -> {
				if (ctx.underground()) return false;
			}
			case UNDERGROUND -> {
				if (!ctx.underground()) return false;
			}
			default -> { }
		}

		return true;
	}

	public List<String> biomeList() {
		List<String> list = new ArrayList<>();
		for (String part : biomes.split(",")) {
			String trimmed = part.trim().toLowerCase(Locale.ROOT);
			if (!trimmed.isEmpty()) {
				list.add(trimmed);
			}
		}

		return list;
	}

	private boolean biomeMatches(String biomeId) {
		List<String> wanted = biomeList();
		if (wanted.isEmpty()) {
			return true;
		}

		if (biomeId == null) {
			return false;
		}

		String full = biomeId.toLowerCase(Locale.ROOT);
		int colon = full.indexOf(':');
		String path = colon >= 0 ? full.substring(colon + 1) : full;

		for (String entry : wanted) {
			String target = entry.indexOf(':') >= 0 ? full : path;
			boolean starStart = entry.startsWith("*");
			boolean starEnd = entry.length() > 1 && entry.endsWith("*");
			String core = entry.substring(starStart ? 1 : 0, entry.length() - (starEnd ? 1 : 0));

			if (starStart && starEnd ? target.contains(core)
					: starStart ? target.endsWith(core)
					: starEnd ? target.startsWith(core)
					: target.equals(core)) {
				return true;
			}
		}

		return false;
	}

	/** Short description for the button next to each song in the song list. */
	public String summary() {
		if (where == Where.MENU) {
			return "Title screen only";
		}

		List<String> parts = new ArrayList<>();
		if (where == Where.BOTH) parts.add("Title +");

		List<String> dims = new ArrayList<>();
		if (overworld) dims.add("Overworld");
		if (nether) dims.add("Nether");
		if (end) dims.add("End");
		if (otherDimensions) dims.add("Other");

		if (dims.isEmpty()) parts.add("No dimension!");
		else if (dims.size() == 4) parts.add("All dims");
		else parts.add(String.join("/", dims));

		if (!(survival && creative && adventure && spectator)) {
			List<String> modes = new ArrayList<>();
			if (survival) modes.add("Surv");
			if (creative) modes.add("Crea");
			if (adventure) modes.add("Adv");
			if (spectator) modes.add("Spec");
			parts.add(modes.isEmpty() ? "No modes!" : String.join("/", modes));
		}

		int biomeCount = biomeList().size();
		if (biomeCount > 0) parts.add(biomeCount == 1 ? biomeList().get(0) : biomeCount + " biomes");
		if (underwater == Underwater.ONLY) parts.add("underwater");
		if (underwater == Underwater.NEVER) parts.add("dry");
		if (weather != Weather.ANY) parts.add(weather.name().toLowerCase(Locale.ROOT));
		if (time != TimeOfDay.ANY) parts.add(time.name().toLowerCase(Locale.ROOT));
		if (height != Height.ANY) parts.add(height.name().toLowerCase(Locale.ROOT));

		return String.join(" ", parts);
	}
}
