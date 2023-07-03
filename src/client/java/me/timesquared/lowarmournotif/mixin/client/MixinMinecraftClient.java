package me.timesquared.lowarmournotif.mixin.client;

import me.timesquared.lowarmournotif.MainClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo callbackInfo) {
		MainClient.INSTANCE.onTick();
	}
}
