package com.musiccontrol.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import com.musiccontrol.config.ModConfig;
import com.musiccontrol.config.TrackRule;
import com.musiccontrol.play.MusicController;
import com.musiccontrol.track.Track;
import com.musiccontrol.track.TrackCatalog;

/** Every song, grouped by composer: left button turns it on/off, right button opens its conditions. */
public class TrackListScreen extends OptionsSubScreen {
	private static final int MAX_SUMMARY = 26;

	public TrackListScreen(Screen parent) {
		super(parent, Minecraft.getInstance().options, Component.translatable("musiccontrol.songs.title"));
	}

	@Override
	protected void addOptions() {
		if (this.list == null) {
			return;
		}

		ModConfig config = ModConfig.get();

		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.button.c418"),
						Component.translatable("musiccontrol.button.c418.tooltip"), () -> {
							config.onlyC418();
							refresh();
						}),
				Widgets.button(Component.translatable("musiccontrol.button.all_on"),
						Component.translatable("musiccontrol.button.all_on.tooltip"), () -> {
							config.setAllEnabled(true);
							refresh();
						}));
		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.button.all_off"), () -> {
					config.setAllEnabled(false);
					refresh();
				}),
				null);

		String composer = null;
		for (Track track : TrackCatalog.TRACKS) {
			if (!track.composer().equals(composer)) {
				composer = track.composer();
				this.list.addHeader(Component.literal(composer));
			}

			TrackRule rule = config.rule(track);

			Button toggle = Button.builder(toggleLabel(track, rule), b -> {
				rule.enabled = !rule.enabled;
				b.setMessage(toggleLabel(track, rule));
				ModConfig.save();
				MusicController.settingsChanged();
			}).size(150, 20).build();
			toggle.setTooltip(Tooltip.create(Component.literal(track.name() + " by " + track.composer())));

			String summary = rule.summary();
			String shortSummary = summary.length() > MAX_SUMMARY ? summary.substring(0, MAX_SUMMARY - 1) + "…" : summary;
			Button edit = Button.builder(Component.literal(shortSummary), b ->
					this.minecraft.gui.setScreen(new TrackRulesScreen(this, track))).size(150, 20).build();
			edit.setTooltip(Tooltip.create(Component.translatable("musiccontrol.songs.edit_tooltip", summary)));

			this.list.addSmall(toggle, edit);
		}
	}

	private static Component toggleLabel(Track track, TrackRule rule) {
		MutableComponent state = rule.enabled
				? Component.literal("ON").withStyle(ChatFormatting.GREEN)
				: Component.literal("OFF").withStyle(ChatFormatting.RED);
		return Component.literal(track.name() + ": ").append(state);
	}

	Screen parentScreen() {
		return this.lastScreen;
	}

	private void refresh() {
		ModConfig.save();
		MusicController.settingsChanged();
		this.minecraft.gui.setScreen(new TrackListScreen(this.lastScreen));
	}

	@Override
	public void removed() {
		super.removed();
		ModConfig.save();
		MusicController.settingsChanged();
	}
}
