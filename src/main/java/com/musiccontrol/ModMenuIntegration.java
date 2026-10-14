package com.musiccontrol;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import com.musiccontrol.gui.MainScreen;

/** Adds a "Configure" button for this mod in Mod Menu, if Mod Menu is installed. */
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return MainScreen::new;
	}
}
