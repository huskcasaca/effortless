package dev.huskcasaca.effortless.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface ServerMessageHandler<M extends Message> {

    void handleServerSide(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, M message, PacketSender responseSender);
}
