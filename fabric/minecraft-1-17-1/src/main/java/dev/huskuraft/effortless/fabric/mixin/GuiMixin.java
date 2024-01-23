package dev.huskuraft.effortless.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.huskuraft.effortless.fabric.events.ClientRenderEvents;
import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;setVisible(Z)V", shift = At.Shift.AFTER))
    private void onRenderGui(PoseStack matrixStack, float f, CallbackInfo ci) {
        ClientRenderEvents.GUI.invoker().onRenderGui(matrixStack, f);
    }

}
