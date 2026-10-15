package com.musiccontrol.play;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundSource;

import com.musiccontrol.track.Track;

/** A music sound that plays one exact track and can fade itself out. */
public class TrackSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	private final Track track;
	private float fade = 1.0F;
	private float fadeStep;
	private boolean stopped;

	public TrackSoundInstance(Track track) {
		super(track.eventId(), SoundSource.MUSIC, SoundInstance.createUnseededRandom());
		this.track = track;
		this.volume = 1.0F;
		this.pitch = 1.0F;
		this.looping = false;
		this.delay = 0;
		this.attenuation = SoundInstance.Attenuation.NONE;
		this.relative = true;
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	public Track track() {
		return track;
	}

	public void fadeOut(int ticks) {
		float step = 1.0F / Math.max(1, ticks);
		if (fadeStep == 0.0F || step > fadeStep) {
			fadeStep = step;
		}
	}

	public boolean isFadingOut() {
		return fadeStep > 0.0F;
	}

	@Override
	public void tick() {
		if (fadeStep > 0.0F && !stopped) {
			fade -= fadeStep;
			if (fade <= 0.0F) {
				fade = 0.0F;
				stopped = true;
			}

			this.volume = fade;
		}
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}
}
