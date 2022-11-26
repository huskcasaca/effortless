package dev.huskcasaca.effortless.mixin;

import dev.huskcasaca.effortless.render.MinecraftRendererProvider;
import dev.huskcasaca.effortless.render.BlockPreviewRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements MinecraftRendererProvider {

    private BlockPreviewRenderer blockPreviewRenderer;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;<init>(Lnet/minecraft/client/Minecraft;)V"))
    private void init(GameConfig gameConfig, CallbackInfo ci) {
        blockPreviewRenderer = new BlockPreviewRenderer((Minecraft) (Object) this);
    }

    @Override
    public BlockPreviewRenderer getBlockPreviewRenderer() {
        return blockPreviewRenderer;
    }

}
