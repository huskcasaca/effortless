package dev.huskcasaca.effortless.mixin;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.building.ReachHelper;
import dev.huskcasaca.effortless.building.BuildActionHandler;
import dev.huskcasaca.effortless.buildmode.BuildModeHandler;
import dev.huskcasaca.effortless.buildmode.BuildModeHelper;
import dev.huskcasaca.effortless.buildmodifier.BuildModifierHelper;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.*;
import io.netty.buffer.Unpooled;
import net.minecraft.network.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPacketListenerMixin implements ServerEffortlessPacketListener {

    @Shadow
    public ServerPlayer player;

    @Shadow
    public abstract void send(Packet<?> packet, @Nullable PacketSendListener packetSendListener);

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, @Nullable PacketSendListener packetSendListener, CallbackInfo ci) {
        var resourceLocation = Packets.getKey(packet);
        if (resourceLocation == null) return;
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(buf);
        send(new ClientboundCustomPayloadPacket(resourceLocation, buf), packetSendListener);
        ci.cancel();
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    void handleCustomPayload(ServerboundCustomPayloadPacket serverboundCustomPayloadPacket, CallbackInfo ci) {
        ResourceLocation resourceLocation = serverboundCustomPayloadPacket.getIdentifier();
        FriendlyByteBuf friendlyByteBuf = null;
        if (Objects.equals(resourceLocation.getNamespace(), Effortless.MOD_ID)) {
            try {
                friendlyByteBuf = serverboundCustomPayloadPacket.getData();
                Packets.getDeserializer(resourceLocation).apply(friendlyByteBuf).handle(this);
            } finally {
                if (friendlyByteBuf != null) {
//                friendlyByteBuf.release();
                }
            }
            ci.cancel();
        }
    }

    @Override
    public void handle(ServerboundPlayerBreakBlockPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        BuildModeHandler.onBlockBrokenPacketReceived(player, packet);
    }

    @Override
    public void handle(ServerboundPlayerBuildActionPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        BuildActionHandler.performAction(player, packet.action());
    }

    @Override
    public void handle(ServerboundPlayerPlaceBlockPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        BuildModeHandler.onBlockPlacedPacketReceived(player, packet);
    }

    @Override
    public void handle(ServerboundPlayerSetBuildModePacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        BuildModeHelper.setModeSettings(player, BuildModeHelper.sanitize(packet.modeSettings(), player));
        BuildModeHandler.initializeMode(player);
    }

    @Override
    public void handle(ServerboundPlayerSetBuildModifierPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        BuildModifierHelper.setModifierSettings(player, BuildModifierHelper.sanitize(packet.modifierSettings(), player));
    }

    @Override
    public void handle(ServerboundPlayerSetBuildReachPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, this, player.getLevel());
        ReachHelper.setReachSettings(player, ReachHelper.sanitize(packet.reachSettings(), player));
        BuildModeHandler.initializeMode(player);
    }

}
