package com.musiccontrol.config;

import java.util.Random;

/** How long to wait between songs while in a world (title screen gaps are always short). */
public enum GapPreset {
	CONSTANT(2, 5),
	FREQUENT(60, 120),
	RELAXED(180, 360),
	VANILLA(600, 1200);

	private final int minSeconds;
	private final int maxSeconds;

	GapPreset(int minSeconds, int maxSeconds) {
		this.minSeconds = minSeconds;
		this.maxSeconds = maxSeconds;
	}

	public int randomTicks(Random random) {
		return (minSeconds + random.nextInt(maxSeconds - minSeconds + 1)) * 20;
	}
}
