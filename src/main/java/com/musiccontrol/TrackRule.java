package com.musiccontrol.config;

/** Where a song is allowed to play. */
public enum Where {
	WORLD,
	MENU,
	BOTH;

	public boolean allowsMenu() {
		return this != WORLD;
	}

	public boolean allowsWorld() {
		return this != MENU;
	}
}
