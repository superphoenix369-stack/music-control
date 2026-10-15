package com.musiccontrol.mixin;

import net.minecraft.client.sounds.MusicManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.musiccontrol.play.MusicController;

/** Lets the mod decide what plays; vanilla's music logic only runs when the mod hands control back. */
@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void musiccontrol$tick(CallbackInfo ci) {
		if (MusicController.tick((MusicManager) (Object) this)) {
			ci.cancel();
		}
	}
}
