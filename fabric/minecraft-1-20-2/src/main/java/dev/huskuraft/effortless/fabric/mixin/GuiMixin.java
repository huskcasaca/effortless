package dev.huskuraft.effortless.fabric.mixin;

import dev.huskuraft.effortless.fabric.events.ClientRenderEvents;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSavingIndicator(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = At.Shift.AFTER))
    private void onRenderGui(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        ClientRenderEvents.GUI.invoker().onRenderGui(guiGraphics, f);
    }

}
