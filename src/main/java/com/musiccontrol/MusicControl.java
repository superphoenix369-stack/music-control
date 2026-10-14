package com.musiccontrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import com.musiccontrol.config.ModConfig;
import com.musiccontrol.gui.MainScreen;
import com.musiccontrol.play.MusicController;
import com.musiccontrol.track.Track;

public class MusicControl implements ClientModInitializer {
	public static final String MOD_ID = "musiccontrol";
	public static final Logger LOGGER = LoggerFactory.getLogger("Music Control");

	private static boolean openScreenNextTick;

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {
		ModConfig.load();

		// Chat closes itself after a command runs, so open the settings screen on the following tick.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openScreenNextTick) {
				openScreenNextTick = false;
				client.gui.setScreen(new MainScreen(null));
			}
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> dispatcher.register(
				ClientCommands.literal("music")
						.executes(context -> {
							openScreenNextTick = true;
							return 1;
						})
						.then(ClientCommands.literal("skip").executes(context -> {
							MusicController.skip();
							context.getSource().sendFeedback(Component.literal("Skipping to another song…"));
							return 1;
						}))
						.then(ClientCommands.literal("now").executes(context -> {
							Track track = MusicController.currentTrack();
							context.getSource().sendFeedback(track == null
									? Component.literal("Nothing from Music Control is playing right now.")
									: Component.literal("Now playing: ").append(Component.literal(track.name()).withStyle(ChatFormatting.AQUA))
											.append(Component.literal(" — " + track.composer()).withStyle(ChatFormatting.GRAY)));
							return 1;
						}))
						.then(ClientCommands.literal("c418").executes(context -> {
							ModConfig.get().onlyC418();
							ModConfig.save();
							MusicController.settingsChanged();
							context.getSource().sendFeedback(Component.literal("Only C418 songs are turned on now."));
							return 1;
						}))
						.then(ClientCommands.literal("on").executes(context -> {
							ModConfig.get().enabled = true;
							ModConfig.save();
							MusicController.settingsChanged();
							context.getSource().sendFeedback(Component.literal("Music Control is on."));
							return 1;
						}))
						.then(ClientCommands.literal("off").executes(context -> {
							ModConfig.get().enabled = false;
							ModConfig.save();
							context.getSource().sendFeedback(Component.literal("Music Control is off; vanilla music is back."));
							return 1;
						}))));

		LOGGER.info("Music Control ready: {} songs available", com.musiccontrol.track.TrackCatalog.TRACKS.size());
	}
}
