package com.musiccontrol.track;

import java.util.List;

import com.musiccontrol.config.Dim;
import com.musiccontrol.config.Underwater;
import com.musiccontrol.config.Where;

/**
 * Every background-music track in vanilla Minecraft 26.3, with the defaults this mod starts from.
 *
 * <p>Generated from the game's own sounds.json. Defaults follow vanilla (Nether tracks in the Nether,
 * biome-only tracks in their biomes, water tracks underwater) except that title-screen and creative
 * tracks are also allowed in survival.
 */
public final class TrackCatalog {
	public static final List<Track> TRACKS = List.of(
		new Track("alpha", "Alpha", "C418", Where.WORLD, Dim.END, Underwater.ANY, ""),
		new Track("aria_math", "Aria Math", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("axolotl", "Axolotl", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ONLY, ""),
		new Track("ballad_of_the_cats", "Ballad of the Cats", "C418", Where.WORLD, Dim.NETHER, Underwater.ANY, ""),
		new Track("beginning_2", "Beginning 2", "C418", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("biome_fest", "Biome Fest", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("blind_spots", "Blind Spots", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("boss", "Boss", "C418", Where.WORLD, Dim.END, Underwater.ANY, ""),
		new Track("clark", "Clark", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("concrete_halls", "Concrete Halls", "C418", Where.WORLD, Dim.NETHER, Underwater.ANY, ""),
		new Track("danny", "Danny", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("dead_voxel", "Dead Voxel", "C418", Where.WORLD, Dim.NETHER, Underwater.ANY, ""),
		new Track("dragon_fish", "Dragon Fish", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ONLY, ""),
		new Track("dreiton", "Dreiton", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("dry_hands", "Dry Hands", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("floating_trees", "Floating Trees", "C418", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("haggstrom", "Haggstrom", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("haunt_muskie", "Haunt Muskie", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("key", "Key", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("living_mice", "Living Mice", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("mice_on_venus", "Mice on Venus", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("minecraft", "Minecraft", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("moog_city_2", "Moog City 2", "C418", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("mutation", "Mutation", "C418", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("oxygene", "Oxygène", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("shuniji", "Shuniji", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ONLY, ""),
		new Track("subwoofer_lullaby", "Subwoofer Lullaby", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("sweden", "Sweden", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("taswell", "Taswell", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("the_end", "The End", "C418", Where.WORLD, Dim.END, Underwater.ANY, ""),
		new Track("warmth", "Warmth", "C418", Where.WORLD, Dim.NETHER, Underwater.ANY, ""),
		new Track("wet_hands", "Wet Hands", "C418", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("aerie", "Aerie", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "forest, lush_caves, old_growth_taiga, swamp"),
		new Track("ancestry", "Ancestry", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "deep_dark"),
		new Track("chrysopoeia", "Chrysopoeia", "Lena Raine", Where.WORLD, Dim.NETHER, Underwater.ANY, "crimson_forest"),
		new Track("deeper", "Deeper", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "deep_dark, dripstone_caves, sulfur_caves"),
		new Track("eld_unknown", "Eld Unknown", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, grove, jagged_peaks, stony_peaks, sulfur_caves"),
		new Track("endless", "Endless", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, grove, jagged_peaks, stony_peaks, sulfur_caves"),
		new Track("firebugs", "Firebugs", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "forest, lush_caves, old_growth_taiga, swamp"),
		new Track("infinite_amethyst", "Infinite Amethyst", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, grove, sulfur_caves"),
		new Track("labyrinthine", "Labyrinthine", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "forest, lush_caves, old_growth_taiga, swamp"),
		new Track("left_to_bloom", "Left to Bloom", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("one_more_day", "One More Day", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("rubedo", "Rubedo", "Lena Raine", Where.WORLD, Dim.NETHER, Underwater.ANY, "nether_wastes"),
		new Track("so_below", "So Below", "Lena Raine", Where.WORLD, Dim.NETHER, Underwater.ANY, "basalt_deltas, soul_sand_valley"),
		new Track("stand_tall", "Stand Tall", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "frozen_peaks, jagged_peaks, snowy_slopes, stony_peaks"),
		new Track("wending", "Wending", "Lena Raine", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, grove, jagged_peaks, stony_peaks, sulfur_caves"),
		new Track("an_ordinary_day", "An Ordinary Day", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, lush_caves, snowy_slopes, sulfur_caves"),
		new Track("comforting_memories", "Comforting Memories", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("floating_dream", "Floating Dream", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("komorebi", "Komorebi", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("pokopoko", "Pokopoko", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "dripstone_caves, grove, jagged_peaks, snowy_slopes, sulfur_caves"),
		new Track("yakusoku", "Yakusoku", "Kumi Tanioka", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("a_familiar_room", "A Familiar Room", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("bromeliad", "Bromeliad", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "bamboo_jungle, cherry_grove, flower_forest, forest, jungle, sparse_jungle"),
		new Track("crescent_dunes", "Crescent Dunes", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "badlands, desert"),
		new Track("echo_in_the_wind", "Echo in the Wind", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, "badlands, cherry_grove, flower_forest, lush_caves"),
		new Track("featherfall", "Featherfall", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("puzzlebox", "Puzzlebox", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("watcher", "Watcher", "Aaron Cherof", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("below_and_above", "Below and Above", "Amos Roddy", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("broken_clocks", "Broken Clocks", "Amos Roddy", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("fireflies", "Fireflies", "Amos Roddy", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("lilypad", "Lilypad", "Amos Roddy", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("os_piano", "O's Piano", "Amos Roddy", Where.WORLD, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("ebb", "Ebb", "Paula Ruiz (fingerspit)", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("home", "Home", "Paula Ruiz (fingerspit)", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("memories", "Memories", "Paula Ruiz (fingerspit)", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("nightly", "Nightly", "Paula Ruiz (fingerspit)", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, ""),
		new Track("shores", "Shores", "Paula Ruiz (fingerspit)", Where.BOTH, Dim.OVERWORLD, Underwater.ANY, "")
	);

	private TrackCatalog() {
	}

	public static Track byId(String id) {
		for (Track track : TRACKS) {
			if (track.id().equals(id)) {
				return track;
			}
		}

		return null;
	}
}
