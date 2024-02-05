package dev.huskuraft.effortless.fabric.mixin;

import dev.huskuraft.effortless.fabric.events.KeyboardInputEvents;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "keyPress", at = @At(value = "RETURN"))
    private void onKeyPress(long l, int i, int j, int k, int m, CallbackInfo ci) {
        if (l == minecraft.getWindow().getWindow()) {
            KeyboardInputEvents.KEY_INPUT.invoker().onKeyInput(i, j, k, m);
        }
    }

}
