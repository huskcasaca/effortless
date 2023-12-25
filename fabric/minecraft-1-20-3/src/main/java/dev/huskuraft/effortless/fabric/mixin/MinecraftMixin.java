package dev.huskuraft.effortless.fabric.mixin;

import dev.huskuraft.effortless.fabric.events.InteractionInputEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"), cancellable = true)
    private void onPlayerStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (InteractionInputEvents.ATTACK.invoker().onAttack(player, InteractionHand.MAIN_HAND)) {
            cir.setReturnValue(false);
            player.swing(InteractionHand.MAIN_HAND);
        }
    }

    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPlayerStartUseItem(CallbackInfo ci, InteractionHand[] var1, int var2, int var3, InteractionHand interactionHand) {
        if (InteractionInputEvents.USE_ITEM.invoker().onUseItem(player, interactionHand)) {
            ci.cancel();
            player.swing(interactionHand);
        }
    }

    @Inject(method = "pickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;"), cancellable = true)
    private void onPlayerPickBlock(CallbackInfo ci) {
        if (InteractionInputEvents.PICK_BLOCK.invoker().onPickBlock(player, InteractionHand.MAIN_HAND)) {
            ci.cancel();
        }
    }

}