package com.musiccontrol.play;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import com.musiccontrol.config.Dim;
import com.musiccontrol.config.Mode;

/** A snapshot of the situation the player is in, used to decide which songs may play. */
public record GameContext(boolean menu, Mode mode, Dim dim, String biome, boolean underwater, boolean raining,
		boolean thundering, boolean day, boolean underground) {
	public static final GameContext MENU = new GameContext(true, Mode.SURVIVAL, Dim.OVERWORLD, null, false, false, false, true, false);

	public static GameContext capture(Minecraft mc) {
		ClientLevel level = mc.level;
		LocalPlayer player = mc.player;

		if (level == null || player == null) {
			return MENU;
		}

		Mode mode = Mode.SURVIVAL;
		if (mc.gameMode != null) {
			GameType type = mc.gameMode.getPlayerMode();
			if (type == GameType.CREATIVE) mode = Mode.CREATIVE;
			else if (type == GameType.ADVENTURE) mode = Mode.ADVENTURE;
			else if (type == GameType.SPECTATOR) mode = Mode.SPECTATOR;
		}

		Dim dim;
		if (level.dimension() == Level.OVERWORLD) dim = Dim.OVERWORLD;
		else if (level.dimension() == Level.NETHER) dim = Dim.NETHER;
		else if (level.dimension() == Level.END) dim = Dim.END;
		else dim = Dim.OTHER;

		BlockPos pos = player.blockPosition();
		String biome = null;
		try {
			biome = level.getBiome(pos).getRegisteredName();
		} catch (RuntimeException ignored) {
			// Biome not loaded yet; treat as unknown.
		}

		boolean underground = !level.canSeeSky(pos) && pos.getY() < level.getSeaLevel();

		return new GameContext(false, mode, dim, biome, player.isUnderWater(), level.isRaining(), level.isThundering(),
				level.isBrightOutside(), underground);
	}
}
