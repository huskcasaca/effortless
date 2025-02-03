package dev.huskuraft.effortless.fabric.mixin;

import java.io.IOException;

import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderManager;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.huskuraft.effortless.fabric.events.common.ClientShadersEvents;

@Mixin(value = ShaderManager.class, priority = 1100)
public abstract class ShaderManagerMixin {

    @Inject(method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "HEAD"))
    public void onRegisterShaders(ShaderManager.Configs configs, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) throws IOException {
        ClientShadersEvents.REGISTER.invoker().onRegisterShader(resourceManager, (shader, callback) -> {
            CoreShaders.getProgramsToPreload().add(shader);
            callback.accept(shader);
        });
    }
}
