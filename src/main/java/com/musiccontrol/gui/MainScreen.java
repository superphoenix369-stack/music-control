package com.musiccontrol.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import com.musiccontrol.config.Fallback;
import com.musiccontrol.config.GapPreset;
import com.musiccontrol.config.ModConfig;
import com.musiccontrol.play.MusicController;

/** Music Control's main settings screen. */
public class MainScreen extends OptionsSubScreen {
	private final Screen parent;

	public MainScreen(Screen parent) {
		super(parent, Minecraft.getInstance().options, Component.translatable("musiccontrol.title"));
		this.parent = parent;
	}

	@Override
	protected void addOptions() {
		if (this.list == null) {
			return;
		}

		ModConfig config = ModConfig.get();

		this.list.addSmall(
				Widgets.toggle("musiccontrol.option.enabled", "musiccontrol.option.enabled.tooltip", config.enabled, v -> {
					config.enabled = v;
					MusicController.settingsChanged();
				}),
				Widgets.toggle("musiccontrol.option.switch", "musiccontrol.option.switch.tooltip", config.switchWhenSituationChanges,
						v -> config.switchWhenSituationChanges = v));
		this.list.addSmall(
				Widgets.cycle("musiccontrol.option.gap", GapPreset.class, config.gap, v -> config.gap = v),
				Widgets.cycle("musiccontrol.option.fallback", Fallback.class, config.fallback, v -> config.fallback = v));

		this.list.addHeader(Component.translatable("musiccontrol.header.songs"));
		this.list.addSmall(Widgets.button(Component.translatable("musiccontrol.button.songs"),
				() -> this.minecraft.gui.setScreen(new TrackListScreen(this))), null);

		this.list.addHeader(Component.translatable("musiccontrol.header.presets"));
		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.button.c418"),
						Component.translatable("musiccontrol.button.c418.tooltip"), () -> {
							config.onlyC418();
							applied();
						}),
				Widgets.button(Component.translatable("musiccontrol.button.all_on"),
						Component.translatable("musiccontrol.button.all_on.tooltip"), () -> {
							config.setAllEnabled(true);
							applied();
						}));
		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.button.everywhere"),
						Component.translatable("musiccontrol.button.everywhere.tooltip"), () -> {
							config.playEverywhere();
							applied();
						}),
				Widgets.button(Component.translatable("musiccontrol.button.reset_all"),
						Component.translatable("musiccontrol.button.reset_all.tooltip"), () -> {
							config.resetAll();
							applied();
							this.minecraft.gui.setScreen(new MainScreen(this.parent));
						}));

		this.list.addHeader(Component.translatable("musiccontrol.header.now"));
		this.list.addSmall(
				Widgets.button(Component.translatable("musiccontrol.button.skip"), MusicController::skip),
				null);
	}

	private void applied() {
		ModConfig.save();
		MusicController.settingsChanged();
	}

	@Override
	public void removed() {
		super.removed();
		ModConfig.save();
		MusicController.settingsChanged();
	}
}
