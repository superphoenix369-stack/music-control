package com.musiccontrol.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MusicManager.class)
public interface MusicManagerAccessor {
	@Accessor("currentMusic")
	SoundInstance musiccontrol$getCurrentMusic();

	@Accessor("currentMusic")
	void musiccontrol$setCurrentMusic(SoundInstance instance);

	@Accessor("toastShown")
	void musiccontrol$setToastShown(boolean shown);
}
