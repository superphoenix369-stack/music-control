package com.musiccontrol.track;

import net.minecraft.resources.Identifier;

import com.musiccontrol.MusicControl;
import com.musiccontrol.config.Dim;
import com.musiccontrol.config.Underwater;
import com.musiccontrol.config.Where;

/**
 * One background-music track. Each has its own sound event in this mod's sounds.json
 * ({@code musiccontrol:track.<id>}) that points at the vanilla file, so one exact song can be played.
 */
public record Track(String id, String name, String composer, Where defaultWhere, Dim defaultDim,
		Underwater defaultUnderwater, String defaultBiomes) {
	public Identifier eventId() {
		return MusicControl.id("track." + id);
	}

	public boolean isC418() {
		return "C418".equals(composer);
	}
}
