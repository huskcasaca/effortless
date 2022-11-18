package dev.huskcasaca.effortless.mixin;

import dev.huskcasaca.effortless.EffortlessClient;
import dev.huskcasaca.effortless.buildreach.ReachSettingsManager;
import dev.huskcasaca.effortless.buildmode.BuildModeHandler;
import dev.huskcasaca.effortless.buildmode.ModeSettingsManager;
import dev.huskcasaca.effortless.buildmodifier.ModifierSettingsManager;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.*;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientGamePacketListenerMixin implements ClientPlayerPacketListener {

    @Shadow public abstract void send(Packet<?> packet);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo ci) {
        var resourceLocation = Packets.getKey(packet);
        if (resourceLocation == null) return;
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(buf);
        send(new ServerboundCustomPayloadPacket(resourceLocation, buf));
        ci.cancel();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayload(ClientboundCustomPayloadPacket clientboundCustomPayloadPacket, CallbackInfo ci) {
//        PacketUtils.ensureRunningOnSameThread(clientboundCustomPayloadPacket, this, this.minecraft);
        ResourceLocation resourceLocation = clientboundCustomPayloadPacket.getIdentifier();
        var deserializer = Packets.getDeserializer(resourceLocation);
        if (deserializer == null) {
            return;
        } else {
            ci.cancel();
        }
        FriendlyByteBuf friendlyByteBuf = null;
        try {
            friendlyByteBuf = clientboundCustomPayloadPacket.getData();
            deserializer.apply(friendlyByteBuf).handle(this);
        } finally {
            if (friendlyByteBuf != null) {
//                friendlyByteBuf.release();
            }
        }
    }

    @Override
    public void handle(ClientboundPlayerBuildModePacket packet) {
        minecraft.execute(() -> {
            ModeSettingsManager.setModeSettings(minecraft.player, ModeSettingsManager.sanitize(packet.modeSettings(), minecraft.player));
            BuildModeHandler.initializeMode(minecraft.player);
        });
    }

    @Override
    public void handle(ClientboundPlayerBuildModifierPacket packet) {
        minecraft.execute(() -> {

            ModifierSettingsManager.setModifierSettings(minecraft.player, ModifierSettingsManager.sanitize(packet.modifierSettings(), minecraft.player));
        });
    }

    @Override
    public void handle(ClientboundPlayerReachPacket packet) {

        minecraft.execute(() -> {
            ReachSettingsManager.setReachSettings(minecraft.player, ReachSettingsManager.sanitize(packet.reachSettings(), minecraft.player));
            BuildModeHandler.initializeMode(minecraft.player);
        });
    }

    @Override
    public void handle(ClientboundPlayerRequestLookAtPacket packet) {
        minecraft.execute(() -> {
//            //Send back your info
//                var player = client.player;
//            //Prevent double placing in normal mode with placeStartPos false
//            //Unless QuickReplace is on, then we do need to place start pos.
            if (EffortlessClient.previousLookAt.getType() == HitResult.Type.BLOCK) {
                Packets.sendToServer(new ServerboundPlayerPlaceBlockPacket((BlockHitResult) EffortlessClient.previousLookAt, packet.placeStartPos()));
            } else {
                Packets.sendToServer(new ServerboundPlayerPlaceBlockPacket());
            }
        });
    }

}
