package com.musiccontrol.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;

import com.musiccontrol.config.Height;
import com.musiccontrol.config.ModConfig;
import com.musiccontrol.config.TimeOfDay;
import com.musiccontrol.config.TrackRule;
import com.musiccontrol.config.Underwater;
import com.musiccontrol.config.Weather;
import com.musiccontrol.config.Where;
import com.musiccontrol.play.MusicController;
import com.musiccontrol.track.Track;

/** All the conditions for one song. A song plays only when every condition here is met. */
public class TrackRulesScreen extends OptionsSubScreen {
	private final TrackListScreen songList;
	private final Track track;

	public TrackRulesScreen(TrackListScreen songList, Track track) {
		super(songList, Minecraft.getInstance().options, Component.literal(track.name() + " — " + track.composer()));
		this.songList = songList;
		this.track = track;
	}

	@Override
	protected void addOptions() {
		if (this.list == null) {
			return;
		}

		ModConfig config = ModConfig.get();
		TrackRule rule = config.rule(track);

		this.list.addSmall(
				Widgets.toggle("musiccontrol.rule.enabled", rule.enabled, v -> rule.enabled = v),
				Widgets.cycle("musiccontrol.rule.where", Where.class, rule.where, v -> rule.where = v));

		this.list.addHeader(Component.translatable("musiccontrol.rule.header.modes"));
		this.list.addSmall(
				Widgets.toggle("musiccontrol.rule.survival", rule.survival, v -> rule.survival = v),
				Widgets.toggle("musiccontrol.rule.creative", rule.creative, v -> rule.creative = v));
		this.list.addSmall(
				Widgets.toggle("musiccontrol.rule.adventure", rule.adventure, v -> rule.adventure = v),
				Widgets.toggle("musiccontrol.rule.spectator", rule.spectator, v -> rule.spectator = v));

		this.list.addHeader(Component.translatable("musiccontrol.rule.header.dimensions"));
		this.list.addSmall(
				Widgets.toggle("musiccontrol.rule.overworld", rule.overworld, v -> rule.overworld = v),
				Widgets.toggle("musiccontrol.rule.nether", rule.nether, v -> rule.nether = v));
		this.list.addSmall(
				Widgets.toggle("musiccontrol.rule.end", rule.end, v -> rule.end = v),
				Widgets.toggle("musiccontrol.rule.other", "musiccontrol.rule.other.tooltip", rule.otherDimensions,
						v -> rule.otherDimensions = v));

		this.list.addHeader(Component.translatable("musiccontrol.rule.header.situation"));
		this.list.addSmall(
				Widgets.cycle("musiccontrol.rule.underwater", Underwater.class, rule.underwater, v -> rule.underwater = v),
				Widgets.cycle("musiccontrol.rule.weather", Weather.class, rule.weather, v -> rule.weather = v));
		this.list.addSmall(
				Widgets.cycle("musiccontrol.rule.time", TimeOfDay.class, rule.time, v -> rule.time = v),
				Widgets.cycle("musiccontrol.rule.height", Height.class, rule.height, v -> rule.height = v));

		this.list.addHeader(Component.translatable("musiccontrol.rule.header.biomes"));
		EditBox biomes = new EditBox(this.font, 310, 20, Component.translatable("musiccontrol.rule.header.biomes"));
		biomes.setMaxLength(2000);
		biomes.setValue(rule.biomes);
		biomes.setHint(Component.translatable("musiccontrol.rule.biomes.hint").withStyle(ChatFormatting.DARK_GRAY));
		biomes.setResponder(text -> rule.biomes = text);
		this.list.addSmall(biomes, null);

		this.list.addHeader(Component.translatable("musiccontrol.rule.header.actions"));
		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.rule.play_now"),
						Component.translatable("musiccontrol.rule.play_now.tooltip"), () -> MusicController.playNow(track)),
				Widgets.button(Component.translatable("musiccontrol.rule.reset"), () -> {
					config.resetTrack(track);
					ModConfig.save();
					this.minecraft.gui.setScreen(new TrackRulesScreen(this.songList, this.track));
				}));
	}

	@Override
	public void onClose() {
		if (this.list != null) {
			this.list.applyUnsavedChanges();
		}

		// Open a fresh song list so the summary next to this song is up to date.
		this.minecraft.gui.setScreen(new TrackListScreen(this.songList.parentScreen()));
	}

	@Override
	public void removed() {
		super.removed();
		ModConfig.save();
		MusicController.settingsChanged();
	}
}
