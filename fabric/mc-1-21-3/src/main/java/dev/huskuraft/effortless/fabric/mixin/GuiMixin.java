package dev.huskuraft.effortless.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.huskuraft.effortless.fabric.events.common.ClientRenderEvents;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void onRenderGui(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker deltaTracker, CallbackInfo ci) {
        ClientRenderEvents.GUI.invoker().onRenderGui(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
    }

}
