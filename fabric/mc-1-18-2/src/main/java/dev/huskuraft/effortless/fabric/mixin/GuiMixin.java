package dev.huskuraft.effortless.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.fabric.events.ClientRenderEvents;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSavingIndicator(Lcom/mojang/blaze3d/vertex/PoseStack;)V", shift = At.Shift.AFTER))
    private void onRenderGui(PoseStack matrixStack, float f, CallbackInfo ci) {
        ClientRenderEvents.GUI.invoker().onRenderGui(matrixStack, f);
    }

}
